# TV App

Aplikasi browser tontonan TV sederhana untuk technical test Intern Mobile Engineer. 
Datanya diambil dari [TVMaze API](https://www.tvmaze.com/api) 
ada halaman list show, detail show (poster, summary, tanggal tayang, plus bonus season/episode & cast), 
serta share action dari halaman detail.

Di luar requirement utama, saya menambahkan splash screen, onboarding, dan login/sign up 
menggunakan Firebase Authentication (wajib verifikasi email dulu sebelum bisa masuk ke halaman utama)  
untuk demo sudah saya buat videonya di link paling bawah file ini.

## Cara menjalankan apps

1. Clone repo ini, buka pakai Android Studio (dites di Android Studio versi terbaru, minSdk 23, compileSdk/targetSdk 35).
2. Kalau cuma mau coba fitur utamanya (browsing show — list & detail), langsung Run aja,
   tidak perlu setup tambahan apa-apa.
   Halaman List jadi start destination begitu splash & onboarding dilewati (atau langsung sign in dulu, tergantung alur).
4. Kalau mau coba fitur Sign In/Sign Up-nya, anda butuh `google-services.json` (sudah saya up juga di repo):
6. Sync gradle, run ke emulator/device Android 6.0 (API 23) ke atas.

Unit test bisa dijalankan lewat `./gradlew test` atau langsung klik run di Android Studio (ada di `app/src/test`).

## Keputusan Arsitektur

Polanya MVVM yang cukup standar: **UI (Compose) → ViewModel → Repository → data source** (Retrofit ke TVMaze API, Firebase SDK buat auth). Tiap layer cuma kenal interface layer di bawahnya (`ShowRepository`, `AuthRepository`), bukan implementasinya langsung, biar gampang di-swap/di-test.

Beberapa keputusan yang mungkin worth dijelaskan :

- **State loading/error/success** dibungkus satu sealed interface `UiState<T>` yang dipakai bersamaan di semua ViewModel (list, detail, auth), daripada tiap fitur bikin state class sendiri-sendiri.
- **yang tidak saya gunakan DI framework** (Hilt/Koin) sengaja — scope app-nya masih kecil buat butuh itu. Dependency di-inject lewat default constructor argument, contoh: `class ShowRepositoryImpl(private val api: TvMazeApiService = NetworkModule.api)`. Kalau appnya berkembang lebih jauh, ini bagian pertama yang bakal aku migrasiin ke Hilt.
- Networking pakai Retrofit + kotlinx.serialization (bukan Gson/Moshi), agar satu ekosistem dengan serialization yang sudah dipakai di model.
- Image loading menggunakan Coil, navigasi antar layar menggunakan Navigation Compose (single Activity, banyak Composable screen).
- HTML tag di field `summary` (kayak `<p>`, `<b>`) di-strip manual sebelum ditampilin ke `Text`, biar gak keluar tag mentahnya.
- Rating (`rating.average`) di-handle nullable dari awal (model + formatter-nya), soalnya beberapa show memang belum ada rating-nya.

## Kalau Ada Waktu Lebih, saya akan...

- menambahkan pagination di list (sekarang cuma load 1 halaman ~250 show sesuai minimum requirement, belum lanjut ke halaman berikutnya).
- menambahkan lebih banyak unit test — sekarang baru ada test buat `RatingFormatter` sama `ShowListViewModel` (pakai fake repository), belum nyentuh repository/network layer atau ViewModel di sisi auth.
- menambahkan caching lokal (Room) biar list show gak fetch ulang tiap buka app, sekalian dukungan offline.
- Kasih cooldown timer di tombol "Resend verification email" biar gak bisa di-spam terus-terusan (sekarang cuma mengandalkan rate limit bawaan Firebase).
- Bikin pesan error API lebih spesifik lagi, sekarang masih agak generic buat beberapa kasus.
- Dark/light theme toggle — sekarang onboarding & layar auth full dark ngikutin desain yang dikasih, belum ngikut sistem.

## File Lain

Sesuai instruksi tes, ada juga `AI_LOG.md`, `CODE_REVIEW.md`, dan `REFLECTION.md` di root repo ini.

## Video Walkthrough

[TODO: (https://youtu.be/Q-Rujmv5Ilo)]
