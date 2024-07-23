package com.example.mannig.models

import android.os.Parcel
import android.os.Parcelable

data class Card (
    val name :String = "",
    val createdby:String = "",
    val assignedto: ArrayList<String> = ArrayList(),
    val labelcolor: String = "",
    val duedate:Long=0

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString()!!
        ,parcel.readLong()!!

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(createdby)
        parcel.writeStringList(assignedto)
        parcel.writeString(labelcolor)
        parcel.writeLong(duedate)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }
}