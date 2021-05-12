package edu.bluejack20_2.Konnect.models

import android.os.Parcel
import android.os.Parcelable

class ChatDetail() : Parcelable{
    var connectionDoc: String? = null
    var connectionName: String? = null
    var lastMessage: String? = null
    var photoURL: String? = null

    constructor(parcel: Parcel) : this() {
        connectionDoc = parcel.readString()
        connectionName = parcel.readString()
        lastMessage = parcel.readString()
        photoURL = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(connectionDoc)
        parcel.writeString(connectionName)
        parcel.writeString(lastMessage)
        parcel.writeString(photoURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChatDetail> {
        override fun createFromParcel(parcel: Parcel): ChatDetail {
            return ChatDetail(parcel)
        }

        override fun newArray(size: Int): Array<ChatDetail?> {
            return arrayOfNulls(size)
        }
    }
}