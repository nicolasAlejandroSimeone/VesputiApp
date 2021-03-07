package com.example.vesputiapp.view

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.vesputiapp.R

class NoConnectionView (context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet){
    var tryAgain: Button
    private var bundle: Bundle

    lateinit var tryAgainAction: () -> Unit

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_no_connection, this, true)
        tryAgain = view.findViewById(R.id.try_again)
        bundle = Bundle()

        tryAgain.setOnClickListener {
            if(:: tryAgainAction.isInitialized){
                tryAgainAction()
            }
        }
    }
}