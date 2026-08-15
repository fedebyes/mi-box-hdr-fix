package dev.fedebyes.miboxhdrfix

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent

/**
 * Accessibility service that triggers the deep-color toggle at boot and on
 * every screen-on. One [ToggleCoordinator] is shared by all triggers so the
 * in-flight guard prevents overlapping sequences; the BootGuard inside the
 * coordinator ensures the sequence runs at most once per boot/wake window.
 */
class DeepColorAccessibilityService : AccessibilityService() {

    private val handler = Handler(Looper.getMainLooper())
    private var coordinator: ToggleCoordinator? = null
    private val screenOnReceiver = ScreenOnReceiver {
        coordinator?.runToggle()
    }
    private var bootToggleRunnable: Runnable? = null
    private var screenOnRegistered = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this

        // N2: a rebind without an intervening onDestroy must not re-register.
        if (screenOnRegistered) return

        coordinator = ToggleCoordinator(this, this)

        // React to every screen-on; plain registerReceiver is fine on API 28
        // (and ACTION_SCREEN_ON is a protected system broadcast, exempt from
        // the RECEIVER_EXPORTED/RECEIVER_NOT_EXPORTED requirement).
        registerReceiver(screenOnReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
        screenOnRegistered = true

        // Boot-delayed toggle: let the TV/LED chain settle (~20s) before
        // driving the sequence. The coordinator's guard prevents re-triggering.
        bootToggleRunnable = Runnable {
            coordinator?.runToggle()
        }
        handler.postDelayed(bootToggleRunnable!!, Config.BOOT_DELAY_MS)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Gestures do not depend on window events; nothing to do here.
    }

    override fun onInterrupt() {
        // No-op.
    }

    override fun onDestroy() {
        bootToggleRunnable?.let { handler.removeCallbacks(it) }
        bootToggleRunnable = null
        coordinator?.cancel() // N3: also cancels in-flight sequence steps
        coordinator = null
        if (screenOnRegistered) {
            unregisterReceiver(screenOnReceiver)
            screenOnRegistered = false
        }
        instance = null
        super.onDestroy()
    }

    /** Manual trigger, invoked by the MainActivity "Toggle now" button. */
    fun toggleNow() {
        coordinator?.runToggle()
    }

    companion object {
        @Volatile
        var instance: DeepColorAccessibilityService? = null
    }
}
