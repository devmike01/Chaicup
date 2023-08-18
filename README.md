# Chaicu


### How to use

##### Step 1
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

##### Step 2
Add these dependencies to your project
```kotlin
implementation("com.github.devmike01.Chaicup:annotations:1.3.0")
ksp("com.github.devmike01.Chaicup:compiler:1.3.0")
```

##### Step 3
Add this to your app level gradle file
```kotlin
 kotlin.sourceSets.main {
        kotlin.srcDirs(
                file("$buildDir/generated/ksp/{BUILD_TYPE}/kotlin/io/devmike01"),
        )
    }
```
The `BUILD_TYPE` is your app build type. In typical case, it should be `release`, `debug` or any other build types.

### Implementation
- Add `@ChaiRoute` to your compose function.
- Build your project and the routes would be automagically generated for you
- All the routes are located inside `ChaiCupRoutes`
- If you add `@ChaiRoute` to your function called `BuildScreen()`, it would generate a route named `BUILDSCREEN`.

### Milestone
- Add support for arguments in path
- Use underscores to string together routes

License
-------
```text
Copyright 2023 Oladipupo Gbenga

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
