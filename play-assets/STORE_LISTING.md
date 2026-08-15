# Play Store Listing (copy-paste draft)

App name: **Mi Box HDR Fix**

## Short description (80 chars max)

Fix HDR/color handshake failures on Mi Box S — automatic HDMI re-negotiation. No root.

## Full description (~1500 chars)

**Fix your HDMI HDR handshake automatically.**

If your Mi Box S is connected through an HDMI sync box, switch, or receiver, the
HDMI handshake often fails at boot: HDR negotiation breaks and the picture comes
up with the wrong colors until you manually toggle the color depth off and on.

Mi Box HDR Fix automates that fix. After you enable it once, it:

- forces an HDMI re-negotiation a few seconds after boot;
- re-runs it when the screen turns on (wake from standby);
- never repeats within the same boot/wake window, so it stays out of your way.

**How it works**

The app opens the system display-settings screen and emits the exact taps needed
to toggle Color Depth off → on (the verified Mi Box S sequence), forcing the TV
to re-negotiate HDR. It uses Android's Accessibility API — no root, no PC at
boot, nothing to configure.

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

MIT licensed on GitHub: github.com/fedebyes/mi-box-hdr-fix

## Data safety (Play Console answers)

- Does this app collect data? **No**
- Data shared? **No**
- Security practices: data encrypted in transit (N/A — no data), data deletion (N/A)

## Content rating

No content categories apply → expected rating: **Everyone**.

## Accessibility declaration (Play Console)

Declare `DeepColorAccessibilityService` with purpose:
"Forces HDMI re-negotiation on Mi Box S by toggling the display Color Depth
setting at boot/wake, fixing HDR handshake failures with sync boxes. Does not
read screen content or keystrokes."

## Assets

- Icon: `play-assets/icon-512.png` (512×512)
- Feature graphic: `play-assets/feature-graphic-1024x500.png` (1024×500)
- Phone screenshot: `play-assets/screenshot-app-main.png`
- TV screenshot: `play-assets/screenshot-settings.png`
- Privacy policy URL: `https://fedebyes.github.io/mi-box-hdr-fix/PRIVACY_POLICY.md`
