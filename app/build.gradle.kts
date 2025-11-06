plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.huerto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.huerto"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    // 👇 Alinea Kotlin a 17
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }

}
kotlin {
    jvmToolchain(21)
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Lifecycle + ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Biometría (opcional)
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    // Iconos extendidos
    implementation("androidx.compose.material:material-icons-extended")
}
