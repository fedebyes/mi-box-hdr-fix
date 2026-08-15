# Play Store Listing (copy-paste draft)

App name: **Deep Color Toggle**

## Short description (80 chars max)

Fix HDMI color-depth handshake issues on Android TV boxes automatically. No root.

## Full description (~1500 chars)

**Fix your HDMI handshake automatically.**

TV boxes connected through an HDMI switch, LED sync box, or receiver often fail
the HDMI handshake at boot: the picture comes up with the wrong colors until you
manually toggle *Color Depth Settings* off and on.

Deep Color Toggle automates that fix. After you enable it once, it:

- runs the toggle automatically a few seconds after boot;
- re-runs it when the screen turns on (wake from standby);
- never repeats within the same boot/wake window, so it stays out of your way.

**How it works**

The app opens the system display-settings screen and emits the exact taps needed
to toggle Color Depth off → on (the verified Mi Box S sequence). It uses Android's
Accessibility API — no root, no PC at boot, nothing to configure.

**Compatibility**

- Android 8.0+ (API 28)
- Tested on Xiaomi Mi Box S (Android 9, DroidLogic firmware)
- Requires the DroidLogic display settings UI (1080p coordinate layout)

Note: the automation is coordinate-based and verified against the Mi Box S
firmware. On other boxes the display-settings layout may differ.

**Privacy first**

- No data collected, period.
- No Internet permission — the app cannot connect to anything.
- No ads, no analytics, no tracking.
- The accessibility service is off until you enable it, and it does not read
  screen content or keystrokes.

**Open source**

MIT licensed on GitHub: github.com/fedebyes/mibox-deep-color-toggle

## Data safety (Play Console answers)

- Does this app collect data? **No**
- Data shared? **No**
- Security practices: data encrypted in transit (N/A — no data), data deletion (N/A)

## Content rating

No content categories apply → expected rating: **Everyone**.

## Accessibility declaration (Play Console)

Declare `DeepColorAccessibilityService` with purpose:
"Automates a display-settings toggle (Color Depth) on Android TV boxes to fix
HDMI handshake/color issues. Does not read screen content or keystrokes."

## Assets

- Icon: `play-assets/icon-512.png` (512×512)
- Feature graphic: `play-assets/feature-graphic-1024x500.png` (1024×500)
- Phone screenshot: `play-assets/screenshot-app-main.png`
- TV screenshot: `play-assets/screenshot-settings.png`
- Privacy policy URL: `https://fedebyes.github.io/mibox-deep-color-toggle/PRIVACY_POLICY.md`
