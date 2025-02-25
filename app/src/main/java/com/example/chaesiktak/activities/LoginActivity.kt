package com.example.chaesiktak.activities

import LoadingDialog
import RetrofitClient
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.LoginRequest
import com.example.chaesiktak.R
import com.example.chaesiktak.utils.UserSessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var loadingDialog: LoadingDialog

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_login)

        val signUpText: TextView = findViewById(R.id.sign_up_text)
        val forgotPasswordText: TextView = findViewById(R.id.forgot_password_text)
        val LoginButton: Button = findViewById(R.id.Login_button)
        val IDEditText: EditText = findViewById(R.id.username_input)
        val PWEditText: EditText = findViewById(R.id.password_input)

        // 로딩 다이얼로그 초기화
        loadingDialog = LoadingDialog(this)

        forgotPasswordText.setOnClickListener {
            startActivity(Intent(this, FindingPasswordActivity::class.java))
        }

        signUpText.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }

        LoginButton.setOnClickListener {
            val ID = IDEditText.text.toString().trim()
            val PWD = PWEditText.text.toString().trim()

            if (ID.isEmpty() || PWD.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Login", "로그인 버튼 클릭")
            loadingDialog.show()
            loadingDialog.startAnimation()

            login(ID, PWD)
        }
    }

    // 로그인 API 호출
    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(this@LoginActivity)
                    .login(LoginRequest(email, password))

                Log.d("Login", "HTTP 응답 코드: ${response.code()}")
                Log.d("Login", "서버 응답 메시지: ${response.message()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("ServerResponse", "서버 응답 (성공): $loginResponse")

                    val accessToken = loginResponse?.data?.accessToken ?: ""

                    if (accessToken.isNotEmpty()) {
                        saveAccessToken(accessToken)
                        Log.d("Login", "로그인 성공: AccessToken=$accessToken")

                        //다이얼로그 닫기
                        loadingDialog.stopAnimation()

                        val token = loginResponse?.data?.accessToken ?: ""  // 로그인 API 응답에서 토큰 가져오기
                        if (!token.isNullOrEmpty()) {
                            UserSessionManager.saveToken(this@LoginActivity, token)
                        }
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Log.d("Login", "로그인 실패: 서버에서 토큰을 받지 못함")
                        Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.d("ServerResponse", "서버 응답 (실패): $errorBody")
                    Toast.makeText(this@LoginActivity, "로그인 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("Login", "네트워크 오류", e)
                Toast.makeText(this@LoginActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            } finally {

                loadingDialog.stopAnimation()
            }
        }
    }

    private fun saveAccessToken(token: String) {
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPref.edit()
        val isSaved = editor.putString("accessToken", token).commit()
        if (isSaved) {
            Log.d("Token", "토큰 저장 완료: $token")
        } else {
            Log.e("Token", "토큰 저장 실패")
        }

        //저장된 토큰을 다시 확인
        val savedToken = sharedPref.getString("accessToken", "저장된 값 없음")
        Log.d("Token", "SharedPreferences에 저장된 토큰: $savedToken")
    }

}
