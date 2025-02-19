package com.example.chaesiktak

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

// 데이터 클래스
data class Noticeitem(
    val noticeWriter: String,
    val noticeHits: Int,
    val noticeTime: String,
    val noticeTitle: String,
    val url: String,
    val noticeContent: String )

// 어댑터 클래스
class NoticeAdapter(private val itemList: List<Noticeitem>) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noticeTitle: TextView = itemView.findViewById(R.id.noticeTitle)
        val noticeDate: TextView = itemView.findViewById(R.id.noticeDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_notice_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.noticeTitle.text = item.noticeTitle
        holder.noticeDate.text = item.noticeTime.split("T")[0] // 날짜만 표시

        // 항목 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NoticeDetail::class.java).apply {
                putExtra("noticeTitle", item.noticeTitle)
                putExtra("noticeContent", item.noticeContent)
                putExtra("noticeWriter", item.noticeWriter)
                putExtra("noticeDate", item.noticeTime.split("T")[0])
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = itemList.size
}

// 메인 액티비티 클래스
class NoticeBoard : AppCompatActivity() {
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
                    "noticeWriter": "관리자",
                    "noticeHits": 3,
                    "noticeTime": "2025-02-07T12:51:07.212947",
                    "noticeTitle": "공지사항 5 재수정",
                    "noticeContent": "공지사항 5의 내용입니다.",
                    "url": "/notice/5"
                },
                {
                    "noticeWriter": "관리자",
                    "noticeHits": 1,
                    "noticeTime": "2025-02-06T21:24:56.3299",
                    "noticeTitle": "공지사항4",
                    "noticeContent": "공지사항 4의 내용입니다.",
                    "url": "/notice/4"
                },
                {
                    "noticeWriter": "관리자",
                    "noticeHits": 0,
                    "noticeTime": "2025-02-06T20:52:19.766921",
                    "noticeTitle": "공지사항3",
                    "noticeContent": "공지사항 3의 내용입니다.",
                    "url": "/notice/3"
                },
                {
                    "noticeWriter": "관리자",
                    "noticeHits": 0,
                    "noticeTime": "2025-02-06T20:52:11.565033",
                    "noticeTitle": "공지사항2",
                    "noticeContent": "공지사항 2의 내용입니다.",
                    "url": "/notice/2"
                },
                {
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
        val noticeList = parseNoticeJson(jsonString)

        val recyclerView = findViewById<RecyclerView>(R.id.noticeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NoticeAdapter(noticeList)

        val addNoticeButton = findViewById<Button>(R.id.addNoticeButton)
        addNoticeButton.setOnClickListener {
            val intent = Intent(this, AddNotice::class.java)
            startActivity(intent)
        }
    }

    private fun parseNoticeJson(jsonString: String): List<Noticeitem> {
        val jsonObject = JSONObject(jsonString)
        val dataArray = jsonObject.getJSONArray("data")
        val noticeList = mutableListOf<Noticeitem>()

        for (i in 0 until dataArray.length()) {
            val dataObject = dataArray.getJSONObject(i)
            val noticeItem = Noticeitem(
                noticeWriter = dataObject.getString("noticeWriter"),
                noticeHits = dataObject.getInt("noticeHits"),
                noticeTime = dataObject.getString("noticeTime"),
                noticeTitle = dataObject.getString("noticeTitle"),
                noticeContent = dataObject.getString("noticeContent"),
                url = dataObject.getString("url")
            )
            noticeList.add(noticeItem)
        }
        return noticeList
    }
}