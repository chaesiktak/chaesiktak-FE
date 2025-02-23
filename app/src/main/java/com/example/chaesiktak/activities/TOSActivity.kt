package com.example.chaesiktak.activities

import LoadingDialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.R

class TOSActivity : AppCompatActivity() {
    private lateinit var closeButton: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tosactivity)

        val textView: TextView = findViewById(R.id.textOverlay)
        val closeButton: Button = findViewById(R.id.close_button)

        closeButton.setOnClickListener {
            finish()
        }


        // strings.xml에서 HTML 텍스트 가져오기
        val privacyPolicy = getString(R.string.privacy_policy)
        textView.text = Html.fromHtml(privacyPolicy, Html.FROM_HTML_MODE_LEGACY)



    }
}