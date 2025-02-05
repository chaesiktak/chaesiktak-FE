package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivityRecipeDetailBinding

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private var recipe: RecommendRecipe? = null  // recipe 데이터를 멤버 변수로 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // API 33 이상 대응 (Parcelable)
        recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("RECIPE", RecommendRecipe::class.java)
        } else {
            intent.getParcelableExtra("RECIPE")
        }

        recipe?.let {
            binding.detailTitle.text = it.title
            binding.detaiLTitle2.text = it.title
            binding.detailKcal.text = it.kcal
            binding.detailImageView2.setImageResource(it.image)

            // 좋아요 버튼 UI 업데이트
            updateLikeButtonUI(it.isFavorite)

            // 좋아요 버튼 클릭 이벤트 추가
            binding.detailLike.setOnClickListener {
                toggleLike()
            }
        }

        // 재료 상세 페이지로 이동
        binding.ingredientArrow.setOnClickListener {
            recipe?.let {
                val intent = Intent(this, IngredientDetailActivity::class.java).apply {
                    putExtra("RECIPE", it)
                }
                startActivity(intent)
            }
        }

        binding.recipeArrow.setOnClickListener {
            recipe?.let {
                val intent = Intent(this, RecipeContentsActivity::class.java).apply {
                    putExtra("RECIPE", it)
                }
                startActivity(intent)
            }
        }

        //뒤로가기 (backarrow icon 클릭시)
        binding.backArrowIcon.setOnClickListener {
            finish()
        }
    }

    private fun updateLikeButtonUI(isFavorite: Boolean) {
        binding.detailLike.setImageResource(
            if (isFavorite) R.drawable.likebutton_onclicked else R.drawable.likebutton
        )
    }

    private fun toggleLike() {
        recipe?.let {
            it.isFavorite = !it.isFavorite  // 상태 변경
            updateLikeButtonUI(it.isFavorite)  // UI 업데이트

            // 변경된 데이터를 HomeFragment로 전달
            val resultIntent = Intent().apply {
                putExtra("UPDATED_RECIPE", it)
            }
            setResult(RESULT_OK, resultIntent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResultAndFinish()
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent().apply {
            putExtra("UPDATED_RECIPE", recipe)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}

