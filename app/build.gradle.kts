plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lumiglass.widgets"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lumiglass.widgets"
        minSdk = 26
        targetSdk = 36
        versionCode = 2
        versionName = "1.2"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Native Android View-based Liquid Glass with AGSL lens rendering on API 33+.
    implementation("com.github.QWEA0:liquidglass:v2.0.11")
}
