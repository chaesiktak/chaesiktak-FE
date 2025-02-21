package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NoticeDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notice_detail)

        val noticeTitle = intent.getStringExtra("noticeTitle")
        val noticeContent = intent.getStringExtra("noticeContent")
        val noticeWriter = intent.getStringExtra("noticeWriter")
        val noticeDate = intent.getStringExtra("noticeDate")
        val noticeHits = intent.getIntExtra("noticeHits",0)
        val noticeUpdate = intent.getStringExtra("noticeUpdate")


        val titleTextView = findViewById<TextView>(R.id.noticeDetailTitle)
        val contentTextView = findViewById<TextView>(R.id.noticeDetailContent)
        val writerTextView = findViewById<TextView>(R.id.noticeDetailWriter)
        val dateTextView = findViewById<TextView>(R.id.noticeDetailDate)
        val hitsTextView = findViewById<TextView>(R.id.noticeDetailHits)
        val UpdatedTime = findViewById<TextView>(R.id.noticeDetailUpdatedTime)


        titleTextView.text = noticeTitle
        contentTextView.text = noticeContent
        writerTextView.text = "작성자: $noticeWriter"
        dateTextView.text = "작성일: $noticeDate"
        hitsTextView.text = "조회수: $noticeHits"
        UpdatedTime.text = "수정일: $noticeUpdate"

        // 뒤로가기 버튼 클릭 시 공지사항 목록 탭으로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, NoticeBoard::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}