package com.example.minigrocerydeliveryapp.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int,
    val unit: String
)