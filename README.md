# TechFix Mobile App - RepairHub

Android coursework project for the NIBM Higher National Diploma in Software Engineering, Mobile Application Development CW1.

## Coursework mapping

The CW1 brief requires a TechFix Android application covering branches, repair services/prices, spare parts/technicians, customer accounts, repair requests, nearest-branch assignment, payment, repair tracking and repair history. It also asks teams to demonstrate Android technologies such as GPS/maps, web services/remote data, complex models/adapters, camera, or SQLite/offline support. fileciteturn0file0L26-L45

## Implemented

- Customer registration and login
- Device categories and repair services
- Service prices and repair details
- Appointment/repair request creation
- GPS-based nearest branch selection
- Colombo and Galle branch data
- Appointment status and repair history
- Payment record handling for the demo
- Camera capture for a repair/device photo
- Room SQLite local database for offline-first data
- RecyclerView adapters and Room entities/DAOs
- Admin screen for management/demo
- Runtime location and camera permissions

## Free tools and APIs

The core coursework demo does not require a paid API key. GPS, camera and SQLite/Room are free Android/device capabilities. If remote data is added later, a free-tier service such as Firebase Spark can be used within its current quotas. Never commit API keys, passwords or google-services.json.

## Tech stack

- Kotlin
- Android Studio
- Android SDK 36
- AndroidX / Material
- Room Database (SQLite)
- Google Play Services Location
- RecyclerView
- GitHub Actions CI

## Run

1. Open this repository in Android Studio.
2. Use JDK 17.
3. Let Gradle sync.
4. Run the app on an Android 8+ device/emulator.
5. Allow location and camera permissions when requested.
6. Test registration/login, services, booking, tracking, history, payment and camera features.

## Repository

Suggested repository name: TechFix-MobileApp

GitHub: https://github.com/chanura-madushan/TechFix-MobileApp

## Suggested group split

1. UI/navigation + customer screens
2. Room database + data models/adapters
3. GPS + nearest branch + booking
4. Camera + payment + admin + testing/documentation

## Academic note

Your group should understand and test every feature, customize the design/data, capture your own screenshots/video, write the report in your own words, and follow your lecturer's rules on AI assistance and originality.
