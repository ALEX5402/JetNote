package com.example.local.dao

import androidx.room.*
import com.example.local.model.NoteAndTaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteAndTaskDao {

    @Query("select * from note_and_todo")
    fun getAllNoteAndTasks(): Flow<List<NoteAndTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteAndTask(noteAndTodo: NoteAndTaskEntity)

    @Delete
    suspend fun deleteNoteAndTask(noteAndTodo: NoteAndTaskEntity)
}