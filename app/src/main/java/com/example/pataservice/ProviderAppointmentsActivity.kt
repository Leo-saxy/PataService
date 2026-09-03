package com.example.pataservice

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProviderAppointmentsActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider_appointments)

        db = FirebaseFirestore.getInstance()

        val container = findViewById<LinearLayout>(R.id.providerAppointmentsContainer)
        val providerId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("appointments")
            .whereEqualTo("providerId", providerId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val customer = document.getString("customerName") ?: "Customer"
                    val service = document.getString("serviceName") ?: "Service"
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    val status = document.getString("status") ?: ""

                    val box = LinearLayout(this)
                    box.orientation = LinearLayout.VERTICAL
                    box.setPadding(10, 20, 10, 20)

                    val information = TextView(this)
                    information.text = """
                        Customer: $customer
                        Service: $service
                        Date: $date
                        Time: $time
                        Status: $status
                    """.trimIndent()
                    information.textSize = 17f

                    val accept = Button(this)
                    accept.text = "Accept"
                    val decline = Button(this)
                    decline.text = "Decline"

                    accept.setOnClickListener {
                        db.collection("appointments").document(document.id)
                            .update("status", "Accepted")
                        information.append("\nStatus updated to Accepted")
                    }

                    decline.setOnClickListener {
                        db.collection("appointments").document(document.id)
                            .update("status", "Declined")
                        information.append("\nStatus updated to Declined")
                    }

                    box.addView(information)
                    box.addView(accept)
                    box.addView(decline)
                    container.addView(box)
                }
            }
    }
}
