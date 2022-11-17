plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
}




dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    val nav_version = "2.3.5"

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    implementation("com.jakewharton.timber:timber:4.7.1")


    implementation("com.google.dagger:hilt-android:2.37")
    kapt("com.google.dagger:hilt-android-compiler:2.37")

    implementation(Kotlinx.datetime)
    implementation(Ktor.android)
    implementation(Ktor.clientSerialization)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha02")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("joda-time:joda-time:2.10.10")
//    implementation("com.github.AnyChart:AnyChart-Android:1.1.2")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.github.kizitonwose:CalendarView:1.0.4")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("com.google.android.exoplayer:exoplayer:2.15.0")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.15.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.github.amsiq:SwipeRevealLayout:1.4.1-x")




}

android {
    compileSdkVersion(30)
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
    defaultConfig {
        applicationId = "com.colinjp.inblo.android"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    dependencies {
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    }

    buildFeatures {
        viewBinding = true
    }

}
