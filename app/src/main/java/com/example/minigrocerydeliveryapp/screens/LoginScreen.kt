package com.example.minigrocerydeliveryapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minigrocerydeliveryapp.R
import com.example.minigrocerydeliveryapp.Room.GroceryViewModel

@Composable
fun LoginScreen(
    viewModel: GroceryViewModel,
    onLoginSuccess: () -> Unit
) {
    // Local state for OTP visibility
    var otpValue by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App Logo or Illustration
        Image(
            painter = painterResource(id = R.drawable.fresh_dash_logo), // Use your actual logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to FreshDash",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF006D3B)
        )

        Text(
            text = if (!isOtpSent) "Enter details to continue" else "Verify your number",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        if (!isOtpSent) {
            // 1. NAME INPUT (Linked to ViewModel)
            OutlinedTextField(
                value = viewModel.userName,
                onValueChange = { viewModel.userName = it },
                label = { Text("Full Name") },
                placeholder = { Text("e.g. Alex Johnson") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF006D3B)) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D3B),
                    focusedLabelColor = Color(0xFF006D3B)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. PHONE INPUT (Linked to ViewModel)
            OutlinedTextField(
                value = viewModel.userPhone,
                onValueChange = { viewModel.userPhone = it },
                label = { Text("Phone Number") },
                placeholder = { Text("") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF006D3B)) },
                prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D3B),
                    focusedLabelColor = Color(0xFF006D3B)
                )
            )
        } else {
            // 3. OTP INPUT
            OutlinedTextField(
                value = otpValue,
                onValueChange = { if (it.length <= 4) otpValue = it },
                label = { Text("Enter 4-Digit OTP") },
                placeholder = { Text("Hint: 1234") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF006D3B),
                    focusedLabelColor = Color(0xFF006D3B)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ACTION BUTTON
        Button(
            onClick = {
                if (!isOtpSent) {
                    if (viewModel.userName.isNotBlank() && viewModel.userPhone.length >= 10) {
                        isOtpSent = true
                    }
                } else {
                    if (otpValue == "1234") {
                        onLoginSuccess()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D3B))
        ) {
            Text(
                text = if (!isOtpSent) "Send OTP" else "Verify & Login",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (isOtpSent) {
            TextButton(onClick = { isOtpSent = false }) {
                Text("Edit Details", color = Color.Gray)
            }
        }
    }
}