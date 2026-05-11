package com.example.minigrocerydeliveryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.minigrocerydeliveryapp.Accounts.OrdersScreen
import com.example.minigrocerydeliveryapp.Accounts.SavedAddressScreen
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
        composable(route = "home") {
            HomeScreen(
                viewModel = viewModel,
                onCartClick = { navController.navigate("cart") },
                onAddressClick = { navController.navigate("address") },
                onSeeAllCategories = { navController.navigate("categories") },
                onAccountClick = { navController.navigate("account") }
            )
        }

        composable(route = "address") {
            SavedAddressScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = "categories") {
            CategoriesScreen {
                navController.popBackStack()
            }
        }

        composable("cart") {
            CartScreen(
                viewModel = viewModel,
                onCheckout = { navController.navigate("checkout") },
                onBack = { navController.popBackStack() }
            )
        }
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
                onOrdersClick = { navController.navigate("orders") },
                onSavedAddressesClick = { navController.navigate("address") }
            )
        }
        composable(route = "orders") {
            OrdersScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
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