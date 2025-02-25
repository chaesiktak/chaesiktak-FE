package com.example.chaesiktak

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/*
*
* 회원가입 응답 & 요청
* */
// 회원가입 요청
data class SignUpRequest(
    val email: String,
    val password: String,
    val userName: String,
    val userNickName: String
)

// 사용자 정보
data class User(
    val id: Long,          // 사용자 ID
    val email: String,     // 이메일
    val userName: String,  // 사용자 이름
    val userNickName: String, // 닉네임
    val role: String       // 역할 (예: "USER", "ADMIN")
)

/*
*
* 로그인 응답 & 요청
* */

// 로그인 요청
data class LoginRequest(
    val email: String,
    val password: String
)

// 로그인 응답
data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val email: String
)

// API 응답을 감싸는 공통 구조
data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T?
)

// 전체 레시피 조회 (/recipe/)
data class Recipe(
    val recipeId: Int,
    val recipeTitle: String,
    val recipeTag: String,
    val recipeImage: Int,
    val recipePrevtext: String,
    val url: String
)

// 개별 레시피 조회 (/recipe/{id})
data class RecommendRecipe(
    val id: Int,
    val image: String,       // 이미지 리소스 ID
    val title: String,    // 레시피 제목
    val subtext: String,  // 서브 텍스트 (0인분, 0시간)
    val kcal: String,     // 칼로리
    var tag: String,      // 태그
    var prevtext: String, // 레시피 간단 설명
    var isFavorite: Boolean = false, // '좋아요' 상태
    val ingredients: List<Ingredient>, // 재료 리스트
    val contents: List<RecipeStep> // 조리 과정
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), // id 읽기
        parcel.readString()!!,
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
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id) // id 저장
        parcel.writeString(image)
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


// 개별 레시피 상세 조회 응답
data class RecipeDetailResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: RecommendRecipe
)

//레시피 재료
data class Ingredient(
    val name: String,
    val amount: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(amount)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient = Ingredient(parcel)
        override fun newArray(size: Int): Array<Ingredient?> = arrayOfNulls(size)
    }
}

//레시피 순서
data class RecipeStep(
    val step: Int, // 1 -> Step 1
    val description: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(step)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RecipeStep> {
        override fun createFromParcel(parcel: Parcel) = RecipeStep(parcel)
        override fun newArray(size: Int) = arrayOfNulls<RecipeStep?>(size)
    }
}
/**비밀번호 찾기 FindingPassword Activity **/
// 비밀번호 찾기 - 요청 데이터 클래스
data class ResetPasswordRequest(
    val email: String,
    val userName: String
)

// 비밀번호 찾기 - 응답 데이터 클래스
data class ResetPasswordResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: String?
)

/**회원가입 Join Activity **/
//이메일 중복 확인 요청
data class EmailCheckRequestBody(
    val email: String
)

//닉네임 중복 확인 요청
data class NickNameCheckRequestBody(
    val userNickName: String
)

//이메일 중복 확인 응답
data class EmailCheckResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Boolean
)

//닉네임 중복 확인 응답
data class NickNameCheckResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Boolean
)

/**LLM - Scanner **/
//이미지 업로드 요청
data class uploadImageRequest(
    val Image: String
)

//이미지 업로드 응답
data class uploadImageResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: String
)

data class ImageAnalyzeRequest(
    val Image: String
)

data class ImageAnalyzeResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: LLMResponse
)

data class LLMResponse(
    val response: String,
    @SerializedName("output_dict") val outputDict: Map<String, String>
)

data class MypageResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: Mypage
)

data class Mypage(
    val email: String,
    val userNickName: String,
    val userName: String,
    val veganType: String
)
