# CorpSync Mobile

Android app for corporate field operations. Built with Jetpack Compose and Supabase.

## Features

- **Authentication** — email/password sign-in and account creation via Supabase Auth
- **Ticket photos** — capture ticket images using the device camera

## Tech Stack

| Layer | Library / Version |
|---|---|
| Language | Kotlin 2.2.10 |
| UI | Jetpack Compose + Material3 (BOM 2026.02.01) |
| Backend | Supabase (supabase-kt 3.5.0) — Auth, PostgREST, Realtime |
| HTTP | Ktor CIO 3.4.0 |
| Min SDK | 29 (Android 10) |
| Target SDK | 36 |
| Build | AGP 9.1.1, Gradle Kotlin DSL |

## Project Structure

```
app/src/main/java/com/example/corpsyncmobile/
├── MainActivity.kt        # Entry point; screen-state router (Login → SignUp | Home)
├── LoginScreen.kt         # Email/password sign-in form
├── SignUpScreen.kt        # New account registration form
├── SupabaseClient.kt      # Supabase singleton (Auth, PostgREST, Realtime)
├── TicketCameraScreen.kt  # Camera permission handling + ticket photo capture
└── ui/theme/              # Material3 theme (Color, Type, Theme)
```

## Setup

### 1. Prerequisites

- Android Studio Hedgehog or later
- JDK 11+ (JetBrains Toolbox JRE works)
- A Supabase project with email auth enabled

### 2. Configure credentials

Create `local.properties` in the project root (it is gitignored):

```properties
supabase.url=https://<your-project>.supabase.co
supabase.anon_key=<your-anon-key>
```

### 3. Build & run

```bash
# Debug APK
JAVA_HOME="/Applications/JetBrains Toolbox.app/Contents/jre/Contents/Home" ./gradlew assembleDebug

# Install on connected device
JAVA_HOME="/Applications/JetBrains Toolbox.app/Contents/jre/Contents/Home" ./gradlew installDebug

# Unit tests
./gradlew test

# Lint
./gradlew lint
```

## Permissions

| Permission | Purpose |
|---|---|
| `INTERNET` | Supabase API calls |
| `CAMERA` | Capture ticket photos |

Camera is declared with `required="false"` so the app installs on devices without a rear camera.
