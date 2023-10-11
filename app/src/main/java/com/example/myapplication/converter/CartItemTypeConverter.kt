package com.example.myapplication.converter

import androidx.room.TypeConverter
import com.example.myapplication.entities.CartItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartItemTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<CartItem>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(json: String): List<CartItem> {
        val type = object : TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(json, type)
    }
}
