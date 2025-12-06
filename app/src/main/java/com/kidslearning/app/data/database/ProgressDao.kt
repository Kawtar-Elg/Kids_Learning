package com.kidslearning.app.data.database

import androidx.room.*
import com.kidslearning.app.data.model.UserProgress
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour la progression de l'utilisateur
 */
@Dao
interface ProgressDao {
    
    @Query("SELECT * FROM user_progress WHERE letterId = :letterId")
    fun getProgress(letterId: Int): Flow<UserProgress?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgress)
    
    @Update
    suspend fun updateProgress(progress: UserProgress)
    
    @Query("UPDATE user_progress SET timesDrawn = timesDrawn + 1, lastPracticed = :timestamp WHERE letterId = :letterId")
    suspend fun incrementPracticeCount(letterId: Int, timestamp: Long = System.currentTimeMillis())
    
    @Query("SELECT * FROM user_progress ORDER BY lastPracticed DESC LIMIT 10")
    fun getRecentProgress(): Flow<List<UserProgress>>
}
