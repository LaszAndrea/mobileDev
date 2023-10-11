package com.example.myapplication.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CartActivity
import com.example.myapplication.CartItemClickListener
import com.example.myapplication.R
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.entities.CartItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(private val myListener: CartItemClickListener) : RecyclerView.Adapter<CartAdapter.FoodHolder>()  {

    var listener: FoodAdapter.OnItemClickListener? = null
    var ctx: Context? = null
    var arrCartItems = ArrayList<CartItem>()

    class FoodHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<CartItem>){
        arrCartItems = arrData as ArrayList<CartItem>
    }

    fun setClickListener(listener1: FoodAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        ctx = parent.context
        return FoodHolder(LayoutInflater.from(parent.context).inflate(R.layout.checkout_card_element_layout,parent,false))
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

        //megjelenítési elemek beállítása
        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.foodName)
        val dishPrice = holder.itemView.findViewById<TextView>(R.id.foodPrice)
        val dishQuantity = holder.itemView.findViewById<TextView>(R.id.quantityText)

        dishNameTextView.text = arrCartItems[position].foodAdded.foodName
        dishPrice.text = "Price: " + arrCartItems[position].foodAdded.price.toString() + "$"
        dishQuantity.text = arrCartItems[position].quantity.toString()

        //URL kép beállítása
        val imageView = holder.itemView.findViewById<ImageView>(R.id.foodPicture)
        val imageUrl = arrCartItems[position].foodAdded.picture
        Picasso.get().load(imageUrl).into(imageView)

        //Gombok lekérdezése
        val minusBtn = holder.itemView.findViewById<Button>(R.id.minusQuantity)
        val plusBtn = holder.itemView.findViewById<Button>(R.id.plusQuantity)
        val deleteItem = holder.itemView.findViewById<Button>(R.id.deleteItem)

        //Törlés gomb beállítása
        deleteItem.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = FoodDatabase.getDatabase(view.context).getCartDao()
                cartDao.deleteItem(arrCartItems[position])

                (view.context as? CartActivity)?.runOnUiThread {
                    myListener.onItemDelete(arrCartItems[position])
                    arrCartItems.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

        //Mennyiség csökkentése gomb beállítása
        minusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = FoodDatabase.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCartItems[position].id)

                cartItem.quantity--
                cartDao.updateQuantity(cartItem)
                arrCartItems[position].quantity--

                (view.context as? CartActivity)?.runOnUiThread {
                    if(cartItem.quantity<=0){
                        CoroutineScope(Dispatchers.IO).launch {

                            cartDao.deleteItem(arrCartItems[position])

                            (view.context as? CartActivity)?.runOnUiThread {
                                myListener.onItemQuantityMinus(cartItem.foodAdded.price)
                                arrCartItems.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                    }else{
                        dishQuantity.text = cartItem.quantity.toString()
                        myListener.onItemQuantityMinus(cartItem.foodAdded.price)
                    }
                }
            }
        }

        //Mennyiség növelése gomb beállítása
        plusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = FoodDatabase.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCartItems[position].id)

                cartItem.quantity++
                cartDao.updateQuantity(cartItem)
                arrCartItems[position].quantity++

                (view.context as? CartActivity)?.runOnUiThread {
                    dishQuantity.text = cartItem.quantity.toString()
                    myListener.onItemQuantityPlus(cartItem.foodAdded.price)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return arrCartItems.size
    }

    fun clear() {
        arrCartItems.clear()
        notifyDataSetChanged()
    }

}