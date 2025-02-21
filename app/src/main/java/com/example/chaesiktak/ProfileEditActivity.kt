package com.example.chaesiktak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.regex.Pattern

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var nicknameEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_edit)

        // nameEditText nicknameEditText 초기화
        nameEditText = findViewById(R.id.nameEditText)
        nicknameEditText = findViewById(R.id.nicknameEditText)

        // SharedPreferences에서 현재 설정된 이름과 닉네임 가져오기
        val sharedPref = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val currentName = sharedPref.getString("name", "현재 이름") ?: "현재 이름"
        val currentNickname = sharedPref.getString("nickname", "현재 닉네임") ?: "현재 닉네임"

        nameEditText.hint = currentName
        nicknameEditText.hint = currentNickname

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

        // 수정 버튼 클릭 시 이름과 닉네임 변경
        val updateButton = findViewById<Button>(R.id.updateButton)
        updateButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newNickname = nicknameEditText.text.toString()

            if (newName.isBlank() || newNickname.isBlank()) {
                // 이름 또는 닉네임이 비어 있을 경우 처리
                Toast.makeText(this, "이름과 닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (!isValidName(newName)) {
                // 입력 제한 조건
                Toast.makeText(this, "이름은 한글 또는 영문만 가능합니다.", Toast.LENGTH_SHORT).show()
            } else if (!isValidNickname(newNickname)) {
                Toast.makeText(this, "닉네임은 2글자 이상 8글자 이하이며, 한글, 영문, 숫자만 가능합니다.", Toast.LENGTH_SHORT).show()
            } else if (isNicknameDuplicate(newNickname)) {
                Toast.makeText(this, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // SharedPreferences에 이름과 닉네임 저장
                with (sharedPref.edit()) {
                    putString("name", newName)
                    putString("nickname", newNickname)
                    apply()
                }

                // 이름과 닉네임을 변경하고 메인 액티비티로 전달
                val resultIntent = Intent().apply {
                    putExtra("newName", newName)
                    putExtra("newNickname", newNickname)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    // 이름 유효성 검사
    private fun isValidName(name: String): Boolean {
        val pattern = Pattern.compile("^[가-힣a-zA-Z]+$")
        return pattern.matcher(name).matches()
    }

    // 닉네임 유효성 검사
    private fun isValidNickname(nickname: String): Boolean {
        val pattern = Pattern.compile("^[가-힣a-zA-Z0-9]{2,8}$")
        return pattern.matcher(nickname).matches()
    }

    // 닉네임 중복 확인 (예제)
    private fun isNicknameDuplicate(nickname: String): Boolean {
        // 여기에 실제 닉네임 중복 확인 로직을 추가하세요.
        // 예: 서버에 중복 확인 요청 보내기 또는 로컬 데이터베이스에서 확인
        // 지금은 단순히 예제를 위해 "duplicate"라는 닉네임만 중복으로 처리함
        return nickname == "duplicate"
    }

}




        /* Intent로 전달된 값 가져오기
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
         */

