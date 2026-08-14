package dev.fedebyes.deepcolortoggle

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BootGuardTest {

    private fun guard(wallGapMs: Long = Config.WALL_GAP_MS) =
        BootGuard({ 0L }, { 0L }, wallGapMs)

    private fun last(elapsedMs: Long, wallMs: Long) =
        BootGuard.LastToggle(elapsedMs = elapsedMs, wallMs = wallMs)

    @Test
    fun `null last toggle means never toggled - should toggle`() {
        assertTrue(guard().shouldToggle(currentElapsedMs = 5_000L, currentWallMs = 1_000_000L, last = null))
    }

    @Test
    fun `current before last means reboot happened - should toggle`() {
        // elapsedRealtime() reset on reboot: last toggle was recorded at 40s
        // on the previous boot, now we're only at 5s into this boot.
        assertTrue(
            guard().shouldToggle(
                currentElapsedMs = 5_000L, currentWallMs = 2_000_000L,
                last = last(elapsedMs = 40_000L, wallMs = 1_000_000L)
            )
        )
    }

    @Test
    fun `same boot recent toggle - should skip`() {
        // elapsed advanced (60s > 40s) and wall gap is small (30s) -> same boot.
        assertFalse(
            guard().shouldToggle(
                currentElapsedMs = 60_000L, currentWallMs = 1_000_030L,
                last = last(elapsedMs = 40_000L, wallMs = 1_000_000L)
            )
        )
    }

    @Test
    fun `equal elapsed small wall gap means same boot - should skip`() {
        assertFalse(
            guard().shouldToggle(
                currentElapsedMs = 40_000L, currentWallMs = 1_000_010L,
                last = last(elapsedMs = 40_000L, wallMs = 1_000_000L)
            )
        )
    }

    @Test
    fun `large wall gap means new boot or wake - should toggle`() {
        // Slow bind after reboot: elapsed already past last, but the wall clock
        // gap (> WALL_GAP_MS) proves a new boot / real wake happened.
        assertTrue(
            guard().shouldToggle(
                currentElapsedMs = 60_000L, currentWallMs = 1_700_000L,
                last = last(elapsedMs = 40_000L, wallMs = 1_000_000L)
            )
        )
    }

    @Test
    fun `convenience overload reads clocks from injected lambdas`() {
        val elapsedClock = { 123_456L }
        val wallClock = { 5_000_000L }
        val g = BootGuard(elapsedClock, wallClock)
        assertTrue(g.shouldToggle(last = null))
        // elapsed 123s >= 40s, wall gap 5s < threshold -> same boot -> skip
        assertFalse(g.shouldToggle(last = last(elapsedMs = 40_000L, wallMs = 4_995_000L)))
        // wall gap 4_000_000ms > 600_000ms -> new boot -> toggle
        assertTrue(g.shouldToggle(last = last(elapsedMs = 40_000L, wallMs = 1_000_000L)))
    }
}
