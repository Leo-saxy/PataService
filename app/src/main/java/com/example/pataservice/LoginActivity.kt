package com.example.pataservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val email = findViewById<EditText>(R.id.etLoginEmail)
        val password = findViewById<EditText>(R.id.etLoginPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val registerButton = findViewById<Button>(R.id.btnGoToRegister)

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnSuccessListener {
                    val userId = auth.currentUser!!.uid
                    db.collection("users")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { document ->
                            val role = document.getString("role")
                            if (role == "Customer") {
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            } else if (role == "Service Provider") {
                                startActivity(Intent(this, ProviderActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "User role not found", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, error.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
        }
    }
}
