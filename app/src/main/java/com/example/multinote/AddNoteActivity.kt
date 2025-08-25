package com.example.multinote

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.multinote.room.Note
import com.example.multinote.viewmodel.NoteViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddNoteActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private var currentNote: Note? = null
    private var saveJob: Job? = null  // to debounce saves

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        val titleInput = findViewById<EditText>(R.id.editTitle)
        val contentInput = findViewById<EditText>(R.id.editContent)

        // 1. Get data from intent
        val noteId = intent.getLongExtra("noteId", 0L)
        val noteTitle = intent.getStringExtra("noteTitle") ?: ""
        val noteContent = intent.getStringExtra("noteContent") ?: ""

        // 2. If noteId != 0 → it's an existing note
        currentNote = if (noteId != 0L) {
            Note(id = noteId, title = noteTitle, content = noteContent)
        } else {
            null
        }

        // 3. Set text to EditTexts
        titleInput.setText(noteTitle)
        contentInput.setText(noteContent)

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                autoSave(titleInput.text.toString(), contentInput.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        titleInput.addTextChangedListener(watcher)
        contentInput.addTextChangedListener(watcher)
    }

    private fun autoSave(title: String, content: String) {
        saveJob?.cancel()
        saveJob = MainScope().launch {
            delay(600)

            if (title.isEmpty() && content.isEmpty()) {
                // If user cleared everything and note already exists → delete
                currentNote?.let { existingNote ->
                    noteViewModel.delete(existingNote)
                    currentNote = null
                }
                return@launch
            }

            val note = currentNote?.copy(
                title = title,
                content = content
            ) ?: Note(title = title, content = content)

            if (title.isEmpty() && content.isEmpty() && currentNote != null) {
                noteViewModel.delete(currentNote!!)
                currentNote = null
            } else {
                noteViewModel.update(note)
                currentNote = note
            }
        }
    }
}