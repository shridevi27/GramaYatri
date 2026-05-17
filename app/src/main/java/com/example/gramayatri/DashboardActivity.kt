package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val user = FirebaseAuth.getInstance().currentUser
        val welcomeText = findViewById<TextView>(R.id.welcomeText)
        welcomeText.text = "Welcome, ${user?.email?.substringBefore('@') ?: "Traveller"}!"

        // Seed all routes to Firebase on first open
        RouteSeeder.seedIfNeeded()

        findViewById<CardView>(R.id.cardRoutesPings).setOnClickListener {
            startActivity(Intent(this, RoutesActivity::class.java))
        }

        findViewById<CardView>(R.id.cardNearMyStop).setOnClickListener {
            startActivity(Intent(this, NearMyStopActivity::class.java))
        }

        findViewById<CardView>(R.id.cardReports).setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }

        findViewById<ImageButton>(R.id.notificationBtn).setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        findViewById<ImageButton>(R.id.logoutBtn).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Logout") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}