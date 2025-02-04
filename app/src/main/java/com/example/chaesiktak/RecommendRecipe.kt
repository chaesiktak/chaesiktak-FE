package com.example.chaesiktak

import android.os.Parcel
import android.os.Parcelable

data class RecommendRecipe(
    val image: Int,       // 이미지 리소스 ID
    val title: String,    // 레시피 제목
    val subtext: String,   // 서브 텍스트 (0인분, 0시간)
    val kcal: String, //칼로리
    var isFavorite:Boolean = false // '좋아요' 상태

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,  // Non-nullable field, force unwrap
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(title)
        parcel.writeString(subtext)
        parcel.writeString(kcal)
        parcel.writeByte(if (isFavorite) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecommendRecipe> {
        override fun createFromParcel(parcel: Parcel): RecommendRecipe {
            return RecommendRecipe(parcel)
        }

        override fun newArray(size: Int): Array<RecommendRecipe?> {
            return arrayOfNulls(size)
        }
    }
}
