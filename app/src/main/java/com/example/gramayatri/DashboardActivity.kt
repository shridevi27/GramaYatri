package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        findViewById<Button>(R.id.routesBtn).setOnClickListener {
            startActivity(Intent(this, RoutesActivity::class.java))
        }

        findViewById<Button>(R.id.pingBtn).setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
        }
    }
}