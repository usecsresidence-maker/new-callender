# 3 Calendar — Android MVP

A bilingual English/Urdu Android calendar showing:
- Gregorian date
- Islamic/Hijri date
- Punjabi/Bikrami date
- Pakistan public holidays
- Light/dark mode
- English/Urdu toggle
- Offline operation

## Open in Android Studio

1. Install Android Studio.
2. Choose **Open** and select this `3Calendar` folder.
3. Let Gradle sync.
4. Run on an Android emulator or connected Android phone.
5. Build > Build APK(s) to create an APK.

## Important calendar note

The Hijri conversion currently uses a tabular Islamic civil-calendar algorithm. Pakistan's moon-sighting dates can differ, so the production version should add a user-adjustable Hijri offset and a maintained Pakistan holiday dataset.

The Punjabi/Bikrami conversion in this MVP is a practical modern presentation layer. For a production release, it should be replaced with the exact Punjabi/Bikrami convention you want to publish (e.g. a specified regional/astronomical standard).

## Production roadmap

- Proper Punjabi/Bikrami astronomical conversion
- Annual holiday database with official Cabinet Division updates
- Province-specific holidays (Punjab, Sindh, KP, Balochistan, GB, AJK)
- Urdu calendar typography and RTL layout
- Date converter
- Reminders/notifications
- Widget
- Search and holiday list
- Play Store release assets


## Developer photo
The uploaded developer photo is included in the app as a small profile image in the main header and in the Developer card. Replace `app/src/main/res/drawable-nodpi/developer_photo.png` if you want to use a different image later.


## Build APK with Codemagic (phone-friendly)

1. Upload this project to a GitHub repository. Keep the repository private if preferred.
2. In Codemagic, connect the GitHub repository and select the native Android project.
3. Codemagic will detect `codemagic.yaml` in the repository root.
4. Start the `android-debug-apk` workflow.
5. After a successful build, download `app-debug.apk` from the build's Artifacts section.

The workflow builds a debug APK. It is suitable for installing/testing on an Android phone. A signed release workflow can be added later when preparing for Google Play.
