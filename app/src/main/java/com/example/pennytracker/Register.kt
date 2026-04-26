package com.example.pennytracker

import Data.database.AppDatabase
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
import com.example.pennytracker.data.User
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {

    //global declarations
    //these variable are declared globally so we can use them in multiple functions

    private lateinit var edtUsername : EditText
    private lateinit var edtPassword : EditText
    private lateinit var edtConfirmPassword : EditText
    private lateinit var btnRegAcc : Button
    private lateinit var btnExistingAcc : Button

    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        //Typecasting
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnRegAcc = findViewById(R.id.btnRegAcc)
        btnExistingAcc = findViewById(R.id.btnExistingAcc)

        //initializing the database
        db = AppDatabase.getDatabase(this)

        addDefaultUser() //function to add default user

        //when the user clicks the register button, it will activate the code within
        btnRegAcc.setOnClickListener {
            registerUser() //calling the function
        }

        //if the user already has an account
        btnExistingAcc.setOnClickListener {
            openLoginScreen() //user will be directed to the login screen
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //function to handle registration logic
    private fun registerUser(){
        val username = edtUsername.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        //validation
        //check if the user left fields empty

        if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        //check if the password are the same
        if(password != confirmPassword){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()

        }

        //database operation

        lifecycleScope.launch{
            //check if user exists in the database
            val existingUser = db.userDao().getUserByUsername(username)

            if(existingUser != null){
                //if user exists, show them a message
                runOnUiThread {
                    Toast.makeText(this@Register, "Username already exists", Toast.LENGTH_SHORT).show()

                }
            }else{
                //if the user does not exist, create a new user object
                val newUser = User(
                    username = username,
                    password = password
                )

                //insert new user into the database
                db.userDao().insertUser(newUser)

                //show success message and move to the login screen
                runOnUiThread {
                    Toast.makeText(this@Register,"Registration successful", Toast.LENGTH_SHORT).show()

                    clearFields()
                    openLoginScreen()
                }
            }
        }

    }

    private fun addDefaultUser(){
        lifecycleScope.launch {
            //check if admin already exists
            val existingUser = db.userDao().getUserByUsername("admin")
            if(existingUser == null){
                //if not, insert the default admin user
                db.userDao().insertUser(
                    User(username = "admin", password = "1234")
                )
            }
        }
    }

    private fun openLoginScreen(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() //close the current screen so the user cannot go back

    }

    private fun clearFields(){
        edtUsername.text.clear()
        edtPassword.text.clear()
        edtConfirmPassword.text.clear()
    }

}