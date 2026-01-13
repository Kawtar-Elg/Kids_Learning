package com.alphapals.app.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Helper class to manage language switching between French and Arabic
 */
object LanguageHelper {

    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "selected_language"

    const val LANGUAGE_FRENCH = "fr"
    const val LANGUAGE_ARABIC = "ar"

    /**
     * Get the currently saved language
     */
    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, LANGUAGE_FRENCH) ?: LANGUAGE_FRENCH
    }

    /**
     * Save the selected language
     */
    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    /**
     * Check if current language is Arabic
     */
    fun isArabic(context: Context): Boolean {
        return getSavedLanguage(context) == LANGUAGE_ARABIC
    }

    /**
     * Check if current language is French
     */
    fun isFrench(context: Context): Boolean {
        return getSavedLanguage(context) == LANGUAGE_FRENCH
    }

    /**
     * Toggle between French and Arabic
     */
    fun toggleLanguage(context: Context): String {
        val currentLang = getSavedLanguage(context)
        val newLang = if (currentLang == LANGUAGE_FRENCH) LANGUAGE_ARABIC else LANGUAGE_FRENCH
        saveLanguage(context, newLang)
        return newLang
    }

    /**
     * Set the app locale based on saved language
     */
    fun setLocale(context: Context): Context {
        val languageCode = getSavedLanguage(context)
        return updateResources(context, languageCode)
    }

    /**
     * Set a specific locale
     */
    fun setLocale(context: Context, languageCode: String): Context {
        saveLanguage(context, languageCode)
        return updateResources(context, languageCode)
    }

    /**
     * Update the app resources with the new locale
     */
    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    /**
     * Apply locale to activity and recreate if needed
     */
    fun applyLocale(activity: Activity, languageCode: String) {
        saveLanguage(activity, languageCode)

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)

        // Recreate activity to apply changes
        activity.recreate()
    }

    /**
     * Get language display name
     */
    fun getLanguageDisplayName(context: Context, languageCode: String): String {
        return when (languageCode) {
            LANGUAGE_ARABIC -> "العربية"
            LANGUAGE_FRENCH -> "Français"
            else -> "Français"
        }
    }

    /**
     * Get the opposite language code (for toggle display)
     */
    fun getOppositeLanguage(context: Context): String {
        return if (isArabic(context)) LANGUAGE_FRENCH else LANGUAGE_ARABIC
    }

    /**
     * Get the opposite language display name (for toggle button)
     */
    fun getOppositeLanguageDisplayName(context: Context): String {
        return if (isArabic(context)) "Français" else "العربية"
    }
}
