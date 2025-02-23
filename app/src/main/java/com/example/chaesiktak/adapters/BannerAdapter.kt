package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.BannerData
import com.example.chaesiktak.R

class BannerAdapter(private val banners: List<BannerData>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.bannerText)
        val subtitle: TextView = itemView.findViewById(R.id.bannerSubtitle)
        val image: ImageView = itemView.findViewById(R.id.bannerImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.banner1, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val realPosition = position % banners.size
        val banner = banners[realPosition]

        holder.title.text = banner.title
        holder.subtitle.text = banner.subtitle
        holder.image.setImageResource(banner.imageResId)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}


