package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entities.Food

@Dao
interface FoodDao {

    @Query("SELECT * FROM food ORDER BY id DESC")
    fun getAllFoods(): List<Food>

}