package com.flash.flashnote

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // ✅ Get all notes as Flow (for RecyclerView)
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotesFlow(): Flow<List<Note>>

    // ✅ Get single note (for NoteView)
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)
}