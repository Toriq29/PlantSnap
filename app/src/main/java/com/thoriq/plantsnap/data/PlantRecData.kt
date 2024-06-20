package com.thoriq.plantsnap.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlantRecData(
    val name: String,
    val description: String,
    val suhu: String,
    val ketinggian: String,
    val photo: Int
) : Parcelable

