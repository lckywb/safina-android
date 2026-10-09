# SAFINA Android Starter

Proyek awal Android native Kotlin + Jetpack Compose dengan enam fitur SAFINA:
Learn, Pause, Check, Boundary, Reflection, Alert.

## Build APK melalui GitHub Actions
Workflow tersedia di `.github/workflows/android.yml`.
Setelah file diunggah ke repositori branch `main`, buka tab Actions, pilih
"Build SAFINA Android APK", lalu pilih Run workflow. Setelah sukses, unduh
artifact `safina-debug-apk` dan ekstrak untuk memperoleh `app-debug.apk`.

## Catatan prototipe
- Learn memiliki lima modul, kuis, tautan sumber video, dan penyimpanan progres lokal.
- Check adalah checklist manual, bukan pemeriksa fakta otomatis.
- Boundary menyimpan rencana pengguna, belum membatasi aplikasi lain.
- Alert adalah pengingat di layar, belum notifikasi sistem.
- APK debug untuk uji internal, bukan rilis Play Store.
