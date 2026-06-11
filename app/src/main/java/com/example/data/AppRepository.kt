package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val vehicleDao: VehicleDao,
    private val serviceDao: ServiceDao,
    private val serviceConfigDao: ServiceConfigDao
) {
    val allVehicles: Flow<List<Vehicle>> = vehicleDao.getAllVehicles()

    fun getVehicleById(id: Int): Flow<Vehicle?> = vehicleDao.getVehicleById(id)

    suspend fun insertVehicle(vehicle: Vehicle): Long = vehicleDao.insertVehicle(vehicle)
    
    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.updateVehicle(vehicle)

    suspend fun deleteVehicle(vehicle: Vehicle) = vehicleDao.deleteVehicle(vehicle)

    suspend fun deleteAllVehicles() = vehicleDao.deleteAllVehicles()

    fun getServiceRecordsForVehicle(vehicleId: Int): Flow<List<ServiceRecord>> =
        serviceDao.getServiceRecordsForVehicle(vehicleId)

    suspend fun insertServiceRecord(record: ServiceRecord) = serviceDao.insertServiceRecord(record)

    suspend fun updateServiceRecord(record: ServiceRecord) = serviceDao.updateServiceRecord(record)

    suspend fun deleteServiceRecord(record: ServiceRecord) = serviceDao.deleteServiceRecord(record)

    fun getConfigsForVehicle(vehicleId: Int): Flow<List<VehicleServiceConfig>> =
        serviceConfigDao.getConfigsForVehicle(vehicleId)

    suspend fun insertConfig(config: VehicleServiceConfig): Long =
        serviceConfigDao.insertConfig(config)

    suspend fun updateConfig(config: VehicleServiceConfig) =
        serviceConfigDao.updateConfig(config)

    suspend fun deleteConfig(config: VehicleServiceConfig) =
        serviceConfigDao.deleteConfig(config)
}
