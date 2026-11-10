package com.example.telematyka

import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var listViewVisits: ListView
    private val visitsList = mutableListOf<CalendarItem>()
    private lateinit var visitsAdapter: ArrayAdapter<CalendarItem>

    private lateinit var listViewMeasurements: ListView
    private val measurementsList = mutableListOf<CalendarItem>()
    private lateinit var measurementsAdapter: ArrayAdapter<CalendarItem>

    // NEW: Variable storing the currently clicked date on the calendar
    private var currentSelectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        dbHelper = DatabaseHelper(this)

        listViewVisits = findViewById(R.id.listViewVisits)
        listViewMeasurements = findViewById(R.id.listViewMeasurements)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        visitsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, visitsList)
        listViewVisits.adapter = visitsAdapter

        measurementsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, measurementsList)
        listViewMeasurements.adapter = measurementsAdapter

        // NEW: When opening the calendar, we set today's date as selected
        val today = Calendar.getInstance()
        currentSelectedDate = String.format("%02d.%02d.%d", today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH) + 1, today.get(Calendar.YEAR))

        // Load data for today
        loadDataFromDatabase()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%02d.%02d.%d", dayOfMonth, month + 1, year)

            // NEW: Update the selected date and immediately refresh lists on the screen!
            currentSelectedDate = selectedDate
            loadDataFromDatabase()

            val todayDate = String.format("%02d.%02d.%d", today.get(Calendar.DAY_OF_MONTH), today.get(Calendar.MONTH) + 1, today.get(Calendar.YEAR))

            val options = if (selectedDate == todayDate) {
                arrayOf("Schedule visit", "Add today's measurements")
            } else {
                arrayOf("Schedule visit")
            }

            AlertDialog.Builder(this)
                .setTitle("Select action for: $selectedDate")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> showAddVisitDialog(selectedDate)
                        1 -> showAddMeasurementsDialog(selectedDate)
                    }
                }.show()
        }

        listViewVisits.setOnItemClickListener { _, _, position, _ ->
            val item = visitsList[position]
            showItemOptionsDialog(item)
        }

        listViewMeasurements.setOnItemClickListener { _, _, position, _ ->
            val item = measurementsList[position]
            showItemOptionsDialog(item)
        }
    }

    private fun showItemOptionsDialog(item: CalendarItem) {
        AlertDialog.Builder(this)
            .setTitle("Options")
            .setItems(arrayOf("Edit", "Delete")) { _, which ->
                when (which) {
                    0 -> if (item.isVisit) showEditVisitDialog(item) else showEditMeasurementDialog(item)
                    1 -> deleteItem(item)
                }
            }.show()
    }

    // function for adding visits
    private fun showAddVisitDialog(date: String) {
        val input = EditText(this).apply { hint = "E.g. Ultrasound, 14:00" }
        AlertDialog.Builder(this)
            .setTitle("Schedule visit")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                if (input.text.isNotEmpty()) {
                    dbHelper.insertVisit(date, input.text.toString())
                    loadDataFromDatabase()
                }
            }.setNegativeButton("Cancel", null).show()
    }

    // function for adding measurements
    private fun showAddMeasurementsDialog(date: String) {
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 40, 50, 10) }
        val bpInput = EditText(this).apply { hint = "Blood pressure (e.g. 120/80)" }
        val weightInput = EditText(this).apply { hint = "Weight (kg)"; inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val sugarInput = EditText(this).apply { hint = "Blood sugar (mg/dL)"; inputType = InputType.TYPE_CLASS_NUMBER }

        layout.addView(bpInput); layout.addView(weightInput); layout.addView(sugarInput)

        AlertDialog.Builder(this)
            .setTitle("Measurements from: $date")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val dateTime = "$date ${timeFormat.format(Date())}"
                dbHelper.insertMeasurement(dateTime, bpInput.text.toString(), weightInput.text.toString(), sugarInput.text.toString())
                loadDataFromDatabase()
            }.setNegativeButton("Cancel", null).show()
    }

    // function for editing visits
    private fun showEditVisitDialog(item: CalendarItem) {
        val input = EditText(this).apply { setText(item.val1) }
        AlertDialog.Builder(this)
            .setTitle("Edit visit")
            .setView(input)
            .setPositiveButton("Update") { _, _ ->
                dbHelper.updateVisit(item.id, input.text.toString())
                loadDataFromDatabase()
            }.setNegativeButton("Cancel", null).show()
    }

    // function for editing measurements
    private fun showEditMeasurementDialog(item: CalendarItem) {
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 40, 50, 10) }
        val bpInput = EditText(this).apply { setText(item.val1) }
        val weightInput = EditText(this).apply { setText(item.val2); inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL }
        val sugarInput = EditText(this).apply { setText(item.val3); inputType = InputType.TYPE_CLASS_NUMBER }

        layout.addView(bpInput); layout.addView(weightInput); layout.addView(sugarInput)

        AlertDialog.Builder(this)
            .setTitle("Edit measurement from: ${item.date}")
            .setView(layout)
            .setPositiveButton("Update") { _, _ ->
                dbHelper.updateMeasurement(item.id, bpInput.text.toString(), weightInput.text.toString(), sugarInput.text.toString())
                loadDataFromDatabase()
            }.setNegativeButton("Cancel", null).show()
    }

    // function for deleting entries
    private fun deleteItem(item: CalendarItem) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this entry?")
            .setPositiveButton("Delete") { _, _ ->
                if (item.isVisit) dbHelper.deleteVisit(item.id) else dbHelper.deleteMeasurement(item.id)
                loadDataFromDatabase()
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            }.setNegativeButton("Cancel", null).show()
    }

    private fun loadDataFromDatabase() {
        val allItems = dbHelper.getCalendarItems()

        visitsList.clear()
        // CHANGE: Added condition "it.date.startsWith(currentSelectedDate)"
        visitsList.addAll(allItems.filter { it.isVisit && it.date.startsWith(currentSelectedDate) })

        measurementsList.clear()
        // CHANGE: Added condition "it.date.startsWith(currentSelectedDate)"
        measurementsList.addAll(allItems.filter { !it.isVisit && it.date.startsWith(currentSelectedDate) })

        visitsAdapter.notifyDataSetChanged()
        measurementsAdapter.notifyDataSetChanged()
    }
}