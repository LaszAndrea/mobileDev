package com.example.myapplication.database

import com.example.myapplication.converter.FoodTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.converter.CartItemTypeConverter
import com.example.myapplication.converter.UserTypeConverter
import com.example.myapplication.dao.CartDao
import com.example.myapplication.dao.FoodDao
import com.example.myapplication.dao.OrderDao
import com.example.myapplication.dao.UserDao
import com.example.myapplication.entities.CartItem
import com.example.myapplication.entities.Food
import com.example.myapplication.entities.Order
import com.example.myapplication.entities.User

@Database(entities = [Food::class, User::class, CartItem::class, Order::class], version = 7,exportSchema = false)
@TypeConverters(FoodTypeConverter::class, CartItemTypeConverter::class, UserTypeConverter::class)
abstract class FoodDatabase: RoomDatabase() {

    companion object{

        private var foodDatabase:FoodDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): FoodDatabase{
            if (foodDatabase == null){
                foodDatabase = Room.databaseBuilder(
                    context,
                    FoodDatabase::class.java,
                    "foods.db"
                ).fallbackToDestructiveMigration().build()
            }
            return foodDatabase!!
        }
    }

    abstract fun getFoodDao():FoodDao
    abstract fun getUserDao():UserDao
    abstract fun getCartDao(): CartDao
    abstract fun getOrderDao(): OrderDao

}