package com.example.gramayatri

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ping)

        val routeName = intent.getStringExtra("routeName") ?: "Route"
        val routeTitle = findViewById<TextView>(R.id.routeTitle)
        routeTitle.text = "Ping: $routeName"

        findViewById<android.widget.ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        // Populate stop spinner with stops for this specific route
        val stops = RouteDataHelper.ALL_ROUTES.find { it.first == routeName }?.second ?: listOf()
        val stopNames = stops.map { it.name }
        val stopSpinner = findViewById<Spinner>(R.id.stopSpinner)
        stopSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stopNames)

        val latestPingText = findViewById<TextView>(R.id.latestPing)
        val pingBtn = findViewById<Button>(R.id.pingBtn)

        val database = FirebaseDatabase.getInstance().reference
        val routeRef = database.child("routes").child(routeName)

        // Show current live ping
        routeRef.child("latestPing")
            .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                    val ping = if (snapshot.exists()) snapshot.getValue(Ping::class.java) else null
                    latestPingText.text = ping?.let { "Bus at: ${it.stopName}" } ?: "No ping yet"
                }
                override fun onCancelled(error: com.google.firebase.database.DatabaseError) {}
            })

        pingBtn.setOnClickListener {
            val selectedStop = stopSpinner.selectedItem?.toString() ?: return@setOnClickListener
            val user = FirebaseAuth.getInstance().currentUser

            val ping = Ping(
                stopName = selectedStop,
                message = "Bus just passed",
                timestamp = System.currentTimeMillis(),
                reportedBy = user?.email ?: "Anonymous",
                userId = user?.uid ?: ""
            )

            routeRef.child("latestPing").setValue(ping).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "✅ Ping sent! Thanks for helping.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to send ping", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}