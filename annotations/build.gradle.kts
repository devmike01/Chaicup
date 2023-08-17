plugins {
    kotlin("jvm")
    id("maven-publish")
}

afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("mavenKotlin"){
                from(components["kotlin"])
                groupId = "io.devmike01.annotations"
                artifactId = "chaicup"
                version = "1.1"
            }

//            repositories {
//                maven{
//                    url = uri(layout.buildDirectory.dir("repo"))
//                }
//            }

        }

    }
}