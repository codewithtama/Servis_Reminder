package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val vehicleDao: VehicleDao,
    private val serviceDao: ServiceDao
) {
    val allVehicles: Flow<List<Vehicle>> = vehicleDao.getAllVehicles()

    fun getVehicleById(id: Int): Flow<Vehicle?> = vehicleDao.getVehicleById(id)

    suspend fun insertVehicle(vehicle: Vehicle) = vehicleDao.insertVehicle(vehicle)
    
    suspend fun updateVehicle(vehicle: Vehicle) = vehicleDao.updateVehicle(vehicle)

    suspend fun deleteVehicle(vehicle: Vehicle) = vehicleDao.deleteVehicle(vehicle)

    fun getServiceRecordsForVehicle(vehicleId: Int): Flow<List<ServiceRecord>> =
        serviceDao.getServiceRecordsForVehicle(vehicleId)

    suspend fun insertServiceRecord(record: ServiceRecord) = serviceDao.insertServiceRecord(record)

    suspend fun deleteServiceRecord(record: ServiceRecord) = serviceDao.deleteServiceRecord(record)
}
