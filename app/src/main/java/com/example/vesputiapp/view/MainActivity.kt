package com.example.vesputiapp.view

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.vesputiapp.R
import com.example.vesputiapp.models.Items
import com.example.vesputiapp.viewModel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {

        private const val TAG = "MainActivity"
        private const val PERMISSION_REQUESTS = 1
        private const val SIMPLE_POI = "SimplePoi"
    }

    private val viewModel by viewModel<MainViewModel>()
    private var results: MutableList<Items> = arrayListOf()
    private var mapView: MapView? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView?.onSaveInstanceState(outState)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allPermissionsGranted()) {
            getRuntimePermissions()
        }

        Mapbox.getInstance(this, getString(R.string.token))

        setContentView(R.layout.activity_main)


        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        bottom_sheet_arrow.setImageResource(R.drawable.icn_chevron_down)
                    }
                }
            }

        })

        mapView = findViewById(R.id.mapView)

        mapView?.onCreate(savedInstanceState)

        llProgressBar.visibility = View.VISIBLE

        no_internet_detail.tryAgainAction = {
            no_internet_detail.visibility = View.GONE
            mapView?.visibility = View.VISIBLE
        }

        bottom_sheet_arrow.setOnClickListener(View.OnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        })


        setObservers()

        viewModel.getItems(SIMPLE_POI)
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setObservers() {
        viewModel.results.observe(this, Observer {
            results = it as MutableList<Items>
            llProgressBar.visibility = View.GONE
            getMap(results)
        })
    }

    private fun getMap(results: MutableList<Items>) {
        mapView?.getMapAsync { mapBoxMap ->

            mapBoxMap.setOnMarkerClickListener {
                if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN){
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    titleTxt.text = it.title
                } else
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                true
            }

            mapBoxMap.setStyle(Style.MAPBOX_STREETS) {
                for (result in results) {
                    val options = MarkerOptions()
                    options.title(result.title)
                    options.position(LatLng(result.position!![0], result.position[1]))
                    mapBoxMap.addMarker(options)
                }
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    return false
                }
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in getRequiredPermissions()) {
            permission?.let {
                if (!isPermissionGranted(this, it)) {
                    allNeededPermissions.add(permission)
                }
            }
        }

        if (allNeededPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS
            )
        }
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Log.i(TAG, "Permission granted: $permission")
            return true
        }
        Log.i(TAG, "Permission NOT granted: $permission")
        return false
    }

    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }
    }
}