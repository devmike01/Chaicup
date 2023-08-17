plugins {
    kotlin("jvm")
    id("maven-publish")
    id("java-library")
}



afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("maven") {
                groupId = "io.devmike01.annotations"
                artifactId = "chaicup"
                version = "1.1"
                from(components["java"])
            }
        }

    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


kotlin {
    jvmToolchain(17)
}

//repositories {
//    mavenCentral()
//}