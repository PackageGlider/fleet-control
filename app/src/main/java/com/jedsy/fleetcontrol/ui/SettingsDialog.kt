package com.jedsy.fleetcontrol.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.jedsy.fleetcontrol.R
import com.jedsy.fleetcontrol.data.api.RetrofitClient

/**
 * Settings dialog for switching between dev and prod environments
 */
class SettingsDialog : DialogFragment() {
    
    companion object {
        private const val PREFS_NAME = "fleet_control_prefs"
        private const val KEY_IS_DEV = "is_dev_environment"
        
        fun getIsDev(context: Context): Boolean {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_IS_DEV, true) // Default to dev
        }
        
        fun setIsDev(context: Context, isDev: Boolean) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_IS_DEV, isDev)
                .apply()
        }
    }
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentIsDev = prefs.getBoolean(KEY_IS_DEV, true)
        
        val view = layoutInflater.inflate(R.layout.dialog_settings, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroupEnvironment)
        val radioDev = view.findViewById<RadioButton>(R.id.radioDev)
        val radioProd = view.findViewById<RadioButton>(R.id.radioProd)
        
        // Set current selection
        if (currentIsDev) {
            radioDev.isChecked = true
        } else {
            radioProd.isChecked = true
        }
        
        return AlertDialog.Builder(context)
            .setTitle("Backend Environment")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val isDev = radioDev.isChecked
                
                // Save preference
                setIsDev(context, isDev)
                
                // Update Retrofit client
                RetrofitClient.setEnvironment(isDev)
                
                // Notify user
                val envName = if (isDev) "Development (ping.uphi.cc)" else "Production (ping.uphi.ch)"
                android.widget.Toast.makeText(
                    context,
                    "Switched to $envName",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                
                // Refresh data
                (activity as? com.jedsy.fleetcontrol.MainActivity)?.refreshData()
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
