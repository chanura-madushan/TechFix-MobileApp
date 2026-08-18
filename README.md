# 🔧 TechFix — Mobile Service & Repair Booking App

<p align="center">
  <strong>A modern Android application for discovering repair services, finding nearby TechFix branches, and managing service appointments.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Language-Java+Kotlin-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Database-Room-6DB33F?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Maps-Google%20Maps-4285F4?style=for-the-badge&logo=googlemaps&logoColor=white" />
</p>

---

## 📱 About TechFix

**TechFix** is an Android mobile application developed as part of a Higher National Diploma in Software Engineering mobile application development coursework project.

The application is designed around a simple goal:

> **Make it easier for customers to find technical repair services, locate the nearest branch, and book an appointment directly from their mobile device.**

TechFix is built using native Android technologies and uses a local **Room database** for structured application data, while **Google Maps** and **Google Play Services Location** provide location and mapping functionality.

---

## ✨ Features

### 👤 Customer Features

* 🔐 User registration and login
* 🏠 Customer home dashboard
* 🛠️ Browse available repair services
* 💰 View service pricing
* 📅 Book repair appointments
* 📍 Automatically find the nearest TechFix branch
* 🗺️ View TechFix branches on a map
* 📋 View existing appointments
* 🔎 View detailed appointment information

### 🧑‍💼 Admin Features

* 📊 Admin appointment dashboard
* 👥 View customer appointment information
* 🏢 View selected branch and service details
* 🔄 Update appointment status
* ⚡ Perform database operations without blocking the UI

---

## 🧠 Application Workflow

```text
                 ┌───────────────┐
                 │    Customer   │
                 └───────┬───────┘
                         │
                         ▼
                 ┌─────────────────┐
                 │ Register / Login│
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Browse Services │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Select Service  │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Get User        │
                 │ Location        │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Find Nearest     │
                 │ Branch           │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Confirm Booking │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Room Database   │
                 └────────┬────────┘
                          │
                          ▼
                 ┌─────────────────┐
                 │ Admin Management│
                 └─────────────────┘
```

---

## 🏗️ Project Architecture

The application separates the user interface, data management, and supporting functionality into logical components.

```text
app/
│
└── src/
    └── main/
        └── java/
            └── com/example/techfix/
                │
                ├── Activities
                │   ├── LoginActivity
                │   ├── RegisterActivity
                │   ├── HomeActivity
                │   ├── ServicesActivity
                │   ├── BookingActivity
                │   ├── AppointmentsActivity
                │   ├── AppointmentDetailActivity
                │   ├── MapActivity
                │   └── AdminActivity
                │
                ├── data/
                │   ├── AppDatabase.java
                │   ├── DataSeeder.java
                │   ├── dao/
                │   ├── entity/
                │   └── repository/
                │
                ├── ui/
                │
                └── util/
```

The data layer uses **Room**, with entities, DAOs, repositories, and a central database configuration.

This structure keeps database operations separated from the application UI and makes the project easier to maintain and extend.

---

## 🛠️ Technology Stack

| Technology                     | Purpose                      |
| ------------------------------ | ---------------------------- |
| ☕ **Java**                     | Core application development |
| 🤖 **Android SDK**             | Android platform             |
| 🧩 **AndroidX**                | Modern Android components    |
| 🎨 **Material Components**     | UI components and styling    |
| 🗄️ **Room Database**          | Local data persistence       |
| 📋 **RecyclerView**            | Efficient list rendering     |
| 🗺️ **Google Maps SDK**        | Map and branch visualization |
| 📍 **Fused Location Provider** | Device location services     |
| ⚙️ **Gradle Kotlin DSL**       | Build configuration          |
| 🧪 **JUnit**                   | Unit testing                 |
| 🧪 **Espresso**                | Android UI testing           |

---

## 📍 Smart Nearest-Branch Detection

One of TechFix's main features is location-based branch selection.

When a customer starts the booking process, the application:

1. Requests location permission.
2. Obtains the customer's current location.
3. Loads available branches from the local database.
4. Calculates the distance between the customer and each branch.
5. Finds the closest branch.
6. Displays the nearest branch and distance.
7. Allows the customer to confirm the appointment.

```text
Customer Location
       │
       ▼
Get Available Branches
       │
       ▼
Calculate Distances
       │
       ▼
Find Minimum Distance
       │
       ▼
Nearest TechFix Branch
       │
       ▼
Confirm Appointment
```

---

## 🗺️ Google Maps Integration

TechFix integrates Google Maps and Google Play Services Location to provide location-based functionality.

The application supports:

* 📍 Current user location
* 🏢 TechFix branch locations
* 🗺️ Map visualization
* 📏 Distance calculation
* 📌 Nearest branch detection

> **Security:** Never commit your real Google Maps API key to GitHub. Configure it securely through your local build configuration.

---

## 🗄️ Database

TechFix uses **Android Room** for local data persistence.

The database layer contains:

