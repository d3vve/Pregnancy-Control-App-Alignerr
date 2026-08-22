package com.example.telematyka

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class NotesActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private val notesList = mutableListOf<NoteItem>()
    private lateinit var adapter: ArrayAdapter<NoteItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val listViewNotes = findViewById<ListView>(R.id.listNotes)
        val btnAddNote = findViewById<Button>(R.id.btnAddNote)

        databaseHelper = DatabaseHelper(this)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notesList)
        listViewNotes.adapter = adapter

        // Wywołanie funkcji pobierającej notatki z bazy danych
        loadNotesFromDatabase()

        // logika dodawania nowych notatek
        btnAddNote.setOnClickListener {
            val input = EditText(this)
            input.hint = "Wpisz objawy lub notatkę..."
            AlertDialog.Builder(this)
                .setTitle("Nowa notatka")
                .setView(input)
                .setPositiveButton("Zapisz") { _, _ ->
                    val newNoteText = input.text.toString()
                    if (newNoteText.isNotEmpty()) {
                        // Przesyłanie notatek do bazy danych
                        databaseHelper.insertNote(newNoteText, System.currentTimeMillis(), 12)
                        loadNotesFromDatabase()
                    }
                }
                .setNegativeButton("Anuluj", null).show()
        }

        // edycja/usuwanie
        listViewNotes.setOnItemClickListener { _, _, position, _ ->
            val selectedNote = notesList[position]

            AlertDialog.Builder(this)
                .setTitle("Opcje notatki")
                .setItems(arrayOf("Edytuj", "Usuń")) { _, which ->
                    when (which) {
                        0 -> showEditNoteDialog(selectedNote)
                        1 -> deleteNote(selectedNote.id)
                    }
                }
                .show()
        }
    }

    private fun showEditNoteDialog(note: NoteItem) {
        val input = EditText(this)
        input.setText(note.rawContent) // Wpisujemy stary tekst do okienka

        AlertDialog.Builder(this)
            .setTitle("Edytuj notatkę")
            .setView(input)
            .setPositiveButton("Zaktualizuj") { _, _ ->
                val updatedText = input.text.toString()
                if (updatedText.isNotEmpty()) {
                    databaseHelper.updateNote(note.id, updatedText)
                    loadNotesFromDatabase()
                    Toast.makeText(this, "Zaktualizowano!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Anuluj", null).show()
    }

    private fun deleteNote(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Potwierdzenie")
            .setMessage("Czy na pewno chcesz usunąć tę notatkę?")
            .setPositiveButton("Usuń") { _, _ ->
                // usuwanie notatki z bazy danych
                databaseHelper.deleteNote(id)
                loadNotesFromDatabase()
                Toast.makeText(this, "Usunięto!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Anuluj", null).show()
    }

    private fun loadNotesFromDatabase() {
        notesList.clear()
        notesList.addAll(databaseHelper.getNotesWithIds())
        adapter.notifyDataSetChanged()
    }
}