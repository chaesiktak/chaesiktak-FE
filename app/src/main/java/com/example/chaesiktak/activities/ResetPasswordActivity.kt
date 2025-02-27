package com.example.chaesiktak.activities

import CustomToast
import LoadingDialog
import RetrofitClient
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.R
import com.example.chaesiktak.ResetPasswordRequest
import com.example.chaesiktak.passwordUpdateRequest
import com.example.chaesiktak.passwordUpdateResponse
import com.example.chaesiktak.utils.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        val email = findViewById<EditText>(R.id.EamilEdittext)
        val CurrentPWD = findViewById<EditText>(R.id.CurrentPWDEdittext)
        val NewPWD = findViewById<EditText>(R.id.NewPasswordEdittext)
        val resetButton = findViewById<Button>(R.id.finding_password_button)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        // 로딩 다이얼로그 초기화
        loadingDialog = LoadingDialog(this)

        // 취소 버튼 클릭 리스너 설정
        cancelButton.setOnClickListener {
            // 현재 액티비티 종료하여 이전 프래그먼트로 돌아가기
            finish()
        }

        resetButton.setOnClickListener {
            val email =email.text.toString().trim()
            val CurrentPWD = CurrentPWD.text.toString().trim()
            val NewPWD =NewPWD.text.toString().trim()

            loadingDialog.show()
            loadingDialog.startAnimation()

            if (email.isEmpty() || CurrentPWD.isEmpty() || NewPWD.isEmpty()) {
                loadingDialog.stopAnimation()
                CustomToast.show(this@ResetPasswordActivity, "입력 필드를 전부 채워주세요.")
                return@setOnClickListener
            }
            if(CurrentPWD == NewPWD){
                CustomToast.show(this@ResetPasswordActivity, "기존 비밀번호와 현재 비밀번호가 일치합니다. ")
            }
            // 서버에 비밀번호 변경 요청
            passwordUpdate(email, CurrentPWD, NewPWD)
        }

    }
    private fun passwordUpdate(email: String, CurrentPWD: String, NewPWD: String) {
        lifecycleScope.launch {
            try {
                val requestBody = passwordUpdateRequest(email, CurrentPWD, NewPWD)

                // 요청 본문을 JSON 형태로 변환하여 로그 출력
                val jsonRequest = JSONObject().apply {
                    put("email", requestBody.email)
                    put("currentPassword", requestBody.currentPassword)
                    put("newPassword", requestBody.newPassword)
                }
                Log.d("RequestBody", "보낸 요청 본문: $jsonRequest")

                val response = RetrofitClient.instance(this@ResetPasswordActivity)
                    .passwordUpdate(requestBody)

                Log.d("Login", "HTTP 응답 코드: ${response.code()}")
                Log.d("Login", "서버 응답 메시지: ${response.message()}")

                if (response.isSuccessful) {
                    val passwordUpdateResponse = response.body()
                    Log.d("ServerResponse", "서버응답 성공: $passwordUpdateResponse")
                    startActivity(Intent(this@ResetPasswordActivity, ResetPasswordActivity2::class.java))
                    finish()
                } else{
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        JSONObject(errorBody?: "").getString("data")
                    } catch (e: Exception){
                        "서버외의 문제 발생, log.e 확인"
                    }
                    Log.d("ServerResponse", "서버 응답 (실패): $errorMessage")
                   CustomToast.show(this@ResetPasswordActivity, "$errorMessage")
                }
            } catch (e:Exception){
                Log.e("Login", "네트워크 오류", e)
                Toast.makeText(this@ResetPasswordActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
            finally {
                loadingDialog.stopAnimation()
            }
        }
    }


}
