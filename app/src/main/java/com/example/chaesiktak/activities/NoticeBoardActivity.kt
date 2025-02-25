package com.example.chaesiktak.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.NoticeAdapter
import com.example.chaesiktak.Noticeitem
import com.example.chaesiktak.R
import com.example.chaesiktak.fragments.MyInfoFragment
import org.json.JSONObject

// 메인 액티비티 클래스
class NoticeBoardActivity : AppCompatActivity() {

    private lateinit var noticeRecyclerView: RecyclerView
    private lateinit var noticeAdapter: NoticeAdapter
    private var noticeList = mutableListOf<Noticeitem>()

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

        // SharedPreferences에서 공지사항 불러오기
        loadNoticesFromSharedPreferences()

        noticeRecyclerView = findViewById(R.id.noticeRecyclerView)
        noticeRecyclerView.layoutManager = LinearLayoutManager(this)
        noticeAdapter = NoticeAdapter(noticeList)
        noticeRecyclerView.adapter = noticeAdapter

        noticeAdapter.setOnItemClickListener { notice ->
            val intent = Intent(this, NoticeDetailActivity::class.java)
            intent.putExtra("notice_item", notice)
            startActivityForResult(intent, REQUEST_NOTICE_DETAIL)
        }

        // AddNoticeActivity로부터 데이터 받기
        val addNoticeButton = findViewById<Button>(R.id.addNoticeButton)
        addNoticeButton.setOnClickListener {
            val intent = Intent(this, AddNoticeActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_NOTICE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ADD_NOTICE && resultCode == Activity.RESULT_OK) {
            // 새로운 공지 추가 처리
            data?.getParcelableExtra<Noticeitem>("new_notice")?.let {
                noticeList.add(it)
                noticeAdapter.notifyDataSetChanged()
            }
        } else if (requestCode == REQUEST_NOTICE_DETAIL && resultCode == Activity.RESULT_OK) {
            // 공지 삭제 처리
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

    private fun loadNoticesFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE)
        val totalNotices = sharedPreferences.getInt("totalNotices", 0)

        for (index in 0 until totalNotices) {
            val title = sharedPreferences.getString("noticeTitle_$index", null)
            val content = sharedPreferences.getString("noticeContent_$index", null)
            if (title != null && content != null) {
                noticeList.add(
                    Noticeitem(
                    id = index, // 인덱스를 ID로 사용
                    noticeWriter = "관리자", // 작성자 정보를 수정할 필요가 있다면 추가
                    noticeTitle = title,
                    noticeContent = content,
                    noticeHits = 0, // 초기 조회수
                    noticeCreatedTime = "2025-02-07T12:51:07.212947" // 생성시간은 필요한 형식으로 수정
                )
                )
            }
        }
    }

    private fun saveNoticeToSharedPreferences(notice: Noticeitem) {
        val sharedPreferences = getSharedPreferences("NoticePrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val totalNotices = sharedPreferences.getInt("totalNotices", 0)

        // 공지사항 추가
        editor.putString("noticeTitle_$totalNotices", notice.noticeTitle)
        editor.putString("noticeContent_$totalNotices", notice.noticeContent)
        editor.putInt("totalNotices", totalNotices + 1) // 공지사항 개수 증가
        editor.apply() // 변경 사항 적용
    }

    private fun parseNoticeJson(jsonString: String): List<Noticeitem> {
        val jsonObject = JSONObject(jsonString)
        val dataArray = jsonObject.getJSONArray("data")
        val noticeList = mutableListOf<Noticeitem>()

        for (i in 0 until dataArray.length()) {
            val dataObject = dataArray.getJSONObject(i)
            val noticeItem = Noticeitem(
                id = dataObject.optInt("id", 0),
                noticeWriter = dataObject.getString("noticeWriter"),
                noticeTitle = dataObject.getString("noticeTitle"),
                noticeContent = dataObject.getString("noticeContent"),
                noticeHits = dataObject.getInt("noticeHits"),
                noticeCreatedTime = dataObject.getString("noticeTime"),
                // noticeUpdatedTime = dataObject.getString("noticeUpdatedTime")
            )
            noticeList.add(noticeItem)
        }
        return noticeList


        /*
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

         */
    }
}
