plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "tn.esprit.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "tn.esprit.myapplication"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Room components
    implementation ("androidx.room:room-runtime:2.5.0") // Replace with the latest version

    // Annotation processor for Room
    annotationProcessor ("androidx.room:room-compiler:2.5.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.swiperefreshlayout)
    implementation("androidx.biometric:biometric:1.2.0-alpha03")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation ("com.github.bumptech.glide:glide:4.13.0") // Correct Glide dependency
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0") // Annotation processor for Glide


}