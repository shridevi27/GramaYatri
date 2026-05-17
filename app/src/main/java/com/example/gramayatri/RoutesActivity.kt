package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RoutesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)

        findViewById<ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        val recyclerView = findViewById<RecyclerView>(R.id.routeRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = RouteAdapter(RouteDataHelper.getRouteNames()) { routeName ->
            val intent = Intent(this, RouteDetailsActivity::class.java)
            intent.putExtra("route", routeName)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }
}