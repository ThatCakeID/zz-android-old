plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "tw.music.streamer"
    compileSdk = 34

    defaultConfig {
        applicationId = "tw.music.streamer"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("androidx.lifecycle:lifecycle-livedata:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
    implementation("androidx.media:media:1.7.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.firebase:firebase-auth:22.1.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.android.gms:play-services-tasks:18.0.2")
    implementation("com.google.android.material:material:1.13.0-alpha10")
    implementation("com.android.support:support-media-compat:28.0.0")
}

apply(plugin = "com.google.gms.google-services")