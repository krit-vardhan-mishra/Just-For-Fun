plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.just_for_fun.justforfun"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.just_for_fun.justforfun"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    viewBinding {
        enable = true
    }
    dataBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.material.v130alpha03)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.databinding.runtime)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.material.v190)
    implementation (libs.androidx.navigation.fragment.ktx.v275)
    implementation (libs.androidx.navigation.ui.ktx.v275)
    implementation (libs.androidx.viewpager2)
    implementation (libs.roundedimageview)
    implementation (libs.androidx.preference)
    implementation (libs.library)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Coroutines
    implementation (libs.kotlinx.coroutines.android)

    // Coil for image loading
    implementation (libs.coil)
}