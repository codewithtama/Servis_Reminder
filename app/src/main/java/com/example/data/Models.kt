package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val plateNumber: String = "",
    val engineType: String = "",
    val type: String, // "MOTOR" or "MOBIL"
    val currentMileage: Int,
    val taxDueDateMs: Long = 0L,
    val startingMileage: Int,
    val subType: String = "MATIC"
)

@Entity(
    tableName = "vehicle_service_configs",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VehicleServiceConfig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val serviceType: String,
    val intervalKm: Int
)

@Entity(
    tableName = "service_records",
    foreignKeys = [
        ForeignKey(
            entity = Vehicle::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val serviceType: String,
    val title: String, 
    val mileageAtService: Int,
    val notes: String,
    val dateMs: Long = System.currentTimeMillis(),
    val cost: Double = 0.0
)
