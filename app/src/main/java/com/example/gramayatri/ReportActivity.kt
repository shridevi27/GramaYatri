package com.example.gramayatri

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ReportActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var reportAdapter: ReportAdapter
    private val reportList = mutableListOf<Report>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        database = FirebaseDatabase.getInstance().reference
        findViewById<android.widget.ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        // Problem type spinner
        val problemTypes = arrayOf(
            "Bus Delayed", "Bus Was Early", "Bus Breakdown",
            "Heavy Crowd", "Route Changed", "Bus Not Coming", "Other"
        )
        val typeSpinner = findViewById<Spinner>(R.id.problemTypeSpinner)
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, problemTypes)

        // Route spinner
        val routeSpinner = findViewById<Spinner>(R.id.routeSpinner)
        routeSpinner.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            RouteDataHelper.getRouteNames()
        )

        // Stop spinner — updates when route changes
        val stopSpinner = findViewById<Spinner>(R.id.stopSpinner)
        fun updateStops(routeName: String) {
            val stops = RouteDataHelper.ALL_ROUTES.find { it.first == routeName }?.second ?: listOf()
            stopSpinner.adapter = ArrayAdapter(
                this, android.R.layout.simple_spinner_dropdown_item,
                stops.map { it.name }
            )
        }
        updateStops(RouteDataHelper.getRouteNames().firstOrNull() ?: "")

        routeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                updateStops(routeSpinner.getItemAtPosition(pos).toString())
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        val descInput = findViewById<EditText>(R.id.descriptionInput)
        val submitBtn = findViewById<Button>(R.id.submitReportBtn)
        val emptyText = findViewById<TextView>(R.id.emptyReportText)
        val recyclerView = findViewById<RecyclerView>(R.id.reportRecyclerView)

        reportAdapter = ReportAdapter(reportList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reportAdapter

        submitBtn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val report = Report(
                routeName = routeSpinner.selectedItem.toString(),
                stopName = stopSpinner.selectedItem?.toString() ?: "",
                type = typeSpinner.selectedItem.toString(),
                description = descInput.text.toString().trim(),
                timestamp = System.currentTimeMillis(),
                userId = user?.uid ?: "",
                userEmail = user?.email ?: "Anonymous"
            )
            database.child("reports").push().setValue(report).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "✅ Report submitted!", Toast.LENGTH_SHORT).show()
                    descInput.text.clear()
                } else {
                    Toast.makeText(this, "Failed to submit", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Listen for all reports in real-time
        database.child("reports")
            .orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    reportList.clear()
                    for (child in snapshot.children) {
                        val r = child.getValue(Report::class.java)
                        if (r != null) reportList.add(0, r) // newest first
                    }
                    reportAdapter.notifyDataSetChanged()
                    emptyText.visibility = if (reportList.isEmpty()) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (reportList.isEmpty()) View.GONE else View.VISIBLE
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}