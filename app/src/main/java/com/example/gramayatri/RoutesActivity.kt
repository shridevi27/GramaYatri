package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class RoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        val listView = findViewById<ListView>(R.id.routesList)

        val routes = listOf(

            "Mysore Route (10:30 AM - 2:30 PM)",
            "Kengeri Route (9:00 AM - 1:00 PM)",
            "Mandya Route (11:00 AM - 4:00 PM)",
            "Ramanagara Route (8:30 AM - 12:30 PM)",
            "Tumkur Route (7:30 AM - 11:30 AM)",
            "Hassan Route (1:00 PM - 6:00 PM)",
            "Bidadi Route (9:30 AM - 3:00 PM)",
            "Nelamangala Route (10:00 AM - 5:00 PM)",
            "Channapatna Route (6:30 AM - 10:30 AM)",
            "Kanakapura Route (12:00 PM - 5:30 PM)"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            routes
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->

            val selectedRoute = routes[position]

            val intent = Intent(this, PingActivity::class.java)
            intent.putExtra("routeName", selectedRoute)

            startActivity(intent)
        }
    }
}