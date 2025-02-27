package com.example.chaesiktak
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecentViewManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("recent_views", Context.MODE_PRIVATE)
    private val gson = Gson()

    // 최근 본 항목 저장 (최대 10개 유지)
    fun saveRecentItem(recipeId: Int) {
        val items = getRecentItems().toMutableList()
        items.remove(recipeId) // 중복 제거
        items.add(0, recipeId) // 최신 항목을 맨 앞에 추가
        if (items.size > 10) items.removeAt(items.lastIndex) // 최대 10개 유지

        val json = gson.toJson(items)
        prefs.edit().putString("recent_list", json).apply()
    }

    // 최근 본 항목 가져오기
    fun getRecentItems(): List<Int> {
        val json = prefs.getString("recent_list", null) ?: return emptyList()
        val type = object : TypeToken<List<Int>>() {}.type
        return gson.fromJson(json, type)
    }
}
