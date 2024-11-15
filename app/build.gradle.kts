plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.amiweatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.amiweatherapp"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Добавленные вручную
    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    //Gson
    implementation(libs.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.gson)

    //Сеть
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    //Coil
    implementation(libs.coil.compose)

    //Coroutines
    implementation (libs.jetbrains.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //Location
    implementation (libs.accompanist.permissions)
    implementation (libs.play.services.location)

    //Pull to refresh
    implementation ("androidx.compose.material:material:1.4.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation ("androidx.compose.material3:material3:1.3.0")
    implementation ("androidx.compose.material3:material3-window-size-class:1.3.0")
    implementation ("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha01")


}
kapt {
    correctErrorTypes = true
    arguments {
        arg("room.schemaLocation", "false")
    }
}