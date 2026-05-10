package com.example.minigrocerydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.screens.*

@Composable
fun AppNavigation(
    viewModel: GroceryViewModel,
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // 1. Login Screen
        composable(route = "login") {
            LoginScreen(
                viewModel = viewModel,
                // FIXED: Explicitly naming parameters to fix type inference errors
                onLoginSuccess = { name: String, phone: String ->
                    viewModel.loginUser(name, phone)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // 2. Home Screen
        composable(route = "home") {
            HomeScreen(
                viewModel = viewModel,
                onCartClick = { navController.navigate("cart") },
                onAddressClick = { navController.navigate("address") },
                onSeeAllCategories = { navController.navigate("categories") },
                onAccountClick = { navController.navigate("account") }
            )
        }

        // 3. Address Selection
        composable(route = "address") {
            AddressScreen(viewModel) {
                navController.popBackStack()
            }
        }

        // 4. Categories
        composable(route = "categories") {
            CategoriesScreen {
                navController.popBackStack()
            }
        }

        // 5. Review Cart
        composable("cart") {
            CartScreen(
                viewModel = viewModel,
                onCheckout = { navController.navigate("checkout") },
                onBack = { navController.popBackStack() }
            )
        }

        // 6. Checkout
        composable(route = "checkout") {
            CheckoutScreen(
                viewModel = viewModel,
                onOrderPlaced = {
                    navController.navigate("success")
                },
                onBack = { navController.popBackStack() },
                onEditAddress = { navController.navigate("address") }
            )
        }

        // 7. Account Screen
        composable("account") {
            AccountScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
                // FIXED: Added missing parameters required by your AccountScreen.kt
                onNavigateHome = {
                    navController.navigate("home") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                    }
                },
                onNavigateCart = {
                    navController.navigate("cart") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 8. Order Success Screen
        composable(route = "success") {
            OrderSuccessScreen(
                viewModel = viewModel, // Pass the viewModel here
                onContinueShopping = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}