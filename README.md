# LUMI Glass

Liquid Glass-inspired native Android Home Screen widgets for Honor 200 / MagicOS 10.

## Version 1.1
- Live digital clock with Android TextClock and automatic 12/24-hour format.
- Date widget with date/time/time-zone refresh handling.
- Battery percentage, charging state and level bar.
- Nine tint/opacity combinations: Midnight, Arctic and Violet, each in Light, Balanced and Deep.
- Widget tap opens the LUMI settings studio.
- Resizable Home Screen widgets.

## Build
1. Open this repository in Android Studio.
2. Use JDK 17 and Android SDK Platform 35.
3. Allow Gradle sync.
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Install the APK on your Honor device.

## Add widgets
Long-press an empty Home Screen area, open Widgets, find **LUMI Glass**, then drag Clock, Date or Battery onto the Home Screen.

## Transparency note
Android RemoteViews widgets cannot reliably blur/sample the live wallpaper behind them across all launchers. LUMI uses translucent gradients, highlight strokes, rounded corners and tint/opacity presets to evoke Liquid Glass. True wallpaper-aware live blur is not claimed.

## Current scope
Weather, music controls, independent per-widget themes and true backdrop blur are not included in this release.
