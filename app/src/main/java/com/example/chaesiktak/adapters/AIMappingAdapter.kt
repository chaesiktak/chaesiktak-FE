package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.R

class AIMappingAdapter(private val foodList: List<Pair<String, List<String>>>) :
    RecyclerView.Adapter<AIMappingAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodCategory: TextView = view.findViewById(R.id.txtFoodCategory)
        val alternatives: TextView = view.findViewById(R.id.txtFoodAlternatives)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.llm_item, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val (category, alternatives) = foodList[position]
        holder.foodCategory.text = category
        holder.alternatives.text = alternatives.joinToString(", ")
    }

    override fun getItemCount() = foodList.size
}
