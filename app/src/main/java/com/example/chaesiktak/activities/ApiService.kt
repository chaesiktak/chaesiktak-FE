
import com.example.chaesiktak.ApiResponse
import com.example.chaesiktak.LoginRequest
import com.example.chaesiktak.LoginResponse
import com.example.chaesiktak.Recipe
import com.example.chaesiktak.RecipeDetailResponse
import com.example.chaesiktak.SignUpRequest
import com.example.chaesiktak.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //회원가입 API

    @POST("api/sign-up")
    suspend fun signUp(@Body request: SignUpRequest): Response<ApiResponse<User>>

    //로그인 API

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    //레시피 목록 조회

    @GET("recipe/")
    suspend fun getRecommendedRecipes(): Response<ApiResponse<List<Recipe>>>

    //세부 레시피

    @GET("recipe/{id}")
    suspend fun getRecipeDetail(@Path("id") recipeId: Int): Response<RecipeDetailResponse>


}
