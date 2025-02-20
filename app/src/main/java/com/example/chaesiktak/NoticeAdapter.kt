package com.example.chaesiktak

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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