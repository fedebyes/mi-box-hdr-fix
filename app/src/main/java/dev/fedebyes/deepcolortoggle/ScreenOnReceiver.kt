package dev.fedebyes.deepcolortoggle

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Dynamic receiver registered by [DeepColorAccessibilityService]; notifies the
 * service when the screen turns on. The toggle is guarded by BootGuard inside
 * [ToggleCoordinator], so a screen-on during the same boot does not re-trigger
 * the sequence.
 */
class ScreenOnReceiver(private val onScreenOn: () -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_ON) {
            onScreenOn()
        }
    }
}
