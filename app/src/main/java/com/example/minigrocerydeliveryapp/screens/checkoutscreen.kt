package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: GroceryViewModel,
    onOrderPlaced: () -> Unit,
    onBack: () -> Unit,
    onEditAddress: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val itemTotal by viewModel.totalAmount.collectAsState()
    val isDark = viewModel.isDarkMode

    // Selection state for payment
    var selectedPaymentMethod by remember { mutableStateOf("COD") }

    val handlingFee = 15.0
    val grandTotal = itemTotal + handlingFee

    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF7F8FA)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor)
            )
        },
        bottomBar = {
            Surface(color = surfaceColor, shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        // FIXED: Generate unique Order ID and pass grandTotal
                        val uniqueOrderId = "GOC-${System.currentTimeMillis().toString().takeLast(5)}"
                        viewModel.placeOrder(amount = grandTotal, orderId = uniqueOrderId)
                        onOrderPlaced()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD523)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val actionText = if (selectedPaymentMethod == "ONLINE") "PAY & PLACE ORDER" else "PLACE ORDER"
                    Text("$actionText • ₹${grandTotal.toInt()}", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- DELIVERY ADDRESS SECTION ---
            CheckoutSectionCard(title = "Delivery Address", isDark = isDark) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFF006D3B), modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = viewModel.userAddress,
                        modifier = Modifier.weight(1f),
                        color = textColor,
                        fontSize = 14.sp
                    )
                    TextButton(onClick = onEditAddress) {
                        Text("Change", color = Color(0xFF006D3B), fontWeight = FontWeight.Bold)
                    }
                }
            }

            // --- PAYMENT METHOD SECTION ---
            CheckoutSectionCard(title = "Payment Method", isDark = isDark) {
                Column {
                    // Cash on Delivery Option
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "COD" }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (selectedPaymentMethod == "COD"),
                            onClick = { selectedPaymentMethod = "COD" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF006D3B))
                        )
                        Icon(Icons.Default.Payments, null, tint = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                        Text("Cash on Delivery", color = textColor)
                    }

                    // Online Payment Option
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentMethod = "ONLINE" }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = (selectedPaymentMethod == "ONLINE"),
                            onClick = { selectedPaymentMethod = "ONLINE" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF006D3B))
                        )
                        Icon(Icons.Default.Language, null, tint = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                        Text("Pay Online (UPI, Card, NetBanking)", color = textColor)
                    }
                }
            }

            // --- ORDER SUMMARY SECTION ---
            CheckoutSectionCard(title = "Order Summary", isDark = isDark) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cartItems.forEach { item ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${item.quantity}x ${item.name}", color = Color.Gray, fontSize = 14.sp)
                            Text("₹${(item.price * item.quantity).toInt()}", color = textColor, fontSize = 14.sp)
                        }
                    }
                    HorizontalDivider(Modifier.padding(vertical = 8.dp), color = if (isDark) Color.DarkGray else Color(0xFFEEEEEE))

                    CheckoutSummaryRow("Item Total", "₹${itemTotal.toInt()}", textColor)
                    CheckoutSummaryRow("Handling Fee", "₹${handlingFee.toInt()}", textColor)

                    Row(
                        Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                        Text("₹${grandTotal.toInt()}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF006D3B))
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutSectionCard(title: String, isDark: Boolean, content: @Composable () -> Unit) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        border = BorderStroke(1.dp, if (isDark) Color.DarkGray else Color(0xFFEEEEEE))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun CheckoutSummaryRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, color = color, fontSize = 14.sp)
    }
}