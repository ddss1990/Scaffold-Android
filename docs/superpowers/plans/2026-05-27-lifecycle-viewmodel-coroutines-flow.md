# Lifecycle + ViewModel + Coroutines/Flow Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a minimal XML-based app shell that proves lifecycle-aware Flow collection, ViewModel state, and coroutine-backed updates work together.

**Architecture:** Keep the app in one module and one screen. `MainViewModel` owns a `StateFlow` of UI state and uses `viewModelScope` for an asynchronous initial update. `MainActivity` observes that state with `repeatOnLifecycle`, updates a simple `TextView`, and exposes one button to trigger a ViewModel action.

**Tech Stack:** AndroidX Lifecycle, ViewModel KTX, Kotlin Coroutines, StateFlow, AppCompat, Material Components, Android XML views.

---

### Task 1: Declare lifecycle and coroutine dependencies

**Files:**
- Modify: `gradle/libs.versions.toml`
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Add the dependency coordinates**

```toml
[versions]
lifecycle = "2.8.7"
coroutines = "1.9.0"
activity = "1.10.1"

[libraries]
androidx-lifecycle-runtime-ktx = { group = "androidx.lifecycle", name = "lifecycle-runtime-ktx", version.ref = "lifecycle" }
androidx-lifecycle-viewmodel-ktx = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-ktx", version.ref = "lifecycle" }
androidx-activity-ktx = { group = "androidx.activity", name = "activity-ktx", version.ref = "activity" }
kotlinx-coroutines-core = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version.ref = "coroutines" }
kotlinx-coroutines-android = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-android", version.ref = "coroutines" }
kotlinx-coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version.ref = "coroutines" }
```

- [ ] **Step 2: Wire the libraries into the app module**

```kotlin
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
```

- [ ] **Step 3: Verify dependency resolution**

Run: `./gradlew help`
Expected: Gradle configures successfully with no unresolved catalog entries.

### Task 2: Add the ViewModel-backed Flow state

**Files:**
- Create: `app/src/main/java/com/base/androidscaffold/MainUiState.kt`
- Create: `app/src/main/java/com/base/androidscaffold/MainViewModel.kt`
- Create: `app/src/test/java/com/base/androidscaffold/MainViewModelTest.kt`

- [ ] **Step 1: Write the state model and ViewModel**

```kotlin
data class MainUiState(
    val title: String = "Lifecycle + ViewModel + Flow",
    val counter: Int = 0,
    val loaded: Boolean = false,
)

class MainViewModel(
    private val loadDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(loadDispatcher) {
            delay(200)
            _uiState.update { it.copy(loaded = true) }
        }
    }

    fun increment() {
        _uiState.update { it.copy(counter = it.counter + 1) }
    }
}
```

- [ ] **Step 2: Write the unit test first**

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @Test
    fun emitsLoadedStateAfterCoroutineRuns() = runTest {
        val viewModel = MainViewModel(UnconfinedTestDispatcher(testScheduler))

        assertEquals(false, viewModel.uiState.value.loaded)
        advanceUntilIdle()
        assertEquals(true, viewModel.uiState.value.loaded)
    }

    @Test
    fun incrementsCounter() = runTest {
        val viewModel = MainViewModel(UnconfinedTestDispatcher(testScheduler))

        viewModel.increment()
        assertEquals(1, viewModel.uiState.value.counter)
    }
}
```

- [ ] **Step 3: Run the unit test and confirm it fails before the implementation exists**

Run: `./gradlew :app:testDebugUnitTest --tests com.base.androidscaffold.MainViewModelTest`
Expected: FAIL because the new classes are not present yet.

- [ ] **Step 4: Add the minimal implementation and make the test pass**

Run: `./gradlew :app:testDebugUnitTest --tests com.base.androidscaffold.MainViewModelTest`
Expected: PASS.

### Task 3: Add the activity, layout, and manifest entry

**Files:**
- Create: `app/src/main/java/com/base/androidscaffold/MainActivity.kt`
- Create: `app/src/main/res/layout/activity_main.xml`
- Modify: `app/src/main/AndroidManifest.xml`
- Modify: `app/src/main/res/values/strings.xml`

- [ ] **Step 1: Write the Activity and layout**

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText = findViewById<TextView>(R.id.statusText)
        findViewById<Button>(R.id.incrementButton).setOnClickListener {
            viewModel.increment()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    statusText.text = getString(
                        R.string.main_status,
                        state.title,
                        state.counter,
                        state.loaded
                    )
                }
            }
        }
    }
}
```

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:id="@+id/statusText"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/main_status_initial"
        android:textSize="18sp" />

    <Button
        android:id="@+id/incrementButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/increment" />
</LinearLayout>
```

- [ ] **Step 2: Register the activity and add strings**

```xml
<application
    android:allowBackup="true"
    android:dataExtractionRules="@xml/data_extraction_rules"
    android:fullBackupContent="@xml/backup_rules"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:roundIcon="@mipmap/ic_launcher_round"
    android:supportsRtl="true"
    android:theme="@style/Theme.AndroidScaffold">
    <activity
        android:name=".MainActivity"
        android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
```

```xml
<string name="increment">Increment</string>
<string name="main_status_initial">Lifecycle + ViewModel + Flow</string>
<string name="main_status">%1$s | counter=%2$d | loaded=%3$b</string>
```

- [ ] **Step 3: Build the app module**

Run: `./gradlew :app:assembleDebug`
Expected: PASS with the new screen wired into the manifest.

### Task 4: Final verification

**Files:**
- No additional file changes

- [ ] **Step 1: Re-run the narrowest relevant checks**

Run:
```bash
./gradlew :app:testDebugUnitTest
./gradlew :app:assembleDebug
```
Expected: Both tasks pass cleanly.
