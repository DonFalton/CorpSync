# Changelog

All notable changes to CorpSync Mobile are documented here.

---

## [Unreleased]

> Changes on `main` not yet versioned.

- Added `README.md` with project overview, tech stack, setup instructions, and permissions table.
- Added `CHANGELOG.md` (this file).

---

## 2026-04-22

---

### `465aa58` — feature: create a sign up activity

**New file — `SignUpScreen.kt`**
- New `SignUpScreen` composable reachable from the login form.
- Fields: Email, Password, Confirm Password — all outlined text fields with keyboard type hints and password masking.
- Client-side validation: passwords are compared before any network call; a clear error is shown if they don't match.
- On submit, calls `supabase.auth.signUpWith(Email)` with the trimmed email and password.
- On success, replaces the form with a confirmation message ("Check your email to confirm your account") and a "Go to Sign In" button, handling Supabase's default email-verification flow.
- Error messages from Supabase (duplicate email, weak password, etc.) are shown inline below the fields.
- Loading state disables all inputs and the submit button, and replaces the button label with a `CircularProgressIndicator`.
- "Already have an account? Sign in" `TextButton` at the bottom navigates back to the login form.
- The column is wrapped in `verticalScroll` so the form stays usable on small screens or with the keyboard open.

**Modified — `LoginScreen.kt`**
- Added `onSignUp: () -> Unit` callback parameter.
- Added "New user? Create an account" `TextButton` below the Sign In button, disabled while a login request is in flight.

**Modified — `MainActivity.kt`**
- Replaced the `isLoggedIn: Boolean` state with a `Screen` enum (`Login`, `SignUp`, `Home`).
- Initial screen is determined by `supabase.auth.currentSessionOrNull()`: existing session goes straight to `Home`, otherwise `Login`.
- Single `Scaffold` renders the correct composable via a `when` expression, eliminating duplicated scaffold instances.
- `LoginScreen` now receives `onSignUp = { screen = Screen.SignUp }`.
- `SignUpScreen` receives `onBackToLogin = { screen = Screen.Login }`.

---

### `23c1d03` — feature: add activity for take a picture

**New file — `TicketCameraScreen.kt`**
- New `TicketCameraScreen` composable shown after a successful login.
- Checks `CAMERA` permission at composition time using `ContextCompat.checkSelfPermission`.
- Uses `rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission())` to request the permission at runtime; tracks grant/deny state and shows an explanatory error message if denied.
- Uses `rememberLauncherForActivityResult(ActivityResultContracts.TakePicture())` to launch the system camera. A temp `.jpg` file is created in the app's cache directory, and a `FileProvider` URI is passed to the camera so no broad storage permission is required.
- On camera return, the captured `Uri` is decoded with `BitmapFactory.decodeStream` via the content resolver and displayed in a `Image` composable with `ContentScale.Crop` inside a rounded, bordered frame (4:3 aspect ratio).
- "Retake Photo" `OutlinedButton` appears below the image to replace it.
- While no photo exists, a placeholder box with "No photo yet" is shown.
- The action button label adapts: "Grant Camera Permission" if permission is missing, "Take Ticket Photo" once it is granted.

**New file — `res/xml/file_paths.xml`**
- Declares a `<cache-path>` entry so `FileProvider` can produce `content://` URIs for files written to the app's cache directory.

**Modified — `AndroidManifest.xml`**
- Added `<uses-permission android:name="android.permission.CAMERA" />`.
- Added `<uses-feature android:name="android.hardware.camera" android:required="false" />` — `required="false"` keeps the app installable on devices without a rear camera.
- Registered `androidx.core.content.FileProvider` with authority `${applicationId}.provider`, `exported="false"`, `grantUriPermissions="true"`, and the `file_paths.xml` meta-data entry.

**Modified — `LoginScreen.kt`**
- Created as a new file in this commit alongside the camera work (initial login UI).
- `LoginScreen` composable with email and password `OutlinedTextField`s.
- Sign In button calls `supabase.auth.signInWith(Email)` in a coroutine; on success invokes `onLoginSuccess()`.
- Loading spinner inside the button while the request is in flight; button disabled until both fields are non-blank.
- Inline error message displayed below the password field on failure.

