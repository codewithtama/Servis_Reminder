package com.example.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.MainActivity
import com.example.data.AppDatabase
import kotlinx.coroutines.flow.first
import kotlin.math.abs

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    companion object {
        const val CHANNEL_ID = "servis_reminder_channel"
        const val CHANNEL_NAME = "Pengingat Servis & Pajak"
        const val CHANNEL_DESC = "Saluran notifikasi untuk pengingat servis berkala dan pajak STNK"
    }

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(applicationContext)
        val vehicleDao = database.vehicleDao()
        val configDao = database.serviceConfigDao()
        val serviceDao = database.serviceDao()

        val vehicles = vehicleDao.getAllVehicles().first()

        createNotificationChannel()

        var notificationId = 100

        for (vehicle in vehicles) {
            // 1. Pengecekan Jatuh Tempo Pajak STNK
            if (vehicle.taxDueDateMs > 0L) {
                val targetCal = java.util.Calendar.getInstance().apply {
                    timeInMillis = vehicle.taxDueDateMs
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }
                val currentCal = java.util.Calendar.getInstance().apply {
                    set(java.util.Calendar.HOUR_OF_DAY, 0)
                    set(java.util.Calendar.MINUTE, 0)
                    set(java.util.Calendar.SECOND, 0)
                    set(java.util.Calendar.MILLISECOND, 0)
                }
                val diffMs = targetCal.timeInMillis - currentCal.timeInMillis
                val daysRemaining = diffMs / (24 * 60 * 60 * 1000)

                if (daysRemaining <= 30) {
                    val message = if (daysRemaining < 0) {
                        "Pajak STNK untuk ${vehicle.name} telah telat ${abs(daysRemaining)} hari! Segera lakukan pembayaran."
                    } else if (daysRemaining == 0L) {
                        "Pajak STNK untuk ${vehicle.name} jatuh tempo HARI INI! Segera bayar."
                    } else {
                        "Pajak STNK untuk ${vehicle.name} akan jatuh tempo dalam $daysRemaining hari lagi."
                    }
                    showNotification(
                        id = notificationId++,
                        title = "Pengingat Pajak STNK",
                        message = message
                    )
                }
            }

            // 2. Pengecekan Batas Kilometer Servis
            val configs = configDao.getConfigsForVehicle(vehicle.id).first()
            val services = serviceDao.getServiceRecordsForVehicle(vehicle.id).first()

            for (config in configs.filter { it.intervalKm > 0 }) {
                val lastService = services.filter { it.serviceType.lowercase() == config.serviceType.lowercase() }.maxByOrNull { it.mileageAtService }
                val targetMileage = (lastService?.mileageAtService ?: vehicle.startingMileage) + config.intervalKm
                val remainingKm = targetMileage - vehicle.currentMileage

                if (remainingKm <= 200) {
                    val message = if (remainingKm <= 0) {
                        "Waktunya servis ${config.serviceType} untuk ${vehicle.name}! Sudah terlewat ${abs(remainingKm)} KM."
                    } else {
                        "Servis ${config.serviceType} untuk ${vehicle.name} kurang dari $remainingKm KM lagi."
                    }
                    showNotification(
                        id = notificationId++,
                        title = "Pengingat Servis Kendaraan",
                        message = message
                    )
                }
            }
        }

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(id: Int, title: String, message: String) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))

        try {
            val notificationManager = NotificationManagerCompat.from(applicationContext)
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(id, builder.build())
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
