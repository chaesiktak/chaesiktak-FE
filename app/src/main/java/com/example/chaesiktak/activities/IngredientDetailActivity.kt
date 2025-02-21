package com.example.chaesiktak.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.adapters.IngredientAdapter
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivityIngredientDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IngredientDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIngredientDetailBinding
    private var recipeId: Int = -1
    private var recipe: RecommendRecipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityIngredientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 레시피 ID 가져오기
        recipeId = intent.getIntExtra("RECIPE_ID", -1)

        if (recipeId != -1) {
            fetchRecipeDetail(recipeId) // API 호출
        } else {
            Toast.makeText(this, "레시피 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        // 뒤로가기 버튼
        binding.backArrowIcon.setOnClickListener {
            finish()
        }
    }

    private fun fetchRecipeDetail(recipeId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance(this@IngredientDetailActivity)
                    .getRecipeDetail(recipeId)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!

                    if (responseBody.success) {
                        recipe = responseBody.data // API에서 데이터 가져오기

                        withContext(Dispatchers.Main) {
                            bindRecipeData()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@IngredientDetailActivity, "오류: ${responseBody.message}", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@IngredientDetailActivity, "서버 응답 오류", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@IngredientDetailActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun bindRecipeData() {
        recipe?.let {
            binding.ingredientDetailTitle.text = "${it.title} 재료"

            binding.detailImageView2.load(it.image) {
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
                fallback(R.drawable.placeholder_image)
            }
            // RecyclerView 설정
            val adapter = IngredientAdapter(it.ingredients)
            binding.ingredientRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.ingredientRecyclerView.adapter = adapter
        }
    }
}
