package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.CartItem
import com.example.myapplication.entities.Food

@Dao
interface CartDao {

    @Query("SELECT * FROM cartItem WHERE foodAdded=:food")
    fun checkIfAdded(food: Food): CartItem

    @Insert
    fun insertCartItem(cartItem: CartItem)

    @Update
    fun updateQuantity(cartItem: CartItem)

    @Query("SELECT COUNT(*) FROM cartItem")
    fun getCartItemCount(): Int

    @Query("SELECT * FROM cartItem")
    fun getAllCartItems(): List<CartItem>

    @Delete
    fun deleteItem(cartItem: CartItem)

    @Query("SELECT * FROM cartItem where id=:id")
    fun getCartItem(id: Int?): CartItem

}