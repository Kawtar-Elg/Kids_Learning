package com.alphapals.app.data.model

/**
 * Modèle pour charger les données JSON
 */
data class AlphabetData(
    val arabic: List<LetterData>,
    val french: List<LetterData>
)

data class LetterData(
    val character: String,
    val soundFile: String,
    val soundUrl: String? = null,
    val pronunciation: String
)
