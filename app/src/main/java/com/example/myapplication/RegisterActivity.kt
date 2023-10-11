package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.myapplication.dao.UserDao
import com.example.myapplication.database.FoodDatabase
import com.example.myapplication.entities.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var passwordAgain: EditText
    private lateinit var fullName: EditText
    private lateinit var phoneNumber: EditText

    private lateinit var goToLogin: LinearLayout

    private var isAllowed: Boolean = false

    private lateinit var myDb : FoodDatabase
    private lateinit var userDao:UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        myDb = Room.databaseBuilder(this, FoodDatabase::class.java, "myApp2")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        userDao = myDb.getUserDao()

        btnRegister = findViewById(R.id.registerButton)
        fullName = findViewById(R.id.fullName)
        username = findViewById(R.id.username)
        phoneNumber = findViewById(R.id.phoneNumber)
        password = findViewById(R.id.password)
        passwordAgain = findViewById(R.id.passwordAgain)
        goToLogin = findViewById(R.id.goToLogin)

        registerUser()

        goToLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    private fun registerUser() {
        btnRegister.setOnClickListener(View.OnClickListener {

            validateEmptyForm()
            if (isAllowed) {
                val newUser = User(0, fullName.text.toString(), "user", username.text.toString(), "",  password.text.toString(), phoneNumber.text.toString())

                Thread {
                    userDao.insertUser(newUser)

                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Register Successful!",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.start()
            }

        })
    }

    private fun validateEmptyForm(){

        isAllowed = false

        when {
                TextUtils.isEmpty(fullName.text.toString().trim()) -> {
                    fullName.error = "Please enter name!"
                }

                TextUtils.isEmpty(username.text.toString().trim()) -> {
                    username.error = "Please enter username!"
                }

                TextUtils.isEmpty(phoneNumber.text.toString().trim()) -> {
                    phoneNumber.error = "Please enter phone number!"
                }

                TextUtils.isEmpty(password.text.toString().trim()) -> {
                    password.error = "Please enter password!"
                }

                TextUtils.isEmpty(passwordAgain.text.toString().trim()) -> {
                    passwordAgain.error = "Please repeat password!"
                }

                fullName.text.toString().isNotEmpty() && username.text.isNotEmpty() && phoneNumber.text.toString().isNotEmpty()
                        && password.text.isNotEmpty() && passwordAgain.text.isNotEmpty() -> {

                    if (username.text.toString()
                            .matches(Regex("[a-zA-Z0-9. -]+@[a-z]+\\.+[a-z]+"))
                    ) {
                        if (!(userDao.is_taken(username.text.toString()))) {
                            if(phoneNumber.text.toString().matches(Regex("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}\$"))) {
                                if (password.text.toString().length >= 5) {
                                    if (password.text.toString() == passwordAgain.text.toString()) {

                                        isAllowed = true

                                    } else {
                                        passwordAgain.error = "Passwords don't match!"
                                    }
                                } else {
                                    password.error = "Password must be at least 5 characters long!"
                                }
                            }else{
                                phoneNumber.error = "Phone number must be valid!"
                            }
                        } else {
                            username.error = "This email has already been registered!"
                        }
                    } else {
                        username.error = "Please enter valid email address!"
                    }

                }

            }
        }

}
