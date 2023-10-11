package com.example.myapplication

import com.example.myapplication.entities.CartItem

interface CartItemClickListener {
    fun onItemQuantityPlus(plusPrice: Int)
    fun onItemQuantityMinus(minusPrice: Int)
    fun onItemDelete(cartItem: CartItem)
}
