# Privacy Policy

**Deep Color Toggle** — last updated: 2026-08-15

## Data collection

Deep Color Toggle **does not collect, store, transmit, or share any personal data**.

- The app has **no Internet permission**. It cannot connect to any network.
- The app does not use analytics, crash reporting, advertising, or tracking SDKs.
- The app does not read your contacts, location, files, or media.
- The app does not create any account and does not require login.

## Local-only storage

The app stores a single pair of timestamps in Android's private app storage
(SharedPreferences): the last time the color-depth toggle sequence ran. These
values exist solely to avoid re-running the toggle more than once per boot/wake
cycle. They never leave the device and are deleted when the app is uninstalled.

## Accessibility service

The app includes an accessibility service that is **off by default** and only
works after the user explicitly enables it in Android's Accessibility settings.

When enabled, the service briefly opens the system display-settings screen and
emits touch gestures to toggle the *Color Depth Settings* option. It does this
automatically after device boot and when the screen turns on, to fix HDMI
color-depth handshake issues on Android TV boxes. The service:

- does **not** read screen content (window content retrieval is disabled);
- does **not** intercept or log keystrokes;
- does **not** capture or transmit anything.

## Contact

For questions about this policy, open an issue in the GitHub repository:
https://github.com/fedebyes/mibox-deep-color-toggle
