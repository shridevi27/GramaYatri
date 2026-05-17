package com.example.gramayatri

// ---------- Stop ----------
data class Stop(
    val name: String = "",
    val timeFromStartMins: Int = 0,
    val scheduledTime: String = ""   // "HH:mm" format
)

// ---------- Ping ----------
data class Ping(
    val stopName: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val reportedBy: String = "Anonymous",
    val userId: String = ""
)

// ---------- Route ----------
data class Route(
    val name: String = "",
    val stops: List<Stop> = listOf(),
    val latestPing: Ping? = null
)

// ---------- Report ----------
data class Report(
    val id: String = "",
    val routeName: String = "",
    val stopName: String = "",
    val type: String = "",
    val description: String = "",
    val timestamp: Long = 0L,
    val userId: String = "",
    val userEmail: String = ""
)

// ---------- Bus Notification ----------
data class BusNotification(
    val id: String = "",
    val routeName: String = "",
    val message: String = "",
    val type: String = "",
    val timestamp: Long = 0L,
    val postedBy: String = "",
    val conductorEmail: String = ""
)

// ---------- BusAtStop (for Near My Stop) ----------
data class BusAtStop(
    val routeName: String = "",
    val stopName: String = "",
    val scheduledTime: String = "",
    val etaText: String = "",
    val hasPing: Boolean = false
)