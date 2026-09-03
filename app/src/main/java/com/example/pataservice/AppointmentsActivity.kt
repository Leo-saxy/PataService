package com.example.pataservice

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointments)

        val container = findViewById<LinearLayout>(R.id.appointmentsContainer)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance().collection("appointments")
            .whereEqualTo("customerId", userId)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val service = document.getString("serviceName") ?: "Service"
                    val provider = document.getString("providerName") ?: "Provider"
                    val date = document.getString("date") ?: ""
                    val time = document.getString("time") ?: ""
                    val status = document.getString("status") ?: ""

                    val appointmentView = TextView(this)
                    appointmentView.text = """
                        Service: $service
                        Provider: $provider
                        Date: $date
                        Time: $time
                        Status: $status
                        
                        __________________________
                    """.trimIndent()

                    appointmentView.textSize = 18f
                    appointmentView.setPadding(0, 15, 0, 15)
                    container.addView(appointmentView)
                }
            }
    }
}
