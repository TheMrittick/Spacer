plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.spacer_spacedrepitition"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.spacer_spacedrepitition"
        minSdk = 24
        targetSdk = 36
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Room Database
    implementation("androidx.room:room-runtime:2.8.4")
    annotationProcessor("androidx.room:room-compiler:2.8.4")

    // WorkManager for notifications
    implementation("androidx.work:work-runtime:2.11.0")

    // RecyclerView for lists
    implementation("androidx.recyclerview:recyclerview:1.4.0")
}