pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven("https://jitpack.io")

    }
}

rootProject.name = "Inblo"
include(":androidInblo")
include(":shared")

