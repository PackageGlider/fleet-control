package com.jedsy.fleetcontrol

import android.app.Application
import com.jedsy.fleetcontrol.data.api.RetrofitClient
import com.jedsy.fleetcontrol.ui.SettingsDialog

class FleetControlApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize backend environment from saved preference
        val isDev = SettingsDialog.getIsDev(this)
        RetrofitClient.setEnvironment(isDev)
    }
}
