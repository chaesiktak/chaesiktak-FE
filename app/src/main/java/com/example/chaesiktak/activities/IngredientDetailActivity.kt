package com.example.chaesiktak.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chaesiktak.adapters.IngredientAdapter
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivityIngredientDetailBinding

class IngredientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityIngredientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = intent.getParcelableExtra<RecommendRecipe>("RECIPE")

        //리사이클러뷰 - 데이터랑 바인딩
        if (recipe != null) {
            binding.ingredientDetailTitle.text = "${recipe.title} 재료"
            binding.detailImageView2.setImageResource(recipe.image)

            // RecyclerView 설정
            val adapter = IngredientAdapter(recipe.ingredients)
            binding.ingredientRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.ingredientRecyclerView.adapter = adapter
        }

        //뒤로가기 (backarrow icon 클릭시)
        binding.backArrowIcon.setOnClickListener {
            finish()
        }
    }
}