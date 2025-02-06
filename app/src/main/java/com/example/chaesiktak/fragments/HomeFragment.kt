package com.example.chaesiktak.fragments

import com.example.chaesiktak.adapters.BannerAdapter
import RecommendRecipeAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.chaesiktak.*
import com.example.chaesiktak.activities.RecipeDetailActivity
import com.example.chaesiktak.activities.SearchPanelActivity
import com.example.chaesiktak.adapters.TagRecipeAdapter
import com.example.chaesiktak.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendRecipeAdapter: RecommendRecipeAdapter
    private lateinit var tagRecipeAdapter: TagRecipeAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var viewPager: ViewPager2

    private val recipeList: ArrayList<RecommendRecipe> = arrayListOf()
    private val tagRecipeList: ArrayList<RecommendRecipe> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupSearchBar()
        setupRecyclerViews()
        setupBanner()
        setupBottomNavigation()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSearchBar() {
        binding.homeSearchBar.setOnClickListener {
            startActivity(Intent(requireContext(), SearchPanelActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        recipeList.addAll(getSampleRecipes())
        tagRecipeList.addAll(getTagRecipes())

        recommendRecipeAdapter = RecommendRecipeAdapter(recipeList).apply {
            onItemClick = { navigateToRecipeDetail(it) }
        }
        binding.recipeRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendRecipeAdapter
            setHasFixedSize(true)
        }

        tagRecipeAdapter = TagRecipeAdapter(tagRecipeList).apply {
            onItemClick = { navigateToRecipeDetail(it) }
        }
        binding.tagRecipeRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = tagRecipeAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupBanner() {
        val banners = listOf(
            BannerData("비건 라이프를 더 간편하게,", "채식탁으로 시작하세요!", R.drawable.banner_icon),
            BannerData("건강한 식단을 준비하세요!", "지금 시작해보세요.", R.drawable.banner_icon),
            BannerData("환경을 생각하는 선택!", "채식의 시작은 여기서.", R.drawable.banner_icon)
        )

        bannerAdapter = BannerAdapter(banners)
        viewPager = binding.banner
        viewPager.adapter = bannerAdapter

        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % banners.size
        viewPager.setCurrentItem(startPosition, false)

        binding.homeBannerIndicator.createIndicators(banners.size, 0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val realPosition = position % banners.size
                binding.homeBannerIndicator.animatePageSelected(realPosition)
            }
        })

        startAutoScroll(banners.size)
    }

    private fun startAutoScroll(bannerSize: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeat(Int.MAX_VALUE) { // 무한 반복
                delay(2500) // 2.5초 간격
                if (::viewPager.isInitialized && bannerSize > 0) {
                    val nextPage = (viewPager.currentItem + 1) % Int.MAX_VALUE
                    viewPager.setCurrentItem(nextPage, true)
                    binding.homeBannerIndicator.animatePageSelected(nextPage % bannerSize)
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.scannerTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_scannerFragment)
        }
        binding.myinfoTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_myInfoFragment)
        }
    }

    private fun navigateToRecipeDetail(recipe: RecommendRecipe) {
        val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
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
                    recommendRecipeAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    //데이터 예시 - 추천 레시피 9개
    private fun getSampleRecipes(): List<RecommendRecipe> {
        val recipes = SampleRecipes.recipes
        return if (recipes.size > 9) recipes.subList(0, 9) else recipes
    }

    private fun getTagRecipes(): List<RecommendRecipe> {
        val recipes = SampleRecipes.recipes
        return if (recipes.size > 6) recipes.subList(0, 6) else recipes
    }



}
