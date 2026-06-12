# Servis Reminder - Edisi Bento Grid Kece 🚗

[![Build Status](https://img.shields.io/badge/Build-Success-emerald.svg)]()
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84.svg?logo=android)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF.svg?logo=kotlin)]()
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4.svg?logo=jetpackcompose)]()

Aplikasi Android super simpel buat bantu kamu mencatat riwayat servis motor atau mobil kesayangan dengan tampilan **Bento Grid** yang modern dan memanjakan mata. Aplikasi ini bisa memantau kondisi kendaraan secara akurat sesuai jenis mesinnya, melacak pengeluaran biaya, serta mengingatkan pajak STNK biar gak kelewat.

---

## 🎨 Tampilan Bento Grid yang Kece Banget

Aplikasi ini menggunakan susunan **Bento Grid Layout** yang rapi, adaptif, dan responsif:

*   **Dashboard Utama**: Menyapa kamu secara otomatis sesuai waktu (pagi/siang/sore/malam), menunjukkan jumlah kendaraan di garasi, dan menampilkan peringatan pajak STNK lewat kartu-kartu kecil yang tersusun rapi.
*   **Kartu Kendaraan Dinamis**: Kartu kendaraan di halaman utama disusun secara asimetris (ada yang lebar penuh dan ada yang berpasangan) biar tampilannya terasa dinamis dan gak ngebosenin.
*   **Menu Detail Kendaraan Bento**: Semua info penting dikelompokkan ke dalam blok bento yang fungsional:
    *   **Blok Odometer**: Menampilkan kilometer saat ini lengkap dengan bar status sisa kilometer menuju servis berikutnya.
    *   **Blok Hitung Mundur Pajak**: Menunjukkan sisa hari jatuh tempo pajak STNK dengan warna kartu yang otomatis berubah merah kalau waktu bayar sudah dekat.
    *   **Blok Pengeluaran**: Menghitung total semua biaya servis yang pernah kamu catat dalam nominal Rupiah berukuran besar.
    *   **Blok Spesifikasi**: Info teknis kendaraan seperti merek, model, CC mesin, plat nomor, tahun pembuatan, dan subtipe mesin.
*   **Lingkaran Kesehatan Kendaraan**: Widget melingkar (*Circular Progress*) yang menunjukkan seberapa sehat kendaraan kamu saat ini berdasarkan riwayat perawatan.
*   **Tipografi Outfit Kustom**: Menggunakan font **Outfit** yang bersih dan elegan langsung dari memori lokal aplikasi, jadi tampilannya tetap konsisten di HP Android versi berapa pun.
*   **Tema Warna Premium**:
    *   **Tema Terang**: Warna Slate-50 yang bersih dipadukan dengan aksen Charcoal/Slate-900 yang tegas.
    *   **Tema Gelap**: Latar Navy-Black pekat yang nyaman di mata untuk penggunaan malam hari.

---

## ⚙️ Fitur Unggulan

Aplikasi ini dibuat agar bisa diandalkan dan mudah digunakan sehari-hari:

### 1. Skor Kesehatan Kendaraan
Menghitung kondisi kesehatan kendaraan (0% s.d. 100%) secara otomatis berdasarkan sisa kilometer seluruh pengingat servis yang aktif:
*   **Kondisi Aman ($\ge 80\%$)**: Kendaraan sehat walafiat, siap diajak jalan jauh.
*   **Kondisi Cek Dulu ($50\% - 79\%$)**: Kondisi mulai menurun, sebaiknya mulai persiapkan jadwal servis.
*   **Waktunya Servis! ($< 50\%$)**: Kondisi kritis, yuk segera bawa ke bengkel kesayangan sebelum mogok.

### 2. Template Pengingat Servis Sesuai Jenis Mesin
Aplikasi secara cerdas membuat daftar pengingat servis bawaan yang sesuai dengan jenis mesin kendaraan kamu saat didaftarkan:
*   **Motor Matic**: Oli Mesin, Oli Gardan, V-Belt (CVT), Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Gigi (Semi-Automatic)**: Oli Mesin, Rantai & Gear, Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Kopling (Manual)**: Oli Mesin, Kampas Kopling, Rantai & Gear, Kampas Rem, Busi, Filter Udara, Air Radiator, Minyak Rem.
*   **Motor Listrik (EV)**: Drive Belt/Rantai, Oli Gearbox EV, Kampas Rem, Minyak Rem, Cek Kesehatan Baterai (SOH).
*   **Mobil Bensin / Diesel**: Oli Mesin, Filter Oli, Filter Udara, Timing Belt, Kampas Rem, Rotasi Ban, Oli Transmisi, Minyak Rem, Air Radiator, Servis AC, Busi.
*   **Mobil Hybrid**: Oli Mesin, Filter Oli, Filter Udara, Kampas Rem (Regeneratif), Rotasi Ban, Air Radiator, Servis AC, Cek Sistem & Baterai Hybrid, Busi.
*   **Mobil Listrik (EV)**: Filter AC Kabin, Cairan Gearbox EV, Kampas Rem (Regeneratif), Cek Kesehatan Baterai (SOH), Cairan Pendingin Baterai, Rotasi Ban, Servis AC.

