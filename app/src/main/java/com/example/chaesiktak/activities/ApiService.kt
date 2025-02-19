import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/sign-up")
    fun signUp(@Body request: SignUpRequest): Call<ApiResponse<User>>

}
