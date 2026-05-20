# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build commands

```bash
# Full debug build
./gradlew assembleDebug

# Build a specific module only
./gradlew :core:common:build
./gradlew :core:ui:assembleDebug

# Run unit tests
./gradlew test

# Run tests for a single module
./gradlew :core:common:test

# Run a single test class
./gradlew :core:common:test --tests "com.rahulghag.splittrip.core.common.ExampleTest"

# Install debug APK on connected device/emulator
./gradlew installDebug
```

## Toolchain constraints

- **AGP 8.7.3** and **Kotlin 2.0.0** — do not change either version
- **KSP 2.0.0-1.0.21** — must stay aligned with the Kotlin version
- **minSdk 26**, **compileSdk/targetSdk 35**
- All versions are centralised in `gradle/libs.versions.toml`; never hardcode dependency versions in module `build.gradle.kts` files

## Module architecture

```
:app                  — entry point only; no business logic
:core:common          — pure Kotlin/JVM, zero Android imports (KMP-ready)
:core:ui              — Compose design system, depends on :core:common
```

**Dependency rule:** `:app` → `:core:ui` → `:core:common`. Feature modules (not yet added) will follow the same layering.

### :core:common

Source root: `core/common/src/main/kotlin/`

Uses `kotlin.jvm` plugin (not `android.library`) so it can be migrated to KMP without changes. The hard rule: **no `android.*` imports anywhere in this module**.

Key packages:
- `result/` — `Result<T>` sealed class (Success/Error/Loading) with chainable `onSuccess`, `onError`, `onLoading`, `map` extensions
- `dispatcher/` — `DispatcherProvider` interface + `DefaultDispatcherProvider` / `TestDispatcherProvider` for coroutine dispatcher injection
- `mvi/` — `UiState`, `UiIntent`, `UiEvent` marker interfaces; every screen's state contract implements these three
- `extensions/` — `Flow<T>.asResult()` wraps any flow into `Flow<Result<T>>`; `String.isValidEmail()` (pure Kotlin regex, no `android.util.Patterns`); `String.initials()` for avatar text; `String.toUpiDeepLink()` for payment deep links
- `model/` — `UiText` sealed class (`DynamicString` / `StringResource`) for platform-agnostic UI strings; Android layer resolves `StringResource` via `Context`

### :core:ui

Source root: `core/ui/src/main/java/`

Owns the entire design system. Nothing outside this module should define colours, typography, or spacing.

Key files in `theme/`:
- `Color.kt` — brand palette (Brand50–800), neutrals (Neutral0–900), semantic colours (Success/Warning/Error/Info), `MemberColors` list (6 avatar colours), `MemberContainerColors`
- `Type.kt` — `PlusJakartaSans` font family (Light → ExtraBold), `JetBrainsMono` font family (Regular, Medium), `SplitTripTypography`, and `AmountTextStyle` (JetBrains Mono Medium 16sp — use this for **all** currency amounts)
- `Shape.kt` — `SplitTripShapes` with named sizes (extraSmall=4dp tags/chips → extraLarge=24dp dialogs)
- `Dimens.kt` — `Dimens` object; use `Dimens.spaceL` (16dp) as the standard screen horizontal padding
- `Theme.kt` — `SplitTripTheme` composable; `dynamicColor` is intentionally `false` to preserve brand colours. `SplitTripExtendedColors` holds semantic/member colours not covered by Material slots, exposed via `MaterialTheme.extendedColors`

Font files live in `core/ui/src/main/res/font/` and follow the naming pattern `plus_jakarta_sans_<weight>.ttf` / `jetbrains_mono_<weight>.ttf`.

## MVI pattern

Every screen will follow a strict MVI contract using the three marker interfaces from `:core:common`:

```kotlin
data class FooState(...) : UiState
sealed class FooIntent : UiIntent
sealed class FooEvent : UiEvent   // one-shot side effects (navigation, snackbars)
```

ViewModels emit `StateFlow<UiState>` and `SharedFlow<UiEvent>`; composables send `UiIntent` into the ViewModel.

## DI — Hilt

`SplitTripApplication` is annotated `@HiltAndroidApp`. Activities are `@AndroidEntryPoint`. Use KSP (not kapt) for annotation processing — the plugin alias is `libs.plugins.ksp`.

`DispatcherProvider` should be injected rather than referencing `Dispatchers.*` directly; bind `DefaultDispatcherProvider` in a Hilt module and use `TestDispatcherProvider` in tests.

## New module checklist

When adding a feature module (e.g., `:feature:trips`):
1. Use `android.library` + `kotlin.android` + `kotlin.compose` plugins
2. Set `namespace`, `compileSdk = 35`, `minSdk = 26`
3. Add `include(":feature:trips")` to `settings.gradle.kts`
4. Depend on `:core:ui` (gives Compose + theme) and `:core:common` (gives Result, dispatchers, MVI)
5. Add Hilt + KSP if the module has ViewModels
