package com.example.chaesiktak.activities

import android.content.Intent
import android.os.Bundle
import android.os.Message
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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.chaesiktak.ApiResponse
import com.example.chaesiktak.R
import com.example.chaesiktak.SignUpRequest
import com.example.chaesiktak.User
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JoinActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var nicknameEditText: EditText
    private lateinit var signupButton: Button
    private lateinit var tosCheckbox: CheckBox
    private lateinit var emailErrortext: TextView
    private lateinit var pwErrortext: TextView
    private  lateinit var nicknameErrortext:TextView

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
        val checkNickname: Button = findViewById(R.id.checkNicknameButton)

        checkEmailbutton.setOnClickListener {
            checkEmailbutton.isEnabled = false
            checkEmailbutton.setBackgroundResource(R.drawable.button_disabled_background)
        }

        checkNickname.setOnClickListener {
            checkNickname.isEnabled = false
            checkNickname.setBackgroundResource(R.drawable.button_disabled_background)
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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        emailErrortext.text = errorMessage
        emailErrortext.visibility = View.VISIBLE
    }

    private fun showPasswordError(errorMessage: String) {
        pwErrortext.text = errorMessage
        pwErrortext.visibility = View.VISIBLE
    }

    private fun showNicknameError(errorMessage: String) {
        nicknameErrortext.text = errorMessage
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



}