**Modified — `MainActivity.kt`**
- Replaced the placeholder `Greeting` composable with a session-aware router.
- Checks `supabase.auth.currentSessionOrNull()` on startup to decide the initial screen.
- Renders `LoginScreen` or `TicketCameraScreen` depending on auth state.
- `onLoginSuccess` flips the state to show `TicketCameraScreen`.

---

### `34a7cad` — feature: add supabase client

**New file — `SupabaseClient.kt`**
- Top-level `supabase` singleton created via `createSupabaseClient`.
- Installs three plugins: `Auth`, `Postgrest`, `Realtime`.
- Credentials (`supabaseUrl`, `supabaseKey`) read from `BuildConfig.SUPABASE_URL` and `BuildConfig.SUPABASE_ANON_KEY` — never hardcoded in source.

**Modified — `app/build.gradle.kts`**
- Reads `local.properties` at build time and exposes values as `BuildConfig` fields (`SUPABASE_URL`, `SUPABASE_ANON_KEY`).
- Enabled `buildConfig = true` in `buildFeatures`.
- Added Supabase BOM platform dependency and individual artifacts: `auth-kt`, `postgrest-kt`, `realtime-kt`.
- Added Ktor CIO engine (`ktor-client-cio`) as the HTTP transport for supabase-kt.
- Applied `kotlin-serialization` plugin (required by supabase-kt for JSON handling).

**Modified — `gradle/libs.versions.toml`**
- Added `supabase = "3.5.0"` and `ktor = "3.4.0"` version entries.
- Added library aliases: `supabase-bom`, `supabase-auth`, `supabase-postgrest`, `supabase-realtime`, `ktor-client-cio`.
- Added plugin alias: `kotlin-serialization`.

**Modified — `AndroidManifest.xml`**
- Added `<uses-permission android:name="android.permission.INTERNET" />` for Supabase API calls.

**New file — `.mcp.json`**
- Added MCP server configuration for Supabase tooling.

**Modified — `CLAUDE.md`**
- Documented Supabase setup: singleton import path, credential convention, `JAVA_HOME` requirement for Gradle commands.

---

### `43cf3f8` — chore: init commit

**Project scaffold generated by Android Studio.**

- `app/build.gradle.kts` — single-module app config: `applicationId`, `minSdk 29`, `targetSdk 36`, Compose BOM, Material3, Kotlin 2.2.10, AGP 9.1.1.
- `gradle/libs.versions.toml` — version catalog with all initial dependency aliases.
- `settings.gradle.kts` — project name and module inclusion.
- `build.gradle.kts` (root) — top-level build file with plugin declarations.
- `gradle.properties` — JVM args, AndroidX opt-in, Kotlin code style.
- `gradlew` / `gradlew.bat` — Gradle wrapper scripts.
- `gradle/wrapper/gradle-wrapper.properties` — pins Gradle distribution URL.
- `gradle/gradle-daemon-jvm.properties` — JVM toolchain config for the Gradle daemon.
- `AndroidManifest.xml` — activity declaration with `MAIN`/`LAUNCHER` intent filter.
- `MainActivity.kt` — initial `ComponentActivity` with `CorpSyncMobileTheme` and a `Greeting` composable placeholder.
- `ui/theme/Color.kt` — seed color definitions for light and dark schemes.
- `ui/theme/Theme.kt` — `CorpSyncMobileTheme` with dynamic color support (Android 12+) and static fallback.
- `ui/theme/Type.kt` — Material3 typography scale.
- `res/` — launcher icons (hdpi → xxxhdpi, round variants), `colors.xml`, `strings.xml`, `themes.xml`, `backup_rules.xml`, `data_extraction_rules.xml`.
- `ExampleUnitTest.kt` / `ExampleInstrumentedTest.kt` — placeholder test classes.
- `.gitignore` — standard Android ignores (`build/`, `local.properties`, `*.iml`, etc.).
- `CLAUDE.md` — initial project instructions for Claude Code.
