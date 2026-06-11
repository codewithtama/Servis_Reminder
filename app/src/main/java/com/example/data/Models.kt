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
    val oilIntervalKm: Int = if (type == "MOTOR") 2000 else 5000,
    val beltIntervalKm: Int = if (type == "MOTOR") 24000 else 40000
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
    val dateMs: Long = System.currentTimeMillis()
)
