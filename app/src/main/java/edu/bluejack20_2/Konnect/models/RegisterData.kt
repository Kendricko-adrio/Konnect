package edu.bluejack20_2.Konnect.models

import android.os.Parcel
import android.os.Parcelable

class RegisterData() : Parcelable {
    lateinit var password: String
    lateinit var email: String
    lateinit var name: String

    constructor(parcel: Parcel) : this() {
        password = parcel.readString().toString()
        email = parcel.readString().toString()
        name = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(password)
        parcel.writeString(email)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegisterData> {
        override fun createFromParcel(parcel: Parcel): RegisterData {
            return RegisterData(parcel)
        }

        override fun newArray(size: Int): Array<RegisterData?> {
            return arrayOfNulls(size)
        }
    }

}