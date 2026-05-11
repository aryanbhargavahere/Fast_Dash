package com.example.minigrocerydeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.room.Room
import com.example.minigrocerydeliveryapp.Room.AppDatabase
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "grocery-db"
        ).fallbackToDestructiveMigration().build()

        val dao = db.cartDao()
        val viewModel = GroceryViewModel(dao, applicationContext)

        setContent {
            val isDark = viewModel.isDarkMode
            val colorScheme = if (isDark) {
                darkColorScheme(
                    primary = Color(0xFF006D3B),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E),
                    onBackground = Color.White,
                    onSurface = Color.White
                )
            } else {
                lightColorScheme(
                    primary = Color(0xFF006D3B),
                    background = Color(0xFFF8F9FA),
                    surface = Color.White,
                    onBackground = Color.Black,
                    onSurface = Color.Black
                )
            }

            MaterialTheme(colorScheme = colorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDest = if (viewModel.isLoggedIn) "home" else "login"

                    AppNavigation(
                        viewModel = viewModel,
                        startDestination = startDest
                    )
                }
            }
        }
    }
}