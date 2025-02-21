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

// 로그인 요청
data class LoginRequest(
    val email: String,
    val password: String
)

// 로그인 응답
data class LoginResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: TokenData?
) {
    data class TokenData(
        val accessToken: String,
        val refreshToken: String,
        val email: String
    )
}

// 보호된 데이터 응답 예제
data class ProtectedData(
    val message: String
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
    val recipeTitle: String,
    val recipeTag: String,
    val recipeImage: String,  //URL일 경우 String이 적절함
    val recipePrevtext: String,
    val url: String
)

// 재료 데이터 (예: 토마토 1/2개)
data class Ingredient(
    val name: String,
    val amount: String
)

// 레시피 조리 순서 (예: step1 - 채를 썰고 준비한다)
data class Step(
    val step: Int,
    val description: String
)

// 개별 레시피 조회 (/recipe/{id})
data class RecipeDetail(
    val id: Int,
    val image: String, // 이미지가 URL이라면 String이 적절
    val title: String,
    val subtext: String,
    val kcal: String,
    val tag: String,
    val prevtext: String,
    val ingredients: List<Ingredient>,
    val contents: List<Step>,
    val favorite: Boolean
)

// 개별 레시피 상세 조회 응답
data class RecipeDetailResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: RecipeDetail
)
