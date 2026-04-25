# பரிசுத்த வேதாகமம் – Tamil Bible APK

Auto-builds an Android APK from your single-file HTML Tamil Bible app using GitHub Actions.

---

## 🚀 How to get your APK

### Step 1 — Create a new GitHub repository

1. Go to [github.com/new](https://github.com/new)
2. Name it `tamil-bible-apk` (or anything you like)
3. Set it to **Public** or **Private**
4. **Do NOT** initialize with README (you'll push this folder)

### Step 2 — Drop your HTML file in

Copy your latest `tamil-bible-v13.html` (or whatever version) into the `android/app/src/main/assets/www/` folder and rename it to `index.html`.

```
android/app/src/main/assets/www/index.html   ← your HTML file goes here
```

### Step 3 — Push to GitHub

```bash
cd tamil-bible-apk
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/tamil-bible-apk.git
git push -u origin main
```

### Step 4 — Wait ~5 minutes

Go to your repo → **Actions** tab → watch the build run.

### Step 5 — Download APK

When the build is green ✅:
- Go to **Actions** → click the latest run → scroll to **Artifacts**
- Download `tamil-bible-debug.apk`
- Transfer to your Android phone and install

> **Install tip:** On your phone go to Settings → Security → enable "Install from unknown sources" (or "Install unknown apps"), then open the APK file.

---

## 📁 Project structure

```
tamil-bible-apk/
├── .github/
│   └── workflows/
│       └── build-apk.yml          ← GitHub Actions CI pipeline
├── android/
│   └── app/
│       └── src/
│           └── main/
│               ├── assets/
│               │   └── www/
│               │       └── index.html   ← YOUR HTML FILE HERE
│               ├── res/
│               │   ├── values/
│               │   │   ├── strings.xml
│               │   │   └── colors.xml
│               │   ├── drawable/
│               │   │   └── splash.xml
│               │   └── mipmap-*/
│               │       └── ic_launcher.png  (auto-generated)
│               ├── AndroidManifest.xml
│               └── java/com/tamilbible/app/
│                   └── MainActivity.java
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── gradle.properties
├── settings.gradle
└── README.md
```

---

## ⚙️ Customisation

| What | Where |
|------|-------|
| App name | `android/app/src/main/res/values/strings.xml` |
| App version | `android/app/build.gradle` → `versionName` |
| Package ID | `android/app/build.gradle` → `applicationId` |
| Min Android | `android/app/build.gradle` → `minSdk` (default 21 = Android 5.0+) |
| App icon | Replace the `ic_launcher.png` files in each `mipmap-*` folder |

---

## 🔑 Signed release APK (optional)

The default build produces a **debug APK** — fine for personal use. For a proper release APK:

1. Generate a keystore: `keytool -genkey -v -keystore release.jks -keyalg RSA -keysize 2048 -validity 10000 -alias tamilbible`
2. Add these GitHub Secrets (repo → Settings → Secrets → Actions):
   - `KEYSTORE_BASE64` — base64 of your `.jks` file
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS`
   - `KEY_PASSWORD`
3. Uncomment the `# SIGNED RELEASE` block in `.github/workflows/build-apk.yml`

---

## 📱 Features in the APK

- Full offline Tamil Bible (31,102 வசனங்கள்)
- Tamil + English KJV text
- Verse of the Day, Favorites, Pinned, Reading Goals
- Quiz mode, Story mode
- Search across all books
- Share verse as image card (12 colour themes)
- Font size & colour customisation
- All data stored locally on device (localStorage)
