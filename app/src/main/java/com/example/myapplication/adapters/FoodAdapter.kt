package com.example.myapplication.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dao.CartDao
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.entities.CartItem
import com.example.myapplication.entities.Food
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.launch

class FoodAdapter : RecyclerView.Adapter<FoodAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    var ctx: Context? = null
    var arrFood = ArrayList<Food>()

    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<Food>){
        arrFood = arrData as ArrayList<Food>
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardelement_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return arrFood.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.foodName)
        val dishDesc = holder.itemView.findViewById<TextView>(R.id.foodDescription)
        val dishPrice = holder.itemView.findViewById<TextView>(R.id.foodPrice)
        val plusButton = holder.itemView.findViewById<Button>(R.id.plusButton)

        dishNameTextView.text = arrFood[position].foodName
        dishDesc.text = arrFood[position].description
        dishPrice.text = "Price: " + arrFood[position].price.toString() + "$"

        val imageView = holder.itemView.findViewById<ImageView>(R.id.foodPicture)
        val imageUrl = arrFood[position].picture
        Picasso.get().load(imageUrl).into(imageView)

        val sharedPreferences = holder.itemView.context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("loggedInUser", "")

        if (savedUsername.isNullOrEmpty()) {
            plusButton.visibility = View.INVISIBLE
        } else {
            plusButton.visibility = View.VISIBLE

            plusButton.setOnClickListener { view ->

                val food = arrFood[position]
                val cartDao = FoodDatabase.getDatabase(view.context).getCartDao()

                addToCart(food, cartDao)

                Toast.makeText(
                    view.context,
                    "Added to cart!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    fun addToCart(food: Food, cartDao: CartDao) {

        CoroutineScope(Dispatchers.IO).launch {
            val existingCartItem = cartDao.checkIfAdded(food)

            if (existingCartItem != null) {
                existingCartItem.quantity++
                cartDao.updateQuantity(existingCartItem)
            } else {
                val newCartItem = CartItem(0, food, 1)
                cartDao.insertCartItem(newCartItem)
            }

        }

    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String)
    }

}