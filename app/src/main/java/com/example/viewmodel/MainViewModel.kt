package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.ServiceRecord
import com.example.data.Vehicle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val allVehicles: StateFlow<List<Vehicle>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertVehicle(
        name: String,
        brand: String,
        model: String,
        year: String,
        plateNumber: String,
        engineType: String,
        type: String,
        currentMileage: Int,
        oilIntervalKm: Int,
        beltIntervalKm: Int
    ) {
        viewModelScope.launch {
            repository.insertVehicle(
                Vehicle(
                    name = name,
                    brand = brand,
                    model = model,
                    year = year,
                    plateNumber = plateNumber,
                    engineType = engineType,
                    type = type,
                    currentMileage = currentMileage,
                    oilIntervalKm = oilIntervalKm,
                    beltIntervalKm = beltIntervalKm
                )
            )
        }
    }

    fun updateVehicleMileage(vehicle: Vehicle, currentMileage: Int) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle.copy(currentMileage = currentMileage))
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.deleteVehicle(vehicle)
        }
    }

    fun getVehicle(id: Int): Flow<Vehicle?> = repository.getVehicleById(id)

    fun getServiceRecordsForVehicle(vehicleId: Int): Flow<List<ServiceRecord>> {
        return repository.getServiceRecordsForVehicle(vehicleId)
    }

    fun insertServiceRecord(vehicleId: Int, serviceType: String, title: String, mileageAtService: Int, notes: String) {
        viewModelScope.launch {
            repository.insertServiceRecord(
                ServiceRecord(
                    vehicleId = vehicleId,
                    serviceType = serviceType,
                    title = title,
                    mileageAtService = mileageAtService,
                    notes = notes
                )
            )
            // Coba update jarak tempuh kendaraan juga jika lebih baru
            val flow = repository.getVehicleById(vehicleId)
            val vehicle = flow.first()
            if (vehicle != null && mileageAtService > vehicle.currentMileage) {
                repository.updateVehicle(vehicle.copy(currentMileage = mileageAtService))
            }
        }
    }
}

class MainViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
