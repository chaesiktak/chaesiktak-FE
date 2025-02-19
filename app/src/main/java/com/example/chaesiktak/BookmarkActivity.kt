package com.example.chaesiktak

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

// 데이터 클래스
data class BookmarkItem(val imageResId: Int, val name: String)

// 어댑터 클래스
class BookmarkAdapter(private val itemList: List<BookmarkItem>) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favoriteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_bookmark_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.itemImage.setImageResource(item.imageResId)
        holder.itemName.text = item.name

        // 즐겨찾기 아이콘 클릭 리스너 설정
        holder.favoriteIcon.setOnClickListener {
            // 목록에서 항목 제거
            //itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)

            // 토스트 메시지 표시
            Toast.makeText(holder.itemView.context, "${item.name}를 즐겨찾기에서 제거했습니다.", Toast.LENGTH_SHORT).show()

            // 하트 아이콘 변경
            holder.favoriteIcon.setImageResource(R.drawable.likebutton) // 비워진 하트 아이콘 리소스 사용
        }
    }

    override fun getItemCount() = itemList.size


}

// 메인 액티비티 클래스
class BookmarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = BookmarkAdapter(bookmarkItemList)
    }
}



