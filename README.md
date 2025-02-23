# 📌 Android 애플리케이션 개요

## 📌 소개

---

## 📂 프로젝트 구조

### 📁 Fragments
- **HomeFragment** - 네비게이션의 메인 대시보드/홈 화면을 표시
- **ScannerFragment** - 음식 이미지를 인식하여, 어떤 재료로 구성되어 있는 지 파악하는 기능 제공
- **MyInfoFragment** - 네비게이션 사용자 프로필 및 설정을 관리

---

### 📁 Activities
#### 🔹 인증 및 온보딩
- **IntroActivity** - 첫 사용자를 위한 소개 화면 및 시작 버튼 
- **LoginActivity** - 사용자 로그인 인증 처리
- **JoinActivity** - 신규 사용자 회원가입 관리
- **FindingPasswordActivity** - 비밀번호 찾기 기능을 제공 
- **FindingPasswordActivity2** - '임시 비밀번호 전송'과 관련한 확인 메시지 화면 제공 & 로그인 버튼
- **SignUpCompleteActivity** - 회원가입 완료 후 확인 화면 제공
- **TOSActivity** - 이용 약관 표시 & '닫기'

#### 🔹 유틸리티 및 기능 관련 액티비티
- **SplashActivity** - 앱 실행 시 처음 표시되는 스플래시 화면
- **RecommendRecipe** - 데이터 클래스 정의
```kotlin
  package com.example.chaesiktak
  
  data class RecommendRecipe(
      val image: Int,       // 이미지 리소스 ID
      val title: String,    // 레시피 제목
      val subtext: String   // 서브 텍스트 (0인분, 0시간)
  )
```
 
- **RecommendRecipeAdapter** - 추천 레시피 목록을 관리하는 RecyclerView 어댑터

---

### 📁 Library

https://github.com/ongakuer/CircleIndicator?tab=readme-ov-file //indicator library 

---

## 🏗 아키텍처 및 설계

---

## 🚀 실행 방법

