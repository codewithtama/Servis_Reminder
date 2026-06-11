# Servis Reminder - Premium Bento Grid Edition

[![Build Status](https://img.shields.io/badge/Build-Success-emerald.svg)]()
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?logo=kotlin)]()
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=jetpackcompose)]()

Aplikasi manajemen perawatan kendaraan Android modern dengan desain **Bento Grid** premium. Didesain secara spesifik untuk memantau status servis secara presisi berdasarkan subtipe mesin, melacak pengeluaran biaya, serta mengingatkan pajak STNK secara cerdas dan tepat waktu.

---

## 🎨 Premium Bento Grid Design System

Aplikasi ini mengadopsi filosofi antarmuka **Bento Grid Layout** asimetris, menggabungkan metrik visual ke dalam susunan grid yang rapi, adaptif, dan responsif:

*   **Welcome Dashboard Bento**: Menyajikan salam dinamis berbasis waktu (*time-sensitive greeting*), ringkasan kepemilikan garasi (*Garage Inventory*), serta counter peringatan pajak STNK dalam micro-cards.
*   **Staggered Vehicle Grid**: Kartu kendaraan pada dashboard disusun asimetris, bergantian antara kartu lebar penuh (*Full Width*) dan kartu ganda bersisian (*2-Column Grid*) untuk memberikan dinamisme visual.
*   **Vehicle Metrics Bento Dashboard**: Halaman detail kendaraan menyatukan informasi penting dalam layout bento asimetris:
    *   **Odometer Block**: Dilengkapi dengan visual progress bar linear yang menunjukkan seberapa dekat jarak tempuh dengan batas servis terdekat.
    *   **Tax Countdown Block**: Indikator hitung mundur pajak STNK dengan transisi warna latar belakang adaptif sesuai urgensi (Merah/Kuning/Hijau).
    *   **Expenses Block**: Akumulasi total pengeluaran perawatan dalam visualisasi nominal Rupiah format besar dengan ikon dompet.
    *   **Specifications Block**: Detail spesifikasi teknis seperti merek, model, CC mesin, plat nomor, tahun pembuatan, dan subtipe mesin kendaraan.
*   **Circular Vehicle Health Indicator**: Widget visual melingkar (*Circular Progress Indicator*) yang dinamis pada halaman detail kendaraan untuk memetakan kesehatan kendaraan secara real-time.
*   **Custom Geometric Typography**: Tipografi premium **Outfit** terintegrasi secara luring (*offline*) di dalam aset lokal untuk menjamin visual yang elegan, bersih, dan konsisten di seluruh versi OS Android.
*   **Aesthetic Palette Colors**:
    *   **Mode Terang**: Latar belakang Slate-50 dengan aksen Charcoal/Slate-900 yang modern dan tegas.
    *   **Mode Gelap**: Latar belakang Navy-Black deep yang nyaman dengan kontras permukaan kartu Navy-Slate yang halus.
*   **Launcher Icon Premium**: Ikon aplikasi kustom yang elegan dengan simbol **Kunci Pas (Wrench)** putih bersilang dengan **Roda Gigi (Gear)** biru langit di atas latar belakang Navy Slate berpola grid halus, menggantikan robot hijau bawaan standar.

---

## ⚙️ Fitur Utama & Keunggulan Logika Bisnis

Aplikasi ini dibangun dengan mengedepankan akurasi matematika kalkulasi dan keandalan fungsionalitas:

### 1. Skor Kesehatan Kendaraan (Vehicle Health Score)
Sistem menghitung persentase kesehatan kendaraan (0% s.d. 100%) secara dinamis berdasarkan sisa jarak tempuh pada seluruh target servis aktif (`intervalKm > 0`):
*   **Perhitungan Linear**: Kesehatan setiap item dihitung dari rasio sisa kilometer terhadap intervalnya. Jika servis baru saja diselesaikan, rasio bernilai 1.0 (sangat sehat). Jika terlewat, bernilai 0.0.
*   **Warna Indikator Urgensi**:
    *   **Emerald Green ($\ge 80\%$)**: Kondisi Prima.
    *   **Amber Orange ($50\% - 79\%$)**: Butuh Perhatian.
    *   **Crimson Red ($< 50\%$)**: Kritis / Bahaya (Waktunya Servis).
*   **Distribusi Informasi**: Status kesehatan ditampilkan dalam bentuk badge status di garasi depan (Dashboard) dan widget Circular Progress dinamis di samping Odometer (Detail Kendaraan).

### 2. Klasifikasi Subtipe & Template Servis Bawaan (Default Config Templates)
Sistem membedakan jenis servis default secara otomatis saat kendaraan didaftarkan untuk memastikan efisiensi dan relevansi perawatan:
*   **Motor Matic**: Oli Mesin, Oli Gardan, V-Belt (CVT), Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Gigi (Semi-Automatic)**: Oli Mesin, Rantai & Gear, Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Kopling (Manual)**: Oli Mesin, Kampas Kopling, Rantai & Gear, Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Listrik (EV)**: Drive Belt/Rantai, Oli Gearbox EV, Kampas Rem, Minyak Rem, Cek Kesehatan Baterai (SOH).
*   **Mobil Bensin / Diesel**: Oli Mesin, Filter Oli, Filter Udara, Timing Belt, Kampas Rem, Rotasi Ban, Oli Transmisi, Minyak Rem, Air Radiator, Servis AC, Busi.
*   **Mobil Hybrid**: Oli Mesin, Filter Oli, Filter Udara, Kampas Rem (Regeneratif - lebih awet), Rotasi Ban, Air Radiator, Servis AC, Cek Sistem & Baterai Hybrid, Busi.
*   **Mobil Listrik (EV)**: Filter AC Kabin, Cairan Gearbox EV, Kampas Rem (Regeneratif - sangat awet), Cek Kesehatan Baterai (SOH), Cairan Pendingin Baterai, Rotasi Ban, Servis AC.

