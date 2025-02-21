package com.example.chaesiktak

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.fragments.MyInfoFragment
import org.json.JSONObject

// 메인 액티비티 클래스
class NoticeBoard : AppCompatActivity() {

    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter
    private val noticeList = mutableListOf<Noticeitem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notice_board)

        // 뒤로 가기 버튼 클릭 시 마이페이지로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            val intent = Intent(this, MyInfoFragment::class.java)
            startActivity(intent)
        }

        // JSON 데이터를 파싱하여 NoticeItem 리스트 생성
        val jsonString = """
        {
            "status": 200,
            "success": true,
            "message": "공지사항 조회 성공",
            "data": [
                {
                    "id": 1,
                    "noticeWriter": "관리자",
                    "noticeHits": 3,
                    "noticeTime": "2025-02-07T12:51:07.212947",
                    "noticeTitle": "공지사항 5 재수정",
                    "noticeContent": "공지사항 5의 내용입니다.",
                    "url": "/notice/5"
                },
                {
                    "id": 2,
                    "noticeWriter": "관리자",
                    "noticeHits": 1,
                    "noticeTime": "2025-02-06T21:24:56.3299",
                    "noticeTitle": "공지사항4",
                    "noticeContent": "공지사항 4의 내용입니다.",
                    "url": "/notice/4"
                },
                {
                    "id": 3,
                    "noticeWriter": "관리자",
                    "noticeHits": 0,
                    "noticeTime": "2025-02-06T20:52:19.766921",
                    "noticeTitle": "공지사항3",
                    "noticeContent": "공지사항 3의 내용입니다.",
                    "url": "/notice/3"
                },
                {
                    "id": 4,
                    "noticeWriter": "관리자",
                    "noticeHits": 0,
                    "noticeTime": "2025-02-06T20:52:11.565033",
                    "noticeTitle": "공지사항2",
                    "noticeContent": "공지사항 2의 내용입니다.",
                    "url": "/notice/2"
                },
                {
                    "id": 0,
                    "noticeWriter": "관리자",
                    "noticeHits": 1,
                    "noticeTime": "2025-02-07T13:14:44.468816",
                    "noticeTitle": "공지사항 1 수정",
                    "noticeContent": "공지사항 1 수정의 내용입니다.",
                    "url": "/notice/1"
                }
            ]
        }
        """

        val parsedNotices = parseNoticeJson(jsonString)
        noticeList.addAll(parsedNotices)

        noticeRecyclerView = findViewById(R.id.noticeRecyclerView)
        noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        noticeAdapter = NoticeAdapter(noticeList)
        noticeRecyclerView.adapter = noticeAdapter

        // AddNoticeActivity로부터 데이터 받기
        val addNoticeButton = findViewById<Button>(R.id.addNoticeButton)
        addNoticeButton.setOnClickListener {
            val intent = Intent(this, AddNotice::class.java)
            //startActivity(intent)
            startActivityForResult(intent, REQUEST_ADD_NOTICE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_NOTICE && resultCode == Activity.RESULT_OK) {
            data?.getParcelableExtra<Noticeitem>("new_notice")?.let {
                noticeList.add(it)
                noticeAdapter.notifyDataSetChanged()
            }
        }
    }

    companion object {
        const val REQUEST_ADD_NOTICE = 1
    }



    // ?
    private fun parseNoticeJson(jsonString: String): List<Noticeitem> {
        val jsonObject = JSONObject(jsonString)
        val dataArray = jsonObject.getJSONArray("data")
        val noticeList = mutableListOf<Noticeitem>()

        for (i in 0 until dataArray.length()) {
            val dataObject = dataArray.getJSONObject(i)
            val noticeItem = Noticeitem(
                id = dataObject.optInt("id",0),
                noticeWriter = dataObject.getString("noticeWriter"),
                noticeTitle = dataObject.getString("noticeTitle"),
                noticeContent = dataObject.getString("noticeContent"),
                noticeHits = dataObject.getInt("noticeHits"),
                noticeCreatedTime = dataObject.getString("noticeTime"),
                //noticeUpdatedTime = dataObject.getString("noticeUpdatedTime")
            )
            noticeList.add(noticeItem)
        }
        return noticeList
    }
}