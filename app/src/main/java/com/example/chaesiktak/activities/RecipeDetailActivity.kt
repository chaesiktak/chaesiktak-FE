package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivityRecipeDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private var recipe: RecommendRecipe? = null
    private var recipeId: Int = -1  // 레시피 ID 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 레시피 ID 가져오기
        recipeId = intent.getIntExtra("RECIPE_ID", -1)

        if (recipeId != -1) {
            fetchRecipeDetail(recipeId) // API 호출
        } else {
            Toast.makeText(this, "레시피 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.backArrowIcon.setOnClickListener {
            finish()
        }

        binding.ingredientArrow.setOnClickListener {
            recipe?.let {
                val intent = Intent(this@RecipeDetailActivity, IngredientDetailActivity::class.java).apply {
                    putExtra("RECIPE_ID", it.id) // recipeId 전달
                }
                startActivity(intent)
            } ?: Toast.makeText(this, "레시피 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

        binding.recipeArrow.setOnClickListener {
            recipe?.let {
                val intent =
                    Intent(this@RecipeDetailActivity, RecipeContentsActivity::class.java).apply {
                        putExtra("RECIPE_ID", it.id) // recipeId 전달
                    }
                startActivity(intent)
            } ?: Toast.makeText(this, "레시피 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
        }

    private fun fetchRecipeDetail(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance(this@RecipeDetailActivity)
                    .getRecipeDetail(recipeId)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!

                    if (responseBody.success) {
                        recipe = responseBody.data // RecommendRecipe 데이터 가져오기

                        withContext(Dispatchers.Main) {
                            bindRecipeData()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@RecipeDetailActivity, "오류: ${responseBody.message}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RecipeDetailActivity, "서버 응답 오류", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RecipeDetailActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindRecipeData() {
        recipe?.let {
            binding.detailTitle.text = it.title
            binding.detaiLTitle2.text = it.title
            binding.detailKcal.text = it.kcal

            binding.detailImageView2.load(it.image) {
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
                fallback(R.drawable.placeholder_image)
            }

            updateLikeButtonUI(it.isFavorite)

            binding.detailLike.setOnClickListener {
                toggleLike()
            }
        }
    }

    private fun updateLikeButtonUI(isFavorite: Boolean) {
        binding.detailLike.setImageResource(
            if (isFavorite) R.drawable.likebutton_onclicked else R.drawable.likebutton
        )
    }

    private fun toggleLike() {
        recipe?.let {
            it.isFavorite = !it.isFavorite
            updateLikeButtonUI(it.isFavorite)

            val resultIntent = Intent().apply {
                putExtra("UPDATED_RECIPE", it)
            }
            setResult(RESULT_OK, resultIntent)
        }
    }
}
