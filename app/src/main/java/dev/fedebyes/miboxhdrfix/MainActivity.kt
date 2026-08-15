package dev.fedebyes.miboxhdrfix

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

/**
 * Leanback launcher activity: shows the accessibility-service status and a
 * "Toggle now" button for a manual deep-color toggle.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_toggle).setOnClickListener {
            val service = DeepColorAccessibilityService.instance
            if (service != null) {
                service.toggleNow()
            } else {
                findViewById<TextView>(R.id.text_status).text =
                    getString(R.string.status_accessibility_off)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val statusText = findViewById<TextView>(R.id.text_status)
        statusText.text = if (DeepColorAccessibilityService.instance != null) {
            getString(R.string.status_ok)
        } else {
            getString(R.string.status_accessibility_off)
        }
    }
}
