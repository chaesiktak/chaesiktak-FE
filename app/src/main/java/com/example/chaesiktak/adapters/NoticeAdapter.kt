package com.example.chaesiktak.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.NoticeItem
import com.example.chaesiktak.R
//import com.example.chaesiktak.NoticeDetailActivity // NoticeDetailActivity import 추가

class NoticeAdapter(private val itemList: MutableList<NoticeItem>) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    // 클릭 리스너 변수 추가
    private var onItemClickListener: ((NoticeItem) -> Unit)? = null

    // 클릭 리스너 설정 함수
    fun setOnItemClickListener(listener: (NoticeItem) -> Unit) {
        onItemClickListener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noticeTitle: TextView = itemView.findViewById(R.id.noticeTitle)
        val noticeDate: TextView = itemView.findViewById(R.id.noticeDate)
        val noticeWriter: TextView = itemView.findViewById(R.id.noticeWriter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_notice_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.noticeTitle.text = item.noticeTitle
        holder.noticeDate.text = formatDate(item.noticeCreatedTime)
        holder.noticeWriter.text = item.noticeWriter

        // 클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item) // 리스너 콜백 호출 (외부에서 설정된 함수 실행)

            val context = holder.itemView.context
//            val intent = Intent(context, NoticeDetailActivity::class.java).apply {
//                putExtra("noticeId", item.id)
//                putExtra("noticeTitle", item.noticeTitle)
//                putExtra("noticeContent", item.noticeContent)
//                putExtra("noticeWriter", item.noticeWriter)
//                putExtra("noticeDate", formatDate(item.noticeCreatedTime)) // 안전한 날짜 변환
//                putExtra("noticeHits", item.noticeHits)
//            }
//            context.startActivity(intent)
        }
    }

    override fun getItemCount() = itemList.size

    // 공지사항 추가 (데이터 변경 시 효율적인 갱신 처리)
    fun addNotice(notice: NoticeItem) {
        itemList.add(notice)
        notifyItemInserted(itemList.size - 1)
    }

    // 날짜 변환 함수 (null-safe 처리)
    private fun formatDate(dateString: String?): String {
        return dateString?.split("T")?.getOrNull(0) ?: "날짜 없음"
    }
}
