package com.example.minigrocerydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minigrocerydeliveryapp.Accounts.OrdersScreen
import com.example.minigrocerydeliveryapp.Accounts.SavedAddressScreen
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.screens.*
import kotlin.random.Random

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

        // 3. Saved Address Management (Dynamic Entry)
        composable(route = "address") {
            SavedAddressScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
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

        // 6. Checkout (Link to History)
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

        // 7. Account Screen (Connection Point)
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
                },
                // CONNECTING THE BUTTONS:
                onOrdersClick = { navController.navigate("orders") },
                onSavedAddressesClick = { navController.navigate("address") }
            )
        }

        // 8. Order History Screen
        composable(route = "orders") {
            OrdersScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 9. Order Success Screen
        composable(route = "success") {
            OrderSuccessScreen(
                viewModel = viewModel,
                onContinueShopping = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}