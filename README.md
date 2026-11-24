# Kotlin Multiplatform project (Using Kotlin Toolchain)

This project is targeting Android, iOS, Desktop (fast UI development with Compose Hot Reload), built with
the [Kotlin Toolchain](https://kotlin-toolchain.org/latest/).

- [/androidApp](./androidApp) contains the Android application.
- [/desktopApp](./desktopApp) contains the desktop (JVM) application.
- [/iosApp](./iosApp) contains the iOS application.
- [/shared](./shared) holds the code shared across your applications — Compose UI,
  business logic, and platform-specific implementations. [src](./shared/src) is for common code; the
  sibling `src@<platform>` folders (for example `src@android`) hold code compiled only for the platform named in the
  folder.

The `kotlin` (macOS/Linux) and `kotlin.bat` (Windows) scripts in the project root are self-bootstrapping wrappers for
the Kotlin Toolchain: they download the pinned toolchain version on first use, so no separate installation is required.
Build the whole project with `./kotlin build`.

### Running

Run each module from the command line:

- Android app: `./kotlin run -m androidApp`
- Desktop app: `./kotlin run -m desktopApp`
- iOS app: `./kotlin run -m iosApp`

### Testing

Run the project's tests with `./kotlin test`, or `./kotlin test -m <module>` for a single module.

---

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html), [Kotlin Toolchain](https://kotlin-toolchain.org/latest/), [Compose Multiplatform](https://kotlinlang.org/compose-multiplatform/), [Kotlin/Wasm](https://kotl.in/wasm/), [Ktor](https://ktor.io/).

# ApiClientK (DosthMan)

**ApiClientK** is a modern, cross-platform REST API client built with **Kotlin Multiplatform** and **Compose Multiplatform**, providing a fast and intuitive way to test, debug, and interact with HTTP APIs across **Android**, **iOS**, and **Desktop**. Inspired by tools like Postman, the application focuses on delivering a native, lightweight experience while sharing a single codebase across multiple platforms.

The project showcases modern Kotlin development practices, combining declarative UI, scalable architecture, and cross-platform networking into a production-ready application.

---

### [Screenshots + Demo] will be available Very Soon.

---

## Highlights

- **Cross-Platform Application**
  - Single codebase targeting Android, iOS, and Desktop.
  - Shared business logic and user interface across all platforms.

- **REST API Testing**
  - Send HTTP requests using common methods such as `GET`, `POST`, `PUT`, `PATCH`, and `DELETE`.
  - Configure request URLs, headers, query parameters, and request bodies.
  - View response status, headers, and payload.

- **Custom JSON Viewer**
  - Beautifully formatted and syntax-highlighted JSON responses.
  - Expandable and collapsible JSON tree.
  - Improved readability for large and deeply nested payloads.

- **Responsive User Experience**
  - Adaptive layouts optimized for phones, tablets, and desktop screens.
  - Smooth animations and transitions for an intuitive workflow.

---

## Architecture

The application follows a modular, clean architecture that separates presentation, networking, and shared business logic, enabling maintainability and scalability.

### Core Technologies

- **Kotlin Multiplatform (KMP)**
  - Shared business logic across Android, iOS, and Desktop.
  - Platform-specific implementations only where necessary.

- **Compose Multiplatform (CMP)**
  - Declarative UI shared across all supported platforms.
  - Responsive layouts with a consistent user experience.

- **Ktor Client**
  - Cross-platform HTTP client for executing REST API requests.
  - Supports configurable requests, headers, authentication, and response handling.

- **Koin**
  - Dependency Injection for shared and platform modules.
  - Simplifies dependency management and improves testability.

- **Jetpack Navigation 3**
  - Type-safe navigation.
  - Predictable navigation state.
  - Modular navigation architecture.

---

## Technical Focus

ApiClientK demonstrates how to build a sophisticated developer tool using modern Kotlin technologies while maintaining a shared codebase across multiple platforms.

Key areas include:

- Cross-platform networking with Ktor Client.
- Declarative UI using Compose Multiplatform.
- Responsive layouts for mobile and desktop.
- Custom JSON parsing and visualization.
- State-driven UI with reactive updates.
- Dependency Injection using Koin.
- Navigation using the latest Navigation 3 APIs.
- Modular architecture for scalability and maintainability.

---

## Skills Demonstrated

- Kotlin Multiplatform Development
- Compose Multiplatform UI
- Android Development
- iOS Development
- Desktop Application Development
- REST API Client Development
- HTTP Networking with Ktor Client
- JSON Parsing & Visualization
- Responsive UI Design
- Animated User Interfaces
- Dependency Injection with Koin
- Navigation Architecture
- State Management
- Clean Architecture
- Modular Project Structure