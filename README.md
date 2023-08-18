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

#### Add these dependencies to your project
```kotlin
implementation("com.github.devmike01.Chaicup:annotations:437079d4cc")
ksp("com.github.devmike01.Chaicup:compiler:437079d4cc")
```