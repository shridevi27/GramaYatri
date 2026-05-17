package com.example.gramayatri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ReportAdapter(private val reports: List<Report>) :
    RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    private val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report_card, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]

        val emoji = when (report.type) {
            "Bus Delayed" -> "🕐"
            "Bus Was Early" -> "⚡"
            "Bus Breakdown" -> "🔧"
            "Heavy Crowd" -> "👥"
            "Route Changed" -> "🔄"
            "Bus Not Coming" -> "🚫"
            else -> "⚠️"
        }
        holder.typeIcon.text = emoji
        holder.typeText.text = report.type
        holder.routeText.text = "Route: ${report.routeName}"
        holder.stopText.text = "Stop: ${report.stopName}"
        holder.descText.text = if (report.description.isNotEmpty()) report.description else ""
        holder.descText.visibility = if (report.description.isNotEmpty()) View.VISIBLE else View.GONE
        holder.timeText.text = sdf.format(Date(report.timestamp))
    }

    override fun getItemCount(): Int = reports.size

    class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val typeIcon: TextView = itemView.findViewById(R.id.reportTypeIcon)
        val typeText: TextView = itemView.findViewById(R.id.reportType)
        val routeText: TextView = itemView.findViewById(R.id.reportRoute)
        val stopText: TextView = itemView.findViewById(R.id.reportStop)
        val descText: TextView = itemView.findViewById(R.id.reportDescription)
        val timeText: TextView = itemView.findViewById(R.id.reportTime)
    }
}
