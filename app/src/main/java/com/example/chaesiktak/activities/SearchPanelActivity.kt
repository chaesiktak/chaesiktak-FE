package com.example.chaesiktak.activities

import CustomToast
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.activities.SearchResultActivity
import com.example.chaesiktak.databinding.ActivitySearchPanelBinding

class SearchPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchPanelBinding
    private var selectedFilterID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 0 until binding.ingredientGroup.childCount) {
            val radioButton = binding.ingredientGroup.getChildAt(i)
            if (radioButton is RadioButton) {
                radioButton.setOnClickListener {
                    selectedFilterID = radioButton.id  // 선택한 태그 ID 저장
                    val selectedText = radioButton.text.toString()
                    navigateToSearchResult(selectedText)
                }
            }
        }


        binding.searchGoBtn.setOnClickListener {
            val searchText = binding.searchInput.text.toString().trim()
            if (searchText.isNotEmpty()) {
                navigateToSearchResult(searchText)
            } else {
                CustomToast.show(this, "검색어를 입력하거나 태그를 선택해주세요")
            }
        }
        // 뒤로 가기
        binding.backArrowIcon.setOnClickListener {
            finish()
            resetTagSelection()
        }

        // 홈 버튼
        binding.homeIcon.setOnClickListener {
            finish()
            resetTagSelection()
        }
    }
    private fun navigateToSearchResult(searchText: String) {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra("search_text", searchText)
        startActivity(intent)
    }

    private fun resetTagSelection() {
        if( selectedFilterID != -1) {
            binding.ingredientGroup.clearCheck()
            binding.ingredientGroup.requestLayout()
            selectedFilterID = -1 // 선택된 RadioButton 해제
        }
    }

}
