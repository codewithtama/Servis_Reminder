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
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.MainViewModel
import com.example.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = AppRepository(database.vehicleDao(), database.serviceDao(), database.serviceConfigDao())
        val factory = MainViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: MainViewModel = viewModel(factory = factory)
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(navController, viewModel)
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
