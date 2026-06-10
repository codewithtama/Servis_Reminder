package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: MainViewModel) {
    val vehicles by viewModel.allVehicles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servis Reminder", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_vehicle") },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Kendaraan")
            }
        }
    ) { padding ->
        if (vehicles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Belum ada kendaraan. Tambahkan sekarang!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(), 
                contentPadding = PaddingValues(16.dp), 
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(vehicles) { vehicle ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("vehicle_detail/${vehicle.id}") },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (vehicle.type == "MOTOR") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                                    contentDescription = vehicle.type,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(vehicle.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                
                                val detailParts = listOf(vehicle.brand, vehicle.model, vehicle.plateNumber).filter { it.isNotBlank() }
                                if (detailParts.isNotEmpty()) {
                                    Text(detailParts.joinToString(" • "), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                                
                                Text("Jarak Tempuh: ${vehicle.currentMileage} KM", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(navController: NavController, viewModel: MainViewModel) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("MOTOR") }
    var mileage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Kendaraan") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Panggilan Kendaraan (ex: Motor Kesayangan)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Merek (ex: Honda)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model (ex: Vario 160)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = year, onValueChange = { year = it.filter { char -> char.isDigit() } }, label = { Text("Tahun") }, modifier = Modifier.weight(1f), singleLine = true)
                OutlinedTextField(value = plate, onValueChange = { plate = it }, label = { Text("Nomor Plat") }, modifier = Modifier.weight(1f), singleLine = true)
            }
            
            OutlinedTextField(value = engine, onValueChange = { engine = it }, label = { Text("Kapasitas Mesin (ex: 160cc)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            
            Text("Jenis Kendaraan", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FilterChip(
                    selected = type == "MOTOR",
                    onClick = { type = "MOTOR" },
                    label = { Text("Motor") },
                    leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null) }
                )
                FilterChip(
                    selected = type == "MOBIL",
                    onClick = { type = "MOBIL" },
                    label = { Text("Mobil") },
                    leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) }
                )
            }

            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { char -> char.isDigit() } },
                label = { Text("Jarak Tempuh Saat Ini (KM)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    if (name.isNotBlank() && mileage.isNotBlank()) {
                        viewModel.insertVehicle(name, brand, model, year, plate, engine, type, mileage.toIntOrNull() ?: 0)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = name.isNotBlank() && mileage.isNotBlank()
            ) {
                Text("Simpan Kendaraan")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleDetailScreen(navController: NavController, viewModel: MainViewModel, vehicleId: Int) {
    val vehicle by viewModel.getVehicle(vehicleId).collectAsStateWithLifecycle(null)
    
    val servicesFlow = remember(vehicleId) { viewModel.getServiceRecordsForVehicle(vehicleId) }
    val services by servicesFlow.collectAsStateWithLifecycle(emptyList())

    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(vehicle!!.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        val detailParts = listOf(vehicle!!.brand, vehicle!!.model, vehicle!!.plateNumber).filter { it.isNotBlank() }
                        if (detailParts.isNotEmpty()) {
                            Text(detailParts.joinToString(" • "), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("add_service/$vehicleId") },
                icon = { Icon(Icons.Default.Build, contentDescription = "Tambah Servis") },
                text = { Text("Catat Servis") },
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
            
            val lastOilChange = services.firstOrNull { it.serviceType == "Ganti Oli" }
            val nextTargetOil = (lastOilChange?.mileageAtService ?: vehicle!!.currentMileage) + if (vehicle!!.type == "MOTOR") 2000 else 5000
            val remainingOil = nextTargetOil - vehicle!!.currentMileage

            val lastCvtbelt = services.firstOrNull { it.serviceType == "Ganti CVT/Belt" }
            val nextTargetCvtbelt = (lastCvtbelt?.mileageAtService ?: vehicle!!.currentMileage) + if (vehicle!!.type == "MOTOR") 24000 else 40000
            val remainingCvtbelt = nextTargetCvtbelt - vehicle!!.currentMileage

            item {
                if (remainingOil <= 0) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Oli Perlu Diganti", color = MaterialTheme.colorScheme.onErrorContainer, fontWeight = FontWeight.Bold)
                                Text("Sudah melewati batas target $nextTargetOil KM", color = MaterialTheme.colorScheme.onErrorContainer, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Oil Card
                    Card(
                        modifier = Modifier.weight(1f).height(130.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Column {
                                Text("Oli Mesin", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                if (remainingOil > 0) {
                                    Text("$remainingOil KM", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text("Lagi", style = MaterialTheme.typography.labelSmall)
                                } else {
                                    Text("Ganti!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }

                    // CVT/Belt Card
                    Card(
                        modifier = Modifier.weight(1f).height(130.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Column {
                                Text(if (vehicle!!.type == "MOTOR") "V-Belt / CVT" else "Timing Belt", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                if (remainingCvtbelt > 0) {
                                    Text("$remainingCvtbelt KM", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                    Text("Lagi", style = MaterialTheme.typography.labelSmall)
                                } else {
                                    Text("Periksa!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    "Riwayat Servis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (services.isEmpty()) {
                item {
                    Text("Belum ada riwayat servis.", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                items(services) { service ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primaryContainer, androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(service.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                val date = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(service.dateMs))
                                Text("$date • Odo: ${service.mileageAtService} KM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.tertiary)
                            }
                            Text(service.serviceType, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                        }
                        if (service.notes.isNotBlank()) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            Text(service.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddServiceScreen(navController: NavController, viewModel: MainViewModel, vehicleId: Int) {
    val vehicle by viewModel.getVehicle(vehicleId).collectAsStateWithLifecycle(null)
    
    var title by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("Ganti Oli") }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    LaunchedEffect(vehicle) {
        if (vehicle != null && mileage.isEmpty()) {
            mileage = vehicle!!.currentMileage.toString()
        }
    }

    val serviceOptions = listOf("Ganti Oli", "Ganti CVT/Belt", "Ganti Busi", "Servis Rutin", "Lainnya")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Riwayat Servis") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Nama Tempat/Pembelian (ex: Bengkel X)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Jenis Servis", style = MaterialTheme.typography.titleSmall)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                serviceOptions.forEach { option ->
                    FilterChip(
                        selected = serviceType == option,
                        onClick = { serviceType = option },
                        label = { Text(option) }
                    )
                }
            }

            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { char -> char.isDigit() } },
                label = { Text("Odometer / Jarak Tempuh (KM)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Catatan Tambahan (Opsional)") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 4
            )

            Button(
                onClick = {
                    if (title.isNotBlank() && mileage.isNotBlank()) {
                        viewModel.insertServiceRecord(
                            vehicleId = vehicleId,
                            serviceType = serviceType,
                            title = title,
                            mileageAtService = mileage.toIntOrNull() ?: 0,
                            notes = notes
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = title.isNotBlank() && mileage.isNotBlank()
            ) {
                Text("Simpan Riwayat Servis")
            }
        }
    }
}
