data class SignUpRequest(
    val email: String,
    val password: String,
    val userName: String,
    val userNickName: String
)

data class User(
    val id: Long,          // 사용자 ID
    val email: String,     // 이메일
    val userName: String,  // 사용자 이름
    val userNickName: String, // 닉네임
    val role: String       // 역할 (예: "USER", "ADMIN")
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val email: String
)

data class ProtectedData(
    val message: String
)

data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T?
)
