plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rahulghag.splittrip.core.di"
    compileSdk = 35
    defaultConfig { minSdk = 26 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions { jvmTarget = "11" }
    buildFeatures { buildConfig = true }

    buildTypes {
        debug {
            buildConfigField("boolean", "MOCK_MODE", "true")
        }
        release {
            buildConfigField("boolean", "MOCK_MODE", "false")
        }
    }
}

dependencies {
    // Domain interfaces
    implementation(project(":domain:trips"))
    implementation(project(":domain:settle"))
    implementation(project(":domain:activity"))
    implementation(project(":domain:profile"))

    // Data implementations
    implementation(project(":data:trips"))
    implementation(project(":data:settle"))
    implementation(project(":data:activity"))
    implementation(project(":data:profile"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}
