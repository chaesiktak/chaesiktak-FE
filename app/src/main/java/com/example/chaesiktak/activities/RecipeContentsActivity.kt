package com.example.chaesiktak.activities

import com.example.chaesiktak.adapters.RecipeContentAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivityRecipeContentsBinding

class RecipeContentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeContentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityRecipeContentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<RecommendRecipe>("RECIPE")

        //리사이클러뷰 - 데이터랑 바인딩
        if (recipe != null) {
            binding.contentDetailTitle.text = "${recipe.title} 상세 레시피"
            binding.detailImageView2.setImageResource(recipe.image)

            // RecyclerView 설정
            val adapter = RecipeContentAdapter(recipe.contents)
            binding.recipeContentRecyclerview.layoutManager = LinearLayoutManager(this)
            binding.recipeContentRecyclerview.adapter = adapter
        }

        //뒤로가기 (backarrow icon 클릭시)
        binding.backArrowIcon.setOnClickListener {
            finish()
        }
    }

    }
