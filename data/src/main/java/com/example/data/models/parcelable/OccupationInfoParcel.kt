package com.example.data.models.parcelable

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class OccupationInfoParcel(
    val name: String,
    val place: String,
    val holidaysPeriod: String?
) : Parcelable