import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.dhug.quick_math"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dhug.quick_math"
        minSdk = 21
        targetSdk = 35
        versionCode = 6
        versionName = "1.4"
        vectorDrawables.useSupportLibrary = true

        // Only xxhdpi image resources are retained (currently mainstream resolution 1920 * 1080)
        @Suppress("DEPRECATION")
        resConfigs("xxhdpi")
        // Only the so library of two architectures is retained, and it is concluded based on Bugly statistics
        ndk {
            // armeabi: Wanjinyu Architecture Platform (occupancy rate: 0%)
            // armeabi-v7a: the once mainstream architectural platform (occupancy rate: 10%)
            // arm64-v8a: Currently mainstream architecture platform (occupancy rate: 95%)
            //noinspection ChromeOsAbiSupport
            abiFilters += listOf("armeabi-v7a", "arm64-v8a")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Load the keystore properties
    val keystoreProperties = Properties()
    val keystoreFile = project(":app").file("gradle.properties")
    if (keystoreFile.exists()) {
        keystoreProperties.load(keystoreFile.inputStream())
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["KeyAlias"] as String?
            keyPassword = keystoreProperties["KeyPassword"] as String?
            storeFile = (keystoreProperties["StoreFile"] as String?)?.let { file(it) }
            storePassword = keystoreProperties["StorePassword"] as String?
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
//            applicationIdSuffix = ".debug"
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("boolean", "LOG_ENABLE", "true")
            buildConfigField("String", "PACKAGE_NAME", "\"${defaultConfig.applicationId}\"")
            buildConfigField("String", "BUILD_TYPE", "\"DEBUG\"")
//            buildConfigField("int", "VERSION_CODE", "${defaultConfig.versionCode}")
            buildConfigField("String", "VERSION_NAME", "\"${defaultConfig.versionName}\"")
            buildConfigField("String", "ADMOB_APP_ID", "\"ca-app-pub-3940256099942544~3347511713\"")
            manifestPlaceholders["MAPS_API_KEY"] = "AIzaSyDXrlpexESoQlmUFcUuTGmEkwuseyabxyg"  // AIzaSyAI9kPkskayYti5ttrZL_UfBlL3OkMEbvs
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-3940256099942544~3347511713"
//            // Only one architecture so library is retained in debug mode to improve packaging speed
//            ndk {
//                abiFilters.clear() // Delete default ABI
//                //noinspection ChromeOsAbiSupport
//                abiFilters += "armeabi-v7a" // only keep Armeaabi-V7A for Debug
//            }
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )


            buildConfigField("boolean", "LOG_ENABLE", "true")
            buildConfigField("String", "PACKAGE_NAME", "\"${defaultConfig.applicationId}\"")
            buildConfigField("String", "BUILD_TYPE", "\"RELEASE\"")
//            buildConfigField("Int", "VERSION_CODE", "${defaultConfig.versionCode}")
            buildConfigField("String", "VERSION_NAME", "\"${defaultConfig.versionName}\"")
            buildConfigField("String","ADMOB_APP_ID","\"ca-app-pub-1843002830475037~8810756712\"")
            manifestPlaceholders["MAPS_API_KEY"] = "AIzaSyDXrlpexESoQlmUFcUuTGmEkwuseyabxyg"
            manifestPlaceholders["ADMOB_APP_ID"] = "ca-app-pub-1843002830475037~8810756712"
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
        dataBinding = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
//    dynamicFeatures += setOf(":features:library")

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

    lint {
        baseline = file("lint-baseline.xml")
        disable += "Instantiatable" // Pass Error
    }
}

hilt {
//        enableAggregatingTask = true
    enableExperimentalClasspathAggregation = true
}

dependencies {

    api(project(":library:base"))
    api(project(":library:wiget"))
    // Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    // Add the dependencies for the Remote Config and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Core
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)

    // Hilt for Dependency Injection
    api(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // wakeup
    // WorkManager with Hilt
    implementation(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.common)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Retrofit
    api(libs.retrofit2)
    api(libs.retrofit2.converter.gson)
    // OkHttp - Add log
    api(libs.okhttp3)
    api(libs.okhttp3.logging.interceptor)

    // Coroutines
    api(libs.coroutines.android)
    api(libs.coroutines.core)
    // Viewmodel
    api(libs.lifecycle.viewmodel.ktx)
    api(libs.activity.ktx)
    api(libs.androidx.fragment.ktx)

    // RoomDb
    api(libs.room)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)
    api(libs.room.ktx)

    // Paging3
    api(libs.paging)
    api(libs.room.paging)

    // Flexbox
    api(libs.flexbox)

    // Glide
    api(libs.glide)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.glide.compiler)
    // support blur view
    api(libs.glide.transformations)

    // Lottie
    api(libs.lottie)

    // MotionToast
    api(libs.motion.toast)

    // Immersionbar
    api(libs.immersionbar)
    api(libs.immersionbar.ktx)
    api(libs.immersionbar.components)

    // Timber
    api(libs.timber)
    //
    // Permission request framework
    api(libs.xxpermissions)

    // Title bar framework
    api(libs.titlebar)

    // Toast framework
    api(libs.toastutils)

    // Json parsing framework
    api(libs.gson)

    // Gson parsing fault tolerance
    api(libs.gsonfactory)

    // Shape framework
    api(libs.shapeview)

    // AOP plug-in library
    api(libs.aspectjrt)

    // Gesture ImageView
    api(libs.photoview)

    // Bugly exception catching
//    api(libs.bugly)
//    api(libs.nativecrashreport)

    // Pull up to refresh and pull down to load the framework
//    implementation(libs.refreshlayout)
//    implementation(libs.refreshheader)

    // Indicator framework
    api(libs.circleindicator)

    // Tencent MMKV
    api(libs.mmkv)

    // Memory leak monitoring framework
//    debugImplementation(libs.leakcanary)
    // previewImplementation(libs.leakcanary)

    // ads
    api(libs.play.services.ads)

    // cameraX
    api(libs.androidx.camera.core)
    api(libs.androidx.camera.lifecycle)
    api(libs.androidx.camera.video)
    api(libs.androidx.camera.view)
    api(libs.androidx.camera.extensions)
    api(libs.androidx.camera.camera2)

    // https://mvnrepository.com/artifact/com.google.guava/guava
    api(libs.guava)

    //Ratingbar
    // https://github.com/williamyyu/SimpleRatingBar
    api(libs.simpleratingbar)

    // ExoPlayer2
    api(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.ui)


    // Google map
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)// Nếu cần định vị GPS
    implementation(libs.places)
    // For Kotlin users also import the Kotlin extensions library for Play In-App Review:
    implementation(libs.review.ktx)
    //
    implementation(libs.mp4composer.android)

    // billing
    implementation(libs.billing.ktx)

    // Shimmer
    implementation(libs.shimmer)

    // open street map
    implementation(libs.osmdroid.android)
    implementation(libs.osmbonuspack)


}