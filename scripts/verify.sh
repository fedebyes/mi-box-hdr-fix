#!/usr/bin/env bash
set -euo pipefail

# Verify display mode + accessibility state on the Mi Box S.
BOX_IP="192.168.1.137:5555"

# Connect over WiFi (ignore failure if already connected)
adb connect "$BOX_IP" || true
timeout 30 adb -s "$BOX_IP" wait-for-device || {
    echo "ERROR: box not reachable at $BOX_IP" >&2
    exit 1
}

echo "--- display mode before ---"
adb -s "$BOX_IP" shell dumpsys display | grep mActiveModeId || true

echo "--- accessibility services ---"
adb -s "$BOX_IP" shell settings get secure enabled_accessibility_services

echo "--- hint ---"
echo "After rebooting the box, mActiveModeId should show 65 (toggle fired) and colors correct."
