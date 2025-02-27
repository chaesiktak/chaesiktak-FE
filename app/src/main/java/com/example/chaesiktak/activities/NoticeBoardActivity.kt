package com.example.chaesiktak.activities

import CustomToast
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.NoticeItem
import com.example.chaesiktak.R
import com.example.chaesiktak.adapters.NoticeAdapter
import kotlinx.coroutines.launch

class NoticeBoardActivity : AppCompatActivity() {

    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter
    private var noticeList = mutableListOf<NoticeItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notice_board)

        // 뒤로 가기 버튼 클릭 시 마이페이지로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }

        // RecyclerView 설정
        noticeRecyclerView = findViewById(R.id.noticeRecyclerView)
        noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        noticeAdapter = NoticeAdapter(noticeList)
        noticeRecyclerView.adapter = noticeAdapter

        // 서버에서 공지사항 가져오기
        fetchNoticesFromServer()

        // 공지 추가 버튼 클릭 이벤트
        val addNoticeButton = findViewById<Button>(R.id.addNoticeButton)
        addNoticeButton.setOnClickListener {
            CustomToast.show(this, "관리자만 등록 가능합니다.")
        }
    }

    // 공지사항을 서버에서 가져오는 함수
    private fun fetchNoticesFromServer() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(this@NoticeBoardActivity).getNotice()
                if (response.isSuccessful) {
                    response.body()?.let {
                        noticeList.clear()
                        noticeList.addAll(it.data)
                        noticeAdapter.notifyDataSetChanged()
                    }
                } else {
                    println("공지사항 불러오기 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                println("네트워크 오류: ${e.message}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD_NOTICE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<NoticeItem>("new_notice")?.let {
                noticeList.add(it)
                noticeAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == REQUEST_NOTICE_DETAIL && resultCode == Activity.RESULT_OK) {
            val deletedNoticeId = data?.getIntExtra("deleted_notice_id", -1)
            if (deletedNoticeId != null && deletedNoticeId != -1) {
                val indexToRemove = noticeList.indexOfFirst { it.id == deletedNoticeId }
                if (indexToRemove != -1) {
                    noticeList.removeAt(indexToRemove)
                    noticeAdapter.notifyItemRemoved(indexToRemove)
                }
            }
        }
    }

    companion object {
        const val REQUEST_ADD_NOTICE = 1
        const val REQUEST_NOTICE_DETAIL = 2
    }
}
