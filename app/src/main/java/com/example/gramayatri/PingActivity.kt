package com.example.gramayatri

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class PingActivity : AppCompatActivity() {

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ping)

        val routeTitle = findViewById<TextView>(R.id.routeTitle)
        val latestPing = findViewById<TextView>(R.id.latestPing)
        val etaText = findViewById<TextView>(R.id.etaText)

        val stopInput = findViewById<EditText>(R.id.stopInput)
        val pingBtn = findViewById<Button>(R.id.pingBtn)

        val routeName = intent.getStringExtra("routeName") ?: "Unknown Route"

        routeTitle.text = routeName

        database = FirebaseDatabase.getInstance().reference

        val routeRef = database.child("routes").child(routeName)

        routeRef.child("latestPing")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        val stop = snapshot.child("stop").value.toString()
                        val message = snapshot.child("message").value.toString()

                        latestPing.text =
                            "Latest Ping:\n$message at $stop"

                        etaText.text =
                            "ETA:\nBus may arrive next stop in 10-15 mins"

                    } else {

                        latestPing.text =
                            "No live ping yet"

                        etaText.text =
                            "Using route schedule timing"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        pingBtn.setOnClickListener {

            val stopName = stopInput.text.toString()

            val pingData = HashMap<String, String>()

            pingData["stop"] = stopName
            pingData["message"] = "Bus just passed"

            routeRef.child("latestPing")
                .setValue(pingData)

            Toast.makeText(
                this,
                "Ping Uploaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}