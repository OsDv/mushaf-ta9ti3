# Mushaf Ta9ti3 (مصحف تقطيع)

Offline Android application for reading Holy Quran and doing "Ta9ti3 tests"

## Features

* **Complete Offline Support:** Uses a pre-packaged SQLite database (`quran.db`) to ensure the entire Mushaf is available without an internet connection.
* **Dynamic Typography:** Switch instantly between standard Quranic script and Digital Khatt .
* **Quran Script and Resources:** from Quran Universal Library [QUL](https://qul.tarteel.ai/resources)

## Tech Stack

* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose
* **Database:** Room 3.0 (with Native SQLite & `RoomRawQuery` support)
* **Local Storage:** Jetpack DataStore (Preferences)
* **Architecture:** MVVM (Model-View-ViewModel).

## 🚀 Getting Started

### Prerequisites
* Android Studio (Latest stable version recommended)
* JDK 17 or higher

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/osdv/mushaf-ta9ti3.git
2. Open the project in Android Studio.
3. Important: Ensure the pre-packaged database file (quran.db) is located in app/src/main/assets/.
4. Sync the project with Gradle files.
5. Build and run on an emulator or physical device.

Note: If you update the quran.db asset during development, you may need to clear the app data or reinstall the app to force Room to copy the new database schema.
