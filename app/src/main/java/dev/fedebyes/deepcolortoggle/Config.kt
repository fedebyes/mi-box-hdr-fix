package dev.fedebyes.deepcolortoggle

/**
 * Central configuration for the deep-color toggle automation.
 *
 * Screen coordinates are in the Mi Box's native display-settings coordinate
 * space (1080p UI): row positions where the display-settings dialog shows
 * the resolution selector, the color-depth row, and the two color-depth
 * dialog options (OFF / ON).
 *
 * NOTE: coordinates assume a 1080p-rendered UI. If the box ever renders the
 * settings UI at a different resolution (e.g. 720p scale), every tap misses —
 * re-verify with `uiautomator dump` and update these constants.
 */
object Config {
    /** Package of the system TV settings app that hosts the display settings. */
    const val DISPLAY_ACTIVITY_PKG = "com.android.tv.settings"

    /** Full component name of the Droidlogic display settings activity. */
    const val DISPLAY_ACTIVITY_CLS = "com.droidlogic.tv.settings.display.DisplayActivity"

    /** Tap coordinates (x, y) for the screen-resolution row. */
    val SCREEN_RESOLUTION_ROW = 1350 to 250

    /** Tap coordinates (x, y) for the color-depth row. */
    val COLOR_DEPTH_ROW = 1350 to 530

    /** Tap coordinates (x, y) for the "OFF" option in the color-depth dialog. */
    val DIALOG_OFF = 1350 to 306

    /** Tap coordinates (x, y) for the "ON" option in the color-depth dialog. */
    val DIALOG_ON = 1350 to 220

    /** Extra delay before the FIRST step, letting the settings activity render. */
    const val FIRST_STEP_DELAY_MS = 4000L

    /** Pause between consecutive taps/keys, in milliseconds. */
    const val STEP_DELAY_MS = 1800L

    /** Delay after boot before the toggle sequence starts, in milliseconds. */
    const val BOOT_DELAY_MS = 20000L

    /**
     * Wall-clock gap above which the guard treats the trigger as a new boot
     * (or a real wake from standby) rather than a same-boot retrigger.
     */
    const val WALL_GAP_MS = 600_000L // 10 minutes

    /** SharedPreferences file holding the toggle bookkeeping. */
    const val PREF_NAME = "deepcolor_toggle"

    /** SharedPreferences key storing the elapsedRealtime() of the last toggle. */
    const val PREF_LAST_TOGGLE_ELAPSED = "last_toggle_elapsed_ms"

    /** SharedPreferences key storing the wall-clock (currentTimeMillis) of the last toggle. */
    const val PREF_LAST_TOGGLE_WALL = "last_toggle_wall_ms"
}
