plugins {
    kotlin("jvm")
    `maven-publish`
}


java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}



afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("maven"){
                groupId = "io.devmike01.annotations"
                artifactId = "chaicup"
                version = "1.1"
                from(components["java"])
            }

        }

    }
}