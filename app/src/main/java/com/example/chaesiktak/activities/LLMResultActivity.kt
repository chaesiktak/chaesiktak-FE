package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.R
import com.example.chaesiktak.AIResponse
import com.example.chaesiktak.fragments.HomeFragment

class LLMResultAcitivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_llmresult)


        val gohomeButton: Button = findViewById(R.id.go_home_button)

        // 인텐트로부터 ai_response 데이터 가져오기
        val aiResponse: AIResponse? = intent.getParcelableExtra("ai_response")

        // TextView 찾기
        val textOverlay = findViewById<TextView>(R.id.textOverlay)

        // ai_response가 null이 아닌 경우 데이터를 TextView에 표시
        textOverlay.text = aiResponse?.data?.response ?: "데이터를 불러올 수 없습니다."

        gohomeButton.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }

    }
}
