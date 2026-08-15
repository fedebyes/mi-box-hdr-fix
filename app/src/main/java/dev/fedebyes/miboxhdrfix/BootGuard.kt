package dev.fedebyes.miboxhdrfix

/**
 * Decides whether the deep-color toggle should run.
 *
 * Pure Kotlin, no Android imports. Clocks are injected as lambdas so tests
 * can control them; on Android they are backed by
 * [android.os.SystemClock.elapsedRealtime] (resets on reboot) and
 * [System.currentTimeMillis] (keeps advancing).
 *
 * Toggle condition (OR):
 * 1. Never toggled before (last == null).
 * 2. Reboot detected: current elapsed is *before* the last recorded elapsed
 *    (elapsedRealtime restarted from zero).
 * 3. Long wall-clock gap: now - lastWall > [wallGapMs] — covers the slow-bind
 *    case where elapsed has already climbed past `last` in the new boot, and
 *    also real wake-from-standby (per spec: toggle at boot AND wake).
 */
class BootGuard(
    private val clockElapsed: () -> Long,
    private val clockWall: () -> Long,
    private val wallGapMs: Long = Config.WALL_GAP_MS
) {

    /** Snapshot of the previous toggle's clocks, or null if it never ran. */
    data class LastToggle(val elapsedMs: Long, val wallMs: Long)

    /**
     * @param currentElapsedMs elapsedRealtime() now.
     * @param currentWallMs    System.currentTimeMillis() now.
     * @param last             the previous toggle's clocks, or null.
     */
    fun shouldToggle(currentElapsedMs: Long, currentWallMs: Long, last: LastToggle?): Boolean =
        last == null ||
            currentElapsedMs < last.elapsedMs ||
            currentWallMs - last.wallMs > wallGapMs

    /** Convenience overload that reads both clocks from the injected lambdas. */
    fun shouldToggle(last: LastToggle?): Boolean =
        shouldToggle(clockElapsed(), clockWall(), last)
}
