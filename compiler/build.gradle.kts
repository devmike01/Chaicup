plugins {
    kotlin("jvm")
    `maven-publish`
    `java-library`
}

kotlin {
    jvmToolchain(17)
}

afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("maven"){
                groupId = "io.devmike01.chaicup"
                artifactId = "compiler"
                version = "1.1.0"
                from(components["java"])
            }

        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

sourceSets.main {
    kotlin.srcDirs(
        file("$buildDir/generated/ksp/debug/kotlin/io/devmike01"),
        file("$buildDir/generated/ksp/release/kotlin/io/devmike01"),
    )

}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.8")

}
