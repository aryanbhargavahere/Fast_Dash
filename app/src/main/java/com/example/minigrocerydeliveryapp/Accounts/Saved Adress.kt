package com.example.minigrocerydeliveryapp.Accounts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
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
fun SavedAddressScreen(viewModel: GroceryViewModel, onBack: () -> Unit) {
    val addresses by viewModel.savedAddresses.collectAsState()
    val isDark = viewModel.isDarkMode
    var newAddressInput by remember { mutableStateOf("") }

    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delivery Addresses", fontWeight = FontWeight.Bold, color = textColor) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = textColor) } }
            )
        },
        containerColor = if (isDark) Color(0xFF121212) else Color(0xFFF7F8FA)
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Add New Address", fontWeight = FontWeight.Bold, color = textColor)
            OutlinedTextField(
                value = newAddressInput,
                onValueChange = { newAddressInput = it },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                placeholder = { Text("House No, Street, Landmark...") },
                shape = RoundedCornerShape(12.dp)
            )
            Button(
                onClick = { if(newAddressInput.isNotBlank()){ viewModel.addAddress(newAddressInput); newAddressInput = "" } },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006D3B))
            ) { Text("Save & Use") }

            Spacer(Modifier.height(24.dp))
            Text("Saved Locations", fontWeight = FontWeight.Bold, color = textColor)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
                items(addresses) { address ->
                    AddressItemCard(address, address == viewModel.userAddress, isDark) {
                        viewModel.selectAddress(address); onBack()
                    }
                }
            }
        }
    }
}

@Composable
fun AddressItemCard(address: String, isSelected: Boolean, isDark: Boolean, onSelect: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF006D3B)) else null
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Home, null, tint = if (isSelected) Color(0xFF006D3B) else Color.Gray)
            Text(address, Modifier.padding(start = 12.dp).weight(1f), color = if (isDark) Color.White else Color.Black, fontSize = 14.sp)
            if (isSelected) Text("Selected", color = Color(0xFF006D3B), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}