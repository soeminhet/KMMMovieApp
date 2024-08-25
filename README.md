# KMM Movie App

![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blueviolet.svg)
![Swift](https://img.shields.io/badge/Swift-5.0-orange.svg)
![Platform](https://img.shields.io/badge/Platform-KMM-blue.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)

A cross-platform mobile application for discovering movies, built using Kotlin Multiplatform Mobile (KMM) with a focus on modern development practices. This app leverages shared business logic and platform-specific UI layers for both Android and iOS.

## 📱 Features

- 🎥 **Discover Movies**: Browse through popular, top-rated, and upcoming movies with detailed information.
- 💾 **Offline Support with SQLDelight**: Data caching with SQLDelight for an offline-first experience, allowing users to save their favorite movies.
- 🧭 **Smooth Pagination**: Efficiently load more data with scroll using Ktor.
- 🌐 **Ktor for Networking**: Lightweight HTTP client to fetch movie data from the TMDB API.
- 📊 **Cross-Platform Shared Logic**: Core logic shared across Android and iOS using KMM.
- 📱 **Native UI**: Platform-specific UIs implemented using Jetpack Compose on Android and SwiftUI on iOS.
- 🔄 **Swift Interop with Kotlin via SKIE**: Seamless integration between Kotlin and Swift, utilizing SKIE libraries for effective interoperability.

## 🛠️ Technologies Used

- **Kotlin Multiplatform Mobile (KMM)**: For sharing business logic across Android and iOS.
- **Ktor**: Networking library for API requests.
- **SQLDelight**: Database library for cross-platform persistence, including saving favorite movies.
- **Jetpack Compose**: UI toolkit for Android.
- **SwiftUI**: UI toolkit for iOS.
- **SKIE**: Libraries for Swift-Kotlin interop.
- **Coroutines**: Managing asynchronous tasks in a clean way.

## 🎥 Demo

https://github.com/user-attachments/assets/3bac68b0-f5e0-438b-a321-1a41e4fa5717

## 🚀 Getting Started

To get a local copy up and running, follow these simple steps:

### Prerequisites

- **Android Studio**: Latest version for Android development.
- **Xcode**: Latest version for iOS development.
- **Gradle**: Included in the project.

### Installation

1. **Clone the repository**:

    ```bash
    git clone https://github.com/soeminhet/KMMMovieApp.git
    ```

2. **Open the project**:
    - For Android: Open in Android Studio.
    - For iOS: Open the `iosApp` directory in Xcode.

3. **Build and Run**:
    - Android: Click the `Run` button in Android Studio or press `Shift + F10`.
    - iOS: Build and run the app in Xcode.

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

## 🧑‍💻 Author

**SoeMinHtet** - [@soeminhet](https://github.com/soeminhet)

## 📞 Contact

For any inquiries, please reach out to [soeminhet@gmail.com](mailto:soeminhet@gmail.com).

---
