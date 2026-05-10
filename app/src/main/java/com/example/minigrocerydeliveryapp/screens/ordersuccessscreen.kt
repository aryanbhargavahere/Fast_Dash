package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OrderSuccessScreen(onContinueShopping: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF006D3B),
            modifier = Modifier.size(100.dp)
        )
        Spacer(Modifier.height(24.dp))
        Text("Order Placed Successfully!", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(
            "Your groceries are on their way.",
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onContinueShopping,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D3B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue Shopping", fontWeight = FontWeight.Bold)
        }
    }
}