package com.lumiglass.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun glassColor(context: Context): Int {
    val prefs = context.getSharedPreferences("lumi_settings", Context.MODE_PRIVATE)
    val opacity = (prefs.getInt("opacity", 42) * 2.55f).toInt().coerceIn(35, 217)
    val rgb = when (prefs.getInt("tint", 0)) {
        1 -> intArrayOf(80, 150, 190)
        2 -> intArrayOf(125, 90, 190)
        else -> intArrayOf(42, 54, 82)
    }
    return Color.argb(opacity, rgb[0], rgb[1], rgb[2])
}

private fun applyGlass(context: Context, views: RemoteViews) {
    views.setInt(R.id.glass_root, "setBackgroundColor", glassColor(context))
}

class ClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_clock)
            applyGlass(context, views)
            manager.updateAppWidget(id, views)
        }
    }
}

class DateWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_date)
            views.setTextViewText(R.id.date_day, SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()).uppercase(Locale.getDefault()))
            views.setTextViewText(R.id.date_number, SimpleDateFormat("d", Locale.getDefault()).format(Date()))
            views.setTextViewText(R.id.date_month, SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date()))
            applyGlass(context, views)
            manager.updateAppWidget(id, views)
        }
    }
}

class BatteryWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        val battery = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = battery?.getIntExtra("level", 0) ?: 0
        val plugged = battery?.getIntExtra("plugged", 0) ?: 0
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_battery)
            views.setTextViewText(R.id.battery_percent, "$level%")
            views.setProgressBar(R.id.battery_progress, 100, level, false)
            views.setTextViewText(R.id.battery_caption, if (plugged != 0) "CHARGING" else "BATTERY")
            applyGlass(context, views)
            manager.updateAppWidget(id, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED) {
            val manager = AppWidgetManager.getInstance(context)
            onUpdate(context, manager, manager.getAppWidgetIds(android.content.ComponentName(context, BatteryWidgetProvider::class.java)))
        }
    }
}
