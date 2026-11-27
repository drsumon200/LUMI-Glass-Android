# LUMI Glass

Liquid Glass-inspired Android Home Screen widgets for Honor 200 / MagicOS 10.

## Version 1.2
- Android 16 / API 36 compile and target SDK.
- AGSL-powered Liquid Glass preview in the app's settings studio using QWEA0 LiquidGlass Android View library v2.0.11.
- SDF lens refraction, chromatic dispersion, adaptive tint and sensor-driven highlights in the in-app preview on supported Android versions.
- Live digital clock with Android TextClock and automatic 12/24-hour format.
- Date widget with date/time/time-zone refresh handling.
- Battery percentage, charging state and level bar.
- Nine tint/opacity combinations: Midnight, Arctic and Violet, each in Light, Balanced and Deep.
- Widget tap opens the LUMI settings studio.
- Resizable Home Screen widgets.

## Build
1. Open this repository in Android Studio.
2. Use JDK 17 and Android SDK Platform 36.
3. Allow Gradle sync. JitPack is used for the LiquidGlass library dependency.
4. Select Build > Build Bundle(s) / APK(s) > Build APK(s).
5. Install the APK on your Honor device.

## Add widgets
Long-press an empty Home Screen area, open Widgets, find **LUMI Glass**, then drag Clock, Date or Battery onto the Home Screen.

## Rendering architecture and transparency
The app's settings/preview screen uses the QWEA0 LiquidGlass View library. Its AGSL lens path is available on Android 13+ and Android 16 adds the library's additional AGSL effects.

The Home Screen widgets remain native Android AppWidget / RemoteViews. RemoteViews cannot host arbitrary custom Views, Compose UI, or AGSL runtime shader rendering, and Android does not provide a universal way for third-party widgets to sample and refract the launcher's live wallpaper. Therefore the widgets use translucent gradients, highlight strokes, rounded corners and tint/opacity presets. The in-app AGSL preview must not be confused with live wallpaper refraction on the Home Screen.

## References
- QWEA0 LiquidGlass Android: https://github.com/QWEA0/Liquid-Glass-Android
- Kyant0 AndroidLiquidGlass (Compose): https://github.com/Kyant0/AndroidLiquidGlass
- Chris Banes Haze: https://github.com/chrisbanes/haze
- Athulsn10 Glance Kit: https://github.com/Athulsn10/glance-kit

## Current scope
Weather, music controls, independent per-widget themes and true wallpaper-aware Home Screen backdrop refraction are not included in this release. The Compose libraries are not dependencies in this View-based app; they remain candidates if the settings studio is migrated to Compose later.
