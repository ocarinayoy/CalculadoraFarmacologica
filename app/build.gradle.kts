plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Se agrega este plugin para habilitar kapt
}

android {
    namespace = "com.TI2.famacologiccalc"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.TI2.famacologiccalc"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

kapt {
    correctErrorTypes = true // Permite ignorar errores de tipos desconocidos en anotaciones
}

dependencies {
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.material3.android)

    // Room
    val roomVersion = "2.6.0" // Ajusta a la versión más reciente
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion") // Si usas Kotlin y necesitas anotaciones

    // Opcional: Soporte para coroutines
    implementation("androidx.room:room-ktx:$roomVersion")

    // Lifecycle
    val lifecycleVersion = "2.8.7" // Ajusta a la versión más reciente
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Otras dependencias necesarias
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.android.material:material:1.11.0")
}
