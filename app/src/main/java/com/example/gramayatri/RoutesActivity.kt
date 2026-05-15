package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        val listView = findViewById<ListView>(R.id.routesList)

        val routes = arrayOf(
            "Route 1 - Udupi to Karkala",
            "Route 2 - Karkala to Hebri",
            "Route 3 - Hebri to Agumbe",
            "Route 4 - Udupi to Brahmavar",
            "Route 5 - Kundapura to Byndoor",
            "Route 6 - Karkala to Belman",
            "Route 7 - Udupi to Kaup",
            "Route 8 - Kaup to Padubidri",
            "Route 9 - Udupi to Manipal",
            "Route 10 - Manipal to Hebri"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            routes
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->

            val intent = Intent(this, RouteDetailsActivity::class.java)

            intent.putExtra("route", routes[position])

            startActivity(intent)
        }
    }
}