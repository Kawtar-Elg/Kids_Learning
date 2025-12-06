package com.kidslearning.app.ui.french

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityAlphabetBinding
import com.kidslearning.app.ui.arabic.LetterAdapter
import com.kidslearning.app.ui.drawing.DrawingActivity
import com.kidslearning.app.utils.SoundPlayer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Activité pour l'alphabet français
 */
class FrenchAlphabetActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAlphabetBinding
    private lateinit var repository: LetterRepository
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var adapter: LetterAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlphabetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        repository = LetterRepository(this)
        soundPlayer = SoundPlayer(this)
        
        setupToolbar()
        setupRecyclerView()
        observeLetters()
    }
    
    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun setupRecyclerView() {
        adapter = LetterAdapter { letter ->
            // Jouer le son de la lettre
            soundPlayer.playSound(letter.soundFileName, letter.soundUrl)
            
            // Ouvrir l'activité de dessin après un court délai
            binding.root.postDelayed({
                val intent = Intent(this, DrawingActivity::class.java).apply {
                    putExtra("LETTER_ID", letter.id)
                }
                startActivity(intent)
            }, 500)
        }
        
        binding.rvLetters.apply {
            layoutManager = GridLayoutManager(this@FrenchAlphabetActivity, 4)
            this.adapter = this@FrenchAlphabetActivity.adapter
            setHasFixedSize(true)
        }
    }
    
    private fun observeLetters() {
        lifecycleScope.launch {
            repository.getFrenchLetters().collectLatest { letters ->
                adapter.submitList(letters)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
    }
}
