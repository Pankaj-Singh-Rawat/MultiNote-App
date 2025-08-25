package com.example.multinote.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.multinote.room.Note
import com.example.multinote.room.NoteDatabase
import com.example.multinote.room.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun update(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun delete(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    suspend fun insertAndReturnId(note: Note): Long {
        return repository.insertAndReturnId(note)
    }

    fun insert(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }
}
