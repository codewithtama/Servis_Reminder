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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.viewmodel.MainViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, viewModel: MainViewModel) {
    val vehicles by viewModel.allVehicles.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                androidx.core.content.ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasNotificationPermission = isGranted
        }
    )

    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Servis Reminder", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(
                        onClick = { viewModel.triggerOneTimeReminderCheck(context) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Uji Notifikasi",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Banner Peringatan Izin Notifikasi
            if (!hasNotificationPermission && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsOff,
                            contentDescription = "Peringatan",
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Izin Notifikasi Dimatikan",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Ketuk untuk mengaktifkan kembali agar pengingat servis berfungsi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            if (vehicles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada kendaraan. Tambahkan sekarang!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), 
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
    var oilInterval by remember { mutableStateOf("2000") }
    var beltInterval by remember { mutableStateOf("24000") }
    var hasUserEditedOil by remember { mutableStateOf(false) }
    var hasUserEditedBelt by remember { mutableStateOf(false) }
    var taxDateMs by remember { mutableStateOf(0L) }

    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    if (taxDateMs != 0L) {
        calendar.timeInMillis = taxDateMs
    }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, yearSelected, monthSelected, daySelected ->
            val selectedCal = java.util.Calendar.getInstance()
            selectedCal.set(yearSelected, monthSelected, daySelected)
            taxDateMs = selectedCal.timeInMillis
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

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
                    onClick = { 
                        type = "MOTOR"
                        if (!hasUserEditedOil) oilInterval = "2000"
                        if (!hasUserEditedBelt) beltInterval = "24000"
                    },
                    label = { Text("Motor") },
                    leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null) }
                )
                FilterChip(
                    selected = type == "MOBIL",
                    onClick = { 
                        type = "MOBIL"
                        if (!hasUserEditedOil) oilInterval = "5000"
                        if (!hasUserEditedBelt) beltInterval = "40000"
                    },
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

            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            val taxDateText = if (taxDateMs == 0L) "Pilih Tanggal Pajak STNK" else sdf.format(java.util.Date(taxDateMs))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = taxDateText,
                    onValueChange = {},
                    label = { Text("Jatuh Tempo Pajak STNK") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal", tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                )
            }

            OutlinedTextField(
                value = oilInterval,
                onValueChange = { 
                    oilInterval = it.filter { char -> char.isDigit() }
                    hasUserEditedOil = true
                },
                label = { Text("Batas Interval Ganti Oli (KM)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = beltInterval,
                onValueChange = { 
                    beltInterval = it.filter { char -> char.isDigit() }
                    hasUserEditedBelt = true
                },
                label = { Text(if (type == "MOTOR") "Batas Interval V-Belt/CVT (KM)" else "Batas Interval Timing Belt (KM)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    if (name.isNotBlank() && mileage.isNotBlank()) {
                        viewModel.insertVehicle(
                            name = name,
                            brand = brand,
                            model = model,
                            year = year,
                            plateNumber = plate,
                            engineType = engine,
                            type = type,
                            currentMileage = mileage.toIntOrNull() ?: 0,
                            taxDueDateMs = taxDateMs,
                            oilIntervalKm = oilInterval.toIntOrNull() ?: (if (type == "MOTOR") 2000 else 5000),
                            beltIntervalKm = beltInterval.toIntOrNull() ?: (if (type == "MOTOR") 24000 else 40000)
                        )
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

    val configsFlow = remember(vehicleId) { viewModel.getConfigsForVehicle(vehicleId) }
    val configs by configsFlow.collectAsStateWithLifecycle(emptyList())

    var showUpdateOdoDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var showAddConfigDialog by remember { mutableStateOf(false) }
    var showUpdateTaxDialog by remember { mutableStateOf(false) }
    var showEditServiceDialog by remember { mutableStateOf(false) }
    var showDeleteServiceConfirmDialog by remember { mutableStateOf(false) }
    
    var newOdoValue by remember { mutableStateOf("") }
    var configType by remember { mutableStateOf("") }
    var configInterval by remember { mutableStateOf("") }
    var taxDateMs by remember { mutableStateOf(0L) }

    var selectedServiceToEdit by remember { mutableStateOf<ServiceRecord?>(null) }
    var selectedServiceToDelete by remember { mutableStateOf<ServiceRecord?>(null) }
    var editServiceTitle by remember { mutableStateOf("") }
    var editServiceType by remember { mutableStateOf("") }
    var editServiceMileage by remember { mutableStateOf("") }
    var editServiceCost by remember { mutableStateOf("") }
    var editServiceNotes by remember { mutableStateOf("") }

    LaunchedEffect(showUpdateOdoDialog) {
        if (showUpdateOdoDialog && vehicle != null) {
            newOdoValue = vehicle!!.currentMileage.toString()
        }
    }

    LaunchedEffect(showUpdateTaxDialog) {
        if (showUpdateTaxDialog && vehicle != null) {
            taxDateMs = vehicle!!.taxDueDateMs
        }
    }

    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    if (taxDateMs != 0L) {
        calendar.timeInMillis = taxDateMs
    }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, yearSelected, monthSelected, daySelected ->
            val selectedCal = java.util.Calendar.getInstance()
            selectedCal.set(yearSelected, monthSelected, daySelected)
            viewModel.updateVehicleTaxDate(vehicle!!, selectedCal.timeInMillis)
            showUpdateTaxDialog = false
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    if (showUpdateOdoDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateOdoDialog = false },
            title = { Text("Update Odometer") },
            text = {
                Column {
                    Text("Masukkan kilometer kendaraan saat ini:", modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = newOdoValue,
                        onValueChange = { newOdoValue = it.filter { char -> char.isDigit() } },
                        label = { Text("Odometer (KM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mileage = newOdoValue.toIntOrNull()
                        if (mileage != null) {
                            viewModel.updateVehicleMileage(vehicle!!, mileage)
                            showUpdateOdoDialog = false
                        }
                    },
                    enabled = newOdoValue.isNotBlank()
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showUpdateOdoDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Hapus Kendaraan") },
            text = { Text("Apakah Anda yakin ingin menghapus kendaraan '${vehicle!!.name}'? Seluruh riwayat servis kendaraan ini juga akan dihapus permanen.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteVehicle(vehicle!!)
                        showDeleteConfirmDialog = false
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showAddConfigDialog) {
        AlertDialog(
            onDismissRequest = { showAddConfigDialog = false },
            title = { Text("Tambah Pengingat Servis") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = configType,
                        onValueChange = { configType = it },
                        label = { Text("Jenis Servis (ex: Ganti Kampas Rem)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = configInterval,
                        onValueChange = { configInterval = it.filter { char -> char.isDigit() } },
                        label = { Text("Batas Interval (KM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val interval = configInterval.toIntOrNull()
                        if (configType.isNotBlank() && interval != null) {
                            viewModel.insertServiceConfig(vehicleId, configType, interval)
                            configType = ""
                            configInterval = ""
                            showAddConfigDialog = false
                        }
                    },
                    enabled = configType.isNotBlank() && configInterval.isNotBlank()
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    configType = ""
                    configInterval = ""
                    showAddConfigDialog = false 
                }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showDeleteServiceConfirmDialog && selectedServiceToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteServiceConfirmDialog = false
                selectedServiceToDelete = null
            },
            title = { Text("Hapus Riwayat Servis") },
            text = { Text("Apakah Anda yakin ingin menghapus catatan servis '${selectedServiceToDelete!!.serviceType}' di '${selectedServiceToDelete!!.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteServiceRecord(selectedServiceToDelete!!)
                        showDeleteServiceConfirmDialog = false
                        selectedServiceToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteServiceConfirmDialog = false
                    selectedServiceToDelete = null
                }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showEditServiceDialog && selectedServiceToEdit != null) {
        val editServiceOptions = remember(configs) {
            val list = configs.map { it.serviceType }.toMutableList()
            if (!list.contains("Lainnya")) {
                list.add("Lainnya")
            }
            list
        }

        AlertDialog(
            onDismissRequest = { 
                showEditServiceDialog = false
                selectedServiceToEdit = null
            },
            title = { Text("Edit Riwayat Servis") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = editServiceTitle,
                        onValueChange = { editServiceTitle = it },
                        label = { Text("Nama Tempat/Pembelian") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Text("Jenis Servis", style = MaterialTheme.typography.titleSmall)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        editServiceOptions.forEach { option ->
                            FilterChip(
                                selected = editServiceType == option,
                                onClick = { editServiceType = option },
                                label = { Text(option) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = editServiceMileage,
                        onValueChange = { editServiceMileage = it.filter { char -> char.isDigit() } },
                        label = { Text("Odometer (KM)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editServiceCost,
                        onValueChange = { editServiceCost = it.filter { char -> char.isDigit() } },
                        label = { Text("Biaya (Rp)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    val editCostDouble = editServiceCost.toDoubleOrNull()
                    if (editCostDouble != null && editCostDouble > 0) {
                        val decFormat = java.text.DecimalFormat("#,###")
                        val symbols = java.text.DecimalFormatSymbols(java.util.Locale("in", "ID"))
                        symbols.groupingSeparator = '.'
                        decFormat.decimalFormatSymbols = symbols
                        val formattedCost = "Rp " + decFormat.format(editCostDouble)
                        Text(
                            text = formattedCost,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                        )
                    }

                    OutlinedTextField(
                        value = editServiceNotes,
                        onValueChange = { editServiceNotes = it },
                        label = { Text("Catatan Tambahan (Opsional)") },
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mileageVal = editServiceMileage.toIntOrNull()
                        if (editServiceTitle.isNotBlank() && mileageVal != null) {
                            viewModel.updateServiceRecord(
                                selectedServiceToEdit!!.copy(
                                    title = editServiceTitle,
                                    serviceType = editServiceType,
                                    mileageAtService = mileageVal,
                                    cost = editServiceCost.toDoubleOrNull() ?: 0.0,
                                    notes = editServiceNotes
                                )
                            )
                            if (vehicle != null && mileageVal > vehicle!!.currentMileage) {
                                viewModel.updateVehicleMileage(vehicle!!, mileageVal)
                            }
                            showEditServiceDialog = false
                            selectedServiceToEdit = null
                        }
                    },
                    enabled = editServiceTitle.isNotBlank() && editServiceMileage.isNotBlank()
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showEditServiceDialog = false
                    selectedServiceToEdit = null
                }) {
                    Text("Batal")
                }
            }
        )
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
                },
                actions = {
                    IconButton(onClick = { navController.navigate("edit_vehicle/${vehicle!!.id}") }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Kendaraan", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus Kendaraan", tint = MaterialTheme.colorScheme.error)
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
        val totalExpense = services.sumOf { it.cost }
        val daysRemaining = if (vehicle!!.taxDueDateMs > 0L) {
            val diffMs = vehicle!!.taxDueDateMs - System.currentTimeMillis()
            diffMs / (24 * 60 * 60 * 1000)
        } else {
            null
        }

        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
            
            // Tax Alert Card
            if (daysRemaining != null) {
                item {
                    val isDue = daysRemaining <= 30
                    if (isDue) {
                        val cardColor = if (daysRemaining <= 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.tertiaryContainer
                        val textColor = if (daysRemaining <= 0) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onTertiaryContainer
                        Card(
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (daysRemaining < 0) "Pajak STNK Telat!" else "Pajak STNK Segera Jatuh Tempo!",
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Text(
                                    text = if (daysRemaining < 0) "Sudah telat ${kotlin.math.abs(daysRemaining)} hari." else "Tinggal $daysRemaining hari lagi.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = textColor
                                )
                            }
                        }
                    }
                }
            }

            // Odometer Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (vehicle!!.type == "MOTOR") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                                    contentDescription = vehicle!!.type,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Odometer Saat Ini", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                Text("${vehicle!!.currentMileage} KM", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        IconButton(
                            onClick = { showUpdateOdoDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Update Odometer",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // Tax Odometer & Expenses Cards
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Tax Card
                    Card(
                        modifier = Modifier
                            .weight(1.5f)
                            .height(110.dp)
                            .clickable { datePickerDialog.show() },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Jatuh Tempo Pajak", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Pilih Tanggal Pajak",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Column {
                                if (vehicle!!.taxDueDateMs > 0L) {
                                    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
                                    Text(sdf.format(java.util.Date(vehicle!!.taxDueDateMs)), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                    val remainingText = if (daysRemaining != null) {
                                        if (daysRemaining < 0) "Telat ${kotlin.math.abs(daysRemaining)} Hari" else "$daysRemaining Hari Lagi"
                                    } else ""
                                    Text(remainingText, style = MaterialTheme.typography.labelSmall, color = if (daysRemaining != null && daysRemaining <= 30) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                                } else {
                                    Text("Belum diatur", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                    Text("Klik untuk atur", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    // Total Expense Card
                    Card(
                        modifier = Modifier.weight(1.5f).height(110.dp),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Pengeluaran", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            Column {
                                val decFormat = java.text.DecimalFormat("#,###")
                                val symbols = java.text.DecimalFormatSymbols(java.util.Locale("in", "ID"))
                                symbols.groupingSeparator = '.'
                                decFormat.decimalFormatSymbols = symbols
                                val costText = "Rp " + decFormat.format(totalExpense)
                                Text(costText, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text("Riwayat Servis", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }

            // Section: Target Servis
            item {
                Text(
                    "Target Servis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (configs.isEmpty()) {
                item {
                    Text("Belum ada konfigurasi pengingat.", modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                        configs.forEach { config ->
                            val lastService = services.firstOrNull { it.serviceType.lowercase() == config.serviceType.lowercase() }
                            val nextTarget = (lastService?.mileageAtService ?: vehicle!!.currentMileage) + config.intervalKm
                            val remaining = nextTarget - vehicle!!.currentMileage

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (remaining <= 0) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = config.serviceType,
                                            fontWeight = FontWeight.Bold,
                                            color = if (remaining <= 0) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "Interval: ${config.intervalKm} KM • Terakhir: ${lastService?.mileageAtService ?: "-"} KM",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = if (remaining <= 0) MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        if (remaining <= 0) {
                                            Text("Ganti!", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                                        } else {
                                            Text("$remaining KM", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                            Text("Lagi", style = MaterialTheme.typography.labelSmall)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section: Pengaturan Pengingat Servis
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Konfigurasi Pengingat",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = { showAddConfigDialog = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tambah")
                    }
                }
            }

            if (configs.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        configs.forEach { config ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp, 4.dp, 8.dp, 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(config.serviceType, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                        Text("Interval: ${config.intervalKm} KM", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                                    }
                                    IconButton(onClick = { viewModel.deleteServiceConfig(config) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Hapus Konfigurasi", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section: Riwayat Servis
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Riwayat Servis",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(
                        onClick = {
                            val decFormat = java.text.DecimalFormat("#,###")
                            val symbols = java.text.DecimalFormatSymbols(java.util.Locale("in", "ID"))
                            symbols.groupingSeparator = '.'
                            decFormat.decimalFormatSymbols = symbols
                            val totalCostText = "Rp " + decFormat.format(totalExpense)

                            val sb = StringBuilder()
                            sb.append("==================================\n")
                            sb.append("      BUKU SERVIS DIGITAL\n")
                            sb.append("==================================\n")
                            sb.append("Kendaraan   : ${vehicle!!.name}")
                            if (vehicle!!.brand.isNotBlank() || vehicle!!.model.isNotBlank()) {
                                sb.append(" (${vehicle!!.brand} ${vehicle!!.model})")
                            }
                            sb.append("\n")
                            if (vehicle!!.plateNumber.isNotBlank()) {
                                sb.append("Plat Nomor  : ${vehicle!!.plateNumber}\n")
                            }
                            sb.append("Odometer    : ${vehicle!!.currentMileage} KM\n")
                            sb.append("Total Biaya : $totalCostText\n")
                            sb.append("----------------------------------\n\n")
                            sb.append("DAFTAR RIWAYAT SERVIS:\n\n")

                            if (services.isEmpty()) {
                                sb.append("(Belum ada riwayat servis)\n")
                            } else {
                                services.forEachIndexed { index, record ->
                                    val dateText = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(record.dateMs))
                                    val recordCost = "Rp " + decFormat.format(record.cost)
                                    sb.append("${index + 1}. ${record.serviceType}\n")
                                    sb.append("   Tanggal  : $dateText\n")
                                    sb.append("   Tempat   : ${record.title}\n")
                                    sb.append("   Odometer : ${record.mileageAtService} KM\n")
                                    if (record.cost > 0) {
                                        sb.append("   Biaya    : $recordCost\n")
                                    }
                                    if (record.notes.isNotBlank()) {
                                        sb.append("   Catatan  : ${record.notes}\n")
                                    }
                                    sb.append("\n")
                                }
                            }
                            sb.append("----------------------------------\n")
                            sb.append("Laporan dibuat otomatis menggunakan aplikasi Servis Reminder.")

                            val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(android.content.Intent.EXTRA_SUBJECT, "Buku Servis Digital - ${vehicle!!.name}")
                                putExtra(android.content.Intent.EXTRA_TEXT, sb.toString())
                            }
                            context.startActivity(android.content.Intent.createChooser(shareIntent, "Bagikan Buku Servis"))
                        }
                    ) {
                        Text("Ekspor")
                    }
                }
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
                        Column {
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
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(service.serviceType, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                    if (service.cost > 0) {
                                        val decFormat = java.text.DecimalFormat("#,###")
                                        val symbols = java.text.DecimalFormatSymbols(java.util.Locale("in", "ID"))
                                        symbols.groupingSeparator = '.'
                                        decFormat.decimalFormatSymbols = symbols
                                        Text("Rp " + decFormat.format(service.cost), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    }
                                }
                            }
                            if (service.notes.isNotBlank()) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                Text(service.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(12.dp))
                            }
                            
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = {
                                        selectedServiceToEdit = service
                                        editServiceTitle = service.title
                                        editServiceType = service.serviceType
                                        editServiceMileage = service.mileageAtService.toString()
                                        editServiceCost = service.cost.toInt().toString()
                                        editServiceNotes = service.notes
                                        showEditServiceDialog = true
                                    }
                                ) {
                                    Text("Edit", style = MaterialTheme.typography.labelMedium)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                TextButton(
                                    onClick = {
                                        selectedServiceToDelete = service
                                        showDeleteServiceConfirmDialog = true
                                    }
                                ) {
                                    Text("Hapus", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
                                }
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
fun AddServiceScreen(navController: NavController, viewModel: MainViewModel, vehicleId: Int) {
    val vehicle by viewModel.getVehicle(vehicleId).collectAsStateWithLifecycle(null)
    
    val configsFlow = remember(vehicleId) { viewModel.getConfigsForVehicle(vehicleId) }
    val configs by configsFlow.collectAsStateWithLifecycle(emptyList())

    var title by remember { mutableStateOf("") }
    var serviceType by remember { mutableStateOf("Ganti Oli") }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var costStr by remember { mutableStateOf("") }

    LaunchedEffect(vehicle) {
        if (vehicle != null && mileage.isEmpty()) {
            mileage = vehicle!!.currentMileage.toString()
        }
    }

    val serviceOptions = remember(configs) {
        val list = configs.map { it.serviceType }.toMutableList()
        if (!list.contains("Lainnya")) {
            list.add("Lainnya")
        }
        list
    }

    LaunchedEffect(serviceOptions) {
        if (serviceOptions.isNotEmpty() && !serviceOptions.contains(serviceType)) {
            serviceType = serviceOptions.first()
        }
    }

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
                value = costStr,
                onValueChange = { costStr = it.filter { char -> char.isDigit() } },
                label = { Text("Biaya Servis (Rp) (ex: 150000)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            val costDouble = costStr.toDoubleOrNull()
            if (costDouble != null && costDouble > 0) {
                val decFormat = java.text.DecimalFormat("#,###")
                val symbols = java.text.DecimalFormatSymbols(java.util.Locale("in", "ID"))
                symbols.groupingSeparator = '.'
                decFormat.decimalFormatSymbols = symbols
                val formattedCost = "Rp " + decFormat.format(costDouble)
                Text(
                    text = formattedCost,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }

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
                            notes = notes,
                            cost = costStr.toDoubleOrNull() ?: 0.0
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVehicleScreen(navController: NavController, viewModel: MainViewModel, vehicleId: Int) {
    val vehicle by viewModel.getVehicle(vehicleId).collectAsStateWithLifecycle(null)

    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var engine by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("MOTOR") }
    var mileage by remember { mutableStateOf("") }
    var taxDateMs by remember { mutableStateOf(0L) }

    LaunchedEffect(vehicle) {
        if (vehicle != null) {
            name = vehicle!!.name
            brand = vehicle!!.brand
            model = vehicle!!.model
            year = vehicle!!.year
            plate = vehicle!!.plateNumber
            engine = vehicle!!.engineType
            type = vehicle!!.type
            mileage = vehicle!!.currentMileage.toString()
            taxDateMs = vehicle!!.taxDueDateMs
        }
    }

    if (vehicle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val context = androidx.compose.ui.platform.LocalContext.current
    val calendar = java.util.Calendar.getInstance()
    if (taxDateMs != 0L) {
        calendar.timeInMillis = taxDateMs
    }
    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, yearSelected, monthSelected, daySelected ->
            val selectedCal = java.util.Calendar.getInstance()
            selectedCal.set(yearSelected, monthSelected, daySelected)
            taxDateMs = selectedCal.timeInMillis
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Kendaraan") },
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
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Panggilan Kendaraan") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Merek") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = year, onValueChange = { year = it.filter { char -> char.isDigit() } }, label = { Text("Tahun") }, modifier = Modifier.weight(1f), singleLine = true)
                OutlinedTextField(value = plate, onValueChange = { plate = it }, label = { Text("Nomor Plat") }, modifier = Modifier.weight(1f), singleLine = true)
            }
            
            OutlinedTextField(value = engine, onValueChange = { engine = it }, label = { Text("Kapasitas Mesin") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            
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

            val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            val taxDateText = if (taxDateMs == 0L) "Pilih Tanggal Pajak STNK" else sdf.format(java.util.Date(taxDateMs))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = taxDateText,
                    onValueChange = {},
                    label = { Text("Jatuh Tempo Pajak STNK") },
                    trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal", tint = MaterialTheme.colorScheme.primary) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                )
            }

            Button(
                onClick = {
                    if (name.isNotBlank() && mileage.isNotBlank()) {
                        viewModel.updateVehicle(
                            vehicle!!.copy(
                                name = name,
                                brand = brand,
                                model = model,
                                year = year,
                                plateNumber = plate,
                                engineType = engine,
                                type = type,
                                currentMileage = mileage.toIntOrNull() ?: 0,
                                taxDueDateMs = taxDateMs
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                enabled = name.isNotBlank() && mileage.isNotBlank()
            ) {
                Text("Simpan Perubahan")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
