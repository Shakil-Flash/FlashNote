package com.flash.flashnote

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.flash.flashnote.databinding.ActivityHomeScreenBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var database: NoteDatabase
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = NoteDatabase.getDatabase(this)

        adapter = NoteAdapter { note ->
            val intent = Intent(this, NoteView::class.java)
            intent.putExtra("noteId", note.id)
            startActivity(intent)
        }

        binding.noteRecycler.layoutManager = LinearLayoutManager(this)
        binding.noteRecycler.adapter = adapter

        lifecycleScope.launch {
            database.noteDao().getAllNotesFlow().collectLatest { notes ->
                adapter.submitList(notes)
            }
        }

        binding.addBtn.setOnClickListener {
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, NoteView::class.java))
        }
    }
}