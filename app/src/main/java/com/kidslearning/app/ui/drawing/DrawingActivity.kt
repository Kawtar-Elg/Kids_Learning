package com.kidslearning.app.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kidslearning.app.R
import com.kidslearning.app.data.model.Letter
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityDrawingBinding
import com.kidslearning.app.utils.SoundPlayer
import kotlinx.coroutines.launch

/**
 * Activité pour dessiner une lettre
 */
class DrawingActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDrawingBinding
    private lateinit var repository: LetterRepository
    private lateinit var soundPlayer: SoundPlayer
    private var currentLetter: Letter? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        repository = LetterRepository(this)
        soundPlayer = SoundPlayer(this)
        
        setupViews()
        loadLetter()
    }
    
    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        
        binding.btnClear.setOnClickListener {
            binding.drawingView.clearDrawing()
            Toast.makeText(this, R.string.try_again, Toast.LENGTH_SHORT).show()
        }
        
        binding.btnRepeat.setOnClickListener {
            currentLetter?.let { letter ->
                soundPlayer.playSound(letter.soundFileName, letter.soundUrl)
            }
        }
        
        // Configuration de la vue de dessin
        binding.drawingView.apply {
            setDrawColor(Color.parseColor("#2196F3"))
            setStrokeWidth(12f)
        }
    }
    
    private fun loadLetter() {
        val letterId = intent.getIntExtra("LETTER_ID", -1)
        if (letterId == -1) {
            finish()
            return
        }
        
        lifecycleScope.launch {
            currentLetter = repository.getLetterById(letterId)
            currentLetter?.let { letter ->
                binding.tvLetterDisplay.text = letter.character
                binding.drawingView.setGuideLetter(letter.character)
                
                // Mettre à jour la progression
                repository.updateProgress(letter.id)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
    }
}
