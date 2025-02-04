package com.example.chaesiktak

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TagRecipeAdapter(private val tagRecipeList: ArrayList<TagRecipe>) :
    RecyclerView.Adapter<TagRecipeAdapter.FoodViewHolder>() {

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.tag_recipe_card_img)
        val titleView: TextView = itemView.findViewById(R.id.tag_recipe_card_title)
        val subtextView: TextView = itemView.findViewById(R.id.tag_recipe_card_subtitle)
        val tagTextView: TextView = itemView.findViewById(R.id.tag_recipe_card_tag)  // 태그 TextView 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_tag_recipe_card, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tagRecipeList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val recipe = tagRecipeList[position]
        holder.imageView.setImageResource(recipe.image)
        holder.titleView.text = recipe.title
        holder.subtextView.text = recipe.subtext

        // '비건' 태그 표시 여부 설정 및 색상 변경
        holder.tagTextView.text = recipe.tagType  // 태그 타입 텍스트 설정

        // 태그 배경 색상 동적으로 설정
        when (recipe.tagType) {
            "비건" -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_vegan)) //배경
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
            "락토" -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_lacto))
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
            "오보" -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_ovo))
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
            "락토오보" -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_lacto_ovo))
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
            "페스코" -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_pesco))
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
            else -> {
                holder.tagTextView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.color_pollo)) //폴로 및 기타 태그
                holder.tagTextView.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black_replacement))
            }
        }

    }
}

