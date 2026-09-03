package com.example.pataservice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pataservice.adapters.ServiceAdapter
import com.example.pataservice.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val services = mutableListOf<Service>()
    private lateinit var adapter: ServiceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val recycler = findViewById<RecyclerView>(R.id.recyclerServices)
        val welcome = findViewById<TextView>(R.id.tvWelcome)
        val appointments = findViewById<Button>(R.id.btnMyAppointments)
        val logout = findViewById<Button>(R.id.btnLogout)

        adapter = ServiceAdapter(this, services)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        loadUserName(welcome)
        loadServices()

        appointments.setOnClickListener {
            startActivity(Intent(this, AppointmentsActivity::class.java))
        }

        logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun loadUserName(welcome: TextView) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener {
                val name = it.getString("name") ?: "Customer"
                welcome.text = "Hello, $name"
            }
    }

    private fun loadServices() {
        db.collection("services")
            .get()
            .addOnSuccessListener { result ->
                services.clear()
                for (document in result) {
                    val service = document.toObject(Service::class.java)
                    service.id = document.id
                    services.add(service)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Unable to load services", Toast.LENGTH_SHORT).show()
            }
    }
}
