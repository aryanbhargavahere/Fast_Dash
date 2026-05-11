# 🛒 Fast Dash - Mini Grocery Delivery App

[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com)
[![Database](https://img.shields.io/badge/Database-RoomDB-orange.svg)](https://developer.android.com/training/data-storage/room)

**Fast Dash** is a high-performance, minimalist grocery delivery application built for speed and reliability. Designed with a modern Android tech stack, it provides a seamless shopping experience with localized currency, dark mode support, and offline-first cart persistence.

---

## 📑 Table of Contents
1. [✨ Features](#-features)
2. [🛠️ Tech Stack](#-tech-stack)
3. [🏗️ Architecture](#-architecture)
4. [🛡️ Security & Privacy](#-security--privacy)
5. [📂 Project Structure](#-project-structure)
6. [🚀 Getting Started](#-getting-started)
    - [📋 Prerequisites](#-prerequisites)
    - [▶️ Steps to Run](#-steps-to-run)
7. [🧠 How the App Works](#-how-the-app-works)
8. [📲 Screenshots](#-screenshots)
9. [👤 Author](#-author)

---

## ✨ Features

- **🏠 Dynamic Home Screen**: Seamlessly browse categories and trending products with a fluid interface.
- **🌓 Adaptive Dark Mode**: Full app-wide Dark Mode support that toggles instantly via user settings.
- **🇮🇳 Localized Pricing**: All transactions and bill summaries are calculated in Indian Rupees (₹).
- **🛒 Smart Cart**: Persistent cart management powered by RoomDB—items stay in your cart even if the app closes.
- **💳 Multi-Payment Support**: Choose between "Cash on Delivery" or "Pay Online" (UPI/Card).
- **✅ Order Confirmation**: Unique Order ID generation with randomized delivery time estimates (25–45 mins).

---

## 🛠️ Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) — Declarative UI.
- **Architecture**: **MVVM (Model-View-ViewModel)** — Clean separation of concerns.
- **Local Database**: [RoomDB](https://developer.android.com/training/data-storage/room) — Handles persistent storage of cart items.
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation) — Manages screen transitions.
- **Asynchronous Logic**: **Kotlin Coroutines & Flow** — Smooth, non-blocking operations.

---

## 🏗️ Architecture

The app follows the **MVVM (Model-View-ViewModel)** design pattern to ensure a scalable and maintainable codebase:
- **Model**: Represents the data layer (Room Entities & DAOs).
- **View**: The UI layer built with Jetpack Compose that reacts to state changes.
- **ViewModel**: Acts as the bridge, maintaining UI state and handling business logic like cart totals and theme toggles.

---

## 🛡️ Security & Privacy

Privacy is a core pillar of the Fast Dash experience:
- **Offline First**: All financial and personal data is stored locally on your device via SQLite.
- **No Tracking**: No third-party analytics or external trackers are integrated.
- **Local Session**: Login credentials and address details are stored securely in encrypted SharedPreferences.

---

## 📂 Project Structure

```bash
com.example.minigrocerydeliveryapp
├── Accounts                # User Account & Preference Management
│   ├── Orders.kt           # Order history and tracking logic
│   ├── PaymentMethods.kt   # Payment gateway integrations
│   └── Saved Adress.kt     # User location management
├── datamodel               # Core Data Entities
│   ├── CategoryItem.kt     # Grocery category data class
│   └── Product.kt          # Product details and pricing model
├── Navigation              # App Routing Logic
│   └── AppNavigation.kt    # NavHost and screen definitions
├── Room                    # Local Persistence Layer (SQLite)
│   ├── CartDao.kt          # Database Access Object for cart
│   ├── CartItem.kt         # Database table entity for cart
│   └── viewmodel.kt        # Centralized State Management
├── screens                 # UI Layer - Jetpack Compose Screens
│   ├── Accounts.kt         # User profile and settings UI
│   ├── Adress Change.kt    # Delivery location modifications
│   ├── CartScreen.kt       # Cart review and bill summary (₹)
│   ├── CategoriesScreen.kt # Full grocery categories list
│   ├── checkoutscreen.kt   # Payment and order placement
│   ├── HomeScreen.kt       # Product discovery landing page
│   ├── LoginScreen.kt      # User authentication interface
│   └── ordersuccessscreen.kt # Confirmation with Unique Order ID
├── ui.theme                # Design System (Color, Theme, Type)
└── MainActivity.kt         # App entry point
```
---

##🚀 Getting Started
📋 Prerequisites

    Android Studio (Koala or newer)

    Minimum SDK: 24+ (Android 7.0+)

    Gradle: Version 8.0+
▶️ Steps to Run

    1. Clone the repository: git clone [https://github.com/yourusername/Fast_Dash.git](https://github.com/yourusername/Fast-Dash.git)
    2. Open in Android Studio: Wait for the Gradle sync to finish.
    3. Run: Click the Run button to install on an emulator or physical device.
---

##🧠 How the App Works

    Reactive UI: The UI observes the Room database using StateFlow. When an item is added to the cart, the total amount and item count update instantly across all screens.

    Persistence: Cart items are stored in a local SQLite database via Room. This ensures your shopping progress is saved even if the device restarts.

    Bill Summary: The app calculates a subtotal and adds a fixed handling fee (₹15) to generate the final Grand Total.
---

##📲 Screenshots

<img width="745" height="1600" alt="image" src="https://github.com/user-attachments/assets/90e90960-8472-4ed9-a6c7-54b6314573e6" />
<img width="1080" height="2139" alt="image" src="https://github.com/user-attachments/assets/bd78a77a-861b-47a6-b12a-73127c6bd01d" />
<img width="1080" height="2141" alt="image" src="https://github.com/user-attachments/assets/fbd16bb5-5328-4b3e-b14a-3835dbad3ba9" />

---

##👤 Author

    Name: Aryan
---
