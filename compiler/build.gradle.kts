plugins {
    kotlin("jvm")
    `maven-publish`
}
afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("maven"){
                groupId = "io.devmike01.compiler"
                artifactId = "chaicup"
                version = "1.1"
                from(components["java"])
            }

        }
    }
}


dependencies {
    implementation(project(":annotations")) //1.7.1
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.8")

}

