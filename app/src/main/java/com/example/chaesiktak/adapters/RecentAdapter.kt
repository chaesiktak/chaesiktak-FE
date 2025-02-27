package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe

class RecentAdapter(private val items: List<RecommendRecipe>, private val onItemClick: (RecommendRecipe) -> Unit) :
    RecyclerView.Adapter<RecentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.recentTitle)
        val image: ImageView = view.findViewById(R.id.recentItemImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_recent_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = items[position] // 수정된 부분

        holder.title.text = recipe.title // 제목 설정

        holder.image.load(recipe.image) { // 이미지 로드 (Coil 사용)
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.placeholder_image)
            fallback(R.drawable.placeholder_image)
        }

        holder.itemView.setOnClickListener { onItemClick(recipe) } // 클릭 이벤트
    }

    override fun getItemCount() = items.size
}
