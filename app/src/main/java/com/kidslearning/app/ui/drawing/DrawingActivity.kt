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
import com.kidslearning.app.utils.BackgroundMusicPlayer
import kotlinx.coroutines.launch

/**
 * Drawing Activity for tracing letters with animations and localization support
 */
class DrawingActivity : BaseActivity() {

    private lateinit var binding: ActivityDrawingBinding
    private lateinit var repository: LetterRepository
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var backgroundMusicPlayer: BackgroundMusicPlayer
    private var currentLetter: Letter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LetterRepository(this)
        soundPlayer = SoundPlayer(this)
        // Use singleton instance - music persists across activities! 🎵
        backgroundMusicPlayer = BackgroundMusicPlayer.getInstance(this)

        setupViews()
        loadLetter()
        startAnimations()
        startBackgroundMusic()
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

        // Music toggle button
        binding.btnMusic.setOnClickListener {
            toggleBackgroundMusic()
        }

        // Undo button
        binding.btnUndo.setOnClickListener {
            if (binding.drawingView.canUndo()) {
                binding.drawingView.undo()
            } else {
                Toast.makeText(this, "Rien à annuler!", Toast.LENGTH_SHORT).show()
            }
        }

        // Redo button
        binding.btnRedo.setOnClickListener {
            if (binding.drawingView.canRedo()) {
                binding.drawingView.redo()
            } else {
                Toast.makeText(this, "Rien à refaire!", Toast.LENGTH_SHORT).show()
            }
        }

        // Configuration de la vue de dessin
        binding.drawingView.apply {
            setDrawColor(Color.parseColor("#FF69B4")) // Pink default
            setStrokeWidth(20f) // Thicker for kids
        }

        // Setup color palette
        setupColorPalette()
    }

    private fun setupColorPalette() {
        // Setup each color button
        setupColorButton(R.id.colorPink, "#FF69B4")
        setupColorButton(R.id.colorRed, "#FF5252")
        setupColorButton(R.id.colorOrange, "#FF8C00")
        setupColorButton(R.id.colorYellow, "#FFD700")
        setupColorButton(R.id.colorGreen, "#4CAF50")
        setupColorButton(R.id.colorCyan, "#00BCD4")
        setupColorButton(R.id.colorBlue, "#2196F3")
        setupColorButton(R.id.colorPurple, "#9370DB")
        setupColorButton(R.id.colorBrown, "#8D6E63")
        setupColorButton(R.id.colorBlack, "#212121")
    }

    private fun setupColorButton(viewId: Int, colorHex: String) {
        val colorView = binding.root.findViewById<android.view.View>(viewId)
        val color = Color.parseColor(colorHex)

        colorView?.setOnClickListener {
            binding.drawingView.setDrawColor(color)
            // Animate selected color
            it.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction {
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(150)
                        .start()
                }
                .start()

            Toast.makeText(this, "🎨 Couleur changée!", Toast.LENGTH_SHORT).show()
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

    private fun startBackgroundMusic() {
        // Start playing exciting learning music for the child (if not already playing)
        // Using a lower volume (50%) so it doesn't drown out letter pronunciation
        try {
            if (!backgroundMusicPlayer.isPlaying()) {
                backgroundMusicPlayer.startMusic(
                    track = BackgroundMusicPlayer.MusicTrack.HAPPY_LEARNING,
                    volume = 0.5f
                )
            }
            updateMusicButton()
        } catch (e: Exception) {
            // If background music fails, just continue without it
            e.printStackTrace()
        }
    }

    private fun toggleBackgroundMusic() {
        if (backgroundMusicPlayer.isPlaying()) {
            backgroundMusicPlayer.pauseMusic()
            Toast.makeText(this, R.string.music_paused, Toast.LENGTH_SHORT).show()
        } else {
            backgroundMusicPlayer.resumeMusic()
            Toast.makeText(this, R.string.music_playing, Toast.LENGTH_SHORT).show()
        }
        updateMusicButton()
    }

    private fun updateMusicButton() {
        // Update button icon based on music state
        val iconRes = if (backgroundMusicPlayer.isPlaying()) {
            R.drawable.ic_sound // Playing icon
        } else {
            R.drawable.ic_sound // Paused icon (you can add ic_sound_off if you want)
        }
        binding.btnMusic.setImageResource(iconRes)

        // Add visual feedback with animation
        binding.btnMusicCard.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(100)
            .withEndAction {
                binding.btnMusicCard.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    override fun onResume() {
        super.onResume()
        startAnimations()
        updateMusicButton()
        // Music continues playing automatically! 🎵
    }

    override fun onPause() {
        super.onPause()
        // DON'T pause music here - let it continue across activities! 🎵
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
        // DON'T release music player - it's a singleton that persists! 🎵
    }
}
