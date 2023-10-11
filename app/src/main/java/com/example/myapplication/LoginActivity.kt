package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplication.dao.UserDao
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.entities.User

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var goToRegister: LinearLayout

    lateinit var myDb : FoodDatabase
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        btnLogin = findViewById(R.id.loginButton)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        goToRegister = findViewById(R.id.goToRegister)

        myDb = Room.databaseBuilder(this, FoodDatabase::class.java, "myApp2")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        userDao = myDb.getUserDao()

        btnLogin.setOnClickListener(View.OnClickListener {

            if(userDao.login(username.text.toString(), password.text.toString())){

                Toast.makeText(
                    this,
                    "Login Successful!",
                    Toast.LENGTH_SHORT
                ).show()

                val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("loggedInUser", username.text.toString())
                editor.apply()

                var intent = Intent(this@LoginActivity, FirstScreenActivity::class.java)
                startActivity(intent)
                finish()

            }else{
                Toast.makeText(
                    this,
                    "Invalid username or password!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        goToRegister.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        })

    }

}
