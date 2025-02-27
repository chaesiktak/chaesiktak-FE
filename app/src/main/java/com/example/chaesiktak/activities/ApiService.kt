
import com.example.chaesiktak.AIResponse
import com.example.chaesiktak.ApiResponse
import com.example.chaesiktak.DeleteFavoriteResponse
import com.example.chaesiktak.EmailCheckRequestBody
import com.example.chaesiktak.EmailCheckResponse
import com.example.chaesiktak.GetNoticeResponse
import com.example.chaesiktak.ImageAnalyzeRequest
import com.example.chaesiktak.ImageAnalyzeResponse
import com.example.chaesiktak.LoginRequest
import com.example.chaesiktak.LoginResponse
import com.example.chaesiktak.MyFavoriteListResponse
import com.example.chaesiktak.MyFavoriteResponse
import com.example.chaesiktak.MypageResponse
import com.example.chaesiktak.NickNameCheckRequestBody
import com.example.chaesiktak.NickNameCheckResponse
import com.example.chaesiktak.Recipe
import com.example.chaesiktak.RecipeDetailResponse
import com.example.chaesiktak.ResetPasswordRequest
import com.example.chaesiktak.ResetPasswordResponse
import com.example.chaesiktak.SignUpRequest
import com.example.chaesiktak.User
import com.example.chaesiktak.passwordUpdateRequest
import com.example.chaesiktak.passwordUpdateResponse
import com.example.chaesiktak.uploadImageRequest
import com.example.chaesiktak.uploadImageResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @GET("notice/")
    suspend fun getNotice():Response<GetNoticeResponse>

    @POST("api/verify/passwordupdate")
    suspend fun passwordUpdate(@Body request: passwordUpdateRequest): Response<passwordUpdateResponse>

    @POST("api/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<ApiResponse<User>>

    @POST("api/check/email")
    suspend fun CheckEmailDupe(@Body request: EmailCheckRequestBody): Response<EmailCheckResponse>


    @POST("api/check/nickname")
    suspend fun CheckNickNameDupe(@Body request: NickNameCheckRequestBody): Response<NickNameCheckResponse>

    //로그인 API

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    //레시피 목록 조회

    @GET("recipe/")
    suspend fun getRecommendedRecipes(): Response<ApiResponse<List<Recipe>>>

    //세부 레시피

    @GET("recipe/{id}")
    suspend fun getRecipeDetail(@Path("id") recipeId: Int): Response<RecipeDetailResponse>

    //비밀번호 찾기

    @POST("api/verify/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ResetPasswordResponse>

    /*내 정보 조회*/
    @GET("api/verify/mypage")
    suspend fun UserInfo(): Response<MypageResponse>

    /*좋아요 클릭 -> recipeId값 전달*/
    @POST("api/verify/mypage/favorite/{recipeID}")
    suspend fun saveFavorite(@Path("recipeID") recipdId: Int): Response<MyFavoriteResponse>

    @DELETE("api/verify/mypage/favorite/{recipeID}")
    suspend fun DeleteFavorite(@Path("recipeID") recipdId: Int): Response<DeleteFavoriteResponse>

    @GET("api/verify/mypage/favorite")
    suspend fun GetFavoriteList(): Response<MyFavoriteListResponse>

    // LLM 이미지 분석
    @Multipart
    @POST("api/analyze-image")
    suspend fun AIimage(
        @Part image: MultipartBody.Part
    ): Response<AIResponse>
}

