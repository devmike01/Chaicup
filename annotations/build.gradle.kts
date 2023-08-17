plugins {
    kotlin("jvm")
    `maven-publish`
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