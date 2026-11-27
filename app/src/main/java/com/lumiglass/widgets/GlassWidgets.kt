package com.lumiglass.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private fun backgroundFor(context: Context): Int {
    val prefs = context.getSharedPreferences("lumi_settings", Context.MODE_PRIVATE)
    val tint = prefs.getInt("tint", 0).coerceIn(0, 2)
    val opacity = prefs.getInt("opacityLevel", 1).coerceIn(0, 2)
    val ids = arrayOf(
        intArrayOf(R.drawable.glass_midnight_light, R.drawable.glass_midnight, R.drawable.glass_midnight_deep),
        intArrayOf(R.drawable.glass_arctic_light, R.drawable.glass_arctic, R.drawable.glass_arctic_deep),
        intArrayOf(R.drawable.glass_violet_light, R.drawable.glass_violet, R.drawable.glass_violet_deep)
    )
    return ids[tint][opacity]
}

private fun configureRoot(context: Context, views: RemoteViews) {
    views.setInt(R.id.glass_root, "setBackgroundResource", backgroundFor(context))
    val openApp = PendingIntent.getActivity(
        context, 0, Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    views.setOnClickPendingIntent(R.id.glass_root, openApp)
}

class ClockWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(context: Context, manager: AppWidgetManager, ids: IntArray) {
        ids.forEach { id ->
            val views = RemoteViews(context.packageName, R.layout.widget_clock)
            configureRoot(context, views)
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
            configureRoot(context, views)
            manager.updateAppWidget(id, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_DATE_CHANGED || intent.action == Intent.ACTION_TIME_CHANGED ||
            intent.action == Intent.ACTION_TIMEZONE_CHANGED) {
            val manager = AppWidgetManager.getInstance(context)
            onUpdate(context, manager, manager.getAppWidgetIds(ComponentName(context, DateWidgetProvider::class.java)))
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
            configureRoot(context, views)
            manager.updateAppWidget(id, views)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == Intent.ACTION_POWER_CONNECTED || intent.action == Intent.ACTION_POWER_DISCONNECTED ||
            intent.action == Intent.ACTION_BATTERY_LOW || intent.action == Intent.ACTION_BATTERY_OKAY) {
            val manager = AppWidgetManager.getInstance(context)
            onUpdate(context, manager, manager.getAppWidgetIds(ComponentName(context, BatteryWidgetProvider::class.java)))
        }
    }
}
