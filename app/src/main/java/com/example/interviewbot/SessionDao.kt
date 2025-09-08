package com.example.interviewbot

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: InterviewSession)

    // --- ADD THIS NEW FUNCTION ---
    @Delete
    suspend fun deleteSession(session: InterviewSession)

    @Query("SELECT * FROM interview_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<InterviewSession>>
}