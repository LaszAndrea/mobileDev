package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myapplication.entities.CartItem
import com.example.myapplication.entities.Food
import com.example.myapplication.entities.Order
import com.example.myapplication.entities.User

@Dao
interface OrderDao {

   @Insert
   fun insertOrder(order: Order)

   @Query("SELECT COUNT(*) FROM orderTable where user=:user")
   fun getOrdersCount(user: User): Int

}