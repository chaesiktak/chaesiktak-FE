package com.example.chaesiktak

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class SearchingContentAdapter (private val SearchingContentList: ArrayList<RecommendRecipe>):
    RecyclerView.Adapter<SearchingContentAdapter.FoodViewHolder>(){

    var onItemClick: ((RecommendRecipe) -> Unit)? = null // 클릭 리스너

    //view holder 정의 (이미지, 타이틀, isfavorite, 레시피 미리보기, 태그)
    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.searching_content_img)
        val titleView: TextView = itemView.findViewById(R.id.searching_content_title)
        val prevtextView: TextView = itemView.findViewById(R.id.searching_content_prevtext)
        val tagView: TextView = itemView.findViewById(R.id.searching_content_tag) // 태그View 추가
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searching_content, parent, false)
        return FoodViewHolder(view)
    }

    override fun getItemCount(): Int {
        return SearchingContentList.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val recipe = SearchingContentList[position]
        holder.imageView.setImageResource(recipe.image)
        holder.titleView.text = recipe.title
        holder.prevtextView.text = recipe.prevtext

        // '비건' 태그 표시 여부 설정 및 색상 변경
        holder.tagView.text = recipe.tag  // 태그 타입 텍스트 설정

        // 태그 배경 색상 동적으로 설정
        when (recipe.tag) {
            "비건" -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_vegan
                )) // 배경
            }
            "락토" -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_lacto
                ))
            }
            "오보" -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_ovo
                ))
            }
            "락토오보" -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_lacto_ovo
                ))
            }
            "페스코" -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_pesco
                ))
            }
            else -> {
                holder.tagView.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.context,
                    R.color.color_pollo
                )) // 폴로 및 기타 태그
            }
        }

        // 클릭 리스너 추가
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(recipe)  // 클릭된 레시피 객체 전달
        }
    }
}