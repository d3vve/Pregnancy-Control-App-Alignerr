package com.example.telematyka

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.widget.ImageView
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    101
                )
            }
        }

        val pregnancyStartDateString = "05.11.2025"

        // Automatyczne obliczenie tygodnia na podstawie powyższej daty i dnia dzisiejszego
        val currentPregnancyWeek = calculateCurrentWeek(pregnancyStartDateString)

        val babyImage = findViewById<ImageView>(R.id.babyImageMain)
        val pregnancyWeek = findViewById<TextView>(R.id.PregnancyWeek)

        // Wyświetlamy obliczony tydzień
        pregnancyWeek.text = "Current week: $currentPregnancyWeek"


        val btnCalendar = findViewById<Button>(R.id.btnCalendar)
        val btnInfo = findViewById<Button>(R.id.btnInfo)
        val btnNotes = findViewById<Button>(R.id.btnNotes)
        val btnReport = findViewById<Button>(R.id.btnReport)

        btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        btnInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            // Przesłanie zmiennej currentPregnancyWeek pod kluczem "WEEK_KEY"
            intent.putExtra("WEEK_KEY", currentPregnancyWeek)
            startActivity(intent)
        }

        btnNotes.setOnClickListener {
            val intent = Intent(this, NotesActivity::class.java)
            startActivity(intent)
        }

        btnReport.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            intent.putExtra("WEEK_KEY", currentPregnancyWeek)
            startActivity(intent)
        }

        // wysyłanie powiadomień co 6 godzin, jeśli pacjentka nie wykonała pomiarów tego dnia
        val measurementWorkRequest = PeriodicWorkRequestBuilder<MeasurementWorker>(6, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueue(measurementWorkRequest)

        val imageResource = when (currentPregnancyWeek) {
            in 1..5 -> R.drawable.telematyka_1month_clear
            in 6..9 -> R.drawable.telematyka_2month_clear
            in 10..13 -> R.drawable.telematyka_3month_clear
            in 14..17 -> R.drawable.telematyka_4month_clear
            in 18..22 -> R.drawable.telematyka_5month_clear
            in 23..27 -> R.drawable.telematyka_6month_clear
            in 28..31 -> R.drawable.telematyka_7month_clear
            in 32..35 -> R.drawable.telematyka_8month_clear
            in 36..42 -> R.drawable.telematyka_9month_clear
            else -> R.drawable.telematyka_1month_clear //Domyślne zdjęcie w przypadku anomalii
        }

        babyImage.setImageResource(imageResource)
    }

    private fun calculateCurrentWeek(startDateString: String): Int {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val startDate = sdf.parse(startDateString)
        val currentDate = Date() // Dzisiejsza data i godzina
        // Różnica czasu w milisekundach
        val diffInMillis = currentDate.time - startDate.time
        // Zamiana milisekund na pełne dni
        val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)
        // dni dzielone przez 7 + 1
        val calculatedWeek = (diffInDays / 7).toInt() + 1
        // Zabezpieczenie przed wyjściem poza standardowy czas trwania ciąży
        if (calculatedWeek > 42) 42 else calculatedWeek

        return calculatedWeek
    }
}