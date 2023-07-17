plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":annotations")) //1.7.1
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.8")

}