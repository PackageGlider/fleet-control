package com.jedsy.fleetcontrol.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jedsy.fleetcontrol.R
import com.jedsy.fleetcontrol.data.model.DroneStatus
import java.text.SimpleDateFormat
import java.util.*

class DroneAdapter(private var drones: List<DroneStatus>) : 
    RecyclerView.Adapter<DroneAdapter.DroneViewHolder>() {
    
    class DroneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val deviceName: TextView = view.findViewById(R.id.deviceName)
        val deviceId: TextView = view.findViewById(R.id.deviceId)
        val status: TextView = view.findViewById(R.id.status)
        val lastSeen: TextView = view.findViewById(R.id.lastSeen)
        val batteryLevel: TextView = view.findViewById(R.id.batteryLevel)
        val ipAddress: TextView = view.findViewById(R.id.ipAddress)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DroneViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_drone, parent, false)
        return DroneViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: DroneViewHolder, position: Int) {
        val drone = drones[position]
        
        holder.deviceName.text = drone.deviceName ?: "Unknown Drone"
        holder.deviceId.text = drone.deviceId ?: drone.endpointId
        holder.ipAddress.text = drone.ipAddress
        
        // Status
        val isOnline = isOnline(drone.lastSeen)
        holder.status.text = if (isOnline) "Online" else "Offline"
        holder.status.setTextColor(
            holder.itemView.context.getColor(
                if (isOnline) R.color.status_online else R.color.status_offline
            )
        )
        
        // Last seen
        holder.lastSeen.text = formatLastSeen(drone.lastSeen)
        
        // Battery
        val battery = drone.telemetry?.battery
        if (battery != null && battery.remainingPct != null) {
            holder.batteryLevel.text = "${battery.remainingPct}%"
            holder.batteryLevel.visibility = View.VISIBLE
        } else {
            holder.batteryLevel.visibility = View.GONE
        }
    }
    
    override fun getItemCount() = drones.size
    
    fun updateDrones(newDrones: List<DroneStatus>) {
        drones = newDrones
        notifyDataSetChanged()
    }
    
    private fun isOnline(lastSeenStr: String): Boolean {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val lastSeen = format.parse(lastSeenStr) ?: return false
            val now = Date()
            val diffSeconds = (now.time - lastSeen.time) / 1000
            diffSeconds < 60 // Online if seen within last 60 seconds
        } catch (e: Exception) {
            false
        }
    }
    
    private fun formatLastSeen(lastSeenStr: String): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val lastSeen = format.parse(lastSeenStr) ?: return lastSeenStr
            val now = Date()
            val diffSeconds = (now.time - lastSeen.time) / 1000
            
            when {
                diffSeconds < 60 -> "Just now"
                diffSeconds < 3600 -> "${diffSeconds / 60}m ago"
                diffSeconds < 86400 -> "${diffSeconds / 3600}h ago"
                else -> "${diffSeconds / 86400}d ago"
            }
        } catch (e: Exception) {
            lastSeenStr
        }
    }
}
