// Fichier build.gradle.kts au niveau de l'application
plugins {
    id("com.android.application") // Plugin Android pour une application
    id("com.google.gms.google-services") version "4.4.2" apply false

}

android {
    namespace = "com.example.testfirebase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.testfirebase"
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation (libs.firebase.database)  // Realtime Database
    implementation (libs.firebase.auth)      // Authentification Firebase (si nécessaire)
    implementation (libs.material.v160)   // Pour les composants de l'interface
    implementation (libs.appcompat.v141)
}
  // Appliquez le plugin à la fin du fichier
