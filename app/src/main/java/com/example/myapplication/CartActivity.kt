package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.myapplication.adapters.CartAdapter
import com.example.myapplication.dao.OrderDao
import com.example.myapplication.dao.UserDao
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.entities.CartItem
import com.example.myapplication.entities.Order
import com.example.myapplication.entities.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartActivity : ComponentActivity() , View.OnClickListener, CartItemClickListener {

    private lateinit var myDb : FoodDatabase
    private lateinit var userDao: UserDao
    private lateinit var loggedInUser: User

    private lateinit var checkoutBtn: Button
    private lateinit var orderDao: OrderDao

    private var totalSum: Int = 0
    private var totalQuantity: Int = 0

    var arrCartItems = ArrayList<CartItem>()
    var cartAdapter = CartAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkout_layout)

        myDb = Room.databaseBuilder(this, FoodDatabase::class.java, "myApp2")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        orderDao = myDb.getOrderDao()

        checkoutBtn = findViewById(R.id.checkoutBtn)
        checkoutBtn.setOnClickListener(this)

        getDataFromDb()

        val recyclerView = findViewById<RecyclerView>(R.id.checkoutCardElement)
        recyclerView.adapter = cartAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setBottomSum()
        bottomNavigationHandler()

    }

    override fun onClick(p0: View?) {

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("loggedInUser", "")
        userDao = myDb.getUserDao()

        if(!(savedUsername.isNullOrEmpty())) {
            loggedInUser = userDao.getLoggedInUser(savedUsername)
            if(loggedInUser.address.isNullOrEmpty()){
                Toast.makeText(
                    this,
                    "Have to add address in profile to place an order!",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                orderDao.insertOrder(Order(0, arrCartItems, loggedInUser))
                Toast.makeText(
                    this,
                    "Successful order!",
                    Toast.LENGTH_SHORT
                ).show()
                cartAdapter.clear()
                var bottomSum = findViewById<ConstraintLayout>(R.id.summedOrder)
                bottomSum.visibility = View.INVISIBLE
                var intent = Intent(this@CartActivity, FirstScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }

    private fun getDataFromDb(){
        CoroutineScope(Dispatchers.IO).launch {
            this.let {
                var cat = FoodDatabase.getDatabase(this@CartActivity).getCartDao().getAllCartItems()
                arrCartItems = cat as ArrayList<CartItem>
                cartAdapter.setData(arrCartItems)
            }
        }
    }

    override fun onItemQuantityMinus(minusPrice: Int) {
        totalSum -= minusPrice
        totalQuantity--
        updateTotalSumAndQuantity()
    }

    override fun onItemQuantityPlus(plusPrice: Int) {
        totalSum += plusPrice
        totalQuantity++
        updateTotalSumAndQuantity()
    }

    private fun updateTotalSumAndQuantity() {
        runOnUiThread {
            val sumPrice = findViewById<TextView>(R.id.totalPrice)
            val sumQuantity = findViewById<TextView>(R.id.totalQuantity)

            sumPrice.text = "Total price: " + totalSum.toString() + "$"
            sumQuantity.text = "Total quantity: " + totalQuantity.toString()

            if(totalSum == 0){
                var bottomSum = findViewById<ConstraintLayout>(R.id.summedOrder)
                bottomSum.visibility = View.INVISIBLE
            }

        }
    }

    override fun onItemDelete(cartItem: CartItem){

        totalSum -= cartItem.quantity * cartItem.foodAdded.price
        totalQuantity -= cartItem.quantity
        updateTotalSumAndQuantity()

    }


    private fun setBottomSum(){
            CoroutineScope(Dispatchers.IO).launch {
                this.let {
                    var cat =
                        FoodDatabase.getDatabase(this@CartActivity).getCartDao().getAllCartItems()
                    arrCartItems = cat as ArrayList<CartItem>

                    //Összegzés megjelenítése csak abban az esetben, hogy ha van elem a kosárban
                    val summedOrder = findViewById<ConstraintLayout>(R.id.summedOrder)
                    val sumPrice = findViewById<TextView>(R.id.totalPrice)
                    val sumQuantity = findViewById<TextView>(R.id.totalQuantity)

                    if (arrCartItems.size <= 0) {
                        summedOrder.visibility = View.INVISIBLE
                    } else {
                        summedOrder.visibility = View.VISIBLE

                        var sum: Int = 0
                        var quantity: Int = 0
                        for (cartItem: CartItem in arrCartItems) {

                            if (cartItem.quantity == 1) {
                                sum += cartItem.foodAdded.price
                                quantity++
                            } else if (cartItem.quantity > 1) {
                                sum += cartItem.foodAdded.price * cartItem.quantity
                                quantity += cartItem.quantity
                            }
                        }

                        runOnUiThread {

                            sumPrice.text = "Total price: " + sum.toString() + "$"
                            sumQuantity.text = "Total Quantity: " + quantity.toString()
                            totalSum = sum
                            totalQuantity = quantity

                        }
                }
            }
        }
    }

    private fun bottomNavigationHandler() {

        val fab = findViewById<FloatingActionButton>(R.id.cartIcon)
        fab.setOnClickListener {
            var intent = Intent(this@CartActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.home -> {
                    var intent = Intent(this@CartActivity, FirstScreenActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.profile -> {
                    var intent = Intent(this@CartActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }
}