### 3. Pengisian Formulir yang Pintar
*   **Input Adaptif**: Kolom input ganti oli atau timing belt otomatis disembunyikan kalau kamu memilih kendaraan tipe **Listrik (EV)**, karena motor/mobil listrik memang gak pakai oli mesin atau timing belt biasa.
*   **Batas Odometer Aman**: Pengisian odometer bernilai 0 KM tidak akan memicu eror. Odometer awal saat didaftarkan dikunci sebagai batas dasar, sehingga sisa kilometer ke target berikutnya terhitung secara linear seiring bertambahnya odometer kamu.

### 4. Pilihan Tema Warna Instan
Kamu bisa memilih tema warna aplikasi secara bebas di halaman Pengaturan:
*   **Sistem**: Mengikuti pengaturan tema gelap/terang bawaan HP kamu.
*   **Terang**: Tampilan bersih dengan latar belakang cerah.
*   **Gelap**: Tampilan hitam Navy yang nyaman di mata.
*(Tema berubah secara instan begitu diklik tanpa perlu keluar-masuk aplikasi!)*

### 5. Notifikasi & Ekspor Catatan
*   **Pengingat Harian**: Aplikasi otomatis memeriksa kondisi kendaraan sekali sehari di latar belakang. Kamu bakal dapat notifikasi jika pajak STNK sisa $\le 30$ hari atau ada servis yang sisa $\le 200$ KM lagi.
*   **Tes Notifikasi**: Cukup tekan tombol "Tes Sekarang" di Pengaturan untuk memastikan HP kamu bisa menerima pemberitahuan dari aplikasi dengan lancar.
*   **Bagikan Riwayat Servis**: Kamu bisa membagikan buku riwayat servis kendaraan kamu dalam format teks yang rapi ke WhatsApp, Email, atau aplikasi lainnya.

---

## 🤖 Otomatisasi GitHub Actions (CI/CD)

Repositori ini sudah dilengkapi alur kerja otomatisasi (**GitHub Actions**):
*   **Build Otomatis**: Setiap kali ada pembaruan kode yang di-push ke GitHub, sistem cloud akan otomatis merakit file APK debug & release. Kamu bisa langsung download hasilnya di tab **Actions** pada halaman repositori GitHub ini.
*   **Rilis Otomatis**: Jika kamu membuat tag versi baru (misalnya `v1.2.0`), GitHub Actions akan otomatis membuat halaman rilis download APK baru di tab **Releases** repositori kamu.

---

## 🛠️ Stack Teknologi
*   **Arsitektur**: MVVM (Model-View-ViewModel) yang rapi.
*   **Database**: Room Database SQLite yang terhubung secara reaktif dengan Kotlin Flows. Dilengkapi pengaman relasi data `CASCADE DELETE` (jika kendaraan dihapus, seluruh riwayat dan konfigurasinya otomatis ikut terhapus bersih).
*   **UI Framework**: Jetpack Compose (Material 3).
*   **Proses Latar Belakang**: WorkManager API.

---

## 🚀 Panduan Build APK Sendiri

Jika ingin merakit file APK sendiri, proyek sudah dilengkapi Gradle wrapper. Jalankan perintah ini di terminal direktori utama proyek kamu:

### A. Menggunakan PowerShell (Windows)
```powershell
# Bikin APK Debug
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew assembleDebug

# Bikin APK Release (Ukuran lebih kecil & teroptimasi)
$env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"; .\gradlew assembleRelease
```

### B. Menggunakan Command Prompt (CMD)
```cmd
# Bikin APK Debug
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew assembleDebug

# Bikin APK Release
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew assembleRelease
```

### Jalur Output File APK:
*   **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
*   **Release APK**: `app/build/outputs/apk/release/app-release.apk` (Hanya sekitar 12.7 MB!)

### Cara Pasang Instan ke Emulator:
1. Buka folder output berkas APK di atas.
2. **Seret dan lepas (drag-and-drop)** file APK langsung ke atas layar Emulator kamu yang sedang aktif.
3. Aplikasi akan langsung terpasang dan siap digunakan!
