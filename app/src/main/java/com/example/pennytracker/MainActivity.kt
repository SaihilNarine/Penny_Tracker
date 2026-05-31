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


class MainActivity : AppCompatActivity() {

    //global declarations
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLoginTab: Button
    private lateinit var btnRegisterTab: Button
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLoginAcc: Button

    //private lateinit var db : AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //initialize firebase auth
        auth = FirebaseAuth.getInstance()

        //room database
        //db = AppDatabase.getDatabase(this)

        //Typecasting
        btnLoginTab = findViewById(R.id.btnLoginTab)
        btnRegisterTab = findViewById(R.id.btnRegisterTab)
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLoginAcc = findViewById(R.id.btnLoginAcc)

        //methods
        btnLoginTab.setOnClickListener {
            edtEmail.text.clear()
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

    private fun loginUser() {
        val email = edtEmail.text.toString().trim()
        val password = edtPassword.text.toString().trim()

        //validation checks if empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    openHomePage()
                } else {
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun openHomePage(){
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
        finish()
    }
}

