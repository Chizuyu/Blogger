# Blogger - Modern Android Application

Blogger adalah aplikasi Android modern yang dirancang sebagai platform media sosial dan berbagi artikel. Aplikasi ini dibangun dengan fokus pada performa yang optimal, antarmuka pengguna yang responsif, dan kapabilitas penanganan data secara asinkronus menggunakan arsitektur modern standar industri.

## Fitur Utama
* **Autentikasi Aman:** Sistem Registrasi dan Login yang terintegrasi dengan JWT (JSON Web Token).
* **Manajemen Konten (CRUD):** Kemampuan penuh bagi user untuk Membuat (Add), Membaca (List & Detail), Mengubah (Edit), dan Menghapus (Delete) artikel mereka sendiri.
* **Sistem Interaksi:** Fitur menyukai (*Like/Unlike*) artikel, melihat daftar artikel yang disukai, serta melihat profil dan daftar user lain.
* **Manajemen Profil:** Fitur kustomisasi foto profil dan pembaruan informasi akun user.
* **Sistem Komentar:** Manajemen interaksi diskusi pada setiap artikel.
* **Following System:** Fitur mengikuti profile untuk page Following Post *(Planned)*
* *[Upcoming]* **Paging & Real-time Chat:** Optimalisasi pemuatan data dengan Pagination dan fitur pesan instan *(Planned)*.

## Arsitektur & Tech Stack
Aplikasi ini menerapkan pemisahan kode yang ketat untuk memastikan skalabilitas dan kemudahan pengujian:
* **Architecture:** MVVM (Model-View-ViewModel) Pattern
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose
* **Asynchronous Tool:** Kotlin Coroutines
* **Networking:** Retrofit (REST API Consumption)



##  Cara Menjalankan Project Secara Lokal
1. Clone repository ini ke komputer:
   ```bash
   git clone https://Chizuyu/Blogger.git

## Catatan Pengembangan (Development Note)
Proyek ini pada awalnya dikembangkan secara lokal sebelum diintegrasikan dengan Version Control. Oleh karena itu, riwayat commit awal terangkum dalam satu "Initial Commit". Pengembangan fitur baru ke depannya (seperti fitur Komentar dan ASP.NET Web API) akan diimplementasikan menggunakan Git workflow yang disiplin dengan riwayat commit yang bertahap.
