package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class account_deactivation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_deactivation)

        // 사용자 닉네임을 가져와서 TextView에 설정
        val nickname = "사용자 닉네임" // 실제 사용자 닉네임을 가져오는 로직을 추가하세요
        val nicknameTextView: TextView = findViewById(R.id.nicknameTextView)
        nicknameTextView.text = "$nickname 님 정말 탈퇴하실건가요?"

        // 체크박스의 on/off && 탈퇴하기 버튼
        val deactiveAgreeCheckBox: CheckBox = findViewById(R.id.deactiveAgreeCheckBox)
        val deactivateButton: Button = findViewById(R.id.deactivateButton)

        // Spinner에 탈퇴 이유 목록 추가
        val reasonSpinner: Spinner = findViewById(R.id.reasonSpinner)
        val items = arrayOf("선택해주세요", "원하는 비건 정보가 부족해요", "앱을 사용하는데에 불편이 있어요", "기타")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        reasonSpinner.adapter = adapter

        deactiveAgreeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (deactiveAgreeCheckBox.isChecked) {
                // 체크박스가 체크되었을 때
                // 로그아웃 처리를 하고 첫 화면으로 이동
                val intent = Intent(this@account_deactivation, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } else {
                // 체크박스가 체크 해제되었을 때
                Toast.makeText(this@account_deactivation, "탈퇴사항에 동의하여주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
