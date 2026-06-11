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
        beltIntervalKm: Int,
        subType: String
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
                    taxDueDateMs = taxDueDateMs,
                    startingMileage = currentMileage,
                    subType = subType
                )
            ).toInt()

            // Insert default configurations based on vehicle type and subType
            if (type == "MOTOR") {
                when (subType) {
                    "LISTRIK" -> {
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Cek Drive Belt/Rantai",
                                intervalKm = beltIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Gearbox EV",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Minyak Rem",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Cek Kesehatan Baterai (SOH)",
                                intervalKm = 5000
                            )
                        )
                    }
                    "GIGI" -> {
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Mesin",
                                intervalKm = oilIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Rantai & Gear",
                                intervalKm = beltIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Busi",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Udara",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Air Radiator",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Minyak Rem",
                                intervalKm = 20000
                            )
                        )
                    }
                    "KOPLING" -> {
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Mesin",
                                intervalKm = oilIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Kopling",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Rantai & Gear",
                                intervalKm = beltIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Busi",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Udara",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Air Radiator",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Minyak Rem",
                                intervalKm = 20000
                            )
                        )
                    }
                    else -> { // MATIC
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Mesin",
                                intervalKm = oilIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Gardan",
                                intervalKm = 4000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti V-Belt (CVT)",
                                intervalKm = beltIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Busi",
                                intervalKm = 8000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Udara",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Air Radiator",
                                intervalKm = 12000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Minyak Rem",
                                intervalKm = 20000
                            )
                        )
                    }
                }
            } else { // MOBIL
                when (subType) {
                    "LISTRIK" -> {
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter AC Kabin",
                                intervalKm = 15000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Cairan Gearbox EV",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 50000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Cek Kesehatan Baterai (SOH)",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Cairan Pendingin Baterai",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Rotasi Ban",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Servis AC Mobil",
                                intervalKm = 20000
                            )
                        )
                    }
                    "HYBRID" -> {
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Mesin",
                                intervalKm = oilIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Oli",
                                intervalKm = oilIntervalKm * 2
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Udara",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Rotasi Ban",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Air Radiator",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Servis AC Mobil",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Cek Sistem & Baterai Hybrid",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Busi",
                                intervalKm = 40000
                            )
                        )
                    }
                    else -> { // BENSIN / DIESEL
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Mesin",
                                intervalKm = oilIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Oli",
                                intervalKm = oilIntervalKm * 2
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Filter Udara",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Timing Belt",
                                intervalKm = beltIntervalKm
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Kampas Rem",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Rotasi Ban",
                                intervalKm = 10000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Oli Transmisi",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Minyak Rem",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Air Radiator",
                                intervalKm = 40000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Servis AC Mobil",
                                intervalKm = 20000
                            )
                        )
                        repository.insertConfig(
                            VehicleServiceConfig(
                                vehicleId = vehicleId,
                                serviceType = "Ganti Busi",
                                intervalKm = 40000
                            )
                        )
                    }
                }
            }
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
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.example.worker.ReminderWorker>()
            .setInputData(androidx.work.workDataOf("is_test" to true))
            .build()
        androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
    }

    fun nukeDatabase() {
        viewModelScope.launch {
            repository.deleteAllVehicles()
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
