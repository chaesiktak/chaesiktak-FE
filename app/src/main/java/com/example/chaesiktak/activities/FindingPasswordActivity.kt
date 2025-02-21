package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.R
import com.example.chaesiktak.ResetPasswordRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class FindingPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_finding_password)

        val emailEditText: EditText = findViewById(R.id.email_input)
        val userNameEditText: EditText = findViewById(R.id.name_input)
        val findPasswordButton: Button = findViewById(R.id.finding_password_button)

        findPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val userName = userNameEditText.text.toString().trim()

            if (email.isEmpty() || userName.isEmpty()) {
                Toast.makeText(this, "이메일과 사용자 이름을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            requestPasswordReset(email, userName)
        }
    }

    private fun requestPasswordReset(email: String, userName: String) {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.instance(this@FindingPasswordActivity)
                val response = apiService.resetPassword(ResetPasswordRequest(email, userName))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            startActivity(Intent(this@FindingPasswordActivity, FindingPasswordActivity2::class.java))
                        } ?: run {
                            Toast.makeText(this@FindingPasswordActivity, "서버 응답 오류", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMsg = response.errorBody()?.string()?.let {
                            try {
                                JSONObject(it).getString("message")
                            } catch (e: Exception) {
                                "비밀번호 찾기 실패"
                            }
                        } ?: "비밀번호 찾기 실패"

                        Toast.makeText(this@FindingPasswordActivity, errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("FindingPasswordError", "비밀번호 찾기 중 오류 발생: ${e.localizedMessage}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@FindingPasswordActivity, "오류 발생: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
