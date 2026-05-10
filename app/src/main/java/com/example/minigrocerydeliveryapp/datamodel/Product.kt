package com.example.minigrocerydeliveryapp.datamodel

import com.example.minigrocerydeliveryapp.Room.CartItem

data class Product(
    val id: Int,
    val name: String,
    val shop: String,
    val price: Double,
    val imageRes: Int,
    val unit: String
) {
    fun toCartItem() = CartItem(
        id = id,
        name = name,
        price = price,
        quantity = 1,
        imageRes = imageRes,
        unit = unit
    )
}