package com.alphapals.app.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alphapals.app.utils.LanguageHelper

/**
 * Base Activity that handles language/locale configuration
 * All activities should extend this to properly support language switching
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Apply saved language to the context
        super.attachBaseContext(LanguageHelper.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ensure locale is applied
        LanguageHelper.setLocale(this)
    }

    /**
     * Switch to the opposite language and recreate the activity
     */
    protected fun switchLanguage() {
        val newLang = LanguageHelper.toggleLanguage(this)
        LanguageHelper.applyLocale(this, newLang)
    }

    /**
     * Switch to a specific language
     */
    protected fun setLanguage(languageCode: String) {
        LanguageHelper.applyLocale(this, languageCode)
    }

    /**
     * Get current language code
     */
    protected fun getCurrentLanguage(): String {
        return LanguageHelper.getSavedLanguage(this)
    }

    /**
     * Check if current language is RTL (Arabic)
     */
    protected fun isRtl(): Boolean {
        return LanguageHelper.isArabic(this)
    }
}
