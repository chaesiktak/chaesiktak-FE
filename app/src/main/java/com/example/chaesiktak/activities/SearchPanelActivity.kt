package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.activities.SearchResultActivity
import com.example.chaesiktak.databinding.ActivitySearchPanelBinding

class SearchPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchPanelBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchGoBtn.setOnClickListener {
            val searchText = binding.searchInput.text.toString().trim()

            if (searchText.isNotEmpty()) {
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra("search_text", searchText)  // Intent에 검색어 저장
                startActivity(intent)
            } else {
                binding.searchInput.error = "검색어를 입력하세요."
            }
        }
        // 뒤로 가기
        binding.backArrowIcon.setOnClickListener {
            finish()
        }

        // 홈 버튼
        binding.homeIcon.setOnClickListener {
            finish()
        }
    }
}
