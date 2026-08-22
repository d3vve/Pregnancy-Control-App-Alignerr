package com.example.telematyka

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.Calendar

// plik kontrolujący pomiary danego dnia przez ciężarną (sprawdzanie, czy był dzisiaj pomiar i ewentualne wysyłanie powiadomień)
class MeasurementWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val dbHelper = DatabaseHelper(applicationContext)

        // Pobierz dzisiejszą datę w takim formacie jak w bazie danych
        val today = Calendar.getInstance()
        val todayDate = String.format("%02d.%02d.%d", today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH) + 1, today.get(Calendar.YEAR))

        // Sprawdza, czy w bazie jest już pomiar danego dnia
        if (!dbHelper.hasMeasurementsForDate(todayDate)) {
            sendNotification()
        }

        return Result.success()
    }

    private fun sendNotification() {
        val channelId = "pregnancy_reminders"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // W Android 8.0+ wymagany jest kanał powiadomień
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Przypomnienia", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Czas na pomiary!")
            .setContentText("Nie zapomnij dzisiaj uzupełnić ciśnienia, wagi i cukru w aplikacji.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }
}