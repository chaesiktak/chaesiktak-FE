package com.example.chaesiktak

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecipeDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe_detail)
        val recipe = intent.getParcelableExtra<RecommendRecipe>("RECIPE")

        recipe?.let {
            findViewById<TextView>(R.id.detail_title).text = it.title
            findViewById<TextView>(R.id.detaiL_title2).text = it.title
            findViewById<TextView>(R.id.detail_kcal).text = it.kcal
            findViewById<ImageView>(R.id.detail_imageView2).setImageResource(it.image)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}