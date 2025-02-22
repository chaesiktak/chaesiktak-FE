package com.example.chaesiktak

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookmarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        // SharedPreferences 초기화
        val sharedPreferences = getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)
        val savedItems = mutableListOf<BookmarkItem>()

        /*
        !! 사용자가 좋아요 버튼 클릭 시 항목을 추가하는 코드가 추가로 작성되어야 작동 가능한 코드 !!
        // 저장된 항목 불러오기
        sharedPreferences.all.forEach { (key, value) ->
            // JSON 문자열을 BookmarkItem으로 변환
            val jsonValue = value as? String // value가 String인지 확인
            if (jsonValue != null) {
                val bookmarkItem = Gson().fromJson(jsonValue, BookmarkItem::class.java) // JSON을 BookmarkItem으로 변환
                savedItems.add(bookmarkItem) // 변환된 BookmarkItem을 리스트에 추가
            }
        }

         */

        // 리사이클러뷰 초기화
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = BookmarkAdapter(savedItems)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this) // 리사이클러뷰 레이아웃 매니저 설정


        // 뒤로가기 버튼 클릭 리스너 설정
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
            finish()
        }

        // 예제 북마크 항목 리스트
        val bookmarkItemList = mutableListOf(
            BookmarkItem(R.drawable.food1, "비건 라따뚜이"),
            BookmarkItem(R.drawable.food1, "채식 버거"),
            BookmarkItem(R.drawable.food1, "두부 스테이크")
        )

    }
}



