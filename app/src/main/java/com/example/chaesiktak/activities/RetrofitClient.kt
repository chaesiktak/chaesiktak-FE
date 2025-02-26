import android.content.Context
import com.example.chaesiktak.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://52.78.99.122:8080/"

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var retrofit: Retrofit? = null

    fun instance(context: Context): ApiService {
        if (retrofit == null) {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(AuthInterceptor(context))
                .connectTimeout(60, TimeUnit.SECONDS) // 서버 연결 대기 시간
                .readTimeout(60, TimeUnit.SECONDS)    // 서버 응답 대기 시간
                .writeTimeout(60, TimeUnit.SECONDS)   // 요청 데이터 전송 시간
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return retrofit!!.create(ApiService::class.java)
    }
}