### 3. Smart Input Forms & Odometer O Safeguard
*   **Penyembunyian Field Dinamis**: Kolom input **Batas Interval Ganti Oli (KM)** disembunyikan secara otomatis ketika subtipe **Listrik (EV)** dipilih pada formulir tambah/edit kendaraan. Kolom input V-Belt/Timing Belt juga disembunyikan untuk Mobil Listrik karena tidak memiliki timing belt.
*   **Abaikan Interval 0 KM**: Sistem secara cerdas menyaring konfigurasi dengan `intervalKm <= 0` pada sisa target servis di UI maupun background. Odometer bernilai **0 KM** tidak lagi memicu kesalahan logika atau peringatan "Terlewat 0 KM" untuk item yang dinonaktifkan.
*   **Odometer Baseline Tracking (`startingMileage`)**: Mengunci odometer awal saat kendaraan didaftarkan sebagai baseline statis, sehingga sisa kilometer ke target berikutnya berkurang secara linear saat odometer kendaraan naik tanpa terjadi pergeseran target dinamis.

### 4. Background Processing & Digital Book Export
*   **Daily Background Work Scheduler**: Menggunakan **WorkManager** Android untuk menjalankan pengecekan background berkala 24 jam sekali guna mendeteksi dan memicu notifikasi pengingat pajak STNK (≤ 30 hari) dan batas servis (≤ 200 KM).
*   **Digital Service Book Export**: Fitur ekspor terintegrasi melalui Share Intent native Android untuk membagikan buku riwayat servis kendaraan terformat rapi dalam bentuk teks ke WhatsApp, Email, atau aplikasi chat lainnya.

---

## 🛠️ Stack Teknologi & Arsitektur

*   **Arsitektur**: MVVM (Model-View-ViewModel) dengan pembagian tanggung jawab kode (*separation of concerns*) yang ketat.
*   **Database Lokal**: Room Database SQLite reaktif dengan dukungan `Kotlin Flows` untuk pembaharuan UI instan secara otomatis saat data berubah, serta Foreign Key dengan aksi `CASCADE DELETE` untuk integritas referensi data.
*   **UI Framework**: Jetpack Compose (Material 3) untuk visualisasi antarmuka modern deklaratif.
*   **Background Processing**: WorkManager API untuk fungsionalitas *offline background reminder*.

---

## 📁 Struktur Berkas Penting

*   **Data Layer**:
    *   [Models.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/data/Models.kt) - Entitas Room Database (`Vehicle`, `ServiceRecord`, `VehicleServiceConfig`) lengkap dengan kolom `subType`.
    *   [Daos.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/data/Daos.kt) - Akses Query Database (`VehicleDao`, `ServiceDao`).
*   **UI Layer**:
    *   [Screens.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/screens/Screens.kt) - Halaman UI Bento Grid (`DashboardScreen`, `VehicleDetailScreen`, `AddVehicleScreen`, `AddServiceScreen`, `EditVehicleScreen`).
    *   [Color.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/theme/Color.kt) - Token warna premium Slate & Navy.
    *   [Type.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/theme/Type.kt) - Integrasi dan skala tipografi font **Outfit**.
*   **Business Logic Layer**:
    *   [MainViewModel.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/viewmodel/MainViewModel.kt) - State Management dan operasi bisnis database.
    *   [ReminderWorker.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/worker/ReminderWorker.kt) - Logika background task evaluasi notifikasi berkala.

---

## 🚀 Panduan Build & Pengujian APK

Proyek telah dilengkapi dengan Gradle wrapper resmi (`gradlew` dan `gradlew.bat`) di root direktori. Jalankan perintah di direktori utama menggunakan terminal Anda:

### A. Menggunakan PowerShell (Rekomendasi)
```powershell
# Jalankan kompilasi APK Debug
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew assembleDebug

# Jalankan kompilasi APK Release (R8/ProGuard Shrunk)
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew assembleRelease
```

### B. Menggunakan Command Prompt (CMD)
```cmd
# Jalankan kompilasi APK Debug
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew assembleDebug

# Jalankan kompilasi APK Release (R8/ProGuard Shrunk)
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew assembleRelease
```

### Jalur Output Berkas APK:
*   **Debug APK**: [app-debug.apk](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/build/outputs/apk/debug/app-debug.apk) (Sekitar 19 MB)
*   **Release APK**: [app-release.apk](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/build/outputs/apk/release/app-release.apk) (Hanya 12.7 MB - Teroptimasi & Mengecil berkat R8/ProGuard!)

### Pemasangan Instan ke Emulator (Virtual Device)
1. Buka folder output berkas APK di atas melalui Windows File Explorer.
2. **Seret dan lepas (drag-and-drop)** berkas `app-debug.apk` atau `app-release.apk` langsung ke atas layar Emulator yang sedang aktif.
3. Aplikasi **Servis Reminder** dengan ikon kustom premium akan terpasang secara instan dan siap digunakan.
