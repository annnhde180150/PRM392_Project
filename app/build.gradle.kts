import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
val MAPS_API_KEY = localProperties.getProperty("MAPS_API_KEY") ?: ""
val HASH_SECRET = localProperties.getProperty("HASH_SECRET") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.homehelperfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.homehelperfinder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildFeatures { buildConfig = true }
        buildConfigField("String", "MAPS_API_KEY", "\"${MAPS_API_KEY}\"")
        manifestPlaceholders["MAPS_API_KEY"] = MAPS_API_KEY
        buildConfigField("String", "HASH_SECRET", "\"${HASH_SECRET}\"")
        manifestPlaceholders["HASH_SECRET"] = HASH_SECRET
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildToolsVersion = "35.0.0"
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation(libs.support.annotations)
    implementation(libs.firebase.storage)

    // Retrofit for API calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // HTTP client for logging
    implementation(libs.logging.interceptor)

    // JSON parsing
    implementation(libs.gson)

    // RecyclerView and CardView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson.v285)
    implementation(libs.okhttp)
    implementation(files("libs/merchant-1.0.25.aar"))
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.play.services.maps)
    implementation(libs.places)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.storage)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    compileOnly(libs.projectlombok.lombok)
    annotationProcessor(libs.projectlombok.lombok)

    // Microsoft SignalR Java Client for real-time messaging
    implementation(libs.signalr)
    implementation(libs.rxjava)
}