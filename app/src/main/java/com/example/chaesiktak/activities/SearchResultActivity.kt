package com.example.chaesiktak.activities

import SearchingContentAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.SampleRecipes
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
        val searchText = intent.getStringExtra("search_text")?.trim() ?: ""


        // searchInput에 검색어 출력
        binding.searchInput.setText(searchText)
        binding.searchInput.setSelection(searchText.length) // 커서 맨 마지막으로

        setupRecyclerViews(searchText)

        updateRecipeCountText()

        //검색버튼 클릭 메서드
        binding.searchGoBtn.setOnClickListener {
            val newSearchText = binding.searchInput.text.toString().trim()

            if (newSearchText.isNotEmpty()) {
                filterRecipes(newSearchText) // 새로운 검색어로 필터링 적용
                updateRecipeCountText()
            } else {
                binding.searchInput.error = "검색어를 입력하세요."
            }
        }

        // 뒤로 가기 버튼 클릭 메서드
        binding.backArrowIcon.setOnClickListener {
            finish()
        }

        // 홈 버튼 클릭 메서드
        binding.homeIcon.setOnClickListener {
            finish()
        }

        // RecyclerView 설정 (초기 검색어 적용)
        setupRecyclerViews(searchText)
    }

    private fun setupRecyclerViews(searchText: String) {
        val allRecipes = getSampleRecipes() // 전체 레시피 가져오기
        val filteredRecipes = filterRecipeList(allRecipes, searchText) // 검색 필터 적용

        searchingContentAdapter = SearchingContentAdapter(filteredRecipes.toMutableList()).apply {
            onItemClick = { navigateToRecipeDetail(it) }
        }

        binding.searchingContentRecyclerview.apply {
            layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
            adapter = searchingContentAdapter
            setHasFixedSize(true)
        }
    }

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

    // 레시피 타이틀 필터링 함수
    private fun filterRecipeList(recipes: List<RecommendRecipe>, searchText: String): List<RecommendRecipe> {
        return if (searchText.isNotEmpty()) {
            recipes.filter { it.title.contains(searchText, ignoreCase = true) } // `recipes`로 필터링
        } else {
            recipes // 검색어가 없으면 전체 반환
        }
    }

    // 검색어 입력 시 RecyclerView 업데이트
    private fun filterRecipes(searchText: String) {
        val allRecipes = getSampleRecipes()
        val filteredRecipes = filterRecipeList(allRecipes, searchText)

        searchingContentAdapter.updateList(filteredRecipes.toMutableList())

        // 리스트 개수 업데이트 추가
        updateRecipeCountText()
    }


    private fun updateRecipeCountText() {
        val displayedCount = searchingContentAdapter.getCurrentListsize()
        binding.recipeCountText.text = "총 ${displayedCount}개"
    }

}