* Database configuration
* Entities
* DAOs
* Repository classes
* Seed data

```text
                Room Database
                     │
        ┌────────────┼────────────┐
        ▼            ▼            ▼
     Entities       DAOs      Repositories
        │            │            │
        └────────────┼────────────┘
                     │
                     ▼
              Application Data
```

This provides a structured way to manage users, branches, services, and appointments.

---

## 👨‍💼 Admin Management

The application includes a dedicated administration interface.

Administrators can:

* View active appointments
* View customer information
* View requested services
* View selected branches
* View appointment dates
* Update appointment statuses

Example appointment workflow:

```text
Pending
   │
   ▼
Confirmed
   │
   ▼
In Progress
   │
   ▼
Completed
```

The admin interface uses `RecyclerView` to display appointments efficiently.

---

## 📂 Main Application Components

| Component                   | Responsibility               |
| --------------------------- | ---------------------------- |
| `MainActivity`              | Application entry point      |
| `LoginActivity`             | User login                   |
| `RegisterActivity`          | User registration            |
| `HomeActivity`              | Customer dashboard           |
| `ServicesActivity`          | Browse repair services       |
| `BookingActivity`           | Appointment booking          |
| `AppointmentsActivity`      | Appointment listing          |
| `AppointmentDetailActivity` | Appointment details          |
| `MapActivity`               | Map and branch functionality |
| `AdminActivity`             | Admin appointment management |
| `AppDatabase`               | Room database configuration  |
| `DataSeeder`                | Initial application data     |

---

## 🚀 Getting Started

### Prerequisites

Before running the project, make sure you have:

* Android Studio
* Android SDK 36
* Java 11 compatible JDK
* Android Emulator or physical Android device
* Google Maps API key

### 1. Clone the Repository

```bash
git clone https://github.com/chanura-madushan/TechFix-MobileApp.git
```

### 2. Open in Android Studio

Open the cloned project using Android Studio.

Allow Gradle to synchronize and download the required dependencies.

### 3. Configure Google Maps

Configure your Google Maps API key using the project's expected:

```text
MAPS_API_KEY
```

Do **not** hard-code the API key into the source code.

### 4. Run the Application

Connect an Android device or start an emulator.

Then click:

```text
Run ▶
```

in Android Studio.

---

## 🧪 Testing

The project includes Android testing support using:

* **JUnit**
* **Espresso**
* Android Instrumentation Tests

Tests can be executed directly through Android Studio or using the appropriate Gradle test commands.

---

## 🔐 Permissions

TechFix requires the following permissions for specific functionality:

```text
ACCESS_FINE_LOCATION
ACCESS_COARSE_LOCATION
CAMERA
```

Location permissions are used to identify the customer's position and determine the nearest TechFix branch.

---

## 🔮 Future Improvements

Possible future improvements include:

* ☁️ Cloud database synchronization
* 🔔 Push notifications
* 💳 Online payment integration
* ⭐ Customer reviews and ratings
* 🧑‍🔧 Technician assignment
* 📦 Repair status tracking
* 📈 Advanced admin analytics
* 🔐 Improved authentication and role management
* 🧪 Expanded automated test coverage
* 🌐 Backend API integration
* 📱 Improved responsive UI for different screen sizes

---

## 🎓 Academic Context

**Project:** TechFix Mobile Application

**Programme:** Higher National Diploma in Software Engineering

**Module:** Mobile Application Development

**Platform:** Android

**Project Type:** Academic / Coursework Project

**Development Team:** 3 Members

The project was developed to demonstrate practical knowledge of Android application development, database management, location services, UI design, software architecture, and collaborative software engineering.

---

## 👨‍💻 Contributors

### Chanura Madushan

GitHub:
https://github.com/chanura-madushan

### Team Members

Add the remaining team members here:

```text
1. Team Member Name
   GitHub: https://github.com/AlphaOshyy

2. Team Member Name
   GitHub: https://github.com/chanura-madushan

3. Team Member Name
   GitHub: https://github.com/chanura-madushan
```

---

## 📸 Screenshots

Add application screenshots here to make the repository more visually impressive.

Example:

```markdown
## 📸 Screenshots

| Login | Home | Services |
|---|---|---|
| ![Login](screenshots/login.png) | ![Home](screenshots/home.png) | ![Services](screenshots/services.png) |

| Booking | Map | Appointments |
|---|---|---|
| ![Booking](screenshots/booking.png) | ![Map](screenshots/map.png) | ![Appointments](screenshots/appointments.png) |
```

---

## 📌 Project Status

```text
🟢 Active Development
```

The project is currently being developed as part of the Mobile Application Development coursework.

---

## 📄 License

This project was developed for educational purposes as part of a Higher National Diploma in Software Engineering coursework project.

---

<p align="center">

### 🔧 TechFix

**Repair smarter. Book easier. Stay connected.**

⭐ If you find this project interesting, consider giving the repository a star!

</p>
