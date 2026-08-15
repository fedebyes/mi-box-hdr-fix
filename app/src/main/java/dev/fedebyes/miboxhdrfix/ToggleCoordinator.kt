package dev.fedebyes.miboxhdrfix

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Executes the deep-color toggle sequence against the display settings UI.
 *
 * Flow: in-flight guard -> boot guard check -> launch DisplayActivity ->
 * tap/BACK/HOME steps with FIRST_STEP_DELAY_MS then STEP_DELAY_MS between ->
 * record the toggle clocks ONLY on full-sequence success.
 *
 * A single instance must be shared per service (the in-flight [toggling]
 * guard is per-coordinator); the service owns one and calls [cancel] on
 * destroy.
 */
class ToggleCoordinator(
    private val service: AccessibilityService,
    private val context: Context
) {
    private val handler = Handler(Looper.getMainLooper())
    private val toggling = AtomicBoolean(false)
    private val sequenceCallbacks = mutableListOf<Runnable>()

    fun runToggle() {
        // H2: in-flight guard — never run two overlapping sequences.
        if (!toggling.compareAndSet(false, true)) return

        val prefs = context.getSharedPreferences(Config.PREF_NAME, Context.MODE_PRIVATE)
        val nowElapsed = SystemClock.elapsedRealtime()
        val nowWall = System.currentTimeMillis()
        val last = readLastToggle(prefs)

        if (!BootGuard({ SystemClock.elapsedRealtime() }, { System.currentTimeMillis() })
                .shouldToggle(nowElapsed, nowWall, last)) {
            toggling.set(false)
            return
        }

        // Bring up the display settings UI. Activity may not exist on other
        // firmware builds, so a failed launch aborts the whole toggle.
        try {
            val intent = Intent().setComponent(
                ComponentName(Config.DISPLAY_ACTIVITY_PKG, Config.DISPLAY_ACTIVITY_CLS)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            toggling.set(false)
            return
        }

        // Schedule the sequence; the first step waits longer for the activity
        // to render, subsequent steps STEP_DELAY_MS apart.
        val steps = buildToggleSequence(Direction.OFF_TO_ON)
        var offset = Config.FIRST_STEP_DELAY_MS
        steps.forEachIndexed { index, step ->
            val runnable = Runnable {
                val ok = executeStep(step)
                if (!ok) {
                    // H1: abort on failure — no success record, so a later
                    // trigger (SCREEN_ON / manual) may retry this boot.
                    abort()
                    return@Runnable
                }
                if (index == steps.lastIndex) {
                    prefs.edit()
                        .putLong(Config.PREF_LAST_TOGGLE_ELAPSED, SystemClock.elapsedRealtime())
                        .putLong(Config.PREF_LAST_TOGGLE_WALL, System.currentTimeMillis())
                        .apply()
                    toggling.set(false)
                }
            }
            handler.postDelayed(runnable, offset)
            sequenceCallbacks.add(runnable)
            offset += Config.STEP_DELAY_MS
        }
    }

    /** Cancel any pending sequence steps (service teardown). */
    fun cancel() {
        abort()
    }

    private fun abort() {
        sequenceCallbacks.forEach { handler.removeCallbacks(it) }
        sequenceCallbacks.clear()
        toggling.set(false)
    }

    private fun readLastToggle(prefs: android.content.SharedPreferences): BootGuard.LastToggle? =
        if (prefs.contains(Config.PREF_LAST_TOGGLE_ELAPSED) && prefs.contains(Config.PREF_LAST_TOGGLE_WALL)) {
            BootGuard.LastToggle(
                elapsedMs = prefs.getLong(Config.PREF_LAST_TOGGLE_ELAPSED, 0L),
                wallMs = prefs.getLong(Config.PREF_LAST_TOGGLE_WALL, 0L)
            )
        } else {
            null
        }

    /** @return true if the action was accepted by the system. */
    private fun executeStep(step: Step): Boolean = when (step) {
        is Step.Tap -> {
            val path = Path().apply { moveTo(step.x.toFloat(), step.y.toFloat()) }
            val stroke = StrokeDescription(path, 0, 100L)
            val gesture = GestureDescription.Builder().addStroke(stroke).build()
            service.dispatchGesture(gesture, null, null)
        }
        Step.Back -> service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
        Step.Home -> service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
    }
}
