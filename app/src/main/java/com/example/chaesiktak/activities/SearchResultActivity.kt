package com.example.chaesiktak.activities

import SearchingContentAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chaesiktak.R
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivitySearchResultBinding
import com.example.chaesiktak.databinding.FilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.*
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch



class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding
    private val recipeList: ArrayList<RecommendRecipe> = arrayListOf()
    private lateinit var searchingContentAdapter: SearchingContentAdapter
    private var selectedFilterID: Int = -1

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
        fetchAndSetupRecipes(searchText)

        //검색버튼 클릭 메서드
        binding.searchGoBtn.setOnClickListener {
            val newSearchText = binding.searchInput.text.toString().trim()

            if (newSearchText.isNotEmpty()) {
                filterRecipes(newSearchText) // 새로운 검색어로 필터링 적용
                updateRecipeCountText()
            } else {
                binding.searchInput.error = "검색어를 입력하세요."
            }
            updateFilterIcon(false)
        }

        // 뒤로 가기 버튼 클릭 메서드
        binding.backArrowIcon.setOnClickListener { finish() }

        // 홈 버튼 클릭 메서드
        binding.homeIcon.setOnClickListener { finish() }

        //필터 버튼 클릭 메서드
        binding.filterText.setOnClickListener { showFilterBottomSheet(binding.filterText) }

        // RecyclerView 설정 (초기 검색어 적용)
        setupRecyclerViews(searchText)
    }

    private fun fetchAndSetupRecipes(searchText: String) {
        lifecycleScope.launch {
            // recipeList에 데이터가 없는 경우만 API 호출
            if (recipeList.isEmpty()) {
                recipeList.addAll(fetchAllRecipes())
            }

            val filteredRecipes = filterRecipeList(recipeList, searchText)

            searchingContentAdapter = SearchingContentAdapter(filteredRecipes.toMutableList()).apply {
                onItemClick = { navigateToRecipeDetail(it) }
            }

            binding.searchingContentRecyclerview.apply {
                layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
                adapter = searchingContentAdapter
                setHasFixedSize(true)
            }

            updateRecipeCountText()
        }
    }


    //표시되는 레시피 수
    private fun updateRecipeCountText() {
        val displayedCount = searchingContentAdapter.getCurrentListsize()
        binding.recipeCountText.text = "총 ${displayedCount}개"
    }

    private fun setupRecyclerViews(searchText: String) {
        lifecycleScope.launch {
            val allRecipes = fetchAllRecipes()
            recipeList.clear()
            recipeList.addAll(allRecipes)

            val filteredRecipes = filterRecipeList(recipeList, searchText)

            searchingContentAdapter = SearchingContentAdapter(filteredRecipes.toMutableList()).apply {
                onItemClick = { navigateToRecipeDetail(it) }
            }

            binding.searchingContentRecyclerview.apply {
                layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
                adapter = searchingContentAdapter
                setHasFixedSize(true)
            }

            updateRecipeCountText()
        }
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
            recipes.filter { recipe ->
                recipe.title.contains(searchText, ignoreCase = true) ||
                        recipe.ingredients.any { it.name.contains(searchText, ignoreCase = true) }
            }
        } else {
            recipes
        }
    }

    // 검색어 입력 시 RecyclerView 업데이트
    private fun filterRecipes(searchText: String) {
        lifecycleScope.launch {
            val filteredRecipes = filterRecipeList(recipeList, searchText)
            searchingContentAdapter.updateList(filteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }

    //필터 클릭시 , 하단에서 bottom sheet
    private fun showFilterBottomSheet(tvFilter: ImageView) {
        val dialog = BottomSheetDialog(this)
        val filterbinding = FilterBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(filterbinding.root)

        var isChecking = true
        var selectedFilterText: String? = null

        if (selectedFilterID != -1){
            filterbinding.root.findViewById<RadioButton>(selectedFilterID)?.isChecked = true
            selectedFilterText = filterbinding.root.findViewById<RadioButton>(selectedFilterID)?.text?.toString()
        }

        //첫 번째 linear layout의 태그 선택 시, 두 번째 linear layout 그룹의 태그 선택 해제
        filterbinding.firstGroup.setOnCheckedChangeListener { _, id ->
            if (id != -1 && isChecking) {
                isChecking = false
                filterbinding.secondGroup.clearCheck()
                selectedFilterID = id
                selectedFilterText = filterbinding.root.findViewById<RadioButton>(id)?.text?.toString()
            }
            isChecking = true
        }

        //두 번째 linear layout의 태그 선택 시, 첫 번째 linear layout 그룹의 태그 선택 해제
        filterbinding.secondGroup.setOnCheckedChangeListener { _, id ->
            if (id != -1 && isChecking) {
                isChecking = false
                filterbinding.firstGroup.clearCheck()
                selectedFilterID = id
                selectedFilterText = filterbinding.root.findViewById<RadioButton>(id)?.text?.toString()
            }
            isChecking = true
        }

        filterbinding.goFilter.setOnClickListener {
            selectedFilterText?.let { text ->
                filterRecipesByTag(text)
                updateFilterIcon(true)
                dialog.dismiss()
            }
        }

        // 'X' 버튼 누르면 bottom sheet dismiss
        filterbinding.filterClose.setOnClickListener {
            dialog.dismiss()
        }

        // 선택 초기화 >> 원래 'searchText'로 호출했던 리사이클 뷰 생성 및 radiobutton false값으로 초기화
        filterbinding.filterReset.setOnClickListener{
            selectedFilterID = -1
            selectedFilterText = null

            isChecking = false
            filterbinding.firstGroup.clearCheck()
            filterbinding.secondGroup.clearCheck()
            isChecking = true

            filterbinding.firstGroup.post { filterbinding.firstGroup.clearCheck() }
            filterbinding.secondGroup.post { filterbinding.secondGroup.clearCheck() }

            updateFilterIcon(false)
            resetFilteredRecipes()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun filterRecipesByTag(selectedTag: String) {
        lifecycleScope.launch {
            val filteredBySearchText = filterRecipeList(recipeList, binding.searchInput.text.toString().trim())
            val finalFilteredRecipes = filteredBySearchText.filter { it.tag?.equals(selectedTag) == true }

            searchingContentAdapter.updateList(finalFilteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }

    private fun updateFilterIcon(isFiltered: Boolean) {
        if (isFiltered) {
            binding.filterText.setImageResource(R.drawable.filter_text_selected)
        } else {
            binding.filterText.setImageResource(R.drawable.filter_text)
        }
    }

    private fun resetFilteredRecipes() {
        lifecycleScope.launch {
            val filteredBySearchText = filterRecipeList(recipeList, binding.searchInput.text.toString().trim())
            searchingContentAdapter.updateList(filteredBySearchText.toMutableList())
            updateRecipeCountText()
        }
    }

    private suspend fun fetchAllRecipes(): List<RecommendRecipe> {
        return try {
            val recipeIds = listOf(1, 2, 3, 4, 5, 6, 7, 8) // 하드코딩된 ID 사용
            val recipeDetails = recipeIds.mapNotNull { fetchRecipeDetail(it) }

            if (recipeDetails.isEmpty()) {
                showError("레시피 데이터를 불러올 수 없습니다.")
            }

            recipeDetails
        } catch (e: Exception) {
            showError("네트워크 오류: ${e.message}")
            emptyList()
        }
    }

    private suspend fun fetchRecipeDetail(recipeId: Int): RecommendRecipe? {
        return try {
            val response = RetrofitClient.instance(this).getRecipeDetail(recipeId)

            if (response.isSuccessful) {
                response.body()?.data
            } else {
                showError("서버 응답 오류: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            showError("네트워크 오류: ${e.message}")
            null
        }
    }


    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
