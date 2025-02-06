package com.example.chaesiktak.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.activities.SearchPanelActivity
import com.example.chaesiktak.databinding.ActivitySearchResultBinding

class SearchResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 검색어 받아오기
        var searchText = intent.getStringExtra("search_text") ?: "검색 결과 없음"

        // searchInput에 검색어 출력
        binding.searchInput.setText(searchText)
        binding.searchInput.setSelection(searchText.length) //커서 이동


    }
}
