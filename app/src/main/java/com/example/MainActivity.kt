package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.screens.AddServiceScreen
import com.example.ui.screens.AddVehicleScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.VehicleDetailScreen
import com.example.ui.screens.EditVehicleScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MainViewModelFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.worker.ReminderWorker
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.DisposableEffect

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = AppRepository(database.vehicleDao(), database.serviceDao(), database.serviceConfigDao())
        val factory = MainViewModelFactory(repository)

        // Schedule periodic reminder check every 24 hours
        val reminderWorkRequest = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS) // Avoid running immediately on fresh launch
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ServisReminderWork",
            ExistingPeriodicWorkPolicy.KEEP,
            reminderWorkRequest
        )

        enableEdgeToEdge()
        setContent {
            val sharedPreferences = remember { getSharedPreferences("servis_reminder_prefs", android.content.Context.MODE_PRIVATE) }
            var appThemeSetting by remember { mutableStateOf(sharedPreferences.getString("app_theme", "system") ?: "system") }

            DisposableEffect(Unit) {
                val listener = android.content.SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == "app_theme") {
                        appThemeSetting = sharedPreferences.getString("app_theme", "system") ?: "system"
                    }
                }
                sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
                onDispose {
                    sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
                }
            }

            val darkTheme = when (appThemeSetting) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            MyApplicationTheme(darkTheme = darkTheme) {
                val viewModel: MainViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(navController, viewModel)
                    }
                    composable("settings") {
                        SettingsScreen(navController, viewModel)
                    }
                    composable("add_vehicle") {
                        AddVehicleScreen(navController, viewModel)
                    }
                    composable("vehicle_detail/{vehicleId}") { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toIntOrNull()
                        if (vehicleId != null) {
                            VehicleDetailScreen(navController, viewModel, vehicleId)
                        }
                    }
                    composable("edit_vehicle/{vehicleId}") { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toIntOrNull()
                        if (vehicleId != null) {
                            EditVehicleScreen(navController, viewModel, vehicleId)
                        }
                    }
                    composable("add_service/{vehicleId}") { backStackEntry ->
                        val vehicleId = backStackEntry.arguments?.getString("vehicleId")?.toIntOrNull()
                        if (vehicleId != null) {
                            AddServiceScreen(navController, viewModel, vehicleId)
                        }
                    }
                }
            }
        }
    }
}
