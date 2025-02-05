package com.example.chaesiktak.activities

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chaesiktak.R

class TOSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tosactivity)

        val textView: TextView = findViewById(R.id.textOverlay)

        // strings.xml에서 HTML 텍스트 가져오기
        val privacyPolicy = getString(R.string.privacy_policy)
        textView.text = Html.fromHtml(privacyPolicy, Html.FROM_HTML_MODE_LEGACY)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}