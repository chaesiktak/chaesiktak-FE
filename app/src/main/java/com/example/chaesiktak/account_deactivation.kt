package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.fragments.MyInfoFragment

class account_deactivation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_deactivation)

        // 사용자 닉네임을 가져와서 TextView에 설정
        val nickname = "사용자 닉네임" // 실제 사용자 닉네임을 가져오는 로직을 추가하세요
        val nicknameTextView: TextView = findViewById(R.id.nicknameTextView)
        nicknameTextView.text = "$nickname 님 정말 탈퇴하실건가요?"

        // 체크박스 on/off && 탈퇴하기 버튼
        val deactiveAgreeCheckBox: CheckBox = findViewById(R.id.deactiveAgreeCheckBox)
        val deactivateButton: Button = findViewById(R.id.deactivateButton)

        // 탈퇴 사유 작성란
        val deactiveEditText: EditText = findViewById(R.id.deactiveEditText)

        // 탈퇴하기 버튼 클릭
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

        // 뒤로가기 버튼 클릭 시 마이페이지 탭으로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, MyInfoFragment::class.java)
            startActivity(intent)
        }
    }
}
