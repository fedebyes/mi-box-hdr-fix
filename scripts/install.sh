#!/usr/bin/env bash
set -euo pipefail

# Install the deep color toggle APK on the Mi Box S and enable the
# accessibility service that fires the toggle at boot/wake.
# Override the box address with: BOX_IP=192.168.1.137:5555 ./scripts/install.sh
BOX_IP="${BOX_IP:-192.168.1.137:5555}"
SERVICE="dev.fedebyes.deepcolortoggle/.DeepColorAccessibilityService"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(dirname "$SCRIPT_DIR")"
APK="$ROOT/app/build/outputs/apk/debug/app-debug.apk"

if [ ! -f "$APK" ]; then
    echo "APK not found: $APK" >&2
    echo "Build it first: gradle :app:assembleDebug" >&2
    exit 1
fi

# Connect over WiFi (ignore failure if already connected)
adb connect "$BOX_IP" || true
timeout 30 adb -s "$BOX_IP" wait-for-device || {
    echo "ERROR: box not reachable at $BOX_IP" >&2
    exit 1
}

adb install -r "$APK"

# N1: append our service to the enabled list instead of overwriting others
CURRENT="$(adb -s "$BOX_IP" shell settings get secure enabled_accessibility_services | tr -d '\r')"
if [ -n "$CURRENT" ] && [ "$CURRENT" != "null" ] && [[ "$CURRENT" != *"$SERVICE"* ]]; then
    adb -s "$BOX_IP" shell settings put secure enabled_accessibility_services "${CURRENT}:${SERVICE}"
else
    adb -s "$BOX_IP" shell settings put secure enabled_accessibility_services "$SERVICE"
fi
adb -s "$BOX_IP" shell settings put secure accessibility_enabled 1

echo "Deep color toggle installed + accessibility service enabled on $BOX_IP"

# N6: sanity check that the service is in the accessibility list
echo "--- accessibility services now ---"
adb -s "$BOX_IP" shell settings get secure enabled_accessibility_services
echo "--- note: reboot the box to confirm the service binds at boot ---"
