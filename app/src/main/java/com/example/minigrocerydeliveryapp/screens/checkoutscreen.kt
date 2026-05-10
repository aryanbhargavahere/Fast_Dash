package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.CartItem
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

// Brand Colors
val FreshDashGreen = Color(0xFF006D3B)
val LightGreyBg = Color(0xFFF7F8FA)
val InputBg = Color(0xFFFFF9E5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: GroceryViewModel,
    onOrderPlaced: () -> Unit,
    onBack: () -> Unit,
    onEditAddress: () -> Unit
) {
    val items by viewModel.cartItems.collectAsState()
    val subtotal by viewModel.totalAmount.collectAsState()

    val userAddress = viewModel.userAddress
    val userPhone = viewModel.userPhone

    var selectedPayment by remember { mutableStateOf("NetBanking") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", color = FreshDashGreen, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        color = FreshDashGreen.copy(0.1f),
                        shape = CircleShape,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, null, tint = FreshDashGreen, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("15 MINS", color = FreshDashGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            )
        },
        containerColor = LightGreyBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            CheckoutCard(
                icon = Icons.Default.LocationOn,
                title = "Delivery Address",
                actionText = "Edit",
                onActionClick = onEditAddress
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Current Location", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = if (userAddress.isEmpty() || userAddress == "Add Your Address")
                            "Please set your delivery address" else userAddress,
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (userPhone.isEmpty()) "No phone linked" else "+91 $userPhone",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            CheckoutCard(icon = Icons.Default.Payments, title = "Payment Method") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PaymentRow("NetBanking", Icons.Default.AccountBalance, true, selectedPayment) { selectedPayment = it }
                    PaymentRow("Credit / Debit Card", Icons.Default.CreditCard, false, selectedPayment) { selectedPayment = it }
                    PaymentRow("Cash on Delivery", Icons.Default.Payments, false, selectedPayment) { selectedPayment = it }
                }
            }

            CheckoutCard(title = "Order Items (${items.size})") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items.forEach { item ->
                        CheckoutItemRow(item)
                    }
                }
            }

            CheckoutCard {
                // FIXED: Passing viewModel to use the placeOrder function
                OrderSummarySection(subtotal, onPlaceOrder = {
                    viewModel.placeOrder() // Edit: Increments Active Orders & Clears Cart
                    onOrderPlaced()        // Navigate to Success Screen
                })
            }
        }
    }
}

@Composable
fun OrderSummarySection(subtotal: Double, onPlaceOrder: () -> Unit) {
    val serviceFee = 0.99
    val grandTotal = subtotal + serviceFee
    var promoCode by remember { mutableStateOf("") }

    Column {
        Text("Order Summary", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        Spacer(Modifier.height(16.dp))

        SummaryRow("Subtotal", "$${String.format("%.2f", subtotal)}")
        SummaryRow("Delivery Fee", "FREE", FreshDashGreen)
        SummaryRow("Service Fee", "$${String.format("%.2f", serviceFee)}")

        HorizontalDivider(Modifier.padding(vertical = 16.dp), color = Color(0xFFEEEEEE))

        SummaryRow("Total amount", "$${String.format("%.2f", grandTotal)}", isBold = true)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = promoCode,
            onValueChange = { promoCode = it },
            placeholder = { Text("Promo code", color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Text("Apply", color = Color.Black, fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 8.dp).clickable { })
            },
            leadingIcon = { Icon(Icons.Default.CardGiftcard, null, tint = Color.Black) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onPlaceOrder,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FreshDashGreen)
        ) {
            Text("Place Order", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, null, tint = Color.White)
        }
    }
}

// All other helper components (SummaryRow, CheckoutCard, PaymentRow, CheckoutItemRow)
// remain exactly as you provided to ensure no functionality is lost.

@Composable
fun SummaryRow(label: String, value: String, valueColor: Color = Color.Black, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isBold) Color.Black else Color.Gray,
            fontSize = if (isBold) 16.sp else 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            color = valueColor,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Bold,
            fontSize = if (isBold) 20.sp else 14.sp
        )
    }
}

@Composable
fun CheckoutCard(
    icon: ImageVector? = null,
    title: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (title != null) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (icon != null) Icon(icon, null, tint = FreshDashGreen, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }
                    if (actionText != null) {
                        Text(actionText, color = FreshDashGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                            modifier = Modifier.clickable { onActionClick?.invoke() })
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
            content()
        }
    }
}

@Composable
fun PaymentRow(name: String, icon: ImageVector, isDefault: Boolean, selected: String, onSelect: (String) -> Unit) {
    val isSelected = selected == name
    Surface(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (isSelected) FreshDashGreen else Color(0xFFEEEEEE)),
        color = Color.White,
        modifier = Modifier.fillMaxWidth().clickable { onSelect(name) }
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = isSelected, onClick = null, colors = RadioButtonDefaults.colors(selectedColor = FreshDashGreen))
            Spacer(Modifier.width(8.dp))
            Icon(icon, null, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Text(name, fontWeight = FontWeight.Medium, color = Color.Black, modifier = Modifier.weight(1f))
            if (isDefault) Text("Default", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun CheckoutItemRow(item: CartItem) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(item.imageRes),
            contentDescription = null,
            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Fit
        )
        Column(Modifier.weight(1f).padding(horizontal = 12.dp)) {
            Text(item.name, fontWeight = FontWeight.SemiBold, color = Color.Black, maxLines = 1)
            Text("${item.quantity} x ${item.unit}", color = Color.Gray, fontSize = 12.sp)
        }
        Text("$${String.format("%.2f", item.price * item.quantity)}", fontWeight = FontWeight.Bold, color = Color.Black)
    }
}