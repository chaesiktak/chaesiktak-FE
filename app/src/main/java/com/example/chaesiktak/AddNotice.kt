package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddNotice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notice)

        val noticeTitleEditText = findViewById<EditText>(R.id.noticeDetailTitle)
        val noticeContentEditText = findViewById<EditText>(R.id.noticeDetailContent)
        val addButton = findViewById<Button>(R.id.saveButton)

        // 뒤로가기 버튼 클릭 시 공지사항 목록 탭으로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, NoticeBoard::class.java)
            startActivity(intent)
        }

        // 완료 버튼 클릭 시 공지사항 업로드 (미완)
        addButton.setOnClickListener {
            val noticeTitle = noticeTitleEditText.text.toString()
            val noticeContent = noticeContentEditText.text.toString()

            if (noticeTitle.isBlank() || noticeContent.isBlank()) {
                Toast.makeText(this, "공지사항 제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 공지사항 저장 로직 구현
                val noticeData = mapOf(
                    "noticeTitle" to noticeTitle,
                    "noticeContent" to noticeContent
                )

                // 서버에 데이터 전송 또는 로컬 데이터베이스에 저장
                // 예제) Toast 메시지로 대체
                Toast.makeText(this, "공지사항이 저장되었습니다.", Toast.LENGTH_SHORT).show()

                // 공지사항 목록으로 돌아가기
                finish()
            }
        }
    }
}