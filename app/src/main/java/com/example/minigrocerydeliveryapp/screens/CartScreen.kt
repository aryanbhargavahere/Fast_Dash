package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.CartItem
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: GroceryViewModel,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val items by viewModel.cartItems.collectAsState()
    val total by viewModel.totalAmount.collectAsState()

    val bgColor = if (viewModel.isDarkMode) Color(0xFF121212) else Color(0xFFF7F8FA)
    val surfaceColor = if (viewModel.isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (viewModel.isDarkMode) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Cart", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            if (items.isNotEmpty()) {
                ProceedToCheckoutBar(viewModel.isDarkMode, onCheckout)
            }
        },
        containerColor = bgColor
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (items.isEmpty()) {
                EmptyCartView(onBack)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        CartItemRow(item, viewModel.isDarkMode,
                            onIncrease = { viewModel.updateQuantity(item, 1) },
                            onDecrease = { viewModel.updateQuantity(item, -1) }
                        )
                    }
                    item { BillSummaryCard(total, viewModel.isDarkMode) }
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, isDarkMode: Boolean, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    val cardColor = if (isDarkMode) Color(0xFF2C2C2C) else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, if (isDarkMode) Color.DarkGray else Color(0xFFEEEEEE))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp))
            )
            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                // Updated to Rupees
                Text("${item.unit} • ₹${item.price.toInt()}", color = Color.Gray, fontSize = 12.sp)
                Text("₹${(item.price * item.quantity).toInt()}", fontWeight = FontWeight.ExtraBold, color = Color(0xFF4CAF50))
            }
            Row(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color(0xFF006D3B)).padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, null, tint = Color.White)
                }
                Text(item.quantity.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun BillSummaryCard(itemTotal: Double, isDarkMode: Boolean) {
    val cardColor = if (isDarkMode) Color(0xFF2C2C2C) else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val handlingFee = 15.0
    val grandTotal = itemTotal + handlingFee

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Bill Summary", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = textColor)
            // Updated to Rupees
            SummaryRow("Item Total", "₹${itemTotal.toInt()}", textColor)
            SummaryRow("Handling Fee", "₹${handlingFee.toInt()}", textColor)
            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = if (isDarkMode) Color.Gray else Color(0xFFEEEEEE))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = textColor)
            }
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, color: Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.Gray)
        Text(value, color = color)
    }
}

@Composable
fun ProceedToCheckoutBar(isDarkMode: Boolean, onCheckout: () -> Unit) {
    Surface(color = if (isDarkMode) Color(0xFF1E1E1E) else Color.White, shadowElevation = 20.dp) {
        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD523)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("PROCEED TO CHECKOUT", color = Color.Black, fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ChevronRight, null, tint = Color.Black)
        }
    }
}

@Composable
fun EmptyCartView(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        Icon(Icons.Default.ShoppingCart, null, Modifier.size(80.dp), tint = Color.LightGray)
        Text("Your cart is empty", color = Color.Gray)
        TextButton(onClick = onBack) { Text("Go Shop Now", color = Color(0xFF006D3B)) }
    }
}