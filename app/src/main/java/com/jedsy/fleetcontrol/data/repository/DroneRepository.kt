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
     */
    suspend fun getAllDrones(): Result<List<DroneStatus>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllStatus()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to load drones: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
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
