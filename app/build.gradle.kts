plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ma.ensa.e_gestionnairemedec"
    compileSdk = 34

    defaultConfig {
        applicationId = "ma.ensa.e_gestionnairemedec"
        minSdk = 27
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    // Utilisation de la bibliothèque Material Design moderne
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    // Retrofit avec Gson pour la sérialisation JSON
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    // AndroidX Lifecycle pour LiveData et ViewModel avec Kotlin Extensions
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")

    // RecyclerView AndroidX
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("com.github.bumptech.glide:glide:4.12.0")

    // AndroidX WorkManager pour les tâches en arrière-plan avec Kotlin Extensions
    implementation ("androidx.work:work-runtime-ktx:2.7.1")

    implementation ("com.android.volley:volley:1.2.1")
    // Remplacement des bibliothèques avec les versions du catalogue Libs (si vous utilisez un fichier `libs.versions.toml`)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)  // Cette ligne est en doublon avec com.google.android.material, ajustez selon vos besoins
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Tests unitaires
    testImplementation(libs.junit)

    // Tests instrumentés Android
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}