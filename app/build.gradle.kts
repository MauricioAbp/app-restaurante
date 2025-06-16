plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.login_giraldos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.login_giraldos"
        minSdk = 24
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation(libs.appcompat)
    implementation ("com.android.volley:volley:1.2.1")
    implementation(libs.material)
    implementation(libs.activity)
    implementation("com.loopj.android:android-async-http:1.4.11")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
