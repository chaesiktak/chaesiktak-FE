package com.example.chaesiktak

data class TagRecipe(
    val image: Int,       // 이미지 리소스 ID
    val title: String,    // 레시피 제목
    val subtext: String,  // 서브 텍스트
    val tagType: String = "비건"  // 태그 타입 (예: 비건, 글루텐 프리 등)
)
