package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class RouteDetailsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: TimelineAdapter
    private lateinit var pingText: TextView
    private var currentStops: List<Stop> = listOf()
    private var latestPing: Ping? = null

    private val handler = Handler(Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            adapter.updateData(currentStops, latestPing, System.currentTimeMillis())
            handler.postDelayed(this, 60_000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        val routeName = intent.getStringExtra("route") ?: "Route"
        val title = findViewById<TextView>(R.id.routeTitle)
        pingText = findViewById(R.id.pingText)
        val addPingBtn = findViewById<Button>(R.id.addPingBtn)
        val recyclerView = findViewById<RecyclerView>(R.id.timelineRecyclerView)

        title.text = routeName
        findViewById<ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        // Load stops from RouteDataHelper (guaranteed to have them)
        currentStops = RouteDataHelper.ALL_ROUTES
            .find { it.first == routeName }?.second ?: listOf()

        adapter = TimelineAdapter(currentStops, null, System.currentTimeMillis())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference
        val routeRef = database.child("routes").child(routeName)

        // Listen for live ping updates
        routeRef.child("latestPing").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                latestPing = if (snapshot.exists()) snapshot.getValue(Ping::class.java) else null
                val pingMsg = latestPing?.let { "📡 ${it.stopName} — ${it.message}" } ?: "No live ping yet"
                pingText.text = pingMsg
                adapter.updateData(currentStops, latestPing, System.currentTimeMillis())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RouteDetailsActivity, "Failed to load ping", Toast.LENGTH_SHORT).show()
            }
        })

        addPingBtn.setOnClickListener {
            val intent = Intent(this, PingActivity::class.java)
            intent.putExtra("routeName", routeName)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }
}