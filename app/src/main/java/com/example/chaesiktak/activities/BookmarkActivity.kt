package com.example.chaesiktak.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.adapters.SavedItemAdapter
import kotlinx.coroutines.launch

class BookmarkActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SavedItemAdapter
    private val bookmarkList = ArrayList<RecommendRecipe>() // 즐겨찾기된 레시피 목록

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        recyclerView = findViewById(R.id.recyclerView)

        adapter = SavedItemAdapter(bookmarkList) { recipe ->
            navigateToRecipeDetail(recipe)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 뒤로가기 버튼 설정
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener { finish() }

        // 즐겨찾기된 레시피 목록 가져오기
        fetchFavoriteRecipes()
    }

    private fun fetchFavoriteRecipes() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(this@BookmarkActivity).GetFavoriteList()

                if (response.isSuccessful && response.body() != null) {
                    val favoriteRecipes = response.body()?.data ?: emptyList()

                    bookmarkList.clear()
                    bookmarkList.addAll(favoriteRecipes)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@BookmarkActivity, "즐겨찾기 목록을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("BookmarkActivity", "네트워크 오류: ${e.message}")
                Toast.makeText(this@BookmarkActivity, "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToRecipeDetail(recipe: RecommendRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java).apply {
            putExtra("RECIPE_ID", recipe.id)
            putExtra("IS_FAVORITE", recipe.isFavorite)
        }
        startActivity(intent)
    }
}
