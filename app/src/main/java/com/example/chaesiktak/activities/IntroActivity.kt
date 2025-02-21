package com.example.chaesiktak.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.R

class IntroActivity : AppCompatActivity() {
    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_intro)

        val startButton: Button = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
        }

    }
}

