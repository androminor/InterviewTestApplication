plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    // Unique identifier for the application's R class and resources
    namespace = "com.example.interviewtestapplication"
    // The version of the Android SDK used to compile the app
    compileSdk = 37

    defaultConfig {
        // The unique ID for your app on the Play Store/device
        applicationId = "com.example.interviewtestapplication"
        // Minimum Android version required to run the app (API 24 = Android 7.0)
        minSdk = 24
        // The SDK version the app is tested against and optimized for
        targetSdk = 37
        // Internal version number used for updates
        versionCode = 1
        // Publicly visible version string
        versionName = "1.0"

        // Specifies the test runner for instrumentation tests
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Disables code shrinking and obfuscation for the release build
            isMinifyEnabled = false
            // Sets the ProGuard/R8 rules for the release build
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // Sets Java language compatibility for source code to version 21
        sourceCompatibility = JavaVersion.VERSION_21
        // Sets JVM target compatibility for compiled bytecode to version 21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        // Enables support for Jetpack Compose UI framework
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(project(":feature:userdetail"))
    implementation(project(":feature:userlist"))
    implementation(project(":data:useractions"))
    implementation(project(":core:di"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.truth)
    kspTest(libs.hilt.compiler)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
