package com.example.multinote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multinote.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        val fab = findViewById<FloatingActionButton>(R.id.fabAddNote)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        adapter = NoteAdapter()
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        // Observe LiveData from ViewModel
        noteViewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)  // Update RecyclerView
            emptyMessage.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }
}
