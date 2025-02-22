package com.example.chaesiktak

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNotice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notice)

        val noticeTitleEditText = findViewById<EditText>(R.id.noticeDetailTitle)
        val noticeContentEditText = findViewById<EditText>(R.id.noticeDetailContent)
        val addButton = findViewById<Button>(R.id.saveButton)

        // 현재 날짜를 가져와 TextView에 설정
        val noticeDateTextView = findViewById<TextView>(R.id.noticeDetailDate)
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        noticeDateTextView.text = "작성일: $currentDate"

        val existingNotice = intent.getParcelableExtra<Noticeitem>("edit_notice")
        val noticeCreatedTime = existingNotice?.noticeCreatedTime ?:
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())




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
            val noticeWriter = "관리자"
            val noticeHits = 0 // 초기조회수
            val noticeCreatedTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date())
            val noticeUpdatedTime = noticeCreatedTime // 처음에는 생성시간과 동일하게 설정



            if (noticeTitle.isBlank() || noticeContent.isBlank()) {
                Toast.makeText(this, "공지사항 제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 공지사항을 SharedPreferences에 저장
                val sharedPreferences = getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()

                // 저장된 공지사항 개수를 카운트하기 위한 기본값 가져오기
                val totalNotices = sharedPreferences.getInt("totalNotices", 0)

                // 공지사항 추가
                editor.putString("noticeTitle_$totalNotices", noticeTitle)
                editor.putString("noticeContent_$totalNotices", noticeContent)
                editor.putInt("totalNotices", totalNotices + 1) // 공지사항 개수 증가
                editor.apply() // 변경 사항 적용

                // 공지사항 목록으로 돌아가기
                val intent = Intent()
                intent.putExtra("new_notice", Noticeitem(0, noticeWriter, noticeTitle, noticeContent, noticeHits, noticeCreatedTime))
                setResult(Activity.RESULT_OK, intent)
                finish()

                /*
                // 공지사항 리스트로 데이터 전달
                val notice = Noticeitem(
                    id = 0, // 실제 ID는 서버나 데이터베이스에서 할당할 것
                    noticeWriter = noticeWriter,
                    noticeTitle = noticeTitle,
                    noticeContent = noticeContent,
                    noticeHits = noticeHits,
                    noticeCreatedTime = noticeCreatedTime,
                    //noticeUpdatedTime = noticeUpdatedTime
                )

                val intent = Intent()
                intent.putExtra("new_notice", notice)
                setResult(Activity.RESULT_OK, intent)
                finish()

                // 공지사항 목록으로 돌아가기
                finish()

                 */
            }
        }
    }
}