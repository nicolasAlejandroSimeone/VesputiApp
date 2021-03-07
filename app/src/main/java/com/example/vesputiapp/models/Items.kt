package com.example.vesputiapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Items (
    val id : Int?,
    val icon: String?,
    val title: String?,
    val subtitle: String?,
    val description: String?,
    val position: List<Double>?
):Parcelable