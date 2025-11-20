package com.jedsy.fleetcontrol.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jedsy.fleetcontrol.data.model.DroneStatus
import com.jedsy.fleetcontrol.data.repository.DroneRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for main dashboard
 */
class MainViewModel : ViewModel() {
    
    private val repository = DroneRepository()
    
    private val _drones = MutableLiveData<List<DroneStatus>>()
    val drones: LiveData<List<DroneStatus>> = _drones
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadDrones()
    }
    
    fun loadDrones() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getAllDrones()
                .onSuccess { droneList ->
                    _drones.value = droneList
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Unknown error"
                }
            
            _isLoading.value = false
        }
    }
    
    fun refresh() {
        loadDrones()
    }
}
