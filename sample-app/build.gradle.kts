plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.sampleapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sampleapp"
        minSdk = 23
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17  // ✅ Set to 17
        targetCompatibility = JavaVersion.VERSION_17  // ✅ Set to 17
    }

    kotlinOptions {
        jvmTarget = "17"  // ✅ Ensure Kotlin uses Java 17
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":sdk"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // ✅ Add Material3 for themes
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.multidex:multidex:2.0.1")

    // Retrofit & Gson Converter
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ✅ Add JUnit dependencies for unit testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}