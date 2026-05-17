package com.example.gramayatri

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimelineAdapter(
    private var stops: List<Stop>,
    private var latestPing: Ping?,
    private var currentTimeMillis: Long
) : RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    fun updateData(newStops: List<Stop>, newPing: Ping?, currentMillis: Long) {
        stops = newStops
        latestPing = newPing
        currentTimeMillis = currentMillis
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline_stop, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val stop = stops[position]
        holder.stopNameText.text = stop.name
        holder.scheduledTimeText.text = if (stop.scheduledTime.isNotEmpty()) "Sched: ${stop.scheduledTime}" else ""

        // Show/hide connector lines
        holder.timelineLineTop.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
        holder.timelineLineBottom.visibility = if (position == stops.size - 1) View.INVISIBLE else View.VISIBLE

        val ping = latestPing
        if (ping == null) {
            // No ping — show scheduled time as ETA
            holder.etaValueText.text = if (stop.scheduledTime.isNotEmpty()) stop.scheduledTime else "--"
            holder.etaValueText.setTextColor(Color.parseColor("#9E9E9E"))
            holder.timelineNode.setBackgroundResource(R.drawable.circle_node)
            return
        }

        val pingedIndex = stops.indexOfFirst { it.name == ping.stopName }
        if (pingedIndex == -1) {
            holder.etaValueText.text = stop.scheduledTime
            holder.etaValueText.setTextColor(Color.parseColor("#9E9E9E"))
            return
        }

        val pingedStop = stops[pingedIndex]

        when {
            position < pingedIndex -> {
                // Already passed
                holder.etaValueText.text = "Passed ✓"
                holder.etaValueText.setTextColor(Color.parseColor("#9E9E9E"))
                holder.timelineNode.setBackgroundColor(Color.parseColor("#9E9E9E"))
                holder.timelineLineTop.setBackgroundColor(Color.parseColor("#9E9E9E"))
                holder.timelineLineBottom.setBackgroundColor(Color.parseColor("#9E9E9E"))
            }
            position == pingedIndex -> {
                // Bus is here
                holder.etaValueText.text = "🚌 Bus Here!"
                holder.etaValueText.setTextColor(Color.parseColor("#E65100"))
                holder.timelineNode.setBackgroundResource(R.drawable.circle_node)
            }
            else -> {
                // Upcoming — calculate ETA
                val diffMins = stop.timeFromStartMins - pingedStop.timeFromStartMins
                val ageMins = ((currentTimeMillis - ping.timestamp) / 60_000).toInt()
                val eta = diffMins - ageMins
                when {
                    eta <= 0 -> {
                        holder.etaValueText.text = "Arriving now! 🟢"
                        holder.etaValueText.setTextColor(Color.parseColor("#2E7D32"))
                    }
                    else -> {
                        holder.etaValueText.text = "~$eta min"
                        holder.etaValueText.setTextColor(Color.parseColor("#1565C0"))
                    }
                }
                holder.timelineNode.setBackgroundResource(R.drawable.circle_node_green)
            }
        }
    }

    override fun getItemCount(): Int = stops.size

    class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timelineLineTop: View = itemView.findViewById(R.id.timelineLineTop)
        val timelineNode: View = itemView.findViewById(R.id.timelineNode)
        val timelineLineBottom: View = itemView.findViewById(R.id.timelineLineBottom)
        val stopNameText: TextView = itemView.findViewById(R.id.stopNameText)
        val scheduledTimeText: TextView = itemView.findViewById(R.id.scheduledTimeText)
        val etaValueText: TextView = itemView.findViewById(R.id.etaValueText)
    }
}
