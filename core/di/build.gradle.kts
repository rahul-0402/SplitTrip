import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.rahulghag.splittrip.core.di"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        buildConfigField("String", "SUPABASE_URL",
            "\"${localProperties["SUPABASE_URL"]}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY",
            "\"${localProperties["SUPABASE_ANON_KEY"]}\"")
    }
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
    implementation(project(":domain:auth"))
    implementation(project(":domain:trips"))
    implementation(project(":domain:settle"))
    implementation(project(":domain:activity"))
    implementation(project(":domain:profile"))

    // Data implementations
    implementation(project(":data:auth"))
    implementation(project(":data:trips"))
    implementation(project(":data:settle"))
    implementation(project(":data:activity"))
    implementation(project(":data:profile"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Supabase (for SupabaseModule)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.ktor.android)
}
