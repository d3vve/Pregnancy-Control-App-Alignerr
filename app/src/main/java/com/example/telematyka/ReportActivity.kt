package com.example.telematyka

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private val reportsList = mutableListOf<ReportItem>()
    private lateinit var adapter: ArrayAdapter<ReportItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        val listViewReports = findViewById<ListView>(R.id.listViewReports)
        val btnGenerateReport = findViewById<Button>(R.id.btnGenerateReport)

        dbHelper = DatabaseHelper(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reportsList)
        listViewReports.adapter = adapter

        loadReports()

        btnGenerateReport.setOnClickListener {
            generateAndSaveReport()
        }

        listViewReports.setOnItemClickListener { _, _, position, _ ->
            val selectedReport = reportsList[position]
            deleteReport(selectedReport.id)
        }
    }

    private fun generateAndSaveReport() {
        val newNotes = dbHelper.getUnreportedNotes()
        val newMeasurements = dbHelper.getUnreportedMeasurements()

        if (newNotes.isEmpty() && newMeasurements.isEmpty()) {
            Toast.makeText(this, "Brak nowych wpisów do wygenerowania raportu.", Toast.LENGTH_LONG).show()
            return
        }

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val currentWeek = intent.getIntExtra("WEEK_KEY", 1)

        val reportBuilder = StringBuilder()
        reportBuilder.append("Tydzień ciąży: $currentWeek\n")
        reportBuilder.append("----------------------------\n")

        reportBuilder.append("NOWE OBJAWY I NOTATKI:\n")
        if (newNotes.isEmpty()) {
            reportBuilder.append("Brak nowych notatek.\n")
        } else {
            newNotes.forEach { reportBuilder.append("$it\n") }
        }

        reportBuilder.append("\n----------------------------\n")
        reportBuilder.append("NOWE POMIARY:\n")
        if (newMeasurements.isEmpty()) {
            reportBuilder.append("Brak nowych pomiarów.\n")
        } else {
            reportBuilder.append(newMeasurements)
        }
        reportBuilder.append("\n")

        val finalReportContent = reportBuilder.toString()
        val result = dbHelper.insertReport(currentDate, finalReportContent)

        if (result != -1L) {
            dbHelper.markDataAsReported()
            Toast.makeText(this, "Raport wygenerowany i zapisany!", Toast.LENGTH_SHORT).show()
            loadReports()
        } else {
            Toast.makeText(this, "Wystąpił błąd podczas zapisu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteReport(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Potwierdzenie")
            .setMessage("Czy na pewno chcesz usunąć ten raport?")
            .setPositiveButton("Usuń") { _, _ ->
                dbHelper.deleteReport(id)
                loadReports()
                Toast.makeText(this, "Raport usunięty!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Anuluj", null)
            .show()
    }

    private fun loadReports() {
        val savedReports = dbHelper.getReportsWithIds()
        reportsList.clear()
        reportsList.addAll(savedReports)
        adapter.notifyDataSetChanged()
    }
}