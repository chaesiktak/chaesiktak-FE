package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.R

class SignUpCompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //xml 로그인 바인딩
        val loginButton: Button = findViewById(R.id.Login_button)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_complete)

        //로그인 메서드

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }


    }
}