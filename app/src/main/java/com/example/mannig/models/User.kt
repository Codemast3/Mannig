package com.example.mannig.models

import android.os.Parcel
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts

data class User (    val id : String = "",
            val name:String="",
                    val email: String="",
                            val image: String="",
                                    val mobile : Long = 0,
                                            val fcmToken: String="",
                     var selected: Boolean = false


        ): Parcelable {


        constructor(parcel: Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readLong()!!,
                parcel.readString()!!
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(id)
                parcel.writeString(name)
                parcel.writeString(email)
                parcel.writeString(image)
                parcel.writeLong(mobile)
                parcel.writeString(fcmToken)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
                override fun createFromParcel(parcel: Parcel): User {
                        return User(parcel)
                }

                override fun newArray(size: Int): Array<User?> {
                        return arrayOfNulls(size)
                }
        }
}