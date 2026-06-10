package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val currentMileage: Int
)

@Entity(tableName = "service_records")
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val serviceType: String,
    val title: String, 
    val mileageAtService: Int,
    val notes: String,
    val dateMs: Long = System.currentTimeMillis()
)
