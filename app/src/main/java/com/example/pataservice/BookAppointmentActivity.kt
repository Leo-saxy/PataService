package com.example.pataservice

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class BookAppointmentActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var selectedDate = ""
    private var selectedTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val serviceId = intent.getStringExtra("serviceId") ?: ""
        val providerId = intent.getStringExtra("providerId") ?: ""
        val providerName = intent.getStringExtra("providerName") ?: ""
        val serviceName = intent.getStringExtra("serviceName") ?: ""
        val price = intent.getDoubleExtra("price", 0.0)

        val serviceText = findViewById<TextView>(R.id.tvBookingService)
        val providerText = findViewById<TextView>(R.id.tvBookingProvider)
        val priceText = findViewById<TextView>(R.id.tvBookingPrice)
        val dateButton = findViewById<Button>(R.id.btnSelectDate)
        val timeButton = findViewById<Button>(R.id.btnSelectTime)
        val confirmButton = findViewById<Button>(R.id.btnConfirmBooking)

        serviceText.text = serviceName
        providerText.text = "Provider: $providerName"
        priceText.text = "KES $price"

        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                dateButton.text = selectedDate
            }, year, month, day).show()
        }

        timeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeButton.text = selectedTime
            }, hour, minute, false).show()
        }

        confirmButton.setOnClickListener {
            if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
                Toast.makeText(this, "Select a date and time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val customerId = auth.currentUser?.uid ?: return@setOnClickListener

            db.collection("users").document(customerId).get().addOnSuccessListener {
                val customerName = it.getString("name") ?: "Customer"
                val appointment = hashMapOf(
                    "customerId" to customerId,
                    "customerName" to customerName,
                    "providerId" to providerId,
                    "providerName" to providerName,
                    "serviceId" to serviceId,
                    "serviceName" to serviceName,
                    "date" to selectedDate,
                    "time" to selectedTime,
                    "status" to "Pending"
                )
                db.collection("appointments").add(appointment).addOnSuccessListener {
                    Toast.makeText(this, "Appointment booked", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
