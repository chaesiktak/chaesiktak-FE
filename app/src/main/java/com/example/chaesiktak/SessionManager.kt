package com.example.chaesiktak.utils

import android.content.Context
import android.content.SharedPreferences

object UserSessionManager {
    private const val PREF_NAME = "user_session"
    private const val KEY_AUTH_TOKEN = "auth_token"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 토큰 저장
    fun saveToken(context: Context, token: String) {
        val editor = getPreferences(context).edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    // 토큰 가져오기
    fun getToken(context: Context): String? {
        return getPreferences(context).getString(KEY_AUTH_TOKEN, null)
    }

    // 토큰 삭제 (로그아웃 시)
    fun clearToken(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(KEY_AUTH_TOKEN)
        editor.apply()
    }
}
