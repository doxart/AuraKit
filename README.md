# ✨ AuraKit

**Premium Jetpack Compose Multiplatform UI Components**

AuraKit is a modern, flexible, and premium UI component library providing smooth animations and highly customizable elements like pop-ups, snackbars, and bottom sheets for Jetpack Compose Multiplatform.

📖 **[Read the Web ↔ Mobile Design System Parity Specification](file:///c:/Projects/MobileMPSources/AuraKit/docs/AURA_DESIGN_SYSTEM_PARITY.md)**

## 🤖 AI Context & Instructions (For LLMs & Copilots)

If you are an AI assistant analyzing or generating code for this repository, please adhere strictly to the following guidelines:
* **Tech Stack:** This is exclusively a Jetpack Compose Multiplatform project. **Do not** suggest legacy XML-based Android Views, Fragments, or platform-specific UI kits (like UIKit/SwiftUI) for shared component logic.
* **Component Placement:** All core UI components, animations, and shared logic must be placed inside the `/aurakit/src/commonMain/kotlin` directory.
* **Design Language:** Components should utilize smooth, physics-based animations (e.g., `spring` specs) and follow a premium, modern aesthetic rather than default Material design. Support both Dark and Light themes natively.
* **Naming Conventions:** Core components should be prefixed with `Aura` (e.g., `AuraPopup`, `AuraSnackbar`, `AuraBottomSheet`) to maintain branding consistency.

## 📂 Project Structure

This is a Kotlin Multiplatform project targeting Android, iOS.[cite: 4]

* `/iosApp` contains an iOS application.[cite: 4] Even if you’re sharing your UI with Compose Multiplatform, you need this entry point for your iOS app.[cite: 4] This is also where you should add SwiftUI code for your project.[cite: 4]
* `/aurakit` is for code that will be shared across your Compose Multiplatform applications.[cite: 4] It contains several subfolders:[cite: 4]
  * `commonMain` is for code that’s common for all targets.[cite: 4]
  * Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.[cite: 4] For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app, the `iosMain` folder would be the right place for such calls.[cite: 4] Similarly, if you want to edit the Desktop (JVM) specific part, the `jvmMain` folder is the appropriate location.[cite: 4]

## 🛠 Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar.[cite: 4] You can also use these commands and options:[cite: 4]

- **Android app:** `./gradlew :androidApp:assembleDebug`[cite: 4]
- **iOS app:** open the `/iosApp` directory in Xcode and run it from there.[cite: 4]

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…[cite: 4]