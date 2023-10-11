package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.FoodAdapter
import com.example.myapplication.dao.UserDao
import com.example.myapplication.entities.Food
import com.example.myapplication.entities.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstScreenActivity : ComponentActivity() , View.OnClickListener {

    var arrFood = ArrayList<Food>()
    var foodAdapter = FoodAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        arrFood.add(Food(1, "Hamburger", 10,"very delicious!", "https://kep.cdn.indexvas.hu/1/0/4336/43365/433656/43365656_939c2e71f2a24220017ee71e6a45dfeb_wm.jpg"))
        arrFood.add(Food(2, "Pizza", 15,"ham and cheese pizza", "https://www.mokuslekvar.hu/wp-content/uploads/2022/04/pizza-9.jpg"))
        arrFood.add(Food(3, "Extra cheesy pizza", 20,"ham and extra cheesy pizza", "https://www.mokuslekvar.hu/wp-content/uploads/2022/04/pizza-4.jpg"))
        foodAdapter.setData(arrFood)

        val recyclerView = findViewById<RecyclerView>(R.id.foodCardElement)
        recyclerView.adapter = foodAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        bottomNavigationHandler()

    }

    override fun onClick(p0: View?) {
        var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun bottomNavigationHandler() {

        val cart = findViewById<FloatingActionButton>(R.id.cartIcon)
        cart.setOnClickListener {
            var intent = Intent(this@FirstScreenActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.profile -> {
                    var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }

}
