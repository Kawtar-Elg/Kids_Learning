package com.alphapals.app.data.repository

import android.content.Context
import com.google.gson.Gson
import com.alphapals.app.data.database.AppDatabase
import com.alphapals.app.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/**
 * Repository pour gérer les lettres et la progression
 */
class LetterRepository(private val context: Context) {
    
    private val database = AppDatabase.getDatabase(context)
    private val letterDao = database.letterDao()
    private val progressDao = database.progressDao()
    
    /**
     * Charge les données depuis le fichier JSON dans les assets
     */
    suspend fun initializeDataIfNeeded() = withContext(Dispatchers.IO) {
        val count = letterDao.getLetterCount()
        if (count == 0) {
            loadDataFromJson()
        }
    }
    
    private suspend fun loadDataFromJson() {
        try {
            val inputStream = context.assets.open("alphabet_data.json")
            val reader = InputStreamReader(inputStream)
            val alphabetData = Gson().fromJson(reader, AlphabetData::class.java)
            reader.close()
            
            val letters = mutableListOf<Letter>()
            
            // Charger les lettres arabes
            alphabetData.arabic.forEachIndexed { index, letterData ->
                letters.add(
                    Letter(
                        character = letterData.character,
                        language = LetterLanguage.ARABIC,
                        soundFileName = letterData.soundFile,
                        soundUrl = letterData.soundUrl,
                        pronunciation = letterData.pronunciation,
                        order = index
                    )
                )
            }
            
            // Charger les lettres françaises
            alphabetData.french.forEachIndexed { index, letterData ->
                letters.add(
                    Letter(
                        character = letterData.character,
                        language = LetterLanguage.FRENCH,
                        soundFileName = letterData.soundFile,
                        soundUrl = letterData.soundUrl,
                        pronunciation = letterData.pronunciation,
                        order = index
                    )
                )
            }
            
            letterDao.insertLetters(letters)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun getArabicLetters(): Flow<List<Letter>> {
        return letterDao.getLettersByLanguage(LetterLanguage.ARABIC)
    }
    
    fun getFrenchLetters(): Flow<List<Letter>> {
        return letterDao.getLettersByLanguage(LetterLanguage.FRENCH)
    }
    
    suspend fun getLetterById(id: Int): Letter? {
        return letterDao.getLetterById(id)
    }

    /**
     * Get the next letter in sequence based on current letter
     */
    suspend fun getNextLetter(currentLetter: Letter): Letter? = withContext(Dispatchers.IO) {
        letterDao.getNextLetter(currentLetter.language, currentLetter.order)
    }
    
    fun getProgress(letterId: Int): Flow<UserProgress?> {
        return progressDao.getProgress(letterId)
    }
    
    suspend fun updateProgress(letterId: Int) {
        progressDao.incrementPracticeCount(letterId)
    }
}
