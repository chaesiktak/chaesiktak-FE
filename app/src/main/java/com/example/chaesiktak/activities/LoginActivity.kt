package com.example.chaesiktak.activities

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.LoginRequest
import com.example.chaesiktak.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 레이아웃을 설정

        setContentView(R.layout.activity_login)

        // 텍스트 뷰 참조

        val signUpText: TextView = findViewById(R.id.sign_up_text)
        val forgotPasswordText: TextView = findViewById(R.id.forgot_password_text)
        val LoginButton: Button = findViewById(R.id.Login_button)
        val IDEditText: EditText = findViewById(R.id.username_input)
        val PWEditText: EditText = findViewById(R.id.password_input)

        // 클릭 이벤트 설정 ('비밀번호 찾기' 텍스트 클릭 시, FindingPasswordActivity로 이동)

        forgotPasswordText.setOnClickListener {
            val intent = Intent(this, FindingPasswordActivity::class.java)
            startActivity(intent)
        }

        // 클릭 이벤트 ('회원가입 텍스트' 클릭 시, JoinActivity로 이동)

        signUpText.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        // 클릭 이벤트 설정 : 로그인 액션
        LoginButton.setOnClickListener {

            val ID = IDEditText.text.toString().trim()
            val PWD = PWEditText.text.toString().trim()

            if (ID.isEmpty() || PWD.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("Login", "로그인 버튼 클릭")
            login(ID, PWD)
        }

    }

    // 로그인 API 호출
    private fun login(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(this@LoginActivity).login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    val accessToken = loginResponse?.data?.accessToken ?: ""

                    if (accessToken.isNotEmpty()) {
                        saveAccessToken(accessToken)
                        Log.d("Login", "로그인 성공: AccessToken=$accessToken")
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "로그인 실패: 토큰 없음", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "일치하는 정보가 존재하지 않습니다.: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Login", "로그인 오류", e)
            }
        }
    }

    private fun saveAccessToken(token: String) {
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("accessToken", token)
            apply()
        }
    }

}
