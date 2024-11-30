plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.booking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.booking"
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
    dependencies {
        implementation("com.squareup.retrofit2:retrofit:2.9.0") // Assurez-vous d'avoir cette dépendance pour Retrofit
        implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Pour convertir les réponses JSOn
        implementation("androidx.recyclerview:recyclerview:1.2.1")
        implementation ("com.github.bumptech.glide:glide:4.15.1")
        annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")


    }

}