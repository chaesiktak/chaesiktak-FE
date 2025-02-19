import com.android.tools.r8.internal.me

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    buildFeatures.viewBinding = true
    namespace = "com.example.chaesiktak"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chaesiktak"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true  // View Binding 활성화
    }
}

dependencies {
    val camerax_version = "1.3.0"

    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    //ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

    //indicator library
    implementation("me.relex:circleindicator:2.1.6")

    //retrofit library
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    dependencies {
        implementation(libs.androidx.core.ktx) // Core KTX
        implementation(libs.androidx.appcompat) // AppCompat
        implementation(libs.material) // Material Design
        implementation(libs.androidx.activity) // Activity KTX
        implementation(libs.androidx.constraintlayout) // ConstraintLayout
        testImplementation(libs.junit) // JUnit for unit testing
        androidTestImplementation(libs.androidx.junit) // JUnit for Android testing
        androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    }

}