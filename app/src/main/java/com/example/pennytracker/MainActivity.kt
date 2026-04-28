package com.example.pennytracker

import data.database.AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //global declarations

    private lateinit var btnLoginTab : Button
    private lateinit var btnRegisterTab : Button
    private lateinit var edtUsername : EditText
    private lateinit var edtPassword : EditText
    private lateinit var btnLoginAcc : Button

    private lateinit var db : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //room database
        db = AppDatabase.getDatabase(this)

        //Typecasting
        btnLoginTab = findViewById(R.id.btnLoginTab)
        btnRegisterTab = findViewById(R.id.btnRegisterTab)
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLoginAcc = findViewById(R.id.btnLoginAcc)

        //methods
        btnLoginTab.setOnClickListener {
            edtUsername.text.clear()
            edtPassword.text.clear()
        }

        btnRegisterTab.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        btnLoginAcc.setOnClickListener {
            loginUser()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loginUser(){
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        //validation checks if empty
        if(username.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please enter username and password fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch{
            //check if the user exists with their matching username and password
            val foundUser = db.userDao().loginUser(username, password)

            runOnUiThread {
                if(foundUser != null){
                    //check if the user is found -> login successful
                    Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()

                    //go to the home screen when successful
                    openHomePage(foundUser.username)

                }else{
                    //if the user is not found then login failed
                    Toast.makeText(this@MainActivity, "Invalid login details. Please try again", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun openHomePage(username: String){
        val intent = Intent(this, Home::class.java)

        startActivity(intent)
        finish()
    }

}