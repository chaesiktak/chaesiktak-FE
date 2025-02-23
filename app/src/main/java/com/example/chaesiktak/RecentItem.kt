package com.example.chaesiktak

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RecentItem : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recent_item)

        val recipeImageResId = intent.getIntExtra("RECIPE_IMAGE", -1)
        val recipeImageView: ImageView = findViewById(R.id.recentItemImage)

        if (recipeImageResId != -1) {
            recipeImageView.setImageResource(recipeImageResId)
        }
    }
}