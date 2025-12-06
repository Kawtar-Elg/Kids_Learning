package com.kidslearning.app.data.database

import androidx.room.*
import com.kidslearning.app.data.model.Letter
import com.kidslearning.app.data.model.LetterLanguage
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur les lettres
 */
@Dao
interface LetterDao {
    
    @Query("SELECT * FROM letters WHERE language = :language ORDER BY `order` ASC")
    fun getLettersByLanguage(language: LetterLanguage): Flow<List<Letter>>
    
    @Query("SELECT * FROM letters WHERE id = :letterId")
    suspend fun getLetterById(letterId: Int): Letter?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLetters(letters: List<Letter>)
    
    @Query("SELECT COUNT(*) FROM letters")
    suspend fun getLetterCount(): Int
    
    @Delete
    suspend fun deleteLetter(letter: Letter)
    
    @Query("DELETE FROM letters")
    suspend fun deleteAllLetters()
}
