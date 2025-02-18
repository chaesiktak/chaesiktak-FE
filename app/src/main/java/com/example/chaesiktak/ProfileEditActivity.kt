package com.example.chaesiktak

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_edit)


        // Intent로 전달된 값 가져오기
        val currentName = intent.getStringExtra("NAME") ?: ""
        val currentNickname = intent.getStringExtra("NICKNAME") ?: ""

        // 중복된 닉네임 리스트 (예제용)
        val existingNicknames = listOf("user1", "user2", "user3")

        // EditText에 hint로 설정하기
        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val nicknameEditText = findViewById<EditText>(R.id.nicknameEditText)

        nameEditText.hint = currentName
        nicknameEditText.hint = currentNickname

        // 수정 버튼 클릭 리스너 설정
        val updateButton = findViewById<Button>(R.id.updateButton)
        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newNickname = nicknameEditText.text.toString()

            // 특수문자 확인을 위한 정규 표현식
            val specialCharacterRegex = Regex("[^a-zA-Z0-9]")

            when {
                newNickname.isEmpty() -> {
                    Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                existingNicknames.contains(newNickname) -> {
                    Toast.makeText(this, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                }
                specialCharacterRegex.containsMatchIn(newNickname) -> {
                    Toast.makeText(this, "닉네임에는 특수문자를 포함할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                newName == currentName && newNickname == currentNickname -> {
                    Toast.makeText(this, "이름/닉네임을 변경하여주세요.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // 입력된 값을 SharedPreferences에 저장
                    val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("NAME", newName)
                    editor.putString("NICKNAME", newNickname)
                    editor.apply()

                    Toast.makeText(this, "프로필이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()

                    // 프로필 업데이트 후 이전 화면으로 돌아가기
                    finish()
                }
            }
        }

        // 취소 버튼 클릭 리스너 설정
        val cancelButton = findViewById<Button>(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // 현재 액티비티 종료하여 이전 프래그먼트로 돌아가기
            finish()
            }

        // 뒤로가기 버튼 클릭 리스너 설정
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
            finish()
        }
    }

}