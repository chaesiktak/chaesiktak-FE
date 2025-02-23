package com.example.chaesiktak

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoticeDetail : AppCompatActivity() {

    private var noticeId: Int = -1 // 공지 ID를 저장할 변수 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notice_detail)

        // Intent에서 데이터 받기
        val noticeId = intent.getIntExtra("noticeId", -1)  // ID 받기
        val noticeTitle = intent.getStringExtra("noticeTitle") ?: "제목 없음"
        val noticeContent = intent.getStringExtra("noticeContent") ?: "내용 없음"
        val noticeWriter = intent.getStringExtra("noticeWriter") ?: "작성자 없음"
        val noticeDate = intent.getStringExtra("noticeDate") ?: "날짜 없음"
        val noticeHits = intent.getIntExtra("noticeHits", 0)
        val noticeUpdate = intent.getStringExtra("noticeUpdate") ?: "없음"

        // UI 요소 연결
        findViewById<TextView>(R.id.noticeDetailTitle).text = noticeTitle
        findViewById<TextView>(R.id.noticeDetailContent).text = noticeContent
        findViewById<TextView>(R.id.noticeDetailWriter).text = "작성자: $noticeWriter"
        findViewById<TextView>(R.id.noticeDetailDate).text = "작성일: $noticeDate"
        findViewById<TextView>(R.id.noticeDetailHits).text = "조회수: $noticeHits"
        findViewById<TextView>(R.id.noticeDetailUpdatedTime).text = "수정: $noticeUpdate"


        // 뒤로가기 버튼 클릭 시 공지사항 목록 탭으로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, NoticeBoard::class.java)
            startActivity(intent)
        }

        // 삭제 버튼 설정
        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("공지사항 삭제")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    val intent = Intent()
                    intent.putExtra("deleted_notice_id", noticeId) // ID 전달
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                .setNegativeButton("취소", null)
                .show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}