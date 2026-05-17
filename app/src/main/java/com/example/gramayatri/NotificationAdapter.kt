package com.example.gramayatri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(private val notifications: List<BusNotification>) :
    RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    private val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification_card, parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val notif = notifications[position]
        holder.typeText.text = notif.type
        holder.routeText.text = "Route: ${notif.routeName}"
        holder.messageText.text = notif.message
        holder.postedByText.text = "By: ${notif.postedBy}"
        holder.timeText.text = sdf.format(Date(notif.timestamp))
    }

    override fun getItemCount(): Int = notifications.size

    class NotifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeText: TextView = itemView.findViewById(R.id.notifType)
        val routeText: TextView = itemView.findViewById(R.id.notifRoute)
        val messageText: TextView = itemView.findViewById(R.id.notifMessage)
        val postedByText: TextView = itemView.findViewById(R.id.notifPostedBy)
        val timeText: TextView = itemView.findViewById(R.id.notifTime)
    }
}
