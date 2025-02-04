package com.example.chaesiktak

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.databinding.ActivityRecipeDetailBinding

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<RecommendRecipe>("RECIPE")

        recipe?.let {
            binding.detailTitle.text = it.title
            binding.detaiLTitle2.text = it.title
            binding.detailKcal.text = it.kcal
            binding.detailImageView2.setImageResource(it.image)
        }
        //레시피 상세로 이동
        binding.ingredientArrow.setOnClickListener {
            val intent = Intent(this, IngredientDetailActivity::class.java).apply {
                putExtra("RECIPE", recipe)
            }
            startActivity(intent)
        }
    }
}
