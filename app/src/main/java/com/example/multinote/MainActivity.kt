package com.example.multinote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.multinote.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

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
        recyclerView.setHasFixedSize(true)

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,  // no drag
            ItemTouchHelper.RIGHT // only swipe right
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val noteToDelete = adapter.currentList[position]

                noteViewModel.delete(noteToDelete)

                Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        noteViewModel.insert(noteToDelete)
                    }
                    .show()
            }

            override fun onChildDraw(
                c: android.graphics.Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val background = android.graphics.drawable.ColorDrawable(android.graphics.Color.RED)
                val icon = androidx.core.content.ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_delete
                )

                val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
                val iconTop = itemView.top + (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
                val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)

                if (dX > 0) { // swiping right
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                    icon?.setBounds(
                        itemView.left + iconMargin,
                        iconTop,
                        itemView.left + iconMargin + (icon?.intrinsicWidth ?: 0),
                        iconBottom
                    )
                } else {
                    // no swipe left → background disappears
                    background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon?.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        // attach to RecyclerView
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)


        noteViewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)
            emptyMessage.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }
}
