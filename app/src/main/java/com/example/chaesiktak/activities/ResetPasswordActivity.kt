package com.example.chaesiktak.activities

import CustomToast
import LoadingDialog
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
import com.example.chaesiktak.passwordUpdateRequest
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

        /*
        // 재설정하기 버튼 클릭 리스너 설정
        resetButton.setOnClickListener {
            val currentPassword = currentPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

//            // 기존 비밀번호 검증
//            if (currentPassword != userCurrentPassword) {
//                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

//            // 신규 비밀번호 검증
//            val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{9,20}$")
//            when {
//                newPassword.isEmpty() || confirmPassword.isEmpty() -> {
//                    Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
//                }
//                newPassword != confirmPassword -> {
//                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//                }
//                newPassword.length < 9 || newPassword.length > 20 -> {
//                    Toast.makeText(this, "비밀번호는 9~20자로 입력해주세요.", Toast.LENGTH_SHORT).show()
//                }
//                !newPassword.contains(Regex("[A-Za-z]")) -> {
//                    Toast.makeText(this, "비밀번호에는 영문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
//                }
//                !newPassword.contains(Regex("\\d")) -> {
//                    Toast.makeText(this, "비밀번호에는 숫자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
//                }
//                !newPassword.contains(Regex("[@\$!%*?&]")) -> {
//                    Toast.makeText(this, "비밀번호에는 특수문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
//                }
//                else -> {
//                    // 여기에서 실제 비밀번호 재설정 로직을 구현하세요
//                    // 예: 서버에 요청을 보내거나 데이터베이스를 업데이트하는 작업
//
//                    // 비밀번호 재설정 후 성공 화면으로 이동
//                    val intent = Intent(this, ResetPasswordActivity2::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
        }
        */

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
            // 서버에 비밀번호 변경 요청
//            ㅊ
        }

    }

//    private fun passwordUpdate(currentPWD: String, newPWD: String, confirmnewPWD: String) {
//        val token = "Bearer ${UserSessionManager.getToken()}" // 사용자 토큰 가져오기
//
//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                val apiService = RetrofitClient.instance(this@ResetPasswordActivity)
//                val response = apiService.passwordUpdate(token, passwordUpdateRequest(currentPWD, newPWD, confirmnewPWD))
//
//                withContext(Dispatchers.Main) {
//                    loadingDialog.stopAnimation()
//                    if (response.isSuccessful) {
//                        Log.d("PasswordUpdate", "비밀번호 변경 성공")
//                        startActivity(Intent(this@ResetPasswordActivity, ResetPasswordActivity2::class.java))
//                        finish()
//                    } else {
//                        val errorMsg = response.errorBody()?.string()?.let {
//                            try {
//                                JSONObject(it).getString("message")
//                            } catch (e: Exception) {
//                                Log.e("PasswordUpdate", "JSON 파싱 오류: ${e.message}")
//                                "비밀번호 변경 실패"
//                            }
//                        } ?: "비밀번호 변경 실패"
//
//                        if (response.code() == 401) {
//                            Log.e("PasswordUpdate", "401 Unauthorized: 인증 실패")
//                            CustomToast.show(this@ResetPasswordActivity, "로그인이 만료되었습니다. 다시 로그인해주세요.")
//                            startActivity(Intent(this@ResetPasswordActivity, LoginActivity::class.java))
//                            finish()
//                        } else {
//                            Log.e("PasswordUpdate", "비밀번호 변경 실패: 상태 코드 ${response.code()}, 에러 메시지: $errorMsg")
//                            CustomToast.show(this@ResetPasswordActivity, errorMsg)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    loadingDialog.stopAnimation()
//                    Log.e("PasswordUpdate", "네트워크 요청 중 오류 발생", e)
//                    CustomToast.show(this@ResetPasswordActivity, "네트워크 오류가 발생했습니다.")
//                }
//            }
//        }
//
//    }
}
