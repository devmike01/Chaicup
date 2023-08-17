# Chaicup

### Add this to your app level gradle file
```kotlin
 kotlin.sourceSets.main {
        kotlin.srcDirs(
                file("$buildDir/generated/ksp/debug/kotlin/io/devmike01"),
                file("$buildDir/generated/ksp/release/kotlin/io/devmike01"),
        )
    }
```