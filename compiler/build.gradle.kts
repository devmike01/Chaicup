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

sourceSets {
    main{
        java{
            srcDir("${buildDir}/tmp/kotlin-classes/debug/")
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.7.20-1.0.8")

}
