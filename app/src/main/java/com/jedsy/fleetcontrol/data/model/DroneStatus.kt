package com.jedsy.fleetcontrol.data.model

import com.google.gson.annotations.SerializedName

/**
 * Drone status from Healthcheck API /status endpoint
 */
data class DroneStatus(
    @SerializedName("endpoint_id")
    val endpointId: String,
    
    @SerializedName("device_id")
    val deviceId: String?,
    
    @SerializedName("device_name")
    val deviceName: String?,
    
    @SerializedName("device_type")
    val deviceType: String?,
    
    @SerializedName("ip_address")
    val ipAddress: String,
    
    @SerializedName("interface_type")
    val interfaceType: String?,
    
    @SerializedName("last_seen")
    val lastSeen: String,
    
    @SerializedName("last_status")
    val lastStatus: Boolean,
    
    @SerializedName("last_ping_ms")
    val lastPingMs: Int?,
    
    @SerializedName("failures")
    val failures: Int,
    
    @SerializedName("consecutive_failures")
    val consecutiveFailures: Int,
    
    @SerializedName("last_failure")
    val lastFailure: String?,
    
    @SerializedName("uptime_start")
    val uptimeStart: String?,
    
    @SerializedName("architecture")
    val architecture: String?,
    
    @SerializedName("telemetry")
    val telemetry: TelemetryData?
)

data class TelemetryData(
    @SerializedName("battery")
    val battery: BatteryTelemetry?,
    
    @SerializedName("gps")
    val gps: GPSTelemetry?,
    
    @SerializedName("system")
    val system: SystemTelemetry?
)

data class BatteryTelemetry(
    @SerializedName("voltage_v")
    val voltageV: Double?,
    
    @SerializedName("current_a")
    val currentA: Double?,
    
    @SerializedName("remaining_pct")
    val remainingPct: Int?,
    
    @SerializedName("last_update")
    val lastUpdate: String?
)

data class GPSTelemetry(
    @SerializedName("latitude")
    val latitude: Double?,
    
    @SerializedName("longitude")
    val longitude: Double?,
    
    @SerializedName("altitude_m")
    val altitudeM: Double?,
    
    @SerializedName("satellites")
    val satellites: Int?,
    
    @SerializedName("fix_type")
    val fixType: Int?,
    
    @SerializedName("last_update")
    val lastUpdate: String?
)

data class SystemTelemetry(
    @SerializedName("cpu_usage_pct")
    val cpuUsagePct: Double?,
    
    @SerializedName("memory_usage_pct")
    val memoryUsagePct: Double?,
    
    @SerializedName("memory_used_mb")
    val memoryUsedMb: Int?,
    
    @SerializedName("memory_total_mb")
    val memoryTotalMb: Int?,
    
    @SerializedName("disk_usage_pct")
    val diskUsagePct: Double?,
    
    @SerializedName("disk_used_gb")
    val diskUsedGb: Double?,
    
    @SerializedName("disk_total_gb")
    val diskTotalGb: Double?,
    
    @SerializedName("load_1min")
    val load1min: Double?,
    
    @SerializedName("load_5min")
    val load5min: Double?,
    
    @SerializedName("load_15min")
    val load15min: Double?,
    
    @SerializedName("last_update")
    val lastUpdate: String?
)
