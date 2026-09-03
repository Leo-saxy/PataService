package com.example.pataservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProviderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)

        val addService = findViewById<Button>(R.id.btnAddService)
        val appointments = findViewById<Button>(R.id.btnProviderAppointments)
        val logout = findViewById<Button>(R.id.btnProviderLogout)

        addService.setOnClickListener {
            startActivity(Intent(this, AddServiceActivity::class.java))
        }

        appointments.setOnClickListener {
            startActivity(Intent(this, ProviderAppointmentsActivity::class.java))
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
