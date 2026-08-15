package dev.fedebyes.miboxhdrfix

import dev.fedebyes.miboxhdrfix.Config.COLOR_DEPTH_ROW
import dev.fedebyes.miboxhdrfix.Config.DIALOG_OFF
import dev.fedebyes.miboxhdrfix.Config.DIALOG_ON
import dev.fedebyes.miboxhdrfix.Config.SCREEN_RESOLUTION_ROW

/**
 * Pure Kotlin definition of the UI automation steps. No Android imports —
 * fully unit-testable on the JVM.
 */
sealed class Step {
    /** Tap at screen coordinates (x, y). */
    data class Tap(val x: Int, val y: Int) : Step()

    /** Press the BACK key. */
    object Back : Step()

    /** Press the HOME key. */
    object Home : Step()
}

/** Which way the toggle goes: OFF → ON (enable deep color) or ON → OFF (disable). */
enum class Direction {
    OFF_TO_ON,
    ON_TO_OFF,
}

/**
 * Builds the tap/key sequence that walks the display-settings dialog:
 * open resolution row → color-depth row → pick a dialog option → back out
 * → re-enter color-depth → pick the other option → back → home.
 *
 * OFF_TO_ON picks OFF first, then ON; ON_TO_OFF is its mirror (ON first,
 * then OFF).
 */
fun buildToggleSequence(direction: Direction): List<Step> {
    val (firstOption, secondOption) = when (direction) {
        Direction.OFF_TO_ON -> DIALOG_OFF to DIALOG_ON
        Direction.ON_TO_OFF -> DIALOG_ON to DIALOG_OFF
    }
    return listOf(
        Step.Tap(SCREEN_RESOLUTION_ROW.first, SCREEN_RESOLUTION_ROW.second),
        Step.Tap(COLOR_DEPTH_ROW.first, COLOR_DEPTH_ROW.second),
        Step.Tap(firstOption.first, firstOption.second),
        Step.Back,
        Step.Tap(COLOR_DEPTH_ROW.first, COLOR_DEPTH_ROW.second),
        Step.Tap(secondOption.first, secondOption.second),
        Step.Back,
        Step.Home,
    )
}
