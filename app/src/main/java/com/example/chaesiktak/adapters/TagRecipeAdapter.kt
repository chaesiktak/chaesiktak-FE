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

class TagRecipeAdapter(private val tagRecipeList: ArrayList<RecommendRecipe>) :
    RecyclerView.Adapter<TagRecipeAdapter.FoodViewHolder>() {

    var onItemClick: ((RecommendRecipe) -> Unit)? = null // 클릭 리스너

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.tag_recipe_card_img)
        val titleView: TextView = itemView.findViewById(R.id.tag_recipe_card_title)
        val subtextView: TextView = itemView.findViewById(R.id.tag_recipe_card_subtitle)
        val tagView: TextView = itemView.findViewById(R.id.tag_recipe_card_tag) // 태그View 추가
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

        // Coil을 사용한 이미지 로딩 (null 처리)
        holder.imageView.load(recipe.image) {
            placeholder(R.drawable.placeholder_image)
            error(R.drawable.placeholder_image)
            fallback(R.drawable.placeholder_image)
        }

        holder.titleView.text = recipe.title
        holder.subtextView.text = recipe.subtext

        // '비건' 태그 표시 여부 설정 및 색상 변경
        holder.tagView.text = recipe.tag  // 태그 타입 텍스트 설정

        // 태그 배경 색상 동적으로 설정
        when (recipe.tag) {
            "VEGAN" -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.color_vegan
                )) // 배경
            }
            "LACTO" -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.color_lacto
                ))
            }
            "OVO" -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.color_ovo
                ))
            }
            "LACTO_OVO" -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.color_lacto_ovo
                ))
            }
            "PESCO" -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
                    R.color.color_pesco
                ))
            }
            else -> {
                holder.tagView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,
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
