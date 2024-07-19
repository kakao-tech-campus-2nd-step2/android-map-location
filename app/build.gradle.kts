plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    compileSdk = 34
    namespace = "campus.tech.kakao.map"

    defaultConfig {
        applicationId = "campus.tech.kakao.map"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
        dataBinding {
            enable = true
        }

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.test.ext:junit-ktx:1.2.1@arr")
    androidTestImplementation ("org.mockito:mockito-android:3.11.2")
    testImplementation ("org.mockito:mockito-core:3.11.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("androidx.multidex:multidex:2.0.1")
    // Kakao Map SDK - 최신 버전 사용
    implementation("com.kakao.maps.open:android:2.9.5")
    // Kakao SDK
    implementation("com.kakao.sdk:v2-all:2.20.3")
    // 네트워크 라이브러리
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")
    // AndroidX 및 기타 라이브러리
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.activity:activity:1.9.0")
    // Retrofit 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    // AR 라이브러리
    implementation("com.google.ar.sceneform:core:1.17.1") // ARCore 관련 라이브러리 수정
    // Google Maps 서비스 라이브러리
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.libraries.places:places:3.5.0")
    implementation("androidx.test:core-ktx:1.6.1")
    // 테스트 라이브러리
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}

