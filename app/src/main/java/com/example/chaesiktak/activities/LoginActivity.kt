package com.example.chaesiktak.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.R

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

        // 클릭 이벤트 설정 (로그인 버튼 클릭 시, HomeActivity로 이동)
        LoginButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }



        // WindowInsetsCompat 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
