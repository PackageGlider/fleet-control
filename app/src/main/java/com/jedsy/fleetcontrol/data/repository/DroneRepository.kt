package com.jedsy.fleetcontrol.data.repository

import com.jedsy.fleetcontrol.data.api.RetrofitClient
import com.jedsy.fleetcontrol.data.model.DroneStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for drone fleet data
 */
class DroneRepository {
    
    private val api = RetrofitClient.healthcheckApi
    
    /**
     * Fetch all drone status from Healthcheck API
     * Filters for drones only, sorts by online status first, then by last_seen
     */
    suspend fun getAllDrones(): Result<List<DroneStatus>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllStatus()
            if (response.isSuccessful && response.body() != null) {
                val allDevices = response.body()!!
                
                // Filter: only drones
                val drones = allDevices.filter { 
                    it.deviceType?.lowercase() == "drone" 
                }
                
                // Sort: online first, then by last_seen (most recent first)
                val sorted = drones.sortedWith(
                    compareByDescending<DroneStatus> { isOnline(it.lastSeen) }
                        .thenByDescending { it.lastSeen }
                )
                
                Result.success(sorted)
            } else {
                Result.failure(Exception("Failed to load drones: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Check if device is online (last_seen within 60 seconds)
     */
    private fun isOnline(lastSeenStr: String): Boolean {
        return try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
            format.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val lastSeen = format.parse(lastSeenStr) ?: return false
            val now = java.util.Date()
            val diffSeconds = (now.time - lastSeen.time) / 1000
            diffSeconds < 60
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Fetch status for specific device ID
     */
    suspend fun getDroneById(deviceId: String): Result<DroneStatus?> = withContext(Dispatchers.IO) {
        try {
            val response = api.getStatusByDeviceId(deviceId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.firstOrNull())
            } else {
                Result.failure(Exception("Failed to load drone: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
