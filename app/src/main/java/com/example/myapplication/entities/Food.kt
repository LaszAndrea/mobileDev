package com.example.myapplication.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "food")
class Food (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "foodName")
    var foodName: String,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "pictureUrl")
    var picture: String

) : Serializable