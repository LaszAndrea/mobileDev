package com.example.myapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "cartItem")
class CartItem (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "foodAdded")
    var foodAdded: Food,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

) : Serializable