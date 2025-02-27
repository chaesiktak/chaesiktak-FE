package com.example.chaesiktak.activities

import CustomToast
import SearchingContentAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.chaesiktak.R
import com.example.chaesiktak.RecentViewManager
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.databinding.ActivitySearchResultBinding
import com.example.chaesiktak.databinding.IngredientBottomSheetBinding
import com.example.chaesiktak.databinding.FilterBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class SearchResultActivity : AppCompatActivity() {

    private val recentViewManager by lazy { RecentViewManager(this) }
    private lateinit var binding: ActivitySearchResultBinding
    private val recipeList: ArrayList<RecommendRecipe> = arrayListOf()
    private lateinit var searchingContentAdapter: SearchingContentAdapter
    private var selectedFilterID: Int = -1
    private val selectedIngredients: MutableList<String> = mutableListOf()
    private val dislikedIngredients: MutableList<String> = mutableListOf()
    private var selectedFilterTag: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 검색어 받아오기
        val searchText = intent.getStringExtra("search_text")?.trim() ?: ""

        // searchInput에 검색어 출력
        binding.searchInput.setText(searchText)
        binding.searchInput.setSelection(searchText.length) // 커서 맨 마지막으로

        fetchAndSetupRecipes(searchText)

        //검색버튼 클릭 메서드
        binding.searchGoBtn.setOnClickListener {
            val newSearchText = binding.searchInput.text.toString().trim()

            if (newSearchText.isNotEmpty()) {
                filterRecipes(newSearchText) // 새로운 검색어로 필터링 적용
                updateRecipeCountText()
            } else {
                CustomToast.show(this, "검색어를 입력하세요")
            }
            updateFilterIcon(false)
            updateIngredientIcon(false)
        }

        // 뒤로 가기 버튼 클릭 메서드
        binding.backArrowIcon.setOnClickListener { finish() }

        // 홈 버튼 클릭 메서드
        binding.homeIcon.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )
        }

        //태그 필터 버튼 클릭 메서드
        binding.filterText.setOnClickListener { showFilterBottomSheet(binding.filterText) }

        //재료 필터 버튼 클릭 메서드
        binding.ingredientFilter.setOnClickListener { showIngredientBottomSheet(binding.ingredientFilter) }

    }

    private fun fetchAndSetupRecipes(searchText: String) {
        lifecycleScope.launch {
            // recipeList에 데이터가 없는 경우만 API 호출
            if (recipeList.isEmpty()) {
                recipeList.addAll(fetchAllRecipes())
            }

            val filteredRecipes = filterRecipeList(recipeList, searchText)

            searchingContentAdapter =
                SearchingContentAdapter(filteredRecipes.toMutableList()).apply {
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

    private fun navigateToRecipeDetail(recipe: RecommendRecipe) {
        recentViewManager.saveRecentItem(recipe.id) // 최근 본 항목 저장

        val intent = Intent(this, RecipeDetailActivity::class.java).apply {
            putExtra("RECIPE_ID", recipe.id) // ID만 전달
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
    private fun filterRecipeList(
        recipes: List<RecommendRecipe>,
        searchText: String
    ): List<RecommendRecipe> {
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

    //[타입] 필터 클릭시 , 하단에서 bottom sheet
    private fun showFilterBottomSheet(tvFilter: ImageView) {
        val dialog = BottomSheetDialog(this)
        val filterbinding = FilterBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(filterbinding.root)

        var isChecking = true
        selectedFilterTag?.let { tag ->
            val selectedRadioButton = filterbinding.root.findViewWithTag<RadioButton>(tag)
            selectedRadioButton?.isChecked = true
        }

        filterbinding.firstGroup.setOnCheckedChangeListener { _, id ->
            if (id != -1 && isChecking) {
                isChecking = false
                filterbinding.secondGroup.clearCheck()
                selectedFilterTag = filterbinding.root.findViewById<RadioButton>(id)?.text?.toString()
            }
            isChecking = true
        }

        filterbinding.secondGroup.setOnCheckedChangeListener { _, id ->
            if (id != -1 && isChecking) {
                isChecking = false
                filterbinding.firstGroup.clearCheck()
                selectedFilterTag = filterbinding.root.findViewById<RadioButton>(id)?.text?.toString()
            }
            isChecking = true
        }

        filterbinding.goFilter.setOnClickListener {
            filterRecipesByPreferenceAndTag()
            updateFilterIcon(selectedFilterTag != null)
            dialog.dismiss()
        }

        filterbinding.filterReset.setOnClickListener {
            selectedFilterTag = null
            filterbinding.firstGroup.clearCheck()
            filterbinding.secondGroup.clearCheck()

            filterRecipesByPreferenceAndTag()
            updateFilterIcon(false)
            dialog.dismiss()
        }

        dialog.show()

        filterbinding.filterClose.setOnClickListener {
            dialog.dismiss()
        }
    }


    //[재료] 필터시 , 하단에서 bottom sheet
    private fun showIngredientBottomSheet(tvFilter: ImageView) {
        val dialog = BottomSheetDialog(this)
        val ingredientBinding = IngredientBottomSheetBinding.inflate(layoutInflater)
        dialog.setContentView(ingredientBinding.root)

        val preferredCheckBoxes = listOf(
            ingredientBinding.checkboxSpinach to "시금치",
            ingredientBinding.checkboxPotato to "감자",
            ingredientBinding.checkboxEggplant to "가지"
        )

        val dislikedCheckBoxes = listOf(
            ingredientBinding.checkboxCoriander to "고수",
            ingredientBinding.checkboxCucumber to "오이",
            ingredientBinding.checkboxGarlic to "마늘"
        )

        // 기존 선택한 재료 반영
        preferredCheckBoxes.forEach { (checkBox, ingredient) ->
            checkBox.isChecked = selectedIngredients.contains(ingredient)
        }

        dislikedCheckBoxes.forEach { (checkBox, ingredient) ->
            checkBox.isChecked = dislikedIngredients.contains(ingredient)
        }

        ingredientBinding.goFilter.setOnClickListener {
            selectedIngredients.clear()
            dislikedIngredients.clear()

            preferredCheckBoxes.forEach { (checkBox, ingredient) ->
                if (checkBox.isChecked) selectedIngredients.add(ingredient)
            }

            dislikedCheckBoxes.forEach { (checkBox, ingredient) ->
                if (checkBox.isChecked) dislikedIngredients.add(ingredient)
            }

            filterRecipesByPreferenceAndTag()
            updateIngredientIcon(selectedIngredients.isNotEmpty() || dislikedIngredients.isNotEmpty())
            dialog.dismiss()
        }

        ingredientBinding.filterReset.setOnClickListener {
            selectedIngredients.clear()
            dislikedIngredients.clear()

            preferredCheckBoxes.forEach { (checkBox, _) -> checkBox.isChecked = false }
            dislikedCheckBoxes.forEach { (checkBox, _) -> checkBox.isChecked = false }

            filterRecipesByPreferenceAndTag()
            updateIngredientIcon(false)
            dialog.dismiss()
        }

        dialog.show()

        ingredientBinding.filterClose.setOnClickListener {
            dialog.dismiss()
        }
    }


    //선호 & 비선호 데이터 모아서 비선호 하나라도 포함될 경우, 리사이클러뷰 검색에서 제외
    private fun filterRecipesByPreference(
        preferredIngredients: List<String>,
        dislikedIngredients: List<String>
    ) {
        lifecycleScope.launch {
            val searchText = binding.searchInput.text.toString().trim()

            // 'searchText'로 기존 필터링된 레시피 가져오기
            val filteredBySearchText = filterRecipeList(recipeList, searchText)

            // 1. 선호 재료 포함 여부 확인
            val filteredByPreference = if (preferredIngredients.isNotEmpty()) {
                filteredBySearchText.filter { recipe ->
                    preferredIngredients.any { ingredient ->
                        recipe.ingredients.any { it.name.contains(ingredient, ignoreCase = true) }
                    }
                }
            } else {
                filteredBySearchText
            }

            // 2. 비선호 재료 포함 여부 확인 (비선호 재료가 포함된 레시피는 제거)
            val finalFilteredRecipes = filteredByPreference.filter { recipe ->
                dislikedIngredients.none { ingredient ->
                    recipe.ingredients.any { it.name.contains(ingredient, ignoreCase = true) }
                }
            }

            // RecyclerView 업데이트
            searchingContentAdapter.updateList(finalFilteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }

    private fun filterRecipesByIngredient(selectedIngredients: List<String>) {
        lifecycleScope.launch {
            val searchText = binding.searchInput.text.toString().trim()

            // 'searchText'로 기존 필터링된 레시피 가져오기
            val filteredBySearchText = filterRecipeList(recipeList, searchText)

            // 선택된 재료 중 하나라도 포함된 레시피만 필터링
            val finalFilteredRecipes = filteredBySearchText.filter { recipe ->
                selectedIngredients.any { ingredient ->
                    recipe.ingredients.any { it.name.contains(ingredient, ignoreCase = true) }
                }
            }
            // RecyclerView 업데이트
            searchingContentAdapter.updateList(finalFilteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }

    private fun filterRecipesByTag(selectedTag: String) {
        lifecycleScope.launch {
            val filteredBySearchText =
                filterRecipeList(recipeList, binding.searchInput.text.toString().trim())
            val finalFilteredRecipes =
                filteredBySearchText.filter { it.tag?.equals(selectedTag) == true }

            searchingContentAdapter.updateList(finalFilteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }

    private fun updateFilterIcon(isFiltered: Boolean) {
        if (isFiltered) {
            binding.filterText.setImageResource(R.drawable.type_filter_t)
        } else {
            binding.filterText.setImageResource(R.drawable.type_filter)
        }
    }

    private fun updateIngredientIcon(isFiltered: Boolean) {
        if (isFiltered) {
            binding.ingredientFilter.setImageResource(R.drawable.ingredient_filter_t)
        } else {
            binding.ingredientFilter.setImageResource(R.drawable.ingredient_filter)
        }
    }

    private fun resetFilteredRecipes() {
        lifecycleScope.launch {
            val filteredBySearchText =
                filterRecipeList(recipeList, binding.searchInput.text.toString().trim())
            searchingContentAdapter.updateList(filteredBySearchText.toMutableList())
            updateRecipeCountText()
        }
    }

    private suspend fun fetchAllRecipes(): List<RecommendRecipe> {
        return try {
            val recipeIds = (1..14).toList() // 하드코딩된 ID 사용
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
        CustomToast.show(this, message)
    }


    private fun filterRecipesByPreferenceAndTag() {
        lifecycleScope.launch {
            val searchText = binding.searchInput.text.toString().trim()
            val filteredBySearchText = filterRecipeList(recipeList, searchText)

            // 선호 재료 필터링
            val filteredByPreference = if (selectedIngredients.isNotEmpty()) {
                filteredBySearchText.filter { recipe ->
                    selectedIngredients.any { ingredient ->
                        recipe.ingredients.any { it.name.contains(ingredient, ignoreCase = true) }
                    }
                }
            } else {
                filteredBySearchText
            }

            // 비선호 재료 제외 필터링
            val filteredByDislike = filteredByPreference.filter { recipe ->
                dislikedIngredients.none { ingredient ->
                    recipe.ingredients.any { it.name.contains(ingredient, ignoreCase = true) }
                }
            }

            //  태그 필터링
            val finalFilteredRecipes = if (selectedFilterTag != null) {
                filteredByDislike.filter { it.tag?.equals(selectedFilterTag) == true }
            } else {
                filteredByDislike
            }

            searchingContentAdapter.updateList(finalFilteredRecipes.toMutableList())
            updateRecipeCountText()
        }
    }



}