package com.example.chaesiktak.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.SampleRecipes
import com.example.chaesiktak.SearchingContentAdapter
import com.example.chaesiktak.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private val recipeList: ArrayList<RecommendRecipe> = arrayListOf()
    private lateinit var searchingContentAdapter: SearchingContentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 검색어 받아오기
        val searchText = intent.getStringExtra("search_text") ?: "검색 결과 없음"

        // searchInput에 검색어 출력
        binding.searchInput.setText(searchText)
        binding.searchInput.setSelection(searchText.length) // 커서 이동

        binding.searchGoBtn.setOnClickListener {
            val newSearchText = binding.searchInput.text.toString().trim()

            if (newSearchText.isNotEmpty()) {
                binding.searchInput.setText(newSearchText)
                binding.searchInput.setSelection(newSearchText.length) // 커서 이동
            } else {
                binding.searchInput.error = "검색어를 입력하세요."
            }
        }

        // 뒤로 가기 버튼
        binding.backArrowIcon.setOnClickListener {
            finish()
        }

        // 홈 버튼
        binding.homeIcon.setOnClickListener {
            finish()
        }

        // RecyclerView 설정
        setupRecyclerViews()
    }

    private fun setupRecyclerViews() {
        recipeList.addAll(getSampleRecipes())

        searchingContentAdapter = SearchingContentAdapter(recipeList).apply {
            onItemClick = { navigateToRecipeDetail(it) }
        }

        binding.searchingContentRecyclerview.apply {
            layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
            adapter = searchingContentAdapter
            setHasFixedSize(true)
        }
    }

    // 데이터 예시 - 일단 6개
    private fun getSampleRecipes(): List<RecommendRecipe> {
        return SampleRecipes.recipes
    }

    private fun navigateToRecipeDetail(recipe: RecommendRecipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java).apply {
            putExtra("RECIPE", recipe)
        }
        recipeDetailLauncher.launch(intent)
    }

    private val recipeDetailLauncher = registerForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedRecipe = result.data?.getParcelableExtra<RecommendRecipe>("UPDATED_RECIPE")
            updatedRecipe?.let { updated ->
                val index = recipeList.indexOfFirst { it.title == updated.title }
                if (index != -1) {
                    recipeList[index] = updated
                    searchingContentAdapter.notifyItemChanged(index)
                }
            }
        }
    }
}
