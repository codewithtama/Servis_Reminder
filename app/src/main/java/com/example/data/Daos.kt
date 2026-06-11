package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
    fun getVehicleById(id: Int): Flow<Vehicle?>

    @Insert
    suspend fun insertVehicle(vehicle: Vehicle): Long

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)

    @Query("DELETE FROM vehicles")
    suspend fun deleteAllVehicles()
}

@Dao
interface ServiceConfigDao {
    @Query("SELECT * FROM vehicle_service_configs WHERE vehicleId = :vehicleId")
    fun getConfigsForVehicle(vehicleId: Int): Flow<List<VehicleServiceConfig>>

    @Insert
    suspend fun insertConfig(config: VehicleServiceConfig): Long

    @Update
    suspend fun updateConfig(config: VehicleServiceConfig)

    @Delete
    suspend fun deleteConfig(config: VehicleServiceConfig)
}

@Dao
interface ServiceDao {
    @Query("SELECT * FROM service_records WHERE vehicleId = :vehicleId ORDER BY dateMs DESC")
    fun getServiceRecordsForVehicle(vehicleId: Int): Flow<List<ServiceRecord>>

    @Insert
    suspend fun insertServiceRecord(record: ServiceRecord)

    @Update
    suspend fun updateServiceRecord(record: ServiceRecord)

    @Delete
    suspend fun deleteServiceRecord(record: ServiceRecord)
}
