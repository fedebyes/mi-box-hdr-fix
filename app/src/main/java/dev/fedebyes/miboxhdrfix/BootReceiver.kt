package dev.fedebyes.miboxhdrfix

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Belt-and-suspenders for BOOT_COMPLETED: asks the system to bind the
 * accessibility service (the system usually auto-binds it anyway once the
 * user has enabled it in accessibility settings).
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }
        val ctx = context ?: return
        try {
            ctx.startService(Intent(ctx, DeepColorAccessibilityService::class.java))
        } catch (e: SecurityException) {
            // Service start refused by the system — ignore; the system
            // auto-binds the accessibility service at boot regardless.
        } catch (e: IllegalStateException) {
            // App in a background/stopped state — ignore.
        }
    }
}
