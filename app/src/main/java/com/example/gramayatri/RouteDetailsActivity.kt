package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RouteDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        val title = findViewById<TextView>(R.id.routeTitle)
        val etaText = findViewById<TextView>(R.id.etaText)
        val pingText = findViewById<TextView>(R.id.pingText)
        val addPingBtn = findViewById<Button>(R.id.addPingBtn)

        val routeName = intent.getStringExtra("route")

        title.text = routeName

        pingText.text = "Latest Ping: Bus passed Hanuman Temple"

        etaText.text = """
ETA:
Market Stop - 5 mins
Temple Stop - 10 mins
Bus Stand - 15 mins
School Stop - 20 mins
""".trimIndent()

        addPingBtn.setOnClickListener {
            startActivity(Intent(this, PingActivity::class.java))
        }
    }
}