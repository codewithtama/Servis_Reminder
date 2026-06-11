# Servis Reminder - Premium Bento Grid Edition

[![Build Status](https://img.shields.io/badge/Build-Success-emerald.svg)]()
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?logo=kotlin)]()
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=jetpackcompose)]()

Aplikasi manajemen pemeliharaan kendaraan Android modern bergaya **Bento Grid** premium. Didesain secara spesifik untuk memantau status perawatan, melacak biaya, dan mengingatkan jadwal jatuh tempo pajak STNK secara cerdas. 

---

## 🎨 Premium Bento Grid Design System

Aplikasi ini menggunakan filosofi **Bento Grid Layout** asimetris, melangkah jauh dari tata letak tabel konvensional yang kaku menuju antarmuka dinamis dan interaktif.

*   **Welcome Dashboard Bento**: Kartu sambutan dinamis berbasis waktu (*time-sensitive greeting*) terintegrasi dengan ringkasan garasi (*Garage Inventory*) dan indikator alarm pajak STNK.
*   **Staggered Vehicle Grid**: Tata letak asimetris dinamis yang menyajikan daftar kendaraan secara bergantian antara kartu lebar penuh (*Full Width*) dan kartu ganda bersisian (*2-Column Grid*) untuk kontras estetika visual.
*   **Vehicle Metrics Bento**: Grid asimetris berdasar bobot visual pada halaman detail kendaraan yang menyatukan:
    *   **Odometer Block**: Dilengkapi dengan visual progress bar linear yang menunjukkan seberapa dekat kendaraan Anda dengan batas servis terdekat.
    *   **Tax Countdown Block**: Indikator hitung mundur pajak STNK dengan warna kartu adaptif berbasis status urgensi (Merah/Kuning/Hijau).
    *   **Expenses Block**: Akumulasi biaya perawatan real-time dalam visualisasi nominal Rupiah format besar dengan ikon dompet.
    *   **Specifications Block**: Ringkasan CC mesin, plat nomor, tahun pembuatan, dan tipe kendaraan.
*   **Custom Geometric Typography**: Tipografi premium **Outfit** terintegrasi secara luring (*offline*) di dalam aset lokal untuk menjamin visual yang elegan, bersih, dan konsisten di seluruh versi OS Android.
*   **Harmonious Color Palette**:
    *   **Mode Terang**: Latar belakang Slate-50 dengan aksen Charcoal/Slate-900 yang modern dan tegas.
    *   **Mode Gelap**: Latar belakang Navy-Black deep yang nyaman untuk keterbacaan malam hari dengan kontras kartu Navy-Slate yang halus.

---

## ⚙️ Fitur Utama & Keunggulan Logika Bisnis

Aplikasi ini dibangun dengan mengedepankan akurasi matematika kalkulasi dan keandalan fungsionalitas:

1.  **Pencatatan Riwayat Servis Komprehensif**: Mencatat lokasi bengkel, jenis servis, jarak tempuh (odometer), catatan khusus, dan biaya secara terorganisasi.
2.  **Odometer Baseline Tracking (`startingMileage`)**: Menghindari *bug* target yang bergeser dinamis. Aplikasi mengunci odometer awal saat kendaraan didaftarkan sebagai baseline statis, sehingga sisa kilometer ke target berikutnya berkurang secara linear saat odometer kendaraan naik.
3.  **Visual Overdue Warnings**: Menampilkan peringatan keterlambatan (kilometer minus) dengan progress bar bertransisi warna merah (*error container*) sebagai penunjuk tingkat urgensi tertinggi.
4.  **Timezone-Neutral Tax Countdown**: Perhitungan hari sisa jatuh tempo pajak menggunakan normalisasi Calendar ke waktu tengah malam (*midnight*), mencegah perubahan angka sisa hari secara acak yang disebabkan oleh pergeseran jam atau offset timezone.
5.  **Daily Background Work Scheduler**: Menggunakan **WorkManager** Android untuk menjalankan pengecekan background berkala 24 jam sekali guna mendeteksi dan memicu notifikasi pengingat pajak STNK (≤ 30 hari) dan batas servis (≤ 200 KM).
6.  **Digital Service Book Export**: Fitur ekspor terintegrasi melalui Share Intent native Android untuk membagikan buku riwayat servis kendaraan terformat rapi dalam bentuk teks ke WhatsApp, Email, atau aplikasi chat lainnya.

---

## 🛠️ Stack Teknologi & Arsitektur

*   **Arsitektur**: MVVM (Model-View-ViewModel) dengan pembagian tanggung jawab kode (*separation of concerns*) yang ketat.
*   **Database Lokal**: Room Database SQLite reaktif dengan dukungan `Kotlin Flows` untuk pembaharuan UI instan secara otomatis saat data berubah, serta Foreign Key dengan aksi `CASCADE DELETE` untuk integritas referensi data.
*   **UI Framework**: Jetpack Compose (Material 3) untuk visualisasi antarmuka modern deklaratif.
*   **Background Processing**: WorkManager API untuk fungsionalitas *offline background reminder*.

---

## 📁 Struktur Berkas Penting

*   **Data Layer**:
    *   [Models.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/data/Models.kt) - Entitas Room Database (`Vehicle`, `ServiceRecord`, `VehicleServiceConfig`).
    *   [Daos.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/data/Daos.kt) - Akses Query Database (`VehicleDao`, `ServiceDao`).
*   **UI Layer**:
    *   [Screens.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/screens/Screens.kt) - Berisi komponen UI Bento Grid (`DashboardScreen`, `VehicleDetailScreen`, `AddVehicleScreen`, `AddServiceScreen`, `EditVehicleScreen`).
    *   [Color.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/theme/Color.kt) - Token warna premium Slate & Navy.
    *   [Type.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/ui/theme/Type.kt) - Integrasi dan skala tipografi font **Outfit**.
*   **Business Logic Layer**:
    *   [MainViewModel.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/viewmodel/MainViewModel.kt) - State Management dan operasi bisnis database.
    *   [ReminderWorker.kt](file:///c:/Users/tamav/Desktop/PROYEK/Servis_Reminder/app/src/main/java/com/example/worker/ReminderWorker.kt) - Logika background task evaluasi notifikasi berkala.

---

## 🚀 Panduan Build & Pengujian APK

### Prasyarat
*   **Android Studio** Koala / Ladybug atau versi terbaru.
*   **Android SDK** 24 s.d. 36.

### Langkah Build Melalui Gradle Command

Proyek dikonfigurasi untuk ditandatangani otomatis menggunakan kunci debug lokal yang sudah disediakan. Jalankan perintah di direktori utama:

*   **Build Versi Debug**:
    ```bash
    .\gradlew assembleDebug
    ```
    *Output APK:* `app/build/outputs/apk/debug/app-debug.apk`
*   **Build Versi Release (R8/ProGuard Shrunk - 12.7 MB)**:
    ```bash
    .\gradlew assembleRelease
    ```
    *Output APK:* `app/build/outputs/apk/release/app-release.apk`

### Pemasangan Instan ke Emulator (Virtual Device)
Cukup buka folder output hasil build di atas menggunakan Windows File Explorer, lalu **seret dan lepas (drag-and-drop)** file `.apk` ke atas layar Emulator yang sedang aktif. Aplikasi **Servis Reminder** akan langsung terpasang dan siap digunakan.
