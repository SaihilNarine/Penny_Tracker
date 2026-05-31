package com.example.pennytracker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {

    //global declarations
    //these variable are declared globally so we can use them in multiple functions

    private lateinit var edtEmail : EditText
    private lateinit var edtPassword : EditText
    private lateinit var edtConfirmPassword : EditText
    private lateinit var btnRegAcc : Button
    private lateinit var btnExistingAcc : Button
    private lateinit var auth : FirebaseAuth

    //private late in it var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        //Typecasting
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnRegAcc = findViewById(R.id.btnRegAcc)
        btnExistingAcc = findViewById(R.id.btnExistingAcc)

        //initialize firebase
        auth = FirebaseAuth.getInstance()

        //initializing the database
        //db = AppDatabase.getDatabase(this)



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
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val confirmPassword = edtConfirmPassword.text.toString().trim()

        //validation
        //check if the user left fields empty

        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        //check if the password are the same
        if(password != confirmPassword){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        //firebase registration
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful){
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    clearFields()

                    //go back to log in screen
                    openLoginScreen()
                }else{
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openLoginScreen(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() //close the current screen so the user cannot go back

    }

    private fun clearFields(){
        edtEmail.text.clear()
        edtPassword.text.clear()
        edtConfirmPassword.text.clear()
    }

}