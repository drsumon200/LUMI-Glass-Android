package com.lumiglass.widgets

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.*

class MainActivity : Activity() {
    private val prefs by lazy { getSharedPreferences("lumi_settings", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (20 * resources.displayMetrics.density).toInt()
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(Color.rgb(12, 17, 31))
        }
        content.addView(TextView(this).apply {
            text = "LUMI Glass"
            textSize = 30f
            setTextColor(Color.WHITE)
            typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
        })
        content.addView(TextView(this).apply {
            text = "LIQUID GLASS WIDGET STUDIO  •  MAGICOS 10"
            textSize = 11f
            setTextColor(Color.LTGRAY)
            setPadding(0, 6, 0, 24)
        })
        content.addView(TextView(this).apply {
            text = "  09:41\n  FRIDAY, 27 NOVEMBER"
            textSize = 23f
            setTextColor(Color.WHITE)
            setPadding(24, 28, 24, 28)
            background = getDrawable(R.drawable.glass_card)
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = 24 })

        content.addView(TextView(this).apply {
            text = "Glass opacity"
            textSize = 16f
            setTextColor(Color.WHITE)
        })
        val opacity = SeekBar(this).apply {
            max = 85
            progress = prefs.getInt("opacity", 42)
        }
        content.addView(opacity)
        val value = TextView(this).apply {
            text = "${opacity.progress}%"
            setTextColor(Color.LTGRAY)
        }
        content.addView(value)
        opacity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                value.text = "$progress%"
                prefs.edit().putInt("opacity", progress).apply()
            }
            override fun onStartTrackingTouch(bar: SeekBar?) {}
            override fun onStopTrackingTouch(bar: SeekBar?) { WidgetUpdater.updateAll(this@MainActivity) }
        })

        content.addView(TextView(this).apply {
            text = "Glass tint"
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(0, 22, 0, 8)
        })
        val names = listOf("Midnight", "Arctic", "Violet")
        val spinner = Spinner(this).apply {
            adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, names)
            setSelection(prefs.getInt("tint", 0))
        }
        content.addView(spinner)
        spinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                prefs.edit().putInt("tint", position).apply()
                WidgetUpdater.updateAll(this@MainActivity)
            }
        }
        content.addView(TextView(this).apply {
            text = "\nAdd widgets\nLong-press an empty Home Screen area → Widgets → LUMI Glass. Drag Clock, Date, or Battery onto your screen.\n\nNote: Android launchers limit true wallpaper blur for widgets. LUMI uses translucent glass styling."
            textSize = 14f
            setTextColor(Color.LTGRAY)
            setPadding(0, 24, 0, 12)
        })
        content.addView(Button(this).apply {
            text = "Apply settings"
            setOnClickListener {
                WidgetUpdater.updateAll(this@MainActivity)
                Toast.makeText(this@MainActivity, "Glass settings applied", Toast.LENGTH_SHORT).show()
            }
        })
        setContentView(ScrollView(this).apply { addView(content) })
    }
}
