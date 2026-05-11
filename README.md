# 🛒 Fast Dash - Mini Grocery Delivery App

[![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com)
[![Database](https://img.shields.io/badge/Database-RoomDB-orange.svg)](https://developer.android.com/training/data-storage/room)

**Fast Dash** is a high-performance, minimalist grocery delivery application designed for speed and local efficiency. Built using modern Android development practices, it features a reactive UI, localized Indian Rupee (₹) support, and a robust offline-first architecture.

---

## ✨ Features

- **🏠 Dynamic Home Screen**: Seamlessly browse categories and products with a fluid, modern interface.
- **🌓 Adaptive Dark Mode**: Full app-wide Dark Mode support that toggles instantly via user settings.
- **🇮🇳 Localized Pricing**: All transactions and bill summaries are calculated in Indian Rupees (₹).
- **🛒 Smart Cart**: Persistent cart management powered by RoomDB—items stay in your cart even if the app closes.
- **💳 Multi-Payment Support**: Choose between "Cash on Delivery" or "Pay Online" (UPI/Card) during checkout.
- **✅ Order Confirmation**: Unique Order ID generation with randomized delivery time estimates (25–45 mins).

---

## 🛠️ Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) — For a modern, declarative UI.
- **Architecture**: **MVVM (Model-View-ViewModel)** — Ensures clean separation of concerns.
- **Local Database**: [RoomDB](https://developer.android.com/training/data-storage/room) — Handles persistent storage of cart items.
- **Navigation**: [Jetpack Navigation Compose](https://developer.android.com/guide/navigation/navigation-getting-started) — Manages screen transitions and backstack.
- **Asynchronous Logic**: **Kotlin Coroutines & Flow** — For smooth, non-blocking database operations.

---

## 📂 Project Structure

```bash
com.example.minigrocerydeliveryapp
├── datamodel       # Product and Category data classes
├── navigation      # NavHost and screen route definitions
├── Room            # Room Database, CartDao, and Entities
├── screens         # Feature-specific UI screens
│   ├── HomeScreen.kt       # Product browsing & Search
│   ├── CartScreen.kt       # Bill summary & Quantity updates
│   ├── CheckoutScreen.kt   # Payment & Address selection
│   ├── LoginScreen.kt      # User authentication
│   └── 
└── viewmodels      # GroceryViewModel handling app state and business logic
```
---
