package com.example.minigrocerydeliveryapp.Accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import com.example.minigrocerydeliveryapp.datamodel.Order

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(viewModel: GroceryViewModel, onBack: () -> Unit) {
    val orders by viewModel.orderHistory.collectAsState()
    val isDark = viewModel.isDarkMode

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF7F8FA)
    ) { padding ->
        if (orders.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No previous orders found", color = Color.Gray)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(orders) { order ->
                    OrderCard(order, isDark)
                }
            }
        }
    }
}

@Composable
fun OrderCard(order: Order, isDark: Boolean) { // Changed .Orders to .Order
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBag,
                contentDescription = null,
                tint = Color(0xFF006D3B),
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(text = "Order #${order.id}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(order.date, color = Color.Gray, fontSize = 12.sp)
                // Note: Ensure your Order data class has 'status'.
                // If you get an error here, check if it's called 'status' in ViewModel
                Text(order.status, color = Color(0xFF006D3B), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Text(
                text = "₹${order.amount.toInt()}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
            )
        }
    }
}