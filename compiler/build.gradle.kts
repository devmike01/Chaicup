plugins {
    kotlin("jvm")
    id("maven-publish")
}
afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("mavenKotlin"){
                from(components["kotlin"])
                groupId = "io.devmike01.compiler"
                artifactId = "chaicup"
                version = "1.1"
            }

        }
    }
}


dependencies {
    implementation(project(":annotations")) //1.7.1
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.8")

}

