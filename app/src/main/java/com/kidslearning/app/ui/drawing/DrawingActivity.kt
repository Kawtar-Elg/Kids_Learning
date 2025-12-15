package com.kidslearning.app.ui.drawing

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kidslearning.app.R
import com.kidslearning.app.data.model.Letter
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityDrawingBinding
import com.kidslearning.app.ui.base.BaseActivity
import com.kidslearning.app.utils.SoundPlayer
import kotlinx.coroutines.launch

/**
 * Drawing Activity for tracing letters with animations and localization support
 */
class DrawingActivity : BaseActivity() {

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
        startAnimations()
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
            setDrawColor(Color.parseColor("#FF6B9D"))
            setStrokeWidth(16f)
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

    private fun startAnimations() {
        // Load animations
        val floatSlow = AnimationUtils.loadAnimation(this, R.anim.float_slow)
        val floatUpDown = AnimationUtils.loadAnimation(this, R.anim.float_up_down)
        val pulseStar = AnimationUtils.loadAnimation(this, R.anim.pulse_star)
        val bubbleRise = AnimationUtils.loadAnimation(this, R.anim.bubble_rise)
        val heartBeat = AnimationUtils.loadAnimation(this, R.anim.heart_beat)
        val sparkle = AnimationUtils.loadAnimation(this, R.anim.sparkle)
        val bounceButton = AnimationUtils.loadAnimation(this, R.anim.bounce_button)

        // Letter display bouncing
        binding.letterDisplayCard.startAnimation(bounceButton)

        // Sparkles twinkling
        binding.ivSparkle1.startAnimation(sparkle)
        binding.ivSparkle2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.sparkle).apply {
            startOffset = 300
        })

        // Bubbles
        binding.bubbleYellow.startAnimation(floatUpDown)
        binding.bubblePink.startAnimation(bubbleRise)

        // Cloud floating
        binding.ivBottomCloud.startAnimation(floatSlow)

        // Star pulsing
        binding.ivBottomStar.startAnimation(pulseStar)

        // Heart beating
        binding.ivFloatingHeart.startAnimation(heartBeat)
    }

    override fun onResume() {
        super.onResume()
        startAnimations()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
    }
}
