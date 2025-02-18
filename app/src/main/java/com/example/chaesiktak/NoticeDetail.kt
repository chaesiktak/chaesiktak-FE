package com.example.chaesiktak

import android.os.Bundle
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

        val titleTextView = findViewById<TextView>(R.id.noticeDetailTitle)
        val contentTextView = findViewById<TextView>(R.id.noticeDetailContent)
        val writerTextView = findViewById<TextView>(R.id.noticeDetailWriter)
        val dateTextView = findViewById<TextView>(R.id.noticeDetailDate)

        titleTextView.text = noticeTitle
        contentTextView.text = noticeContent
        writerTextView.text = "작성자: $noticeWriter"
        dateTextView.text = "작성일: $noticeDate"


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}