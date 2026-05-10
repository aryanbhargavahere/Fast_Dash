package com.example.minigrocerydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.screens.*

@Composable
fun AppNavigation(viewModel: GroceryViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        // 1. Login Screen: Captures Name and Phone into ViewModel
        composable(route = "login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
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
                onAccountClick = { navController.navigate("account") } // Link to Account
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
                onBack = { navController.popBackStack() } // This makes the back arrow work
            )
        }

        // 6. Checkout: Final dynamic summary
        composable(route = "checkout") {
            CheckoutScreen(
                viewModel = viewModel,
                onOrderPlaced = {
                    viewModel.clearCart() // Wipe cart in DB
                    navController.navigate("success") // Navigate to Success Screen
                },
                onBack = { navController.popBackStack() },
                onEditAddress = { navController.navigate("address") }
            )
        }

        // 7. Account Screen: Displays captured user data
        composable(route = "account") {
            AccountScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout() // Clears ViewModel state
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        // 8. Order Success Screen: Final confirmation
        composable(route = "success") {
            OrderSuccessScreen(
                onContinueShopping = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}