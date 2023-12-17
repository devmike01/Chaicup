pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.9.20-1.0.14"
        kotlin("jvm") version "1.7.1"
    }
    repositories {
        google()
        mavenCentral()
        maven(uri("https://jitpack.io"))
        gradlePluginPortal()
    }

}

include(":app")
include(":annotations")
include(":compiler")
