package com.example.local.dao

import androidx.room.*
import com.example.local.model.NoteAndLinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteAndLinkDao {

    @Query("select * from note_and_link")
    fun getAllNotesAndLinks(): Flow<List<NoteAndLinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteAndLink(noteAndLink: NoteAndLinkEntity)

    @Delete
    suspend fun deleteNoteAndLink(noteAndLink: NoteAndLinkEntity)
}