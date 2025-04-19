plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.dhug.base"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String","ADMOB_APP_ID","\"ca-app-pub-1843002830475037~8810756712\"")
            buildConfigField("String","ADMOB_APP_ID_INTERSTITIAL","\"ca-app-pub-1843002830475037/7992499060\"")
            buildConfigField("String","ADMOB_APP_ID_BANNER","\"ca-app-pub-1843002830475037/1820874783\"")
            buildConfigField("String","ADMOB_APP_ID_BANNER_OB","\"ca-app-pub-1843002830475037/4711729603\"")
            buildConfigField("String","ADMOB_APP_ID_OPEN_AD","\"ca-app-pub-1843002830475037/8459402926\"")
        }

        debug {
            buildConfigField("String","ADMOB_APP_ID","\"ca-app-pub-3940256099942544~3347511713\"")
            buildConfigField("String","ADMOB_APP_ID_INTERSTITIAL","\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String","ADMOB_APP_ID_BANNER","\"ca-app-pub-3940256099942544/9214589741\"")
            buildConfigField("String","ADMOB_APP_ID_BANNER_OB","\"ca-app-pub-1843002830475037/1829805613\"")
            buildConfigField("String","ADMOB_APP_ID_OPEN_AD","\"ca-app-pub-3940256099942544/9257395921\"")
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
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
        buildConfig = true
    }

    kapt {
        correctErrorTypes = true
        javacOptions {
            // These options are normally set automatically via the Hilt Gradle plugin, but we
            // set them manually to workaround a bug in the Kotlin 1.5.20
            option("-Adagger.fastInit=ENABLED")
            option("-Adagger.hilt.android.internal.disableAndroidSuperclassValidation=true")
        }
        arguments {
            arg("mockk.codegen", "true")
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.play.services.ads.lite)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Shimmer
    implementation(libs.shimmer)
    // Hilt for Dependency Injection
    api(libs.hilt.android)
    kapt(libs.hilt.compiler)
}