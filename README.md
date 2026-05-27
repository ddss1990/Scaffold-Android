# AndroidScaffold

AndroidScaffold is a minimal Android app scaffold for validating third-party library integration, Gradle configuration, and build setup.

It is intentionally small: the repository contains the Android Gradle setup, a single `app` module, and the default generated resources. Use it as a baseline when you want to prove that a dependency, plugin, or SDK change works before moving it into a larger app.

## What is included

- Android application module configured with Kotlin DSL
- Version-catalog-based dependency management
- Gradle 9.5.0 and Android Gradle Plugin 9.1.1
- AndroidX, Material, JUnit, and Espresso defaults

## Recommended integration order

Start with the lowest-level app foundations first, then add UI, dependency injection, and networking on top.

| Priority | Library / stack | Why it comes here | Typical validation |
| --- | --- | --- | --- |
| 1 | Lifecycle + ViewModel + Coroutines/Flow | Establishes the state layer that most app features depend on | `./gradlew :app:testDebugUnitTest` |
| 2 | Jetpack Compose | Proves the UI stack and build setup early | `./gradlew :app:assembleDebug` |
| 3 | Koin | Validates dependency injection once UI and state are in place | `./gradlew :app:assembleDebug` |
| 4 | Retrofit + OkHttp + converter | Verifies network wiring and serialization | `./gradlew :app:assembleDebug` |
| 5 | Navigation Compose / Navigator | Confirms screen-to-screen wiring in the Compose stack | `./gradlew :app:assembleDebug` |
| 6 | Room | Adds local persistence after the core app shell is stable | `./gradlew :app:testDebugUnitTest` |
| 7 | DataStore | Covers lightweight key-value persistence | `./gradlew :app:testDebugUnitTest` |
| 8 | Coil | Validates image loading in the UI layer | `./gradlew :app:assembleDebug` |
| 9 | Paging 3 | Tests scalable list/data loading patterns | `./gradlew :app:assembleDebug` |
| 10 | Timber / logging tools | Useful for debugging, but not required for core structure | `./gradlew :app:assembleDebug` |

## Good next additions

- `androidx.lifecycle:lifecycle-viewmodel-ktx`
- `androidx.lifecycle:lifecycle-runtime-ktx`
- `org.jetbrains.kotlinx:kotlinx-coroutines-core`
- `androidx.compose:*` via the Compose BOM
- `androidx.activity:activity-compose`
- `io.insert-koin:koin-android`
- `io.insert-koin:koin-androidx-compose`
- `com.squareup.retrofit2:retrofit`
- `com.squareup.okhttp3:okhttp`
- `com.squareup.okhttp3:logging-interceptor`
- `androidx.navigation:navigation-compose` (Navigator / screen routing)
- `androidx.room:room-runtime`
- `androidx.datastore:datastore-preferences`
- `io.coil-kt:coil-compose`
- `androidx.paging:paging-runtime`

## Prerequisites

- JDK 17 or newer
- Android Studio with the Android SDK installed
- Network access to the configured Maven repositories

## Repository layout

- `build.gradle.kts` - root build configuration
- `settings.gradle.kts` - project setup and repository configuration
- `gradle/libs.versions.toml` - dependency and plugin versions
- `app/` - Android application module

## Quick start

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run a lightweight build check:

```bash
./gradlew help
```

4. Build the app module when you want to validate a dependency change:

```bash
./gradlew :app:assembleDebug
```

## Validating a third-party library

Use this repository to test one library change at a time.

1. Add the dependency version to `gradle/libs.versions.toml` when possible.
2. Add the library to `app/build.gradle.kts`.
3. Run `./gradlew help` to confirm the build still configures cleanly.
4. Run the narrowest task that exercises the new library, such as:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

5. Record the result and any caveats before promoting the dependency to a larger codebase.

## Suggested validation notes

Track each library with the same fields so comparisons stay easy:

| Library | Version | Why it was added | Validation task | Result | Notes |
| --- | --- | --- | --- | --- | --- |
| Example | 1.0.0 | Test build integration | `./gradlew :app:assembleDebug` | Pass | Replace with real data |

## Notes

- Keep changes small and focused on the library being tested.
- Prefer catalog updates over hard-coded versions.
- If plugin resolution fails, check `settings.gradle.kts` first, then the version catalog.
