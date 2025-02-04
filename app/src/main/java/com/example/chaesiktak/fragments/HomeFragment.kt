package com.example.chaesiktak.fragments
import BannerAdapter
import RecommendRecipeAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.chaesiktak.BannerData
import com.example.chaesiktak.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chaesiktak.HomeActivity
import com.example.chaesiktak.Ingredient
import com.example.chaesiktak.RecipeDetailActivity
import com.example.chaesiktak.RecommendRecipe
import com.example.chaesiktak.SearchPanel
import com.example.chaesiktak.TagRecipe
import com.example.chaesiktak.TagRecipeAdapter
import com.example.chaesiktak.databinding.FragmentHomeBinding
import com.example.chaesiktak.databinding.HomeRecipeCardBinding
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var tagrecyclerView: RecyclerView

    private var tagRecipeList: ArrayList<TagRecipe> = ArrayList()

    private lateinit var recommendrecipeAdapter: RecommendRecipeAdapter
    private lateinit var tagRecipeAdapter: TagRecipeAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: BannerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 검색창 클릭 > 검색 패널
        binding.homeSearchBar.setOnClickListener {
            startActivity(Intent(requireContext(), SearchPanel::class.java))
        }

        // RecyclerView 설정
        recyclerView = binding.recipeRecyclerView
        tagrecyclerView = binding.tagRecipeRecyclerView

        recyclerView.setHasFixedSize(true)
        tagrecyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        tagrecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // RecyclerView 데이터 설정 > 추천 레시피 list
        val recipeList = arrayListOf<RecommendRecipe>().apply {
            add(
                RecommendRecipe(
                    image = R.drawable.sample_image,
                    title = "비건 김치찌개",
                    subtext = "1인분, 20분",
                    kcal = "250kcal",
                    tag = "비건",
                    isFavorite = false,
                    ingredients = listOf(
                        Ingredient("배추김치", "200g"),
                        Ingredient("두부", "1/2모"),
                        Ingredient("양파", "1개"),
                        Ingredient("다진 마늘", "1큰술"),
                        Ingredient("고춧가루", "1큰술"),
                        Ingredient("소금", "약간")
                    ),
                    contents = listOf(
                        "1. 냄비에 물을 넣고 끓인 후, 김치와 양파를 넣어 5분간 끓입니다.",
                        "2. 다진 마늘과 고춧가루를 넣고 잘 저어줍니다.",
                        "3. 두부를 먹기 좋은 크기로 잘라 넣고 3분간 더 끓입니다.",
                        "4. 소금으로 간을 맞춘 후 완성된 김치찌개를 그릇에 담아 즐기세요!"
                    )
                )
            )

            add(
                RecommendRecipe(
                    image = R.drawable.sample_image,
                    title = "채소 카레",
                    subtext = "4인분, 50분",
                    kcal = "380kcal",
                    tag = "락토",
                    isFavorite = false,
                    ingredients = listOf(
                        Ingredient("감자", "2개"),
                        Ingredient("당근", "1개"),
                        Ingredient("양파", "1개"),
                        Ingredient("카레 가루", "3큰술"),
                        Ingredient("물", "2컵"),
                        Ingredient("소금", "약간")
                    ),
                    contents = listOf(
                        "1. 감자, 당근, 양파를 깍둑썰기 하고 냄비에 기름을 두른 후 양파를 볶습니다.",
                        "2. 감자와 당근을 넣고 3분간 더 볶은 후 물을 넣어 끓입니다.",
                        "3. 감자가 익으면 카레 가루를 넣고 잘 저어줍니다.",
                        "4. 약불에서 5분간 더 끓인 후 완성된 카레를 그릇에 담아 맛있게 드세요!"
                    )
                )
            )

            add(
                RecommendRecipe(
                    image = R.drawable.sample_image,
                    title = "비건 떡볶이",
                    subtext = "3인분, 30분",
                    kcal = "360kcal",
                    tag = "락토오보",
                    isFavorite = false,
                    ingredients = listOf(
                        Ingredient("떡", "300g"),
                        Ingredient("고추장", "2큰술"),
                        Ingredient("간장", "1큰술"),
                        Ingredient("설탕", "1큰술"),
                        Ingredient("양파", "1/2개"),
                        Ingredient("양배추", "1/4개"),
                        Ingredient("물", "1컵"),
                        Ingredient("올리고당", "1큰술")
                    ),
                    contents = listOf(
                        "1. 냄비에 물을 넣고 끓인 후, 고추장, 간장, 설탕을 넣어 양념장을 만듭니다.",
                        "2. 양파와 양배추를 썰어 냄비에 넣고 3분간 끓입니다.",
                        "3. 떡을 넣고 약불에서 5분간 끓이며 잘 저어줍니다.",
                        "4. 올리고당을 추가한 후 떡볶이를 접시에 담아 완성합니다!"
                    )
                )
            )
        }


        // RecyclerView 데이터 설정 > 태그 레시피 list
        tagRecipeList.apply {
            add(TagRecipe(R.color.card_subtext, "비건 레시피", "1인분, 15분", "비건"))
            add(TagRecipe(R.color.card_subtext, "락토 레시피", "2인분, 30분", "락토"))
            add(TagRecipe(R.color.card_subtext, "오보 레시피", "3인분, 45분", "오보"))
            add(TagRecipe(R.color.card_subtext, "락토오보 레시피", "4인분, 60분", "락토오보"))
            add(TagRecipe(R.color.card_subtext, "페스코 레시피", "4인분, 60분", "페스코"))
            add(TagRecipe(R.color.card_subtext, "폴로 레시피", "4인분, 60분", "폴로"))
        }

        // Adapter 연결
        recommendrecipeAdapter = RecommendRecipeAdapter(recipeList)
        recyclerView.adapter = recommendrecipeAdapter

        recommendrecipeAdapter.onItemClick = { recipe ->
            val intent = Intent(requireContext(), RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE", recipe)  // 클릭된 레시피 객체 전달
            }
            startActivity(intent)
        }


        // 두 번째 RecyclerView에 TagRecipeAdapter 연결
        tagRecipeAdapter = TagRecipeAdapter(tagRecipeList)
        tagrecyclerView.adapter = tagRecipeAdapter  // 올바르게 어댑터 연결

        // 배너 데이터 설정
        val originalBanners = listOf(
            BannerData("비건 라이프를 더 간편하게,", "채식탁으로 시작하세요!", R.drawable.banner_icon),
            BannerData("건강한 식단을 준비하세요!", "지금 시작해보세요.", R.drawable.banner_icon),
            BannerData("환경을 생각하는 선택!", "채식의 시작은 여기서.", R.drawable.banner_icon)
        )

        adapter = BannerAdapter(originalBanners)
        viewPager = binding.banner
        viewPager.adapter = adapter

        // 초기에 중간 지점으로 이동 (무한 스크롤 효과)
        val startPosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % originalBanners.size
        viewPager.setCurrentItem(startPosition, false)

        // Indicator 개수 설정 (원본 배너 개수 3개로 설정)
        binding.homeBannerIndicator.createIndicators(originalBanners.size, 0)

        // Indicator & 무한 스크롤 동기화
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 실제 배너 개수에 맞춰서 위치 조정
                val realPosition = position % originalBanners.size
                binding.homeBannerIndicator.animatePageSelected(realPosition)
            }
        })

        // 자동 스크롤 시작
        startAutoScroll(binding, originalBanners.size)

        // 하단 탭 네비게이션 설정
        binding.scannerTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_scannerFragment)
        }
        binding.myinfoTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_myInfoFragment)
        }

        return binding.root
    }

    private fun startAutoScroll(binding: FragmentHomeBinding, bannerSize: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            while (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                delay(2500)
                if (::viewPager.isInitialized) {
                    val nextPage = viewPager.currentItem + 1
                    viewPager.setCurrentItem(nextPage, true)

                    // Indicator도 함께 업데이트
                    val realPosition = nextPage % bannerSize
                    binding.homeBannerIndicator.animatePageSelected(realPosition)
                }
            }
        }
    }

    private fun stopAutoScroll() {
        viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
    }
}



