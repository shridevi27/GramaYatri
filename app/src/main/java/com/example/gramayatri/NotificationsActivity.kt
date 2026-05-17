package com.example.gramayatri

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NotificationsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var notifAdapter: NotificationAdapter
    private val notifList = mutableListOf<BusNotification>()
    private lateinit var prefs: SharedPreferences

    companion object {
        const val CONDUCTOR_PIN = "CONDUCTOR123"
        const val PREF_IS_CONDUCTOR = "is_conductor"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        database = FirebaseDatabase.getInstance().reference
        prefs = getSharedPreferences("grama_yatri_prefs", MODE_PRIVATE)

        val recyclerView = findViewById<RecyclerView>(R.id.notifRecyclerView)
        val conductorPanel = findViewById<View>(R.id.conductorPanel)
        val unlockBtn = findViewById<Button>(R.id.unlockConductorBtn)
        val postBtn = findViewById<Button>(R.id.postNotifBtn)
        val messageInput = findViewById<EditText>(R.id.notifMessageInput)
        val typeSpinner = findViewById<Spinner>(R.id.notifTypeSpinner)
        val routeSpinner = findViewById<Spinner>(R.id.notifRouteSpinner)
        val emptyText = findViewById<TextView>(R.id.emptyText)

        findViewById<android.widget.ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        // Spinners
        val types = arrayOf("Route Cancelled", "Bus Delayed Today", "Service Restored", "Route Changed", "General Info")
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)
        routeSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            RouteDataHelper.getRouteNames()
        )

        // RecyclerView
        notifAdapter = NotificationAdapter(notifList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notifAdapter

        // Conductor panel visibility
        updateConductorUI(conductorPanel, unlockBtn)

        unlockBtn.setOnClickListener { showPinDialog(conductorPanel, unlockBtn) }

        postBtn.setOnClickListener {
            val msg = messageInput.text.toString().trim()
            if (msg.isEmpty()) {
                Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val user = FirebaseAuth.getInstance().currentUser
            val notif = BusNotification(
                routeName = routeSpinner.selectedItem.toString(),
                message = msg,
                type = typeSpinner.selectedItem.toString(),
                timestamp = System.currentTimeMillis(),
                postedBy = user?.displayName ?: "Conductor",
                conductorEmail = user?.email ?: ""
            )
            database.child("notifications").push().setValue(notif)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "✅ Notification posted!", Toast.LENGTH_SHORT).show()
                        messageInput.text.clear()
                    } else {
                        Toast.makeText(this, "Failed to post", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Load all notifications (real-time)
        database.child("notifications")
            .orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    notifList.clear()
                    for (child in snapshot.children) {
                        val n = child.getValue(BusNotification::class.java)
                        if (n != null) notifList.add(0, n) // newest first
                    }
                    notifAdapter.notifyDataSetChanged()
                    emptyText.visibility = if (notifList.isEmpty()) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (notifList.isEmpty()) View.GONE else View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun updateConductorUI(panel: View, unlockBtn: Button) {
        val isConductor = prefs.getBoolean(PREF_IS_CONDUCTOR, false)
        panel.visibility = if (isConductor) View.VISIBLE else View.GONE
        unlockBtn.text = if (isConductor) "✅ Conductor Mode Active" else "🔑 I am a Conductor"
    }

    private fun showPinDialog(panel: View, unlockBtn: Button) {
        if (prefs.getBoolean(PREF_IS_CONDUCTOR, false)) {
            // Already unlocked — offer to lock
            AlertDialog.Builder(this)
                .setTitle("Conductor Mode")
                .setMessage("Conductor mode is active. Deactivate?")
                .setPositiveButton("Deactivate") { _, _ ->
                    prefs.edit().putBoolean(PREF_IS_CONDUCTOR, false).apply()
                    updateConductorUI(panel, unlockBtn)
                }
                .setNegativeButton("Cancel", null)
                .show()
            return
        }

        val input = EditText(this).apply {
            hint = "Enter conductor PIN"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        AlertDialog.Builder(this)
            .setTitle("🔑 Conductor Verification")
            .setMessage("Enter the conductor PIN to unlock posting:")
            .setView(input)
            .setPositiveButton("Verify") { _, _ ->
                val entered = input.text.toString()
                if (entered == CONDUCTOR_PIN) {
                    prefs.edit().putBoolean(PREF_IS_CONDUCTOR, true).apply()
                    updateConductorUI(panel, unlockBtn)
                    Toast.makeText(this, "✅ Conductor mode unlocked!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "❌ Incorrect PIN", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
