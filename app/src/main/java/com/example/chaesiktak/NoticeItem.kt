package com.example.chaesiktak

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// 데이터 클래스
data class Noticeitem(
    val id: Int,
    val noticeWriter: String,
    val noticeTitle: String,
    val noticeContent: String,
    val noticeHits: Int,
    val noticeCreatedTime: String,
    //val noticeUpdatedTime: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        //parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(noticeWriter)
        parcel.writeString(noticeTitle)
        parcel.writeString(noticeContent)
        parcel.writeInt(noticeHits)
        parcel.writeString(noticeCreatedTime)
        //parcel.writeString(noticeUpdatedTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Noticeitem> {
        override fun createFromParcel(parcel: Parcel): Noticeitem {
            return Noticeitem(parcel)
        }

        override fun newArray(size: Int): Array<Noticeitem?> {
            return arrayOfNulls(size)
        }
    }
}
