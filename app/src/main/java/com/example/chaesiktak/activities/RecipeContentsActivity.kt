package com.example.chaesiktak.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.adapters.RecipeContentAdapter
import com.example.chaesiktak.databinding.ActivityRecipeContentsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeContentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeContentsBinding
    private var recipeId: Int = -1
    private var recipe: RecommendRecipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 설정
        binding = ActivityRecipeContentsBinding.inflate(layoutInflater)
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
                val response = RetrofitClient.instance(this@RecipeContentsActivity)
                    .getRecipeDetail(recipeId)

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()!!

                    // 디버깅용 로그 추가
                    println("API Response: ${responseBody.data}")

                    if (responseBody.success) {
                        recipe = responseBody.data // API에서 데이터 가져오기

                        withContext(Dispatchers.Main) {
                            bindRecipeData()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@RecipeContentsActivity,
                                "오류: ${responseBody.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RecipeContentsActivity,
                            "서버 응답 오류: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RecipeContentsActivity,
                        "네트워크 오류: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }



    private fun bindRecipeData() {
        recipe?.let {
            binding.contentDetailTitle.text = "${it.title} 상세 레시피"
            binding.detailImageView2.load(it.image) {
                placeholder(R.drawable.placeholder_image)
                error(R.drawable.placeholder_image)
                fallback(R.drawable.placeholder_image)
            }

            // 리스트가 비어 있는 경우 빈 리스트를 전달하여 RecyclerView가 업데이트되도록 함
            val recipeSteps = it.contents ?: emptyList()

            // RecyclerView 설정
            val adapter = RecipeContentAdapter(recipeSteps)
            binding.recipeContentRecyclerview.layoutManager = LinearLayoutManager(this)
            binding.recipeContentRecyclerview.adapter = adapter

            // 디버깅용 로그 추가
            println("Recipe Steps: ${recipeSteps.size}")
        } ?: println("Recipe is null")
    }

}
