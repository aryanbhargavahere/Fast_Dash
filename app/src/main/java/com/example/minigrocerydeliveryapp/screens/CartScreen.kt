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
    onBack: () -> Unit // New parameter for back navigation
) {
    val items by viewModel.cartItems.collectAsState()
    val total by viewModel.totalAmount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Only show the checkout button if the cart has items
            if (items.isNotEmpty()) {
                ProceedToCheckoutBar(onCheckout)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F8FA))
        ) {
            if (items.isEmpty()) {
                // EMPTY STATE UI
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(Modifier.height(16.dp))
                    Text("Your cart is empty", fontSize = 18.sp, color = Color.Gray)
                    TextButton(onClick = onBack) {
                        Text("Go Shop Now", color = Color(0xFF006D3B))
                    }
                }
            } else {
                // CART CONTENT
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items) { item ->
                        CartItemRow(
                            item = item,
                            onIncrease = { viewModel.updateQuantity(item, 1) },
                            onDecrease = { viewModel.updateQuantity(item, -1) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    item { BillSummaryCard(total) }

                    // Extra spacing at the bottom so the last item isn't hidden by the button
                    item { Spacer(modifier = Modifier.height(100.dp)) }
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${item.unit} • $${item.price}", color = Color.Gray, fontSize = 12.sp)
                Text(
                    "$${String.format("%.2f", item.price * item.quantity)}",
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF006D3B)
                )
            }

            // Quantity Controller
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF006D3B))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                Text(
                    text = item.quantity.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                IconButton(onClick = onIncrease, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun BillSummaryCard(itemTotal: Double) {
    val handlingFee = 1.50
    val grandTotal = itemTotal + handlingFee

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Bill Summary", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Item Total", color = Color.Gray)
                Text("$${String.format("%.2f", itemTotal)}")
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Handling Fee", color = Color.Gray)
                Text("$${String.format("%.2f", handlingFee)}")
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color(0xFFEEEEEE))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Grand Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    "$${String.format("%.2f", grandTotal)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun ProceedToCheckoutBar(onCheckout: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 20.dp,
        color = Color.White
    ) {
        Button(
            onClick = onCheckout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD523)), // Yellow Brand Color
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "PROCEED TO CHECKOUT",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Black)
        }
    }
}