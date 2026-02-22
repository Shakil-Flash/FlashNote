package com.flash.flashnote

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.flash.flashnote.databinding.ActivityNoteViewBinding
import kotlinx.coroutines.launch

class NoteView : AppCompatActivity() {

    private lateinit var binding: ActivityNoteViewBinding
    private lateinit var database: NoteDatabase
    private var currentNote: Note? = null
    private var isChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNoteViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NoteDatabase.getDatabase(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val noteId = intent.getIntExtra("noteId", -1)

        // ✅ Load note from database if editing
        if (noteId != -1) {
            lifecycleScope.launch {
                currentNote = database.noteDao().getNoteById(noteId)
                currentNote?.let {
                    binding.etTitle.setText(it.title)
                    binding.etContent.setText(it.content)
                }
            }
        }

        // Detect changes
        binding.etTitle.addTextChangedListener { isChanged = true }
        binding.etContent.addTextChangedListener { isChanged = true }
    }

    override fun onPause() {
        super.onPause()

        if (isChanged) {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) return

        val dao = database.noteDao()

        lifecycleScope.launch {
            if (currentNote == null) {
                // ✅ New Note
                val newNote = Note(
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                dao.insert(newNote)
            } else {
                // ✅ Update Existing
                val updatedNote = currentNote!!.copy(
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )
                dao.update(updatedNote)
            }
        }
    }
}