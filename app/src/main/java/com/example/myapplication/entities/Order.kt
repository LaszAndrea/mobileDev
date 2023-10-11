package com.example.myapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "orderTable")
class Order (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "cartItems")
    var cartItems: List<CartItem>,

    @ColumnInfo(name = "user")
    var user: User

) : Serializable