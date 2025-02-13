plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidApplication)
    kotlin("android")
    kotlin("kapt")
    id("todometer.spotless")
    id("todometer.dependency-graph-generator")
}

if (file("google-services.json").exists()) {
    apply(plugin = libs.plugins.googleServices.get().pluginId)
    apply(plugin = libs.plugins.firebaseCrashlytics.get().pluginId)
}

android {
    namespace = "dev.sergiobelda.todometer.wear"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.sergiobelda.todometer"
        minSdk = libs.versions.androidWearMinSdk.get().toInt()
        targetSdk = libs.versions.androidTargetSdk.get().toInt()

        versionCode = 4150401
        versionName = "wearos-1.5.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            getByName("debug") {
                extra["enableCrashlytics"] = false
            }
        }
        lint {
            abortOnError = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.common.domain)
    implementation(projects.common.navigation)
    implementation(projects.common.resources)
    implementation(projects.common.ui)

    implementation(compose.foundation)
    implementation(compose.materialIconsExtended)
    implementation(compose.uiTooling)

    implementation(libs.androidx.activityCompose)

    implementation(libs.androidx.coreKtx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)

    implementation(libs.wear.wear)
    implementation(libs.wear.compose.foundation)
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.navigation)
    implementation(libs.wear.input)

    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    implementation(libs.google.playServicesWearable)

    implementation(platform(libs.google.firebase.firebaseBom))
    implementation(libs.google.firebase.firebaseAnalyticsKtx)
    implementation(libs.google.firebase.firebaseCrashlyticsKtx)
}
