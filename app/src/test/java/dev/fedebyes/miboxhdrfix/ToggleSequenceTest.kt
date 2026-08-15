package dev.fedebyes.miboxhdrfix

import dev.fedebyes.miboxhdrfix.Config.COLOR_DEPTH_ROW
import dev.fedebyes.miboxhdrfix.Config.DIALOG_OFF
import dev.fedebyes.miboxhdrfix.Config.DIALOG_ON
import dev.fedebyes.miboxhdrfix.Config.SCREEN_RESOLUTION_ROW
import dev.fedebyes.miboxhdrfix.Direction
import dev.fedebyes.miboxhdrfix.Step
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleSequenceTest {

    @Test
    fun `OFF_TO_ON has 8 steps`() {
        assertEquals(8, buildToggleSequence(Direction.OFF_TO_ON).size)
    }

    @Test
    fun `OFF_TO_ON first step taps the screen resolution row`() {
        val first = buildToggleSequence(Direction.OFF_TO_ON).first()
        assertEquals(Step.Tap(SCREEN_RESOLUTION_ROW.first, SCREEN_RESOLUTION_ROW.second), first)
        // Explicit coordinates per spec:
        assertEquals(Step.Tap(1350, 250), first)
    }

    @Test
    fun `OFF_TO_ON step 4 is Back`() {
        assertSame(Step.Back, buildToggleSequence(Direction.OFF_TO_ON)[3])
    }

    @Test
    fun `OFF_TO_ON step 8 is Home`() {
        assertSame(Step.Home, buildToggleSequence(Direction.OFF_TO_ON)[7])
    }

    @Test
    fun `OFF_TO_ON taps DIALOG_OFF before DIALOG_ON`() {
        val steps = buildToggleSequence(Direction.OFF_TO_ON)
        val offIndex = steps.indexOf(Step.Tap(DIALOG_OFF.first, DIALOG_OFF.second))
        val onIndex = steps.indexOf(Step.Tap(DIALOG_ON.first, DIALOG_ON.second))
        assertTrue("DIALOG_OFF should come before DIALOG_ON", offIndex in 0 until onIndex)
    }

    @Test
    fun `ON_TO_OFF has 8 steps`() {
        assertEquals(8, buildToggleSequence(Direction.ON_TO_OFF).size)
    }

    @Test
    fun `ON_TO_OFF taps DIALOG_ON before DIALOG_OFF`() {
        val steps = buildToggleSequence(Direction.ON_TO_OFF)
        val onIndex = steps.indexOf(Step.Tap(DIALOG_ON.first, DIALOG_ON.second))
        val offIndex = steps.indexOf(Step.Tap(DIALOG_OFF.first, DIALOG_OFF.second))
        assertTrue("DIALOG_ON should come before DIALOG_OFF", onIndex in 0 until offIndex)
    }

    @Test
    fun `both directions end with Home`() {
        for (direction in Direction.entries) {
            assertSame(
                "Direction $direction should end with Home",
                Step.Home,
                buildToggleSequence(direction).last(),
            )
        }
    }

    @Test
    fun `both directions mirror each other option order`() {
        val offToOn = buildToggleSequence(Direction.OFF_TO_ON)
        val onToOff = buildToggleSequence(Direction.ON_TO_OFF)
        // Slot 2: OFF_TO_ON picks DIALOG_OFF first, ON_TO_OFF picks DIALOG_ON first.
        assertEquals(
            Step.Tap(DIALOG_OFF.first, DIALOG_OFF.second),
            offToOn[2],
        )
        assertEquals(
            Step.Tap(DIALOG_ON.first, DIALOG_ON.second),
            onToOff[2],
        )
    }
}
