# HMorix Client Portal – Native Kotlin Android App

Official Kotlin Android application for **HMorix Enterprise Clients & Users**. Built with **Jetpack Compose**, **Material 3 Cyber Theme** (`#0D0D0D` Obsidian, `#C8FF00` Electric Lime), **Retrofit & Coroutines**, **Encrypted SharedPreferences**, and the new **Hexagonal Cyber Monogram Brand Icon**.

---

## 🌟 Key Features for Clients & Users

- 📊 **Real-time Client Portal**: View active deliverables, milestone progress bars, assigned team members, and telemetry.
- 🎫 **Support Tickets System**: Submit technical requests, track ticket status (`OPEN`, `IN_PROGRESS`, `RESOLVED`), and chat with engineers.
- 💳 **Billing & Invoices (BillingFlow)**: Check due invoices, payment statuses, and download official PDF statements.
- 🤖 **Enterprise AI Assistant**: Direct neural chatbot integration for instant project assistance, ticket triage, and architectural queries.
- 🌐 **Hybrid Web Bridge**: Secure WebView with synced session cookies for accessing full web services and live demos.
- 🔒 **Biometric & Secure Auth**: Encrypted session storage with AES-256 GCM encryption.
- ⚡ **Automated Free Cloud APK Build**: Pre-configured GitHub Actions workflow that compiles and produces downloadable `.apk` artifacts for **100% free**.

---

## 🚀 1. How to Push to a Separate GitHub Repository

You can initialize `android-client` as its own standalone repository and push it to GitHub:

```bash
# 1. Navigate to the android-client directory
cd android-client

# 2. Initialize a new Git repository
git init
git add .
git commit -m "feat: initial commit for HMorix Kotlin Android Client App"

# 3. Rename branch to main
git branch -M main

# 4. Link to your new separate GitHub repository
git remote add origin https://github.com/<YOUR_USERNAME>/hmorix-client-android.git

# 5. Push to GitHub
git push -u origin main
```

---

## ☁️ 2. How to Open and Build in Cloud Android Studio (Project IDX / Codespaces / Gitpod)

### Option A: Google Project IDX / Cloud Android Studio (100% Free)
1. Go to [https://idx.dev/](https://idx.dev/) (Google Project IDX with Cloud Android Emulator).
2. Click **Import a repo** and enter your GitHub repository URL: `https://github.com/<YOUR_USERNAME>/hmorix-client-android`.
3. Choose the **Android** template. Project IDX will automatically provision the Cloud Android SDK and Kotlin environment.
4. Click **Run** to launch the cloud Android emulator and test the app in your browser!

### Option B: GitHub Codespaces / Gitpod (Free Cloud Linux VM)
1. In your GitHub repository, click **Code** -> **Codespaces** -> **Create codespace on main**.
2. In the terminal, run:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
3. Your compiled APK will be located at `app/build/outputs/apk/debug/app-debug.apk`!

---

## 🛠️ 3. How to Build the APK Free via GitHub Actions CI/CD

This repository includes a pre-configured GitHub Actions workflow in [`.github/workflows/build-apk.yml`](.github/workflows/build-apk.yml).

Every time you push to `main` (or click **Run workflow** in the GitHub Actions tab):
1. GitHub will automatically spin up a clean Ubuntu runner with JDK 17.
2. It compiles the Kotlin code and builds the APK.
3. Once finished, click on the workflow run in the **Actions** tab.
4. Download the ready-to-install **`hmorix-client-debug-apk`** directly to your phone!

---

## 💻 4. Local Build with Android Studio

1. Open **Android Studio** (Hedgehog 2023.1.1 or newer).
2. Select **Open** and select the `android-client` folder.
3. Allow Gradle to sync dependencies.
4. Click the green **Run** button or execute in the terminal:
   ```bash
   ./gradlew assembleDebug
   ```
5. The generated APK will be in:
   `app/build/outputs/apk/debug/app-debug.apk`

---

## 📐 Project Architecture

```
android-client/
├── .github/workflows/
│   └── build-apk.yml              # Free CI/CD APK compilation
├── app/
│   ├── build.gradle.kts           # App-level dependencies & build rules
│   ├── proguard-rules.pro         # Proguard & R8 obfuscation config
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── res/
│       │   ├── drawable/
│       │   │   └── ic_logo_hex.xml # Exact Hexagonal Cyber Logo Vector
│       │   └── values/
│       │       ├── colors.xml     # Obsidian & Electric Lime palette
│       │       └── themes.xml     # Material 3 Cyber Dark Theme
│       └── java/in/hmorix/client/
│           ├── HMorixApp.kt       # Application class & DI setup
│           ├── data/
│           │   ├── api/           # Retrofit & OkHttp client with Cookie Jar
│           │   ├── local/         # EncryptedSharedPreferences session manager
│           │   ├── model/         # User, Portal, Ticket, Invoice models
│           │   └── repository/    # PortalRepository with async Coroutines
│           └── ui/
│               ├── MainActivity.kt # Root Compose navigation & bottom bar
│               ├── auth/          # Sign In & Sign Up screens
│               ├── portal/        # Client Portal dashboard
│               ├── tickets/       # Support tickets management & creation
│               ├── invoices/      # Billing & Invoices overview
│               ├── ai/            # Neural AI Assistant chat
│               ├── webview/       # Full hybrid web bridge
│               └── settings/      # Account settings & server config
├── gradle/
│   └── libs.versions.toml         # Gradle Version Catalog
├── build.gradle.kts               # Project-level Gradle build file
└── settings.gradle.kts            # Project settings
```

---

## 🔐 Configuration & API Server

By default, the client app connects to `https://hmorix.in/api`. You can change the backend URL in real time:
1. Open the app -> Go to **Account & Settings**.
2. Tap **Change** next to **API Server Endpoint**.
3. Enter your custom server or staging IP (e.g. `http://10.0.2.2:5000/api` for Android Emulator localhost testing).
