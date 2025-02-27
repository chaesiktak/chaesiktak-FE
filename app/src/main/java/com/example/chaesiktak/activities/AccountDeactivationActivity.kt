package com.example.chaesiktak.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.chaesiktak.R
import okhttp3.*
import java.io.IOException
import com.example.chaesiktak.fragments.MyInfoFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class AccountDeactivationActivity : AppCompatActivity() {

    private lateinit var nicknameTextView: TextView
    private lateinit var deactiveAgreeCheckBox: CheckBox
    private lateinit var deactivateButton: Button
    private lateinit var deactiveEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_deactivation)

        nicknameTextView = findViewById(R.id.nicknameTextView)
        deactiveAgreeCheckBox = findViewById(R.id.deactiveAgreeCheckBox)
        deactivateButton = findViewById(R.id.deactivateButton)
        deactiveEditText = findViewById(R.id.deactiveEditText)

        /* 체크박스 on/off && 탈퇴하기 버튼
        val deactiveAgreeCheckBox: CheckBox = findViewById(R.id.deactiveAgreeCheckBox)
        val deactivateButton: Button = findViewById(R.id.deactivateButton)
         */


        // SharedPreferences에서 현재 설정된 닉네임 가져오기
        val sharedPref = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val currentNickname = sharedPref.getString("nickname", "닉네임") ?: "닉네임"

        nicknameTextView.text = "$currentNickname 님 정말 탈퇴하실건가요?" // Nickname 설정


        // 탈퇴 사유 작성란
        val deactiveEditText: EditText = findViewById(R.id.deactiveEditText)

        // 탈퇴하기 버튼 클릭 리스너 설정 -> 이거 오류때문에 일단 클릭 못하게 막아놓을게요!
        deactivateButton.setOnClickListener {
//            if (deactiveAgreeCheckBox.isChecked) {
//                val deactiveReason = deactiveEditText.text.toString()
//                sendDeactivationReasonToServer(currentNickname, deactiveReason)
//            } else {
//                Toast.makeText(this@AccountDeactivationActivity, "탈퇴 사항에 동의하여 주세요.", Toast.LENGTH_SHORT)
//                    .show()
//            }
            CustomToast.show(this, "준비중인 기능입니다! ")
        }

        // 뒤로가기 버튼 클릭 시 마이페이지 탭으로 이동
        val backArrow = findViewById<ImageButton>(R.id.backArrow)
        backArrow.setOnClickListener {
            finish()
        }
    }

    private fun sendDeactivationReasonToServer(nickname: String, reason: String) {
        val client = OkHttpClient()
        val url = "http://yourserverurl/deactivate"
        val json = """{"nickname":"$nickname", "reason":"$reason"}"""
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AccountDeactivationActivity, "서버와 통신 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@AccountDeactivationActivity, "탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        // 로그아웃 처리를 하고 첫 화면으로 이동
                        val intent = Intent(this@AccountDeactivationActivity, IntroActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AccountDeactivationActivity, "탈퇴 처리에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }
}
