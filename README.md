# mibox-deep-color-toggle

Custom no-root Android app for the Xiaomi Mi Box S (`oneday`, Android 9) that toggles **Color Depth Settings** (deep color) off → on at boot/wake to force HDMI re-negotiation with the LED sync box + LG Nano 55 chain.

**Status:** plan ready — not built yet.

## Why

Mi Box → LED sync box (HDMI IN) → LG Nano 55. All devices power on together → HDMI handshake fails → wrong color matrix. Manual fix: toggle deep color once. This app automates that, fully on-box, open source, no root, no PC at boot.

Full research + verified tap sequence: vault note `house/mibox-deep-color-automation.md` (2026-08-14).

## How

- **AccessibilityService** (only permission needed, granted at install) dispatches `input tap`-style gestures on the DroidLogic display settings UI
- `BootReceiver` (manifest `BOOT_COMPLETED`) + `ScreenOnReceiver` trigger the toggle sequence
- Guard: toggle once per boot (SharedPreferences flag)
- Verified tap sequence (1920×1080 UI, coords in vault note)

## Build

Toolchain staged on Asus via yay (see plan): `jdk17-openjdk`, `gradle`, `android-tools`, `android-sdk-cmdline-tools-latest`, `android-platform-34`, `build-tools;34.0.0`.

```bash
# from project root, after toolchain installed:
./gradlew assembleDebug
# APK at app/build/outputs/apk/debug/app-debug.apk
```

## Install (one-time, PC needed)

```bash
adb connect 192.168.1.137:5555
adb install app/build/outputs/apk/debug/app-debug.apk
adb shell settings put secure enabled_accessibility_services <pkg>/.DeepColorAccessibilityService
adb shell settings put secure accessibility_enabled 1
```

## License

Private project (2026). Source may be open-sourced later.
