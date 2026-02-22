package com.flash.flashnote

import android.content.Context
import android.renderscript.Type
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flash.flashnote.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val onClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(old: Note, new: Note) = old.id == new.id
            override fun areContentsTheSame(old: Note, new: Note) = old == new
        }
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.noteTitle.text = note.title
            binding.noteContent.text = note.content
            binding.root.setOnClickListener { onClick(note) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}