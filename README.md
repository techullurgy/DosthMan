# Dosthman

## 1. Description
**Dosthman** is a cross-platform HTTP API client and request inspector (Postman clone) built with Kotlin Multiplatform (KMP) and Compose Multiplatform. Operating across Android, iOS, Desktop (JVM), and Web (Kotlin/Wasm/JS), Dosthman enables developers to construct HTTP requests, inspect responses, manage headers and query parameters, and format complex JSON payloads within an intuitive multi-tab interface.

---

## 2. Technical Special Aspects
- **Custom Multiplatform JSON Viewer Engine**: Built from the ground up (`json_viewer` module) to parse raw JSON into dynamic visual node trees (`JsonNode`, `JsonState`) with collapsing/expanding tree depth, custom gutter line numbering, hit-test bound detection (`HitTestBounds`), and customizable styling configurations (`JsonViewerStyle`, `JsonViewerColors`).
- **Use-Case Driven Architecture**: Decouples business operations into dedicated domain use cases (`HttpRequestExecutorUsecase`, `SaveRequestUsecase`, `CreateNewRequestUsecase`, `HttpRequestHeadersChangeUsecase`, `HttpRequestParamsChangeUsecase`).
- **Cross-Platform Dependency Injection**: Utilizes Koin with target-specific platform data modules (`PlatformDataModule.android.kt`, `PlatformDataModule.ios.kt`, `PlatformDataModule.js.kt`).
- **Dynamic Tab & Collection Management**: Supports concurrent HTTP request tab workflows with real-time reactive state updates.

---

## 3. Technologies Used
- **Languages**: Kotlin (100% shared business logic & UI)
- **UI Framework**: Compose Multiplatform (Android, iOS, Desktop, Web)
- **HTTP Engine**: Ktor HTTP Client (`ktor-client-core`, target-specific engines)
- **Dependency Injection**: Koin Multiplatform
- **State & Concurrency**: Kotlin Coroutines, StateFlow, ViewModel
- **Build System**: Gradle KTS, Version Catalogs

---

## 4. Testing Technologies
- **Unit Testing Framework**: `kotlin.test`
- **Asynchronous Flow Testing**: Turbine (Flow assertions)
- **Target Test Suites**:
  - `commonTest`: JSON node conversion logic and HTTP request builder state tests
  - `jvmTest` & `iosTest`: Platform execution verification

---

## 5. Cloud Technologies
- N/A (Standalone desktop, mobile, and web developer productivity application).

---

## 6. ROADMAP
- [ ] Implement Environment Variables & dynamic template substitution (e.g., `{{baseUrl}}/api/v1`).
- [ ] Add support for WebSocket, gRPC, and GraphQL request inspection.
- [ ] Build Postman Collection (v2.1) and OpenAPI/Swagger import/export capabilities.
- [ ] Add pre-request scripting and automated response assertion execution engines.
