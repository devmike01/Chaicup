pluginManagement {
    plugins {
        id("com.google.devtools.ksp") version "1.7.20-1.0.8"
        kotlin("jvm") version "1.7.1"
    }
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
//rootProject.name = "chaicup"
include(":app")
include(":chaicup")
include(":annotations")
include(":compiler")
