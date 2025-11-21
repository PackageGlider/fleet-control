package com.jedsy.fleetcontrol

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.jedsy.fleetcontrol.ui.MainViewModel
import com.jedsy.fleetcontrol.ui.DroneAdapter

class MainActivity : AppCompatActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adapter: DroneAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Setup toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Fleet Control"
        
        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DroneAdapter(emptyList())
        recyclerView.adapter = adapter
        
        // Setup SwipeRefresh
        swipeRefresh = findViewById(R.id.swipeRefresh)
        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
        
        // Observe ViewModel
        viewModel.drones.observe(this) { drones ->
            adapter.updateDrones(drones)
        }
        
        viewModel.isLoading.observe(this) { isLoading ->
            swipeRefresh.isRefreshing = isLoading
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Snackbar.make(recyclerView, it, Snackbar.LENGTH_LONG)
                    .setAction("Retry") { viewModel.refresh() }
                    .show()
            }
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                com.jedsy.fleetcontrol.ui.SettingsDialog().show(supportFragmentManager, "settings")
                true
            }
            R.id.action_refresh -> {
                viewModel.refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    fun refreshData() {
        viewModel.refresh()
    }
}
