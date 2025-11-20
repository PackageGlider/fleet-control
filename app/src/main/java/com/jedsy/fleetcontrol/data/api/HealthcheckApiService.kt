package com.jedsy.fleetcontrol.data.api

import com.jedsy.fleetcontrol.data.model.DroneStatus
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Healthcheck API service for live drone status
 * Base URL: https://ping.uphi.cc (dev) or https://ping.uphi.ch (prod)
 */
interface HealthcheckApiService {
    
    /**
     * Get all endpoint health status
     * GET /status
     */
    @GET("status")
    suspend fun getAllStatus(
        @Query("device_id") deviceId: String? = null,
        @Query("device_name") deviceName: String? = null,
        @Query("ip_address") ipAddress: String? = null,
        @Query("status") status: String? = null
    ): Response<List<DroneStatus>>
    
    /**
     * Get device health status by device ID
     * GET /status/id/{deviceID}
     */
    @GET("status/id/{deviceID}")
    suspend fun getStatusByDeviceId(
        @retrofit2.http.Path("deviceID") deviceId: String
    ): Response<List<DroneStatus>>
    
    /**
     * Get device health status by device name
     * GET /status/name/{deviceName}
     */
    @GET("status/name/{deviceName}")
    suspend fun getStatusByDeviceName(
        @retrofit2.http.Path("deviceName") deviceName: String
    ): Response<List<DroneStatus>>
}
