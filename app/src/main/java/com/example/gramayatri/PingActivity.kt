package com.example.gramayatri

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class PingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ping)

        val submitBtn = findViewById<Button>(R.id.submitPingBtn)

        submitBtn.setOnClickListener {
            Toast.makeText(
                this,
                "Bus Ping Added",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}