package com.example.multinote

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.multinote.viewmodel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup Toolbar with Drawer Toggle
        val toolbar: Toolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Handle Navigation Drawer clicks
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }

        // RecyclerView + FAB setup
        val recyclerView = findViewById<RecyclerView>(R.id.notesRecyclerView)
        val fab = findViewById<FloatingActionButton>(R.id.fabAddNote)
        val emptyMessage = findViewById<TextView>(R.id.emptyMessage)

        adapter = NoteAdapter { note ->
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("noteId", note.id)
            intent.putExtra("noteTitle", note.title)
            intent.putExtra("noteContent", note.content)
            startActivity(intent)
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        // Swipe to delete
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.RIGHT
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
                val background = android.graphics.Color.RED.toDrawable()
                val icon = androidx.core.content.ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_delete
                )

                val iconMargin = (itemView.height - (icon?.intrinsicHeight ?: 0)) / 2
                val iconTop = itemView.top + iconMargin
                val iconBottom = iconTop + (icon?.intrinsicHeight ?: 0)

                if (dX > 0) {
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                    icon?.setBounds(
                        itemView.left + iconMargin,
                        iconTop,
                        itemView.left + iconMargin + icon.intrinsicWidth,
                        iconBottom
                    )
                } else {
                    background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon?.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)

        // Observe notes
        noteViewModel.allNotes.observe(this) { notes ->
            adapter.submitList(notes)
            emptyMessage.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
        }

        // Add note button
        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
    }
}
