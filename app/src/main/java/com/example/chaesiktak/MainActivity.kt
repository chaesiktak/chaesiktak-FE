package com.example.chaesiktak

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    /*
    companion object {
        const val REQUEST_CODE_EDIT_PROFILE = 1
    }

    private lateinit var nameTextView: TextView
    private lateinit var nicknameTextView: TextView

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        /*
        nameTextView = findViewById(R.id.nameTextView)
        nicknameTextView = findViewById(R.id.nicknameTextView)

        // SharedPreferences에서 현재 설정된 이름과 닉네임 가져오기
        val sharedPref = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val currentName = sharedPref.getString("name", "이름") ?: "이름"
        val currentNickname = sharedPref.getString("nickname", "닉네임") ?: "닉네임"

        nameTextView.text = currentName
        nicknameTextView.text = currentNickname

        // 프로필 버튼 클릭 시
        val editProfileButton = findViewById<Button>(R.id.profileButton)
        editProfileButton.setOnClickListener {
            val intent = Intent(this, ProfileEditActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_EDIT_PROFILE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_EDIT_PROFILE && resultCode == Activity.RESULT_OK) {
            val newName = data?.getStringExtra("newName")
            val newNickname = data?.getStringExtra("newNickname")

            if (!newName.isNullOrBlank() && !newNickname.isNullOrBlank()) {
                // 변경된 이름과 닉네임을 SharedPreferences에 저장
                val sharedPref = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("name", newName)
                    putString("nickname", newNickname)
                    apply()
                }

                nameTextView.text = newName
                nicknameTextView.text = newNickname
            }
        }

         */
    }
}