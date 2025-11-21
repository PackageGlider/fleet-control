package com.jedsy.fleetcontrol.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jedsy.fleetcontrol.R
import com.jedsy.fleetcontrol.data.model.DroneStatus
import com.jedsy.fleetcontrol.data.repository.DroneRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Detail view for a single drone
 */
class DroneDetailActivity : AppCompatActivity() {
    
    private val repository = DroneRepository()
    private lateinit var progressBar: ProgressBar
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drone_detail)
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        progressBar = findViewById(R.id.progressBar)
        
        // Get data from intent
        val deviceName = intent.getStringExtra(EXTRA_DEVICE_NAME) ?: "Unknown Drone"
        val deviceId = intent.getStringExtra(EXTRA_DEVICE_ID) ?: ""
        
        // Set title
        supportActionBar?.title = deviceName
        
        // Load full drone data
        loadDroneDetails(deviceId)
    }
    
    private fun loadDroneDetails(deviceId: String) {
        progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            repository.getDroneById(deviceId).fold(
                onSuccess = { drone ->
                    progressBar.visibility = View.GONE
                    if (drone != null) {
                        displayDroneDetails(drone)
                    } else {
                        Toast.makeText(this@DroneDetailActivity, "Drone not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                },
                onFailure = { error ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@DroneDetailActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
    
    private fun displayDroneDetails(drone: DroneStatus) {
        // Basic info
        findViewById<TextView>(R.id.detailDeviceName).text = drone.deviceName ?: "Unknown Drone"
        findViewById<TextView>(R.id.detailDeviceId).text = shortenDeviceId(drone.deviceId ?: "")
        
        // Connection Status
        val isOnline = isOnline(drone.lastSeen)
        val statusText = findViewById<TextView>(R.id.statusValue)
        statusText.text = if (isOnline) "Online" else "Offline"
        statusText.setTextColor(getColor(if (isOnline) R.color.status_online else R.color.status_offline))
        
        findViewById<TextView>(R.id.lastSeenValue).text = formatTimestamp(drone.lastSeen)
        findViewById<TextView>(R.id.uptimeValue).text = drone.uptimeStart?.let { formatUptime(it, drone.lastSeen) } ?: "N/A"
        findViewById<TextView>(R.id.pingValue).text = drone.lastPingMs?.let { "${it}ms" } ?: "N/A"
        
        // Network
        findViewById<TextView>(R.id.ipAddressValue).text = drone.ipAddress
        findViewById<TextView>(R.id.interfaceValue).text = drone.interfaceType ?: "Unknown"
        
        // Battery
        val battery = drone.telemetry?.battery
        if (battery != null && battery.remainingPct != null) {
            findViewById<View>(R.id.batterySection).visibility = View.VISIBLE
            findViewById<TextView>(R.id.batteryPctValue).text = "${battery.remainingPct}%"
            findViewById<TextView>(R.id.batteryVoltageValue).text = battery.voltageV?.let { "%.2fV".format(it) } ?: "N/A"
            findViewById<TextView>(R.id.batteryCurrentValue).text = battery.currentA?.let { "%.1fA".format(it) } ?: "N/A"
            findViewById<TextView>(R.id.batteryUpdateValue).text = battery.lastUpdate?.let { formatTimestamp(it) } ?: "N/A"
        } else {
            findViewById<View>(R.id.batterySection).visibility = View.GONE
        }
        
        // GPS
        val gps = drone.telemetry?.gps
        if (gps != null && gps.latitude != null) {
            findViewById<View>(R.id.gpsSection).visibility = View.VISIBLE
            findViewById<TextView>(R.id.gpsLatValue).text = "%.6f".format(gps.latitude)
            findViewById<TextView>(R.id.gpsLonValue).text = "%.6f".format(gps.longitude ?: 0.0)
            findViewById<TextView>(R.id.gpsAltValue).text = gps.altitudeM?.let { "%.1fm".format(it) } ?: "N/A"
            findViewById<TextView>(R.id.gpsSatValue).text = "${gps.satellites ?: 0}"
            findViewById<TextView>(R.id.gpsFixValue).text = getFixTypeName(gps.fixType ?: 0)
        } else {
            findViewById<View>(R.id.gpsSection).visibility = View.GONE
        }
        
        // System
        val system = drone.telemetry?.system
        if (system != null) {
            findViewById<View>(R.id.systemSection).visibility = View.VISIBLE
            findViewById<TextView>(R.id.cpuValue).text = system.cpuUsagePct?.let { "%.1f%%".format(it) } ?: "N/A"
            findViewById<TextView>(R.id.memoryValue).text = system.memoryUsagePct?.let { 
                "%.1f%% (${system.memoryUsedMb}/${system.memoryTotalMb} MB)".format(it) 
            } ?: "N/A"
            findViewById<TextView>(R.id.diskValue).text = system.diskUsagePct?.let { 
                "%.1f%% (%.1f/%.1f GB)".format(it, system.diskUsedGb, system.diskTotalGb) 
            } ?: "N/A"
            findViewById<TextView>(R.id.loadValue).text = "1m: ${system.load1min ?: 0.0}, 5m: ${system.load5min ?: 0.0}, 15m: ${system.load15min ?: 0.0}"
        } else {
            findViewById<View>(R.id.systemSection).visibility = View.GONE
        }
    }
    
    private fun isOnline(lastSeenStr: String): Boolean {
        return try {
            val cleanTimestamp = lastSeenStr.substringBefore('.') + "Z"
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val lastSeen = format.parse(cleanTimestamp) ?: return false
            val now = Date()
            val diffSeconds = (now.time - lastSeen.time) / 1000
            diffSeconds < 60
        } catch (e: Exception) {
            false
        }
    }
    
    private fun formatTimestamp(timestampStr: String): String {
        return try {
            val cleanTimestamp = timestampStr.substringBefore('.') + "Z"
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val timestamp = format.parse(cleanTimestamp) ?: return timestampStr
            val now = Date()
            val diffSeconds = (now.time - timestamp.time) / 1000
            
            when {
                diffSeconds < 60 -> "Just now"
                diffSeconds < 3600 -> "${diffSeconds / 60}m ago"
                diffSeconds < 86400 -> "${diffSeconds / 3600}h ago"
                else -> SimpleDateFormat("MMM dd, HH:mm", Locale.US).format(timestamp)
            }
        } catch (e: Exception) {
            timestampStr
        }
    }
    
    private fun formatUptime(uptimeStartStr: String, lastSeenStr: String): String {
        return try {
            val cleanStart = uptimeStartStr.substringBefore('.') + "Z"
            val cleanSeen = lastSeenStr.substringBefore('.') + "Z"
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            format.timeZone = TimeZone.getTimeZone("UTC")
            val start = format.parse(cleanStart) ?: return "N/A"
            val seen = format.parse(cleanSeen) ?: return "N/A"
            val diffSeconds = (seen.time - start.time) / 1000
            
            when {
                diffSeconds < 60 -> "${diffSeconds}s"
                diffSeconds < 3600 -> "${diffSeconds / 60}m"
                diffSeconds < 86400 -> "${diffSeconds / 3600}h"
                else -> "${diffSeconds / 86400}d"
            }
        } catch (e: Exception) {
            "N/A"
        }
    }
    
    private fun getFixTypeName(fixType: Int): String {
        return when (fixType) {
            0 -> "No Fix"
            1 -> "Dead Reckoning"
            2 -> "2D Fix"
            3 -> "3D Fix"
            4 -> "GPS + Dead Reckoning"
            5 -> "Time Only"
            else -> "Unknown ($fixType)"
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    /**
     * Shorten device ID: first 8 chars + ... + last 8 chars
     */
    private fun shortenDeviceId(deviceId: String): String {
        return if (deviceId.length > 19) {
            "${deviceId.substring(0, 8)}...${deviceId.substring(deviceId.length - 8)}"
        } else {
            deviceId
        }
    }
    
    companion object {
        const val EXTRA_DEVICE_NAME = "device_name"
        const val EXTRA_DEVICE_ID = "device_id"
    }
}
