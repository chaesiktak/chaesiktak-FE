package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResetPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)

        val currentPasswordInput = findViewById<EditText>(R.id.currentPasswordInput)
        val newPasswordInput = findViewById<EditText>(R.id.newPasswordInput)
        val confirmPasswordInput = findViewById<EditText>(R.id.confirmPasswordInput)
        val resetButton = findViewById<Button>(R.id.finding_password_button)

        // 사용자의 기존 비밀번호 (예: 서버나 데이터베이스에서 가져온 값)
        val userCurrentPassword = "user_password123"

        // 재설정하기 버튼 클릭 리스너 설정
        resetButton.setOnClickListener {
            val currentPassword = currentPasswordInput.text.toString()
            val newPassword = newPasswordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // 기존 비밀번호 검증
            if (currentPassword != userCurrentPassword) {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 신규 비밀번호 검증
            val passwordRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{9,20}$")
            when {
                newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "모든 필드를 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                newPassword != confirmPassword -> {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
                newPassword.length < 9 || newPassword.length > 20 -> {
                    Toast.makeText(this, "비밀번호는 9~20자로 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                !newPassword.contains(Regex("[A-Za-z]")) -> {
                    Toast.makeText(this, "비밀번호에는 영문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                !newPassword.contains(Regex("\\d")) -> {
                    Toast.makeText(this, "비밀번호에는 숫자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                !newPassword.contains(Regex("[@\$!%*?&]")) -> {
                    Toast.makeText(this, "비밀번호에는 특수문자를 포함해야 합니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 여기에서 실제 비밀번호 재설정 로직을 구현하세요
                    // 예: 서버에 요청을 보내거나 데이터베이스를 업데이트하는 작업

                    // 비밀번호 재설정 후 성공 화면으로 이동
                    val intent = Intent(this, ResetPassword2::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}

