
plugins {
//    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

//android {
//    namespace = "io.devmike01.chaicup"
//
//}

dependencies {

    implementation(project(":annotations"))
    ksp(project(":compiler"))

}