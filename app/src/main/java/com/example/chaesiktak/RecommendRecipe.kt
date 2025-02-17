package com.example.chaesiktak


import android.os.Parcel
import android.os.Parcelable

// 추천 레시피 데이터 클래스 (Parcelable)
data class RecommendRecipe(
    val image: Int,       // 이미지 리소스 ID
    val title: String,    // 레시피 제목
    val subtext: String,  // 서브 텍스트 (0인분, 0시간)
    val kcal: String,     // 칼로리
    var tag:String, //태그
    var prevtext:String, // 레시피 간단 설명 ("더원 홀푸드 고수분 TVP로 만든 식물성 카레")
    var isFavorite: Boolean = false,// '좋아요' 상태
    val ingredients: List<Ingredient>, //재료 리스트
    val contents: List<RecipeStep> //조리과정
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        mutableListOf<Ingredient>().apply {
            parcel.readTypedList(this, Ingredient.CREATOR)
        },
        mutableListOf<RecipeStep>().apply {
            parcel.readTypedList(this, RecipeStep.CREATOR)
        },
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(image)
        parcel.writeString(title)
        parcel.writeString(subtext)
        parcel.writeString(kcal)
        parcel.writeString(tag)
        parcel.writeString(prevtext)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeTypedList(ingredients)
        parcel.writeTypedList(contents)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RecommendRecipe> {
        override fun createFromParcel(parcel: Parcel) = RecommendRecipe(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RecommendRecipe?>(size)
    }
}

