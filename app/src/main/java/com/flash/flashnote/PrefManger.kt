package com.flash.flashnote

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object PrefManger {

    private const val PREF_NAME = "notes_pref"
    private const val KEY_NOTES = "notes_list"

    private lateinit var preference: SharedPreferences

    private val gson = Gson()

    fun init(context: Context){
        preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ----- Save notes ------
    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        preference.edit().putString(KEY_NOTES, json).apply()
    }

    fun loadNotes(): List<Note> {
        val json = preference.getString(KEY_NOTES, null) ?: return emptyList()
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addNote(note: Note) {
        val notes = loadNotes().toMutableList()
        notes.add(0,note)
        saveNotes(notes)
    }

    fun deleteNote(note: Note) {
        val notes = loadNotes().toMutableList()
        notes.remove(note)
        saveNotes(notes)
    }

    fun updateNote(index: Int, note: Note) {
        val notes = loadNotes().toMutableList()
        if (index in notes.indices) {
            notes[index] = note
            saveNotes(notes)
        }
    }
}