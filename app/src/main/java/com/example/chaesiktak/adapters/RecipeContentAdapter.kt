package com.example.chaesiktak.adapters

import com.example.chaesiktak.RecipeStep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.R

class RecipeContentAdapter(private val stepList: List<RecipeStep>) :
    RecyclerView.Adapter<RecipeContentAdapter.StepViewHolder>() {


    class StepViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvStepTitle: TextView = itemView.findViewById(R.id.tvStepTitle)
        val tvStepContent: TextView = itemView.findViewById(R.id.tvStepContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_content, parent, false)
        return StepViewHolder(view)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        val step = stepList[position]
        holder.tvStepTitle.text = "Step ${step.stepNumber}" // 숫자를 "Step X" 형식으로 변환
        holder.tvStepContent.text = step.stepContent
    }

    override fun getItemCount(): Int = stepList.size
}
