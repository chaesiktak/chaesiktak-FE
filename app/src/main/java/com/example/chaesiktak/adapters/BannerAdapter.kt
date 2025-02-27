package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.BannerData
import com.example.chaesiktak.R
class BannerAdapter(private val banners: List<BannerData>, private val onItemClick: (BannerData) -> Unit) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.bannerText)
        val subtitle: TextView = itemView.findViewById(R.id.bannerSubtitle)
        val image: ImageView = itemView.findViewById(R.id.bannerImage)

        fun bind(banner: BannerData) {
            title.text = banner.title
            subtitle.text = banner.subtitle
            image.setImageResource(banner.imageResId)

            // 배너 클릭 이벤트 추가
            itemView.setOnClickListener {
                onItemClick(banner)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.banner1, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % banners.size
        holder.bind(banners[realPosition])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}
