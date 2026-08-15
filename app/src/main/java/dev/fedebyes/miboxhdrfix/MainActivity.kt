package dev.fedebyes.miboxhdrfix

import android.app.Activity
import android.graphics.Color
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
                setStatus(getString(R.string.status_accessibility_off), false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (DeepColorAccessibilityService.instance != null) {
            setStatus(getString(R.string.status_ok), true)
        } else {
            setStatus(getString(R.string.status_accessibility_off), false)
        }
    }

    private fun setStatus(text: String, ok: Boolean) {
        val statusText = findViewById<TextView>(R.id.text_status)
        statusText.text = text
        statusText.setTextColor(if (ok) Color.parseColor("#66BB6A") else Color.parseColor("#FFB74D"))
    }
}
