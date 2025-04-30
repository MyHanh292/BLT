plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.chamsocthucung2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chamsocthucung2"
        minSdk = 26
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation("com.google.accompanist:accompanist-flowlayout:0.30.1")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.animation:animation:1.6.0")
    implementation ("io.coil-kt:coil-compose:2.5.0")


    implementation("androidx.compose.material3:material3-window-size-class:1.1.2")

    implementation("androidx.compose.material3:material3:1.2.0")
    implementation(platform("com.google.firebase:firebase-bom:33.12.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("androidx.compose.ui:ui:...")
    implementation("androidx.compose.material3:material3:...")


    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material:material:1.6.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("com.google.accompanist:accompanist-flowlayout:0.30.1")
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.foundation:foundation:1.3.1")
    implementation ("androidx.compose.foundation:foundation-layout:1.3.1")

    // Icons & hình ảnh
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("io.coil-kt:coil-compose:2.4.0") // Load ảnh

    // ViewModel cho Compose
    implementation("androidx.compose.material3:material3:1.1.2") // Hoặc phiên bản mới nhất
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.firebase.database)
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // hoặc phiên bản mới nhất
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

//    dependencies {
//        implementation("androidx.emoji3:emoji-material3:1.4.0")
//        implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
//    }
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)
    androidTestImplementation(libs.ui.test.junit4)

    // Debugging
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}