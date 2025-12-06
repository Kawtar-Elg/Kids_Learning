package com.kidslearning.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityMainBinding
import com.kidslearning.app.ui.arabic.ArabicAlphabetActivity
import com.kidslearning.app.ui.french.FrenchAlphabetActivity
import kotlinx.coroutines.launch

/**
 * Activité principale - écran d'accueil
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: LetterRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        repository = LetterRepository(this)
        
        // Initialiser les données depuis le JSON
        lifecycleScope.launch {
            repository.initializeDataIfNeeded()
        }
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Carte Alphabet Arabe
        binding.cardArabic.setOnClickListener {
            val intent = Intent(this, ArabicAlphabetActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        
        // Carte Alphabet Français
        binding.cardFrench.setOnClickListener {
            val intent = Intent(this, FrenchAlphabetActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
