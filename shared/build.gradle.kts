import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlin-parcelize")
    kotlin(KotlinPlugins.serialization) version Kotlin.version
//    id("dev.icerock.moko.kswift") version "0.3.0"
}

version = "1.0"

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        frameworkName = "shared"
        podfile = project.file("../iosInblo/Podfile")
    }

    sourceSets {
        all {
            languageSettings.optIn("io.ktor.util.InternalAPI")
        }

        val commonMain by getting {
            dependencies {
                implementation(Kotlinx.datetime)
                implementation(Ktor.core)
                implementation(Ktor.clientSerialization)
                implementation(Ktor.logging)
                implementation("com.android.support:support-annotations:28.0.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Ktor.android)
                implementation("com.android.support:support-annotations:28.0.0")

            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Ktor.ios)
            }
        }
        val iosTest by getting {}
    }

//    kswift {
//        install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
//    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
    }
}
dependencies {
    implementation("androidx.annotation:annotation:1.2.0")
}
kotlin.sourceSets.all {
    languageSettings.useExperimentalAnnotation("kotlin.ExperimentalMultiplatform")
}