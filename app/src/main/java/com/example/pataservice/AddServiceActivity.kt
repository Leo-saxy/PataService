package com.example.pataservice

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddServiceActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val serviceName = findViewById<EditText>(R.id.etServiceName)
        val category = findViewById<EditText>(R.id.etCategory)
        val description = findViewById<EditText>(R.id.etDescription)
        val price = findViewById<EditText>(R.id.etPrice)
        val saveButton = findViewById<Button>(R.id.btnSaveService)

        saveButton.setOnClickListener {
            val nameText = serviceName.text.toString().trim()
            val categoryText = category.text.toString().trim()
            val descriptionText = description.text.toString().trim()
            val priceText = price.text.toString().trim()

            if (nameText.isEmpty() || categoryText.isEmpty() || descriptionText.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priceNumber = priceText.toDoubleOrNull()
            if (priceNumber == null) {
                Toast.makeText(this, "Enter valid price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val providerId = auth.currentUser?.uid ?: return@setOnClickListener
            db.collection("users")
                .document(providerId)
                .get()
                .addOnSuccessListener { userDocument ->
                    val providerName = userDocument.getString("name") ?: "Provider"

                    val service = hashMapOf(
                        "providerId" to providerId,
                        "providerName" to providerName,
                        "serviceName" to nameText,
                        "category" to categoryText,
                        "description" to descriptionText,
                        "price" to priceNumber
                    )

                    db.collection("services")
                        .add(service)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                        }
                }
        }
    }
}
