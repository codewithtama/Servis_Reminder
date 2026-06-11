# Servis Reminder

Aplikasi Android modern untuk mencatat riwayat pemeliharaan, memantau batas target servis, melacak pengeluaran biaya perawatan, serta mengingatkan waktu jatuh tempo pajak STNK untuk kendaraan mobil dan motor Anda.

## Fitur Utama

- **Pencatatan Riwayat Servis**: Simpan detail perawatan kendaraan Anda lengkap dengan tanggal, odometer (kilometer), deskripsi tempat, catatan tambahan, dan nominal biaya servis.
- **Kustomisasi Pengingat Dinamis**: Buat, edit, dan hapus pengingat kustom untuk berbagai jenis servis (seperti Oli Mesin, CVT/V-Belt, Kampas Rem, Busi, Ban, dll.) dengan interval kilometer yang dapat disesuaikan per kendaraan.
- **Pemantauan Target Servis**: Aplikasi otomatis mengalkulasi status dan sisa jarak tempuh sebelum Anda harus melakukan servis berikutnya secara real-time.
- **Pengingat Pajak STNK**: Hitung mundur hari menuju tanggal jatuh tempo pajak STNK tahunan dengan kartu peringatan visual saat mendekati batas waktu.
- **Pelacakan Biaya (Expense Tracking)**: Pantau total akumulasi biaya perawatan yang telah dikeluarkan untuk masing-masing kendaraan Anda.
- **Update Odometer Instan**: Perbarui odometer kendaraan dengan cepat langsung dari halaman detail kendaraan tanpa harus mencatat transaksi servis baru.

## Teknologi Utama

- **Jetpack Compose**: Framework UI deklaratif modern untuk performa visual yang premium dan responsif.
- **Room Database**: Penyimpanan database SQLite lokal yang reaktif dengan dukungan `Kotlin Flows`.
- **Material Design 3**: Desain antarmuka premium mengikuti standar desain Android terbaru.
- **MVVM Architecture**: Pemisahan tanggung jawab kode yang rapi untuk pemeliharaan dan pengujian jangka panjang.

## Cara Menjalankan Aplikasi

**Prasyarat:** [Android Studio](https://developer.android.com/studio) terbaru dipasang di komputer Anda.

1. Buka Android Studio.
2. Pilih **Open** dan arahkan ke direktori project ini.
3. Tunggu Gradle selesai melakukan sinkronisasi (*sync*) dependencies.
4. Jalankan aplikasi di Emulator Android atau perangkat fisik Anda.
