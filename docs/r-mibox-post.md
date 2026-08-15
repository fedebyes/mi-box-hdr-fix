# [Release] Mi Box HDR Fix — open-source app that fixes HDMI HDR handshake failures at boot (Mi Box S → sync box / switch / receiver)

**TL;DR:** If your Mi Box S shows wrong colors after booting through an HDMI sync box, switch, or receiver, this tiny open-source app toggles Color Depth automatically at boot/wake to force the TV to re-negotiate HDR. No root, no PC at boot, no data collection.

**GitHub:** https://github.com/fedebyes/mi-box-hdr-fix (MIT, free)

## The problem

TV box → HDMI sync box (Govee/Hue/FancyLEDs) or switch/receiver → TV chains often fail the HDMI handshake at boot: HDR negotiation breaks and the picture comes up with the wrong color matrix. The manual fix is toggling Color Depth off→on once — annoying on every boot.

## The app

- Runs the verified tap sequence automatically **a few seconds after boot** and **on wake from standby**
- **Guards** itself: runs at most once per boot/wake window, aborts if any step fails
- No root, no PC at boot — just Android's accessibility service (enable once)
- **Privacy:** no Internet permission, no data collected, doesn't read screen content or keystrokes
- Framework-only (~200 KB), no bloat, open source (MIT)

## Install

```bash
# one-time, from a PC on the same network
adb connect <BOX_IP>:5555
# grab the APK from GitHub Releases, then:
adb install -r app-release.apk
# enable the accessibility service (Settings → Accessibility → Mi Box HDR Fix)
```

Or follow the README's `scripts/install.sh` which enables the service automatically.

## Compatibility — please read

- **Tested on:** Mi Box S 1st gen (Android 9, DroidLogic firmware) — verified working
- **2nd gen / 3rd gen (Google TV):** the tap coordinates target the DroidLogic settings UI, so **not yet supported out of the box**. The app is open source — if you own a 2nd/3rd gen and want this, open an issue with your firmware version and I'll look at adapting the coordinates (or submit a PR).

## Why open source

This is a niche problem (sync box chains + Android TV), so instead of a paid app with limited reach, it's free and MIT — anyone can adjust the coordinates for their own box/firmware.

Questions, feedback, coordinate reports for other boxes — all welcome in the issues.
