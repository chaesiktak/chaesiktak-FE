import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    //회원가입 API

    @POST("api/sign-up")
    fun signUp(@Body request: SignUpRequest): Call<ApiResponse<User>>

    //로그인 API

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest):Response<LoginResponse>

}
