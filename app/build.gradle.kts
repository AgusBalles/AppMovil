plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
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

        // Esto envía la API key al AndroidManifest
        manifestPlaceholders["MAPS_API_KEY"] =
            project.findProperty("MAPS_API_KEY") ?: ""
    }

    // ⭐ BLOQUE QUE FALTABA (OBLIGATORIO PARA GENERAR BuildConfig) ⭐
    buildTypes {
        debug {
            buildConfigField(
                "String",
                "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY") ?: ""}\""
            )
        }
        release {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "MAPS_API_KEY",
                "\"${project.findProperty("MAPS_API_KEY") ?: ""}\""
            )
        }
    }

    // Activa BuildConfig y Compose
    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions { jvmTarget = "21" }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Agrega estas dependencias en tu build.gradle.kts (Module: app)



        // Retrofit
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // OkHttp
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

        // Gson
        implementation("com.google.code.gson:gson:2.10.1")

        // Coroutines (si no las tienes)
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

        // ViewModel y LiveData (si no los tienes)
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")


    /* ---------------- GOOGLE MAPS ---------------- */
    implementation("com.google.maps.android:maps-compose:4.4.1")
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    /* ---------------- RETROFIT 2 ---------------- */
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    /* ---------------- OKHTTP ---------------- */
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    /* ---------------- COROUTINES ---------------- */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    /* ---------------- ROOM ---------------- */
    implementation("androidx.room:room-runtime:2.8.2")
    implementation("androidx.room:room-ktx:2.8.2")
    ksp("androidx.room:room-compiler:2.8.2")

    /* ---------------- COMPOSE ---------------- */
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    /* ---------------- LIFECYCLE ---------------- */
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")

    /* ---------------- NAVIGATION ---------------- */
    implementation("androidx.navigation:navigation-compose:2.8.3")

    /* ---------------- DATASTORE ---------------- */
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    /* ---------------- BIOMETRIC ---------------- */
    implementation("androidx.biometric:biometric:1.2.0-alpha05")

    /* ---------------- MEDIA3 (si tu proyecto lo usa) ---------------- */
    implementation(libs.androidx.media3.database)
}
