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
        saveJob?.cancel() // cancel previous job (debounce)
        saveJob = MainScope().launch {
            delay(600) // wait 0.6 sec after typing stops
            if (title.isNotEmpty() || content.isNotEmpty()) {
                val note = currentNote?.copy(
                    title = title,
                    content = content
                ) ?: Note(title = title, content = content)

                if (currentNote == null) {
                    noteViewModel.insert(note)  // first time
                } else {
                    noteViewModel.update(note)  // subsequent edits
                }
                currentNote = note
            }
        }
    }
}
