package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val routeBtn = findViewById<Button>(R.id.routeBtn)
        val etaBtn = findViewById<Button>(R.id.etaBtn)
        val alertBtn = findViewById<Button>(R.id.alertBtn)

        routeBtn.setOnClickListener {
            startActivity(Intent(this, RoutesActivity::class.java))
        }

        etaBtn.setOnClickListener {
            startActivity(Intent(this, EtaActivity::class.java))
        }

        alertBtn.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
        }
    }
}