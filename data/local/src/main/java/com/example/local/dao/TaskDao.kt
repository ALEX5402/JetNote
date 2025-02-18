package com.example.local.dao

import androidx.room.*
import com.example.local.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("select * from TASKS_TABLE")
    fun getAllTaskItems():Flow<List<TaskEntity>>

    @Insert
    suspend fun addTaskItem(item: TaskEntity)

    @Update
    suspend fun updateTaskItem(item: TaskEntity)

    @Delete
    suspend fun deleteTaskItem(item: TaskEntity)
}