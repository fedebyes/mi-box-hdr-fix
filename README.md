# Mi Box HDR Fix

Forces HDMI re-negotiation on your **Mi Box S** at boot and on wake — fixing HDR/color handshake failures caused by HDMI sync boxes, switches, and receivers. No root, no PC at boot.

[![Build & Test](https://github.com/fedebyes/mi-box-hdr-fix/actions/workflows/build.yml/badge.svg)](https://github.com/fedebyes/mi-box-hdr-fix/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

<p align="center">
  <img src="play-assets/icon-512.png" alt="Mi Box HDR Fix icon" width="128">
</p>

![App](play-assets/screenshot-app-main.png)

## The problem

TV box → HDMI sync box / switch / receiver → TV chains often **fail the HDMI handshake at boot**: HDR negotiation breaks, the color matrix comes out wrong, and the only fix is toggling the display color depth once to force re-negotiation. Doing that manually on every boot/wake gets old fast.

## What it does

A tiny framework-only Android app (no AndroidX, no Internet permission) that:

- listens for **boot** and **screen-on** events;
- opens the system display-settings screen and emits the verified tap sequence that toggles Color Depth, forcing the TV to **re-negotiate HDR**;
- **guards** itself: the sequence runs at most once per boot/wake window (dual elapsed + wall-clock guard), and aborts if any step is rejected.

Requires **no root** and **no PC at boot**. The only permission is the
accessibility service, which you enable once after install.

## Screenshots

| App | Display settings (what the app automates) |
|---|---|
| ![App main screen](play-assets/screenshot-app-main.png) | ![Display settings](play-assets/screenshot-settings.png) |

## Compatibility

| | |
|---|---|
| Min Android | 8.0 (API 28) |
| Target Android | 15 (API 36) |
| Tested on | Xiaomi Mi Box S (Android 9, DroidLogic firmware) |
| Display settings UI | `com.droidlogic.tv.settings.display.DisplayActivity` (1080p coordinates) |

> ⚠️ The tap sequence is **coordinate-based** and verified against the Mi Box S's
> DroidLogic 1080p settings UI. On other boxes/firmware the coordinates may need
> adjusting — see `Config.kt` and re-verify with `uiautomator dump`.

> 🧪 **2nd gen / 3rd gen (Google TV): NOT yet supported.** The coordinates target
> the DroidLogic settings UI (1st gen only). Any change intended to extend support
> to 2nd/3rd gen boxes **must be validated by the maintainer on real hardware
> before merging** — do not submit coordinate changes for those generations
> without opening an issue first. See [docs/r-mibox-post.md](docs/r-mibox-post.md).

## Build

Requires JDK 17 and the Android SDK (path via `local.properties`).

```bash
gradle :app:testDebugUnitTest   # 15 unit tests (pure Kotlin, no device)
gradle :app:assembleDebug       # APK at app/build/outputs/apk/debug/app-debug.apk
gradle :app:bundleRelease       # AAB for Play Store (signed if keystore configured)
```

Release signing reads an optional `keystore.properties` from
`~/.local/secrets/asus/deepcolor-upload-keystore.properties` — when absent,
release builds are unsigned.

## Install on a TV box (one-time, PC needed)

```bash
bash scripts/install.sh            # adb over WiFi, default 192.168.1.137:5555
BOX_IP=192.168.1.168:5555 bash scripts/install.sh   # override box address
```

Installs the APK and enables the accessibility service (appends to the existing
service list — never overwrites other enabled services).

## Verify

```bash
bash scripts/verify.sh
```

Shows the active display mode and enabled accessibility services. After a reboot,
the box should negotiate the correct 4K HDR mode and colors should be right.

## Privacy

No data is collected, stored remotely, or shared. The app has no Internet
permission. It keeps two timestamps locally (last fix run) to avoid re-toggling
every screen-on. See [PRIVACY_POLICY.md](PRIVACY_POLICY.md).

## License

MIT — see [LICENSE](LICENSE).
