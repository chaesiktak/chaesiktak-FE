package com.example.chaesiktak

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.databinding.ActivityIngredientDetailBinding

class IngredientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityIngredientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<RecommendRecipe>("RECIPE")

        if (recipe != null) {
            binding.ingredientDetailTitle.text = "${recipe.title} 재료"
            binding.detailImageView2.setImageResource(recipe.image)

            // RecyclerView 설정
            val adapter = IngredientAdapter(recipe.ingredients)
            binding.ingredientRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.ingredientRecyclerView.adapter = adapter
        }
    }
}