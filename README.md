# LUMI Glass

Liquid Glass-inspired Android Home Screen widgets for Honor 200 / MagicOS 10.

## Features
- Live digital clock (Android TextClock)
- Date widget
- Battery percentage widget
- Glass opacity and tint settings
- Resizable Home Screen widgets

## Build
1. Open this repository in Android Studio.
2. Use JDK 17 and Android SDK Platform 35.
3. Allow Gradle sync.
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Install the APK on your Honor device.

## Add widgets
Open the Home Screen widget picker, find **LUMI Glass**, then drag Clock, Date, or Battery to the Home Screen.

## Transparency limitation
Android RemoteViews widgets cannot reliably blur/sample the live wallpaper behind them across all launchers. LUMI uses translucent gradients, highlights, rounded corners, and tint controls to approximate Liquid Glass. True wallpaper-aware live blur is not claimed.

## Current scope
Weather, music controls, independent per-widget themes, and true backdrop blur are not included in this initial release.
