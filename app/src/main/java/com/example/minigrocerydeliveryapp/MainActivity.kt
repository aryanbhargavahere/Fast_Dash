package com.example.minigrocerydeliveryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.minigrocerydeliveryapp.Room.AppDatabase
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.navigation.AppNavigation
import com.example.minigrocerydeliveryapp.ui.theme.MiniGroceryDeliveryAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "grocery-db"
        ).build()

        val dao = db.cartDao()

        // Basic way to get ViewModel without Hilt
        val viewModel = GroceryViewModel(dao)

        setContent {
            MaterialTheme {
                Surface(color = Color.White) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}