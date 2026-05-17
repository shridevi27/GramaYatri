package com.example.gramayatri

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class NearMyStopActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var busAdapter: NearMyStopBusAdapter
    private val busList = mutableListOf<BusAtStop>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_my_stop)

        database = FirebaseDatabase.getInstance().reference
        findViewById<android.widget.ImageButton>(R.id.backBtn).setOnClickListener { finish() }

        val stopSpinner = findViewById<Spinner>(R.id.stopSpinner)
        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val busRecycler = findViewById<RecyclerView>(R.id.busRecyclerView)
        val emptyText = findViewById<TextView>(R.id.emptyText)

        // All unique stops across all routes
        val allStops = RouteDataHelper.getAllUniqueStops()
        stopSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allStops)

        busAdapter = NearMyStopBusAdapter(busList) { routeName ->
            val intent = Intent(this, RouteDetailsActivity::class.java)
            intent.putExtra("route", routeName)
            startActivity(intent)
        }
        busRecycler.layoutManager = LinearLayoutManager(this)
        busRecycler.adapter = busAdapter

        searchBtn.setOnClickListener {
            val selectedStop = stopSpinner.selectedItem?.toString() ?: return@setOnClickListener
            loadBusesForStop(selectedStop, emptyText, busRecycler)
        }
    }

    private fun loadBusesForStop(stopName: String, emptyText: TextView, recyclerView: RecyclerView) {
        busList.clear()
        busAdapter.notifyDataSetChanged()

        val matchingRoutes = RouteDataHelper.getRoutesForStop(stopName)
        if (matchingRoutes.isEmpty()) {
            emptyText.text = "No buses found for this stop"
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            return
        }

        emptyText.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        var loadedCount = 0
        val total = matchingRoutes.size

        for ((routeName, stopInfo) in matchingRoutes) {
            database.child("routes").child(routeName).child("latestPing")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val ping = if (snapshot.exists()) snapshot.getValue(Ping::class.java) else null
                        val etaText = calculateEta(stopInfo, ping, routeName)
                        busList.add(BusAtStop(
                            routeName = routeName,
                            stopName = stopName,
                            scheduledTime = stopInfo.scheduledTime,
                            etaText = etaText,
                            hasPing = ping != null
                        ))
                        loadedCount++
                        if (loadedCount == total) {
                            busList.sortBy { it.scheduledTime }
                            busAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        loadedCount++
                        if (loadedCount == total) busAdapter.notifyDataSetChanged()
                    }
                })
        }
    }

    private fun calculateEta(stopInfo: Stop, ping: Ping?, routeName: String): String {
        if (ping == null) return "Scheduled: ${stopInfo.scheduledTime}"

        val routeStops = RouteDataHelper.ALL_ROUTES.find { it.first == routeName }?.second
            ?: return "Scheduled: ${stopInfo.scheduledTime}"
        val pingedStop = routeStops.find { it.name == ping.stopName }
            ?: return "Scheduled: ${stopInfo.scheduledTime}"

        val diffMins = stopInfo.timeFromStartMins - pingedStop.timeFromStartMins
        val ageMins = ((System.currentTimeMillis() - ping.timestamp) / 60_000).toInt()
        val eta = diffMins - ageMins

        return when {
            eta <= 0 -> "Arriving now! 🟢"
            eta < 60 -> "~$eta mins away 🟡 (Live)"
            else -> "Scheduled: ${stopInfo.scheduledTime}"
        }
    }
}
