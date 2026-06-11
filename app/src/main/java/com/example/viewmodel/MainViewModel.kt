package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.ServiceRecord
import com.example.data.Vehicle
import com.example.data.VehicleServiceConfig
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
        taxDueDateMs: Long,
        oilIntervalKm: Int,
        beltIntervalKm: Int
    ) {
        viewModelScope.launch {
            val vehicleId = repository.insertVehicle(
                Vehicle(
                    name = name,
                    brand = brand,
                    model = model,
                    year = year,
                    plateNumber = plateNumber,
                    engineType = engineType,
                    type = type,
                    currentMileage = currentMileage,
                    taxDueDateMs = taxDueDateMs
                )
            ).toInt()

            // Insert default configurations
            repository.insertConfig(
                VehicleServiceConfig(
                    vehicleId = vehicleId,
                    serviceType = "Ganti Oli",
                    intervalKm = oilIntervalKm
                )
            )
            repository.insertConfig(
                VehicleServiceConfig(
                    vehicleId = vehicleId,
                    serviceType = if (type == "MOTOR") "Ganti CVT/Belt" else "Ganti Timing Belt",
                    intervalKm = beltIntervalKm
                )
            )
        }
    }

    fun updateVehicleMileage(vehicle: Vehicle, currentMileage: Int) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle.copy(currentMileage = currentMileage))
        }
    }

    fun updateVehicleTaxDate(vehicle: Vehicle, taxDueDateMs: Long) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle.copy(taxDueDateMs = taxDueDateMs))
        }
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle)
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

    fun getConfigsForVehicle(vehicleId: Int): Flow<List<VehicleServiceConfig>> {
        return repository.getConfigsForVehicle(vehicleId)
    }

    fun insertServiceConfig(vehicleId: Int, serviceType: String, intervalKm: Int) {
        viewModelScope.launch {
            repository.insertConfig(
                VehicleServiceConfig(
                    vehicleId = vehicleId,
                    serviceType = serviceType,
                    intervalKm = intervalKm
                )
            )
        }
    }

    fun updateServiceConfig(config: VehicleServiceConfig) {
        viewModelScope.launch {
            repository.updateConfig(config)
        }
    }

    fun deleteServiceConfig(config: VehicleServiceConfig) {
        viewModelScope.launch {
            repository.deleteConfig(config)
        }
    }

    fun insertServiceRecord(vehicleId: Int, serviceType: String, title: String, mileageAtService: Int, notes: String, cost: Double) {
        viewModelScope.launch {
            repository.insertServiceRecord(
                ServiceRecord(
                    vehicleId = vehicleId,
                    serviceType = serviceType,
                    title = title,
                    mileageAtService = mileageAtService,
                    notes = notes,
                    cost = cost
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

    fun updateServiceRecord(record: ServiceRecord) {
        viewModelScope.launch {
            repository.updateServiceRecord(record)
        }
    }

    fun deleteServiceRecord(record: ServiceRecord) {
        viewModelScope.launch {
            repository.deleteServiceRecord(record)
        }
    }

    fun triggerOneTimeReminderCheck(context: android.content.Context) {
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.example.worker.ReminderWorker>().build()
        androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
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
