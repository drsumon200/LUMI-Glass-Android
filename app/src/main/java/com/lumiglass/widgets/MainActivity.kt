package com.lumiglass.widgets

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.*

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("lumi_settings", MODE_PRIVATE) }
    private val tints = listOf("Midnight", "Arctic", "Violet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (20 * resources.displayMetrics.density).toInt()
        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.rgb(8, 13, 27))
        }
        page.addView(TextView(this).apply {
            text = "LUMI"
            textSize = 34f
            letterSpacing = .16f
            setTextColor(Color.WHITE)
            typeface = android.graphics.Typeface.create("sans-serif-light", android.graphics.Typeface.NORMAL)
        })
        page.addView(TextView(this).apply {
            text = "GLASS WIDGET STUDIO  /  MAGICOS 10"
            textSize = 10f
            letterSpacing = .12f
            setTextColor(Color.rgb(166, 190, 224))
            setPadding(0, 2, 0, dp(24))
        })

        val preview = TextView(this).apply {
            text = "09:41\nFRIDAY  ·  27 NOVEMBER"
            textSize = 25f
            setTextColor(Color.WHITE)
            setPadding(dp(22), dp(26), dp(22), dp(26))
            setLineSpacing(dp(4).toFloat(), 1f)
        }
        page.addView(preview, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(26) })

        section(page, "APPEARANCE")
        label(page, "Glass tint")
        val tintPicker = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, tints)
            setSelection(prefs.getInt("tint", 0).coerceIn(0, 2))
        }
        page.addView(tintPicker)
        label(page, "Glass opacity")
        val opacity = SeekBar(this).apply {
            max = 2
            progress = prefs.getInt("opacityLevel", 1).coerceIn(0, 2)
        }
        page.addView(opacity)
        val opacityText = label(page, opacityName(opacity.progress))
        fun refreshPreview() {
            preview.background = getDrawable(backgroundFor(tintPicker.selectedItemPosition, opacity.progress))
            opacityText.text = opacityName(opacity.progress)
        }
        tintPicker.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                prefs.edit().putInt("tint", position).apply()
                refreshPreview()
                WidgetUpdater.updateAll(this@MainActivity)
            }
        }
        opacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                prefs.edit().putInt("opacityLevel", progress).apply()
                refreshPreview()
            }
            override fun onStartTrackingTouch(bar: SeekBar?) {}
            override fun onStopTrackingTouch(bar: SeekBar?) { WidgetUpdater.updateAll(this@MainActivity) }
        })
        refreshPreview()

        section(page, "YOUR WIDGETS")
        infoCard(page, "◷   CLOCK", "Live digital time with automatic 12/24-hour format.")
        infoCard(page, "▦   DATE", "Day, date and month in a compact glass tile.")
        infoCard(page, "▰   BATTERY", "Battery percentage, charging state and level bar.")

        section(page, "ADD TO HOME SCREEN")
        page.addView(TextView(this).apply {
            text = "1. Long-press an empty area on your Home Screen.\n2. Tap Widgets and find LUMI Glass.\n3. Drag Clock, Date or Battery onto your screen.\n\nFor best results, use a dark or colorful wallpaper so the translucent glass surface has contrast."
            textSize = 13f
            setTextColor(Color.rgb(194, 207, 229))
            setLineSpacing(dp(5).toFloat(), 1f)
        })
        page.addView(Button(this).apply {
            text = "APPLY GLASS SETTINGS"
            setOnClickListener {
                WidgetUpdater.updateAll(this@MainActivity)
                Toast.makeText(this@MainActivity, "LUMI appearance updated", Toast.LENGTH_SHORT).show()
            }
        }, LinearLayout.LayoutParams(-1, dp(52)).apply { topMargin = dp(24) })

        page.addView(TextView(this).apply {
            text = "LUMI GLASS  •  VERSION 1.1\nLiquid-glass inspired styling. True live wallpaper blur depends on launcher support."
            textSize = 10f
            setTextColor(Color.rgb(125, 146, 178))
            gravity = android.view.Gravity.CENTER
            setPadding(0, dp(22), 0, dp(10))
        })
        setContentView(ScrollView(this).apply { addView(page) })
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
    private fun opacityName(level: Int) = when (level) { 0 -> "Light"; 2 -> "Deep"; else -> "Balanced" }
    private fun backgroundFor(tint: Int, opacity: Int): Int {
        val ids = arrayOf(
            intArrayOf(R.drawable.glass_midnight_light, R.drawable.glass_midnight, R.drawable.glass_midnight_deep),
            intArrayOf(R.drawable.glass_arctic_light, R.drawable.glass_arctic, R.drawable.glass_arctic_deep),
            intArrayOf(R.drawable.glass_violet_light, R.drawable.glass_violet, R.drawable.glass_violet_deep)
        )
        return ids[tint.coerceIn(0, 2)][opacity.coerceIn(0, 2)]
    }
    private fun section(parent: LinearLayout, text: String) {
        parent.addView(TextView(this).apply {
            this.text = text
            textSize = 10f
            letterSpacing = .15f
            setTextColor(Color.rgb(139, 178, 229))
            setPadding(0, dp(18), 0, dp(8))
        })
    }
    private fun label(parent: LinearLayout, text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setTextColor(Color.WHITE)
            setPadding(0, dp(10), 0, dp(4))
            parent.addView(this)
        }
    }
    private fun infoCard(parent: LinearLayout, title: String, detail: String) {
        parent.addView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(13), dp(16), dp(13))
            background = getDrawable(R.drawable.glass_midnight_light)
            addView(TextView(this@MainActivity).apply {
                text = title
                textSize = 12f
                letterSpacing = .08f
                setTextColor(Color.WHITE)
            })
            addView(TextView(this@MainActivity).apply {
                text = detail
                textSize = 12f
                setTextColor(Color.rgb(190, 205, 228))
                setPadding(0, dp(5), 0, 0)
            })
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(9) })
    }
}
