package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe

class SavedItemAdapter(
    private val savedRecipeList: ArrayList<RecommendRecipe>,
    private val onItemClick: (RecommendRecipe) -> Unit  // 🔹 클릭 이벤트 추가
) : RecyclerView.Adapter<SavedItemAdapter.SavedRecipeViewHolder>() {

    class SavedRecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.saveditemimg)
        val titleView: TextView = itemView.findViewById(R.id.saveditemtitme)
        val subtextView: TextView = itemView.findViewById(R.id.saveditemsubtitle)
        val tagView: TextView = itemView.findViewById(R.id.saveditemtag) // 태그View 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.saveditem, parent, false)
        return SavedRecipeViewHolder(view)
    }

    override fun getItemCount(): Int = savedRecipeList.size

    override fun onBindViewHolder(holder: SavedRecipeViewHolder, position: Int) {
        val recipe = savedRecipeList[position]

        // Coil을 사용한 이미지 로딩 (null 처리)
        holder.imageView.load(recipe.image) {
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.placeholder_image)
            fallback(R.drawable.placeholder_image)
        }

        holder.titleView.text = recipe.title
        holder.subtextView.text = recipe.subtext
        holder.tagView.text = recipe.tag  // 태그 타입 텍스트 설정

        // 태그 배경 색상 동적으로 설정
        val tagColor = when (recipe.tag) {
            "VEGAN" -> R.color.color_vegan
            "LACTO" -> R.color.color_lacto
            "OVO" -> R.color.color_ovo
            "LACTO_OVO" -> R.color.color_lacto_ovo
            "PESCO" -> R.color.color_pesco
            else -> R.color.color_pollo
        }
        holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, tagColor))

        // 🔹 클릭 리스너 추가
        holder.itemView.setOnClickListener { onItemClick(recipe) }
    }
}
