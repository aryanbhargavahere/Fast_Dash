package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel
import kotlin.random.Random

@Composable
fun OrderSuccessScreen(
    viewModel: GroceryViewModel,
    onContinueShopping: () -> Unit
) {
    val isDark = viewModel.isDarkMode

    val orderId = remember { "GOC-${Random.nextInt(10000, 99999)}" }
    val estimate = remember { "${Random.nextInt(20, 35)}-${Random.nextInt(40, 55)} mins" }

    // Dynamic Colors
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF8F9FA)
    val surfaceColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val subTextColor = if (isDark) Color.LightGray else Color.Gray
    val dividerColor = if (isDark) Color.DarkGray else Color(0xFFF1F1F1)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        if (isDark) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFE8F5E9),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Order Placed Successfully!",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )

            Text(
                text = "Your groceries are on the way to your doorstep.",
                fontSize = 14.sp,
                color = subTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 2.dp),
                border = if (isDark) BorderStroke(1.dp, Color.DarkGray) else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Order ID", color = subTextColor)
                        Text("#$orderId", fontWeight = FontWeight.Bold, color = textColor)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = dividerColor)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Timer,
                                null,
                                tint = if (isDark) Color(0xFF81C784) else Color(0xFF006D3B),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text("Estimated Delivery", color = subTextColor)
                        }
                        Text(
                            estimate,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) Color(0xFF81C784) else Color(0xFF006D3B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = onContinueShopping,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD523)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CONTINUE SHOPPING", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}