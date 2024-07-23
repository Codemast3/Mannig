package com.example.mannig.models

import android.os.Parcel
import android.os.Parcelable

data class selectedmembers(
    val id:String = "",
    val image : String = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<selectedmembers> {
        override fun createFromParcel(parcel: Parcel): selectedmembers {
            return selectedmembers(parcel)
        }

        override fun newArray(size: Int): Array<selectedmembers?> {
            return arrayOfNulls(size)
        }
    }
}