package com.example.myapplication.converter

import androidx.room.TypeConverter
import com.example.myapplication.entities.Food
import com.google.gson.Gson

class FoodTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromFood(food: Food): String {
        return gson.toJson(food)
    }

    @TypeConverter
    fun toFood(json: String): Food {
        return gson.fromJson(json, Food::class.java)
    }
}
