package com.example.chaesiktak

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.activities.HomeActivity
import com.example.chaesiktak.activities.LoginActivity
import com.example.chaesiktak.activities.SignUpCompleteActivity
import com.example.chaesiktak.activities.TOSActivity

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join)

        val backarrow: ImageView = findViewById(R.id.backArrow)
        val homeicon: ImageView = findViewById(R.id.homeIcon)
        val tosText: TextView = findViewById(R.id.TOStext)
        val signupbutton: Button = findViewById(R.id.signUpbutton)

        val checkEmailbutton: Button = findViewById(R.id.checkEmailButton)
        val checkNickname: Button = findViewById(R.id.checkNicknameButton)

        checkEmailbutton.setOnClickListener {
            checkEmailbutton.isEnabled = false
            checkEmailbutton.setBackgroundResource(R.drawable.button_disabled_background)
        }

        checkNickname.setOnClickListener {
            checkNickname.isEnabled = false
            checkNickname.setBackgroundResource(R.drawable.button_disabled_background)
        }

        signupbutton.setOnClickListener {
            startActivity(Intent(this, SignUpCompleteActivity::class.java))
        }
        tosText.setOnClickListener {
            startActivity(Intent(this, TOSActivity::class.java))
        }
        homeicon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        backarrow.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}