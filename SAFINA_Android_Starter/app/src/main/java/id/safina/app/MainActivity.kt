package id.safina.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class SafinaModule(
    val title: String,
    val summary: String,
    val sourceTitle: String,
    val sourceUrl: String,
    val questions: List<Question>
)
private data class Question(val prompt: String, val options: List<String>, val correct: Int, val feedback: String)

private val modules = listOf(
    SafinaModule(
        "Mengenali Epistemic Flooding",
        "Epistemic flooding menggambarkan keadaan ketika arus informasi berlebihan membebani kemampuan memproses dan mengevaluasi informasi. Masalahnya bukan sekadar jumlah, tetapi kesulitan memilah relevansi, kredibilitas, dan bukti. Filter bubble dan echo chamber berkaitan dengan lingkungan informasi, tetapi bukan sinonim epistemic flooding.",
        "Kelas Cek Fakta MAFINDO — Filter Bubble dan Echo Chamber",
        "https://institute.mafindo.or.id/courses/kelascekfakta/",
        listOf(
            Question("Apakah semua informasi yang jumlahnya banyak otomatis berbahaya?", listOf("Ya, selalu.", "Tidak; dampaknya bergantung pada konteks dan kemampuan memilahnya.", "Ya, jika dari media sosial."), 1, "Jumlah saja tidak menentukan bahaya."),
            Question("Apakah informasi yang sering muncul pasti benar?", listOf("Ya.", "Tidak; frekuensi bukan bukti kebenaran.", "Ya, jika banyak komentar."), 1, "Informasi berulang tetap perlu diperiksa."),
            Question("Apa respons awal saat kewalahan oleh informasi?", listOf("Bagikan semuanya.", "Berhenti, tentukan kebutuhan, lalu periksa sumber.", "Anggap semua palsu."), 1, "Jeda dan pemilahan membantu evaluasi.")
        )
    ),
    SafinaModule(
        "Mengenali Manipulasi Informasi",
        "Informasi keliru dapat tersebar karena kesalahan, manipulasi sengaja, atau penggunaan informasi yang merugikan. Pesan bombastis, tekanan emosi, desakan menyebarkan, atau sumber tidak jelas patut diperiksa. Itu tanda kewaspadaan, bukan bukti tunggal bahwa informasi pasti palsu.",
        "Kelas Cek Fakta MAFINDO — Apa Itu Hoaks?",
        "https://institute.mafindo.or.id/courses/kelascekfakta/",
        listOf(
            Question("Pesan meminta kode OTP. Apa tindakan tepat?", listOf("Berikan.", "Jangan bagikan kode dan periksa kanal resmi.", "Kirim ke teman."), 1, "Kode OTP bersifat rahasia."),
            Question("Apakah judul provokatif otomatis membuktikan berita palsu?", listOf("Ya.", "Tidak; periksa isi, sumber, dan bukti.", "Ya jika viral."), 1, "Judul provokatif adalah alasan untuk memeriksa."),
            Question("Apa beda misinformasi dan disinformasi?", listOf("Tidak ada.", "Misinformasi keliru tanpa harus ada niat menipu; disinformasi sengaja menyesatkan.", "Misinformasi hanya video."), 1, "Niat dan konteks penyebaran penting.")
        )
    ),
    SafinaModule(
        "Jeda Sebelum Bereaksi",
        "Sebelum membagikan informasi, baca isi lengkap, periksa sumber, perhatikan keaslian media, dan kenali emosi yang muncul. Menunda respons memberi waktu untuk mengambil keputusan lebih baik.",
        "UNICEF Indonesia — #SabarSebelumSebar",
        "https://www.unicef.org/indonesia/id/lawan-hoaks",
        listOf(
            Question("Pesan berkata “Sebarkan sekarang sebelum dihapus!”", listOf("Langsung teruskan.", "Tahan dan periksa sumber serta bukti.", "Pasti palsu tanpa diperiksa."), 1, "Tekanan waktu adalah tanda untuk berhati-hati."),
            Question("Apakah pesan yang memancing takut pasti hoaks?", listOf("Ya.", "Tidak; emosi bukan bukti benar atau salah.", "Ya jika viral."), 1, "Periksa sumber dan bukti."),
            Question("Informasi belum dapat diverifikasi. Apa yang dilakukan?", listOf("Sebarkan sebagai dugaan.", "Jangan sebarkan sebagai fakta dan cari bukti.", "Ubah judul lalu sebarkan."), 1, "Jangan memperkuat klaim belum terverifikasi.")
        )
    ),
    SafinaModule(
        "Memeriksa Sebelum Memercayai",
        "Periksa sumber asli, tanggal dan konteks, bukti pendukung, serta laporan dari sumber tepercaya. Foto asli pun dapat digunakan dalam konteks yang salah. Hasil pemeriksaan bisa belum konklusif.",
        "Kelas Cek Fakta MAFINDO — Kanal Cek Fakta dan Audit Website",
        "https://institute.mafindo.or.id/courses/kelascekfakta/",
        listOf(
            Question("Foto bencana viral tanpa tanggal dan lokasi.", listOf("Langsung bagikan.", "Telusuri asal foto dan konteksnya.", "Pasti palsu."), 1, "Telusuri sumber, tanggal, lokasi, dan konteks."),
            Question("Satu akun anonim mengklaim berita benar. Cukup?", listOf("Cukup jika banyak pengikut.", "Tidak; cari bukti dan lakukan pemeriksaan silang.", "Cukup jika ada foto."), 1, "Pengikut dan foto bukan bukti tunggal."),
            Question("Bukti masih tidak cukup. Kesimpulan tepat?", listOf("Pasti benar.", "Pasti palsu.", "Belum dapat dipastikan."), 2, "Kesimpulan harus sebanding dengan bukti.")
        )
    ),
    SafinaModule(
        "Mengatur Paparan Digital",
        "Doomscrolling adalah kebiasaan terus menelusuri berita atau informasi negatif dalam waktu lama. Kelola paparan dengan menentukan waktu, membatasi notifikasi yang tidak perlu, memilih sumber, dan mengambil jeda. Tujuannya konsumsi informasi yang lebih terarah, bukan menghindari semua berita buruk.",
        "detikTV/detikHealth — Apa Itu Doomscrolling?",
        "https://health.detik.com/detiktv/d-8045199/video-apa-itu-doomscrolling-dan-bagaimana-cara-menyikapinya",
        listOf(
            Question("Terus menggulir berita meski tidak memperoleh informasi berguna.", listOf("Teruskan sampai habis.", "Berhenti dan tetapkan batas waktu.", "Hapus semua aplikasi permanen."), 1, "Jeda dan batas realistis dapat membantu."),
            Question("Apakah solusi terbaik selalu menghapus media sosial?", listOf("Ya.", "Tidak; sesuaikan batas dengan kebutuhan dan dampak.", "Semua media sosial berbahaya."), 1, "Tujuannya mengelola paparan."),
            Question("Bagaimana memilih akun yang layak diikuti?", listOf("Paling viral.", "Periksa kredibilitas, bukti, dan manfaat.", "Yang selalu setuju."), 1, "Utamakan sumber transparan dan berbukti.")
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SafinaApp() }
    }
}

@Composable
private fun SafinaApp() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("safina_local", 0) }
    var selectedFeature by remember { mutableStateOf("SAFINA Learn") }
    var currentModule by remember { mutableIntStateOf(0) }
    var refresh by remember { mutableIntStateOf(0) }
    val completed = remember(refresh) {
        (0 until modules.size).filter { prefs.getBoolean("module_$it", false) }.toSet()
    }
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFF0D6E70),
            secondary = androidx.compose.ui.graphics.Color(0xFFD6AD57),
            background = androidx.compose.ui.graphics.Color(0xFFF3F7F6)
        )
    ) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color(0xFF092C3B)), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text("SAFINA", color = androidx.compose.ui.graphics.Color(0xFFD6AD57), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Berhenti sejenak. Periksa. Pilih respons.", color = androidx.compose.ui.graphics.Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Perlindungan melalui kesiapan yang terarah", color = androidx.compose.ui.graphics.Color(0xFFD7EEEE))
                }
            }
            Text("RUANG UTAMA", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            val features = listOf("SAFINA Learn", "SAFINA Pause", "SAFINA Check", "SAFINA Boundary", "SAFINA Reflection", "SAFINA Alert")
            features.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { feature ->
                        OutlinedButton(
                            onClick = { selectedFeature = feature },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedFeature == feature) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surface
                            )
                        ) { Text(feature, fontSize = 12.sp) }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            when (selectedFeature) {
                "SAFINA Learn" -> LearnScreen(
                    currentModule, { currentModule = it }, completed,
                    onComplete = { index -> prefs.edit().putBoolean("module_$index", true).apply(); refresh++ },
                    onOpenSource = { url -> context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
                )
                "SAFINA Pause" -> PauseScreen()
                "SAFINA Check" -> CheckScreen(prefs)
                "SAFINA Boundary" -> BoundaryScreen(prefs)
                "SAFINA Reflection" -> ReflectionScreen(prefs)
                else -> AlertScreen()
            }
            Text("Prototipe Android awal • Fitur sistem lanjutan belum aktif", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
private fun LearnScreen(index: Int, onSelect: (Int) -> Unit, completed: Set<Int>, onComplete: (Int) -> Unit, onOpenSource: (String) -> Unit) {
    val m = modules[index]
    var answers by remember(index) { mutableStateOf(mapOf<Int, Int>()) }
    var submitted by remember(index) { mutableStateOf(false) }
    Card {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Text("SAFINA LEARN", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("${completed.size}/5 modul selesai", fontWeight = FontWeight.Bold)
            LinearProgressIndicator(progress = { completed.size / 5f }, modifier = Modifier.fillMaxWidth())
            Text("Pilih modul", fontWeight = FontWeight.Bold)
            modules.forEachIndexed { i, item ->
                val unlocked = i == 0 || completed.contains(i - 1)
                OutlinedButton(onClick = { onSelect(i) }, enabled = unlocked, modifier = Modifier.fillMaxWidth()) {
                    Text("${if (completed.contains(i)) "✓ " else ""}${i + 1}. ${item.title}")
                }
            }
            Divider()
            Text("Modul ${index + 1}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(m.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(m.summary)
            Text("Video referensi", fontWeight = FontWeight.Bold)
            Text(m.sourceTitle, fontSize = 13.sp)
            Button(onClick = { onOpenSource(m.sourceUrl) }, modifier = Modifier.fillMaxWidth()) { Text("Buka sumber video ↗") }
            Text("Video memerlukan internet. Ringkasan dan kuis tersedia di aplikasi.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("Latihan skenario", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            m.questions.forEachIndexed { qi, q ->
                Text("${qi + 1}. ${q.prompt}", fontWeight = FontWeight.SemiBold)
                q.options.forEachIndexed { oi, option ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                        RadioButton(selected = answers[qi] == oi, onClick = { answers = answers + (qi to oi); submitted = false })
                        Text(option, modifier = Modifier.padding(top = 12.dp))
                    }
                }
                if (submitted) {
                    Text(if (answers[qi] == q.correct) "Tepat. ${q.feedback}" else "Belum tepat. ${q.feedback}", color = if (answers[qi] == q.correct) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
            Button(onClick = {
                submitted = true
                if (answers.size == m.questions.size && m.questions.indices.all { answers[it] == m.questions[it].correct }) onComplete(index)
            }, modifier = Modifier.fillMaxWidth()) { Text("Periksa jawaban") }
            if (completed.contains(index)) Text("Modul selesai — boleh diulang.", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun PauseScreen() {
    var started by remember { mutableStateOf(false) }
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("SAFINA PAUSE", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Jeda sebelum bereaksi", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Apa yang membuatku ingin bereaksi cepat? Apakah sumber dan bukti sudah jelas? Apa risikonya jika aku langsung membagikannya?")
        Button(onClick = { started = !started }) { Text(if (started) "Jeda dimulai — ambil napas perlahan" else "Mulai latihan jeda") }
        if (started) Text("Tarik napas, beri waktu untuk berpikir, lalu periksa sumber sebelum bertindak.")
    } }
}

@Composable
private fun CheckScreen(prefs: android.content.SharedPreferences) {
    var claim by remember { mutableStateOf(prefs.getString("check_claim", "") ?: "") }
    var source by remember { mutableStateOf(prefs.getString("check_source", "") ?: "") }
    var saved by remember { mutableStateOf(false) }
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("SAFINA CHECK", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Panduan pemeriksaan manual", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(claim, { claim = it; saved = false }, label = { Text("Klaim atau pesan") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(source, { source = it; saved = false }, label = { Text("Sumber asal") }, modifier = Modifier.fillMaxWidth())
        Text("Checklist: cari sumber asli; periksa tanggal dan konteks; bandingkan sumber lain; pastikan bukti mendukung kesimpulan.")
        Button(onClick = { prefs.edit().putString("check_claim", claim).putString("check_source", source).apply(); saved = true }) { Text("Simpan catatan") }
        if (saved) Text("Catatan disimpan di perangkat.")
        Text("Checklist tidak membuktikan klaim benar atau salah secara otomatis.", fontSize = 12.sp)
    } }
}

@Composable
private fun BoundaryScreen(prefs: android.content.SharedPreferences) {
    var minutes by remember { mutableStateOf(prefs.getString("boundary_minutes", "60") ?: "60") }
    var plan by remember { mutableStateOf(prefs.getString("boundary_plan", "Berhenti setelah 30 menit dan evaluasi kebutuhan") ?: "") }
    var saved by remember { mutableStateOf(false) }
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("SAFINA BOUNDARY", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Rencana batas paparan", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(minutes, { minutes = it; saved = false }, label = { Text("Target menit per hari") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(plan, { plan = it; saved = false }, label = { Text("Rencana jeda") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        Button(onClick = { prefs.edit().putString("boundary_minutes", minutes).putString("boundary_plan", plan).apply(); saved = true }) { Text("Simpan rencana") }
        if (saved) Text("Rencana tersimpan.")
        Text("Versi awal menyimpan rencana; belum membatasi aplikasi lain.", fontSize = 12.sp)
    } }
}

@Composable
private fun ReflectionScreen(prefs: android.content.SharedPreferences) {
    var note by remember { mutableStateOf(prefs.getString("reflection_note", "") ?: "") }
    var action by remember { mutableStateOf(prefs.getString("reflection_action", "") ?: "") }
    var saved by remember { mutableStateOf(false) }
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("SAFINA REFLECTION", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Refleksi konsumsi informasi", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        OutlinedTextField(note, { note = it; saved = false }, label = { Text("Informasi yang menyita perhatian hari ini") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(action, { action = it; saved = false }, label = { Text("Tindakan yang ingin diubah") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        Button(onClick = { prefs.edit().putString("reflection_note", note).putString("reflection_action", action).apply(); saved = true }) { Text("Simpan refleksi") }
        if (saved) Text("Refleksi tersimpan di perangkat.")
    } }
}

@Composable
private fun AlertScreen() {
    var selected by remember { mutableStateOf("Periksa sumber sebelum membagikan") }
    var show by remember { mutableStateOf(false) }
    Card { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text("SAFINA ALERT", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Pengingat edukatif", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        listOf("Periksa sumber sebelum membagikan", "Jeda sebelum bereaksi", "Kelola paparan informasi").forEach {
            Row { RadioButton(selected == it, onClick = { selected = it; show = false }); Text(it, modifier = Modifier.padding(top = 12.dp)) }
        }
        Button(onClick = { show = true }) { Text("Tampilkan pengingat") }
        if (show) Text(selected, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Text("Ini pengingat di dalam aplikasi; notifikasi sistem Android belum dibuat.", fontSize = 12.sp)
    } }
}
