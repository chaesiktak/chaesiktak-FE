package com.example.chaesiktak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecentAdapter(private val items: List<RecentRecipeData>) : RecyclerView.Adapter<RecentAdapter.RecentViewHolder>() {

    class RecentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.recentItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_recent_item, parent, false)
        return RecentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val item = items[position]
        holder.imageView.setImageResource(item.imageResId)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
