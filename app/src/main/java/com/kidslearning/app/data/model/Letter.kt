package com.alphapals.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modèle de données pour une lettre
 */
@Entity(tableName = "letters")
data class Letter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val character: String,
    val language: LetterLanguage,
    val soundFileName: String,
    val soundUrl: String? = null,
    val pronunciation: String,
    val order: Int
)

enum class LetterLanguage {
    ARABIC,
    FRENCH
}

/**
 * Modèle pour la progression de l'utilisateur
 */
@Entity(tableName = "user_progress")
data class UserProgress(
    @PrimaryKey
    val letterId: Int,
    val timesDrawn: Int = 0,
    val lastPracticed: Long = System.currentTimeMillis(),
    val masteryLevel: Int = 0 // 0-100
)
