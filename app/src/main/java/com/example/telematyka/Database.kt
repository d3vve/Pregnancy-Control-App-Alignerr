package com.example.telematyka

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "PregnancyApp.db"
        private const val DATABASE_VERSION = 4

        // notatki
        private const val TABLE_NOTES = "notes_table"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_WEEK = "week"
        private const val COLUMN_IS_REPORTED = "is_reported"

        // wizyty
        private const val TABLE_VISITS = "visits_table"
        private const val COLUMN_VISIT_ID = "visit_id"
        private const val COLUMN_VISIT_DATE = "visit_date"
        private const val COLUMN_VISIT_DESC = "description"

        // raporty
        private const val TABLE_REPORTS = "reports_table"
        private const val COLUMN_REPORT_ID = "report_id"
        private const val COLUMN_REPORT_DATE = "report_date"
        private const val COLUMN_REPORT_CONTENT = "report_content"

        // pomiary
        private const val TABLE_MEASUREMENTS = "measurements_table"
        private const val COLUMN_MEAS_ID = "meas_id"
        private const val COLUMN_MEAS_DATE = "meas_date"
        private const val COLUMN_MEAS_BP = "blood_pressure"
        private const val COLUMN_MEAS_WEIGHT = "weight"
        private const val COLUMN_MEAS_SUGAR = "sugar"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableNotesQuery = ("CREATE TABLE $TABLE_NOTES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_CONTENT TEXT,"
                + "$COLUMN_DATE INTEGER,"
                + "$COLUMN_WEEK INTEGER,"
                + "$COLUMN_IS_REPORTED INTEGER DEFAULT 0)")
        db?.execSQL(createTableNotesQuery)

        val createTableVisitsQuery = ("CREATE TABLE $TABLE_VISITS ("
                + "$COLUMN_VISIT_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_VISIT_DATE TEXT,"
                + "$COLUMN_VISIT_DESC TEXT)")
        db?.execSQL(createTableVisitsQuery)

        val createTableReportsQuery = ("CREATE TABLE $TABLE_REPORTS ("
                + "$COLUMN_REPORT_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_REPORT_DATE TEXT,"
                + "$COLUMN_REPORT_CONTENT TEXT)")
        db?.execSQL(createTableReportsQuery)

        val createTableMeasurementsQuery = ("CREATE TABLE $TABLE_MEASUREMENTS ("
                + "$COLUMN_MEAS_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_MEAS_DATE TEXT,"
                + "$COLUMN_MEAS_BP TEXT,"
                + "$COLUMN_MEAS_WEIGHT TEXT,"
                + "$COLUMN_MEAS_SUGAR TEXT,"
                + "$COLUMN_IS_REPORTED INTEGER DEFAULT 0)")
        db?.execSQL(createTableMeasurementsQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_VISITS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_REPORTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MEASUREMENTS")
        onCreate(db)
    }

    fun insertNote(content: String, date: Long, week: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CONTENT, content)
        values.put(COLUMN_DATE, date)
        values.put(COLUMN_WEEK, week)
        val success = db.insert(TABLE_NOTES, null, values)
        db.close()
        return success
    }

    fun getAllNotes(): List<String> {
        val notesList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val noteContent = cursor.getString(1)
                val timestamp = cursor.getLong(2)
                val formattedDate = sdf.format(Date(timestamp))
                notesList.add("[$formattedDate] $noteContent")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun insertVisit(date: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_VISIT_DATE, date)
        values.put(COLUMN_VISIT_DESC, description)
        val success = db.insert(TABLE_VISITS, null, values)
        db.close()
        return success
    }

    fun getAllVisits(): List<String> {
        val visitsList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_VISITS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val visitDate = cursor.getString(1)
                val visitDesc = cursor.getString(2)
                visitsList.add("• $visitDate: $visitDesc")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return visitsList
    }

    fun insertReport(date: String, content: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_REPORT_DATE, date)
        values.put(COLUMN_REPORT_CONTENT, content)
        val success = db.insert(TABLE_REPORTS, null, values)
        db.close()
        return success
    }

    fun getAllReports(): List<String> {
        val reportsList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_REPORTS ORDER BY $COLUMN_REPORT_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(1)
                val content = cursor.getString(2)
                reportsList.add("Raport z dnia: $date\n\n$content")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return reportsList
    }

    fun insertMeasurement(date: String, bp: String, weight: String, sugar: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MEAS_DATE, date)
        values.put(COLUMN_MEAS_BP, bp)
        values.put(COLUMN_MEAS_WEIGHT, weight)
        values.put(COLUMN_MEAS_SUGAR, sugar)
        val success = db.insert(TABLE_MEASUREMENTS, null, values)
        db.close()
        return success
    }

    fun hasMeasurementsForDate(date: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MEASUREMENTS WHERE $COLUMN_MEAS_DATE LIKE ?", arrayOf("$date%"))
        val hasRows = cursor.count > 0
        cursor.close()
        db.close()
        return hasRows
    }

    fun getAllMeasurementsForReport(): String {
        val builder = StringBuilder()
        val selectQuery = "SELECT * FROM $TABLE_MEASUREMENTS ORDER BY $COLUMN_MEAS_ID ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(1)
                val bp = cursor.getString(2)
                val weight = cursor.getString(3)
                val sugar = cursor.getString(4)
                builder.append("• Data: $date | Ciśnienie: $bp | Waga: $weight kg | Cukier: $sugar mg/dL\n")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return if (builder.isEmpty()) "Brak zapisanych pomiarów." else builder.toString()
    }

    // pobiera tylko te notatki, które jeszcze nie były raportowane (is_reported = 0)
    fun getUnreportedNotes(): List<String> {
        val notesList = ArrayList<String>()
        val selectQuery = "SELECT * FROM $TABLE_NOTES WHERE $COLUMN_IS_REPORTED = 0 ORDER BY $COLUMN_DATE ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val noteContent = cursor.getString(1)
                val timestamp = cursor.getLong(2)
                val formattedDate = sdf.format(Date(timestamp))
                notesList.add("[$formattedDate] $noteContent")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }

    // pobiera tylko te pomiary, które jeszcze nie były raportowane (is_reported = 0)
    fun getUnreportedMeasurements(): String {
        val builder = StringBuilder()
        val selectQuery = "SELECT * FROM $TABLE_MEASUREMENTS WHERE $COLUMN_IS_REPORTED = 0 ORDER BY $COLUMN_MEAS_ID ASC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val date = cursor.getString(1)
                val bp = cursor.getString(2)
                val weight = cursor.getString(3)
                val sugar = cursor.getString(4)
                builder.append("• Data: $date | Ciśnienie: $bp | Waga: $weight kg | Cukier: $sugar mg/dL\n")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return builder.toString() // Zwraca puste, jeśli nie ma nowych
    }

    // po wygenerowaniu raportu zamienia status wszystkich dodanych właśnie rzeczy na zaraportowane
    fun markDataAsReported() {
        val db = this.writableDatabase
        db.execSQL("UPDATE $TABLE_NOTES SET $COLUMN_IS_REPORTED = 1 WHERE $COLUMN_IS_REPORTED = 0")
        db.execSQL("UPDATE $TABLE_MEASUREMENTS SET $COLUMN_IS_REPORTED = 1 WHERE $COLUMN_IS_REPORTED = 0")
        db.close()
    }

    // usuwanie notatek
    fun deleteNote(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NOTES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // aktualizowanie notatek
    fun updateNote(id: Int, newContent: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_CONTENT, newContent) }
        db.update(TABLE_NOTES, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // pobieranie wszystkich notatek wraz z ich ID
    fun getNotesWithIds(): List<NoteItem> {
        val list = mutableListOf<NoteItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NOTES ORDER BY $COLUMN_DATE DESC", null)
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val content = cursor.getString(1)
                val date = sdf.format(Date(cursor.getLong(2)))
                list.add(NoteItem(id, "[$date] $content", content))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun deleteVisit(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_VISITS, "$COLUMN_VISIT_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun updateVisit(id: Int, newDesc: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply { put(COLUMN_VISIT_DESC, newDesc) }
        db.update(TABLE_VISITS, values, "$COLUMN_VISIT_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteMeasurement(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_MEASUREMENTS, "$COLUMN_MEAS_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun updateMeasurement(id: Int, bp: String, weight: String, sugar: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MEAS_BP, bp)
            put(COLUMN_MEAS_WEIGHT, weight)
            put(COLUMN_MEAS_SUGAR, sugar)
        }
        db.update(TABLE_MEASUREMENTS, values, "$COLUMN_MEAS_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    // pobieranie wszystkich elementów do kalendarza
    fun getCalendarItems(): List<CalendarItem> {
        val list = mutableListOf<CalendarItem>()
        val db = this.readableDatabase

        // wizyty
        val cursorVisits = db.rawQuery("SELECT * FROM $TABLE_VISITS", null)
        if (cursorVisits.moveToFirst()) {
            do {
                val id = cursorVisits.getInt(0)
                val date = cursorVisits.getString(1)
                val desc = cursorVisits.getString(2)
                list.add(CalendarItem(id, true, "WIZYTA: $date - $desc", date, desc))
            } while (cursorVisits.moveToNext())
        }
        cursorVisits.close()

        // pomiary
        val cursorMeas = db.rawQuery("SELECT * FROM $TABLE_MEASUREMENTS", null)
        if (cursorMeas.moveToFirst()) {
            do {
                val id = cursorMeas.getInt(0)
                val date = cursorMeas.getString(1)
                val bp = cursorMeas.getString(2)
                val weight = cursorMeas.getString(3)
                val sugar = cursorMeas.getString(4)
                list.add(CalendarItem(id, false, "POMIAR: $date | Ciśnienie: $bp | Waga: $weight kg | Cukier: $sugar", date, bp, weight, sugar))
            } while (cursorMeas.moveToNext())
        }
        cursorMeas.close()
        db.close()

        // Sortujemy by najpierw były wizyty, potem pomiary
        return list.sortedBy { it.isVisit }.reversed()
    }

    // usuwanie i pobieranie raportów wraz z ID
    fun deleteReport(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_REPORTS, "$COLUMN_REPORT_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getReportsWithIds(): List<ReportItem> {
        val list = mutableListOf<ReportItem>()
        val selectQuery = "SELECT * FROM $TABLE_REPORTS ORDER BY $COLUMN_REPORT_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val date = cursor.getString(1)
                val content = cursor.getString(2)

                val display = "Raport z dnia: $date\n\n$content"
                list.add(ReportItem(id, display, content))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }
}


data class NoteItem(val id: Int, val display: String, val rawContent: String) {
    override fun toString() = display
}

data class CalendarItem(
    val id: Int,
    val isVisit: Boolean,
    val display: String,
    val date: String,
    val val1: String, // opis wizyty lub ciśnienie
    val val2: String = "", // waga
    val val3: String = ""  // cukier
) {
    override fun toString() = display
}

data class ReportItem(val id: Int, val display: String, val rawContent: String) {
    override fun toString() = display
}