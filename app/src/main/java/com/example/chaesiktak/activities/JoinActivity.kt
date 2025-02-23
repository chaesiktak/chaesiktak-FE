package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.EmailCheckRequestBody
import com.example.chaesiktak.NickNameCheckRequestBody
import com.example.chaesiktak.R
import com.example.chaesiktak.SignUpRequest
import kotlinx.coroutines.launch

class JoinActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText //이메일 입력
    private lateinit var passwordEditText: EditText //비밀번호 입력
    private lateinit var confirmPasswordEditText: EditText //비밀번호 확인 (비밀번호 입력값과 동일)
    private lateinit var nameEditText: EditText //본명 입력
    private lateinit var nicknameEditText: EditText //닉네임 입력
    private lateinit var signupButton: Button

    private lateinit var tosCheckbox: CheckBox //약관확인 checkbox
    private lateinit var emailErrortext: TextView //이메일 에러 표시 text
    private lateinit var pwErrortext: TextView //비밀번호 에러 표시 text
    private  lateinit var nicknameErrortext:TextView //닉네임 에러 표시 text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_join)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        nameEditText = findViewById(R.id.realnamelInput)
        nicknameEditText = findViewById(R.id.nicknameInput)
        tosCheckbox = findViewById(R.id.toscheckbox)

        emailErrortext = findViewById(R.id.emailError)
        pwErrortext = findViewById(R.id.passwordError)
        nicknameErrortext = findViewById(R.id.NicknameError)


        val backarrow: ImageView = findViewById(R.id.backArrow)
        val homeicon: ImageView = findViewById(R.id.homeIcon)
        val tosText: TextView = findViewById(R.id.TOStext)
        val signupbutton: Button = findViewById(R.id.signUpbutton)

        val checkEmailbutton: Button = findViewById(R.id.checkEmailButton)
        val checkNicknameButton: Button = findViewById(R.id.checkNicknameButton)

        checkEmailbutton.setOnClickListener {
            runOnUiThread {
                checkEmailbutton.isEnabled = false // 버튼 비활성화
                checkEmailbutton.setBackgroundResource(R.drawable.button_disabled_background)
            }

            lifecycleScope.launch {
                val result = checkEmailDupe() // checkEmailDupe이 suspend 함수여야 함

                runOnUiThread {
                    if (result == 1) {
                        // 이메일이 사용 중일 때 버튼 비활성화 유지
                        checkEmailbutton.isEnabled = false
                        checkEmailbutton.setBackgroundResource(R.drawable.button_disabled_background)
                    } else {
                        // 이메일이 사용 가능할 때 버튼 다시 활성화
                        checkEmailbutton.isEnabled = true
                        checkEmailbutton.setBackgroundResource(R.drawable.default_button_md)
                    }
                }
            }
        }

        checkNicknameButton.setOnClickListener {
            runOnUiThread {
                checkNicknameButton.isEnabled = false // 버튼 비활성화
                checkNicknameButton.setBackgroundResource(R.drawable.button_disabled_background)
            }
            lifecycleScope.launch {
                val result = checkNickNameDupe()

                runOnUiThread {
                    if (result == 1) {
                        checkNicknameButton.isEnabled = false
                        checkNicknameButton.setBackgroundResource(R.drawable.button_disabled_background)
                    } else {
                        checkNicknameButton.isEnabled = true
                        checkNicknameButton.setBackgroundResource(R.drawable.default_button_md)
                    }
                }
            }
        }

        passwordEditText.addTextChangedListener(passwordTextWatcher)

        signupbutton.setOnClickListener {
            performSignUp()
        }
        tosText.setOnClickListener {
            startActivity(Intent(this, TOSActivity::class.java))
        }
        homeicon.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        backarrow.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        passwordEditText.addTextChangedListener(passwordTextWatcher)
        confirmPasswordEditText.addTextChangedListener(confirmPasswordTextWatcher)
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validatePasswordMatch()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private val confirmPasswordTextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            validatePasswordMatch()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }


    private fun validatePasswordMatch() {
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (confirmPassword.isNotEmpty() && password != confirmPassword) {
            showPasswordError("비밀번호가 일치하지 않습니다.")
        } else {
            hidePasswordError()
        }
    }

    private fun hidePasswordError() {
        pwErrortext.visibility = View.GONE
    }

    private fun showEmailError(errorMessage: String) {
        emailErrortext.text = " $errorMessage"
        emailErrortext.visibility = View.VISIBLE
    }

    private fun showPasswordError(errorMessage: String) {
        pwErrortext.text = "⚠ $errorMessage"
        pwErrortext.visibility = View.VISIBLE
    }

    private fun showNicknameError(errorMessage: String) {
        nicknameErrortext.text = "⚠ $errorMessage"
        nicknameErrortext.visibility = View.VISIBLE
    }

    private fun hideErrors() {
        emailErrortext.visibility = View.GONE
        pwErrortext.visibility = View.GONE
        nicknameErrortext.visibility = View.GONE
    }

    private fun performSignUp() {
        hideErrors()

        if (!tosCheckbox.isChecked) {
            Toast.makeText(this, "약관에 동의해주세요.", Toast.LENGTH_LONG).show()
            return
        }

        // EditText에서 값 가져오기
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val nickname = nicknameEditText.text.toString().trim()

        // 비밀번호 확인 체크
        if (password != confirmPassword) {
            showPasswordError("비밀번호가 일치하지 않습니다.")
            return
        }

        Log.d("API Request", "email: $email, password: $password, name: $name, nickname: $nickname")
        val request = SignUpRequest(email, password, name, nickname)

        // RetrofitClient의 getInstance(this) 사용
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance(this@JoinActivity).signUp(request)

                if (response.isSuccessful) {
                    val user = response.body()?.data
                    Toast.makeText(
                        this@JoinActivity,
                        "회원가입 성공! ${user?.userName}",
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@JoinActivity, SignUpCompleteActivity::class.java))
                    finish()
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.e("API Response", "회원가입 실패: $errorResponse")

                    when {
                        errorResponse?.contains("이메일") == true -> showEmailError("이미 존재하는 이메일입니다.")
                        errorResponse?.contains("비밀번호") == true -> showPasswordError("비밀번호 형식이 올바르지 않습니다.")
                        errorResponse?.contains("닉네임") == true -> showNicknameError("이미 존재하는 닉네임입니다.")
                        else -> Toast.makeText(
                            this@JoinActivity,
                            "회원가입 실패: $errorResponse",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@JoinActivity, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("JoinActivity", "회원가입 요청 실패", e)
            }
        }
    }

    //이메일 중복 확인
    suspend fun checkEmailDupe(): Int {
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty()) {
            showEmailError("이메일을 입력해주세요.")
            return 0
        }

        val request = EmailCheckRequestBody(email)

        return try {
            val response = RetrofitClient.instance(this@JoinActivity).CheckEmailDupe(request)

            if (response.isSuccessful) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val message = responseBody.message ?: "이메일 중복 확인 실패"

                    return if (responseBody.success) {
                        emailErrortext.apply {
                            text = "✅ $message"
                            setTextColor(ContextCompat.getColor(this@JoinActivity, R.color.banner_bottom_indicator_green))
                            visibility = View.VISIBLE
                        }
                        1 // 이메일 사용 가능
                    } else {
                        showEmailError("⚠ $message")
                        0
                    }
                } else {
                    showEmailError("서버 응답 오류: 이메일 중복 확인 실패")
                    0
                }
            } else {
                val errorResponse = response.errorBody()?.string() ?: "이메일 중복 확인 실패"
                showEmailError("⚠ $errorResponse")
                0
            }
        } catch (e: Exception) {
            Toast.makeText(this@JoinActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("JoinActivity", "이메일 중복 확인 요청 실패", e)
            0
        }
    }


    //닉네임 중복 확인
    suspend fun checkNickNameDupe(): Int {
        val nickname = nicknameEditText.text.toString().trim()

        if (nickname.isEmpty()) {
            showNicknameError("닉네임을 입력해주세요.")
            return 0
        }

        val request = NickNameCheckRequestBody(nickname)

        return try {
            Log.d("JoinActivity", "닉네임 중복 확인 요청 보냄: $nickname") // 요청 보낼 때 로그 추가
            val response = RetrofitClient.instance(this@JoinActivity).CheckNickNameDupe(request)

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("JoinActivity", "닉네임 중복 확인 응답: ${responseBody?.message}") // 서버 응답 로그 추가

                if (responseBody != null) {
                    val message = responseBody.message ?: "닉네임 중복 확인 실패"

                    if (responseBody.success) { //닉네임 사용 가능 (return 1)
                        nicknameErrortext.apply {
                            nicknameErrortext.text = message
                            nicknameErrortext.setTextColor(ContextCompat.getColor(this@JoinActivity, R.color.banner_bottom_indicator_green))
                            visibility = View.VISIBLE
                        }
                        nicknameErrortext.visibility = View.GONE
                        return 1
                    } else {
                        showNicknameError(message)
                        return 0
                    }
                } else {
                    showNicknameError("서버 응답 오류: 닉네임 중복 확인 실패")
                    return 0
                }
            } else {
                val errorResponse = response.errorBody()?.string() ?: "닉네임 중복 확인 실패"
                showNicknameError(errorResponse)
                return 0
            }
        } catch (e: Exception) {
            Toast.makeText(this@JoinActivity, "네트워크 오류: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("JoinActivity", "닉네임 중복 확인 요청 실패", e)
            return 0
        }
    }

}
