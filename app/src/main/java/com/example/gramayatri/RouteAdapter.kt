package com.example.gramayatri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RouteAdapter(
    private val routes: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_route_card, parent, false)
        return RouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        val routeName = routes[position]
        holder.routeNameText.text = routeName

        // Get stop count for this route
        val stops = RouteDataHelper.ALL_ROUTES.find { it.first == routeName }?.second
        val stopCount = stops?.size ?: 0
        val firstStop = stops?.firstOrNull()?.scheduledTime ?: "--"
        val lastStop = stops?.lastOrNull()?.scheduledTime ?: "--"

        holder.routeStopsText.text = "$stopCount stops  |  $firstStop → $lastStop"
        holder.routePingStatus.text = "Tap to view route & live pings"
        holder.itemView.setOnClickListener { onClick(routeName) }
    }

    override fun getItemCount(): Int = routes.size

    class RouteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeNameText: TextView = itemView.findViewById(R.id.routeNameText)
        val routeStopsText: TextView = itemView.findViewById(R.id.routeStopsText)
        val routePingStatus: TextView = itemView.findViewById(R.id.routePingStatus)
    }
}
