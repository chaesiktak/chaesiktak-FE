package com.example.chaesiktak.adapters

import CustomToast
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.NoticeItem
import com.example.chaesiktak.R
//import com.example.chaesiktak.NoticeDetailActivity // NoticeDetailActivity import 추가

class NoticeAdapter(
    private val itemList: MutableList<NoticeItem>,
    private val context: Context
) : RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

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

        //  클릭 이벤트 설정
        holder.itemView.setOnClickListener {
            CustomToast.show(context, "준비중인 기능입니다!")
        }
    }

    override fun getItemCount() = itemList.size

    // 날짜 변환 함수 (null-safe 처리)
    private fun formatDate(dateString: String?): String {
        return dateString?.split("T")?.getOrNull(0) ?: "날짜 없음"
    }
}




