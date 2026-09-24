# 🔧 TechFix — Mobile Repair Management App

<p align="center">
  <strong>A mobile platform for booking, managing, and tracking device repair services.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android" alt="Android">
  <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/Database-Firebase%20Firestore-yellow?style=for-the-badge&logo=firebase" alt="Firebase">
  <img src="https://img.shields.io/badge/IDE-Android%20Studio-blue?style=for-the-badge&logo=androidstudio" alt="Android Studio">
</p>

---

## 📱 About The Project

**TechFix** is an Android application developed to simplify the process of getting electronic devices repaired.

Customers can browse available repair services, select a branch, book appointments, make payments, and track their repair status.

Administrators can manage active appointments and update their status through a dedicated admin interface.

---

## ✨ Features

### 👤 Customer

* 🔐 User registration and login
* 📱 Browse device categories
* 🔧 Browse repair services
* 💰 View repair prices
* 📍 Select nearby repair branches
* 📅 Book repair appointments
* 📋 View appointment history
* 🔎 View appointment details
* 📷 Add repair-related photos using the camera
* 💳 Make simulated card payments
* 📊 Track appointment status

### 🛠️ Administrator

* 🔑 Role-based admin access
* 📋 View active appointments
* 👤 View customer information
* 🔧 View repair details
* 🔄 Update appointment status
* 📊 Manage the repair workflow

---

# 🏗️ Application Architecture

TechFix uses a **Repository-based architecture** to separate the user interface from database operations.

```text
┌─────────────────────────┐
│     Android Activities  │
│        / UI Layer       │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│      Repositories       │
│    Database Operations  │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│   Firebase Firestore    │
│      Cloud Database     │
└─────────────────────────┘
```

This structure makes the application easier to maintain because UI code and database operations are separated.

---

# 🗂️ Project Structure

```text
com.example.techfix
│
├── Activities
│   ├── LoginActivity
│   ├── RegisterActivity
│   ├── HomeActivity
│   ├── ServicesActivity
│   ├── BookingActivity
│   ├── AppointmentsActivity
│   ├── AppointmentDetailActivity
│   ├── PaymentGatewayActivity
│   └── AdminActivity
│
├── data
│   ├── UserRepository
│   ├── AppointmentRepository
│   ├── RepairServiceRepository
│   ├── DeviceCategoryRepository
│   ├── BranchRepository
│   ├── PaymentRepository
│   └── DataSeeder
│
├── model
│   ├── User
│   ├── Appointment
│   ├── RepairService
│   ├── DeviceCategory
│   ├── Branch
│   └── Payment
│
├── ui
│   ├── CategoryAdapter
│   ├── ServiceAdapter
│   ├── AppointmentAdapter
│   └── AdminAppointmentAdapter
│
└── util
    ├── PasswordUtils
    ├── LocationUtils
    └── CardValidationUtils
```

---

# ☁️ Firebase Firestore

TechFix uses **Firebase Cloud Firestore** as its cloud database.

### Collections

```text
📁 users
📁 deviceCategories
📁 repairServices
📁 branches
📁 appointments
📁 payments
```

The application connects to Firestore using:

```java
FirebaseFirestore.getInstance()
```

Repositories then access the required collection.

Example:

```java
servicesRef = FirebaseFirestore.getInstance()
        .collection("repairServices");
```

---

# 🔄 Customer Flow

```text
        ┌──────────────┐
        │ Register /   │
        │    Login     │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │     Home     │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │    Device    │
        │  Categories  │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │    Repair    │
        │   Services   │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │    Booking   │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │    Branch    │
        │   Selection  │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │ Appointment  │
        └──────┬───────┘
               ↓
        ┌──────────────┐
        │   Payment /  │
        │    Repair    │
        └──────────────┘
```

---

# 🛠️ Admin Flow

```text
┌──────────────┐
│ Admin Login  │
└──────┬───────┘
       ↓
┌──────────────┐
│ Admin Panel  │
└──────┬───────┘
       ↓
┌──────────────┐
│    Active    │
│ Appointments │
└──────┬───────┘
       ↓
┌──────────────┐
│ View Details │
└──────┬───────┘
       ↓
┌──────────────┐
│ Update Status│
└──────────────┘
```

---

# 🧰 Technology Stack

| Technology                      | Usage                           |
| ------------------------------- | ------------------------------- |
| ☕ **Java**                      | Android application development |
| 🤖 **Android Studio**           | Development environment         |
| 🔥 **Firebase Firestore**       | Cloud database                  |
| 📋 **RecyclerView**             | Displaying dynamic lists        |
| 📍 **Google Location Services** | Location and branch selection   |
| 📷 **Android Camera**           | Capturing repair photos         |
| ⚙️ **Gradle**                   | Project build system            |

---

# 🔐 Security & Validation

TechFix includes several validation and access-control mechanisms:

### Password Security

Passwords are processed using **SHA-256 hashing** before being stored.

### Card Validation

The payment system validates:

* Card number
* Expiry date
* CVV

Card numbers are checked using the **Luhn algorithm**.

### Admin Access

The application checks the user's role before allowing access to the administrator panel.

```text
User
 │
 ├── Customer → Customer Application
 │
 └── Admin → Admin Panel
```

---

# 📍 Location Services

TechFix uses Android location services to work with branch locations.

The application contains branch information such as:

* Branch name
* Location
* Latitude
* Longitude

This allows the application to work with location-based branch selection.

---

# 📷 Camera Integration

Customers can capture photos related to their repair appointment.

The application uses:

* Camera permission
* `FileProvider`
* Android Activity Result APIs
* Temporary image files

---

# 💳 Payment System

TechFix contains a simulated payment gateway.

The system:

```text
Enter Card Details
        ↓
Validate Card
        ↓
Validate Expiry
        ↓
Validate CVV
        ↓
Process Payment
        ↓
Save Payment Record
```

> **Note:** The current implementation is a simulated payment system and does not process real financial transactions.

---

# 🚀 Getting Started

## Prerequisites

Before running the project, install:

* Android Studio
* JDK 11
* Android SDK
* Git
* A Firebase project

---

## 📥 Clone the Repository

```bash
git clone https://github.com/chanura-madushan/TechFix-MobileApp.git
```

Then open the project in **Android Studio**.

---

## 🔥 Firebase Setup

Connect the Android application to your Firebase project and configure Firestore.

The application requires access to the Firestore database used by the project.

---

## ▶️ Run the Application

1. Open the project in Android Studio.
2. Allow Gradle to synchronize.
3. Connect an Android device or start an emulator.
4. Build the project.
5. Run the application.

---

# 📸 Screenshots

> Add application screenshots here as the project develops.

Example:

```text
screenshots/
├── login.png
├── home.png
├── services.png
├── booking.png
├── appointments.png
└── admin.png
```


---

# 📌 Project Status

🟢 **Active Development**

TechFix is being developed as an Android university coursework project focused on mobile-based device repair management.

---

# 👨‍💻 Development Team

Developed as a **university group project**.

### Team Members

| Member         | Role        |
| -------------- | ----------- |
| 👨‍💻 Madushan | Development |
| 👨‍💻 Oshan    | Development |
| 👨‍💻 Tharindu | Development |

> Replace the member names and roles above with your actual team information.

---

# 📄 License

This project was created for **educational and coursework purposes**.

---

<p align="center">

### 🔧 TechFix

**Making device repairs easier to manage.**

</p>
