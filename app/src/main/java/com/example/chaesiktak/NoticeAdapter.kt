package com.example.chaesiktak

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoticeAdapter(private val itemList: List<Noticeitem>) :
    RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    // 클릭 리스너 변수 추가
    private var onItemClickListener: ((Noticeitem) -> Unit)? = null

    // 클릭 리스너 설정 함수
    fun setOnItemClickListener(listener: (Noticeitem) -> Unit) {
        onItemClickListener = listener
    }

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
        holder.noticeDate.text = item.noticeCreatedTime.split("T")[0] // 날짜만 표시
        // 클릭 이벤트를 NoticeBoard에서 설정할 수 있도록 변경
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }

        // 항목 클릭 리스너 설정
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NoticeDetail::class.java).apply {
                putExtra("noticeId", item.id)
                putExtra("noticeTitle", item.noticeTitle)
                putExtra("noticeContent", item.noticeContent)
                putExtra("noticeWriter", item.noticeWriter)
                putExtra("noticeDate", item.noticeCreatedTime.split("T")[0])
                putExtra("noticeHits", item.noticeHits)
                //putExtra("noticeUpdatedTime", item.noticeUpdatedTime)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = itemList.size
}