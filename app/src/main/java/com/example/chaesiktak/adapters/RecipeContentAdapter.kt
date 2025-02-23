package com.example.chaesiktak.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.RecipeStep
import com.example.chaesiktak.databinding.RecipeContentBinding

class RecipeContentAdapter(private val stepList: List<RecipeStep>) :
    RecyclerView.Adapter<RecipeContentAdapter.StepViewHolder>() {

    class StepViewHolder(private val binding: RecipeContentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(step: RecipeStep) {
            binding.tvStepTitle.text = "Step ${step.step}" // 숫자를 "Step X" 형식으로 변환
            binding.tvStepContent.text = step.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val binding = RecipeContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StepViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepList[position])
    }

    override fun getItemCount(): Int = stepList.size
}
