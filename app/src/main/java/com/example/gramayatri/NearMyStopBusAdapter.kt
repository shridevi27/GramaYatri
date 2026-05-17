package com.example.gramayatri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NearMyStopBusAdapter(
    private val buses: List<BusAtStop>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<NearMyStopBusAdapter.BusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bus_at_stop, parent, false)
        return BusViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusViewHolder, position: Int) {
        val bus = buses[position]
        holder.routeName.text = bus.routeName
        holder.scheduledTime.text = "Scheduled: ${bus.scheduledTime}"
        holder.eta.text = bus.etaText
        holder.itemView.setOnClickListener { onClick(bus.routeName) }
    }

    override fun getItemCount(): Int = buses.size

    class BusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeName: TextView = itemView.findViewById(R.id.busRouteName)
        val scheduledTime: TextView = itemView.findViewById(R.id.busScheduledTime)
        val eta: TextView = itemView.findViewById(R.id.busEta)
    }
}
