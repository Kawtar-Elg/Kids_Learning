package com.alphapals.app.ui.drawing

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.alphapals.app.R
import com.alphapals.app.data.model.Letter
import com.alphapals.app.data.repository.LetterRepository
import com.alphapals.app.databinding.ActivityDrawingBinding
import com.alphapals.app.ui.base.BaseActivity
import com.alphapals.app.utils.SoundPlayer
import com.alphapals.app.utils.BackgroundMusicPlayer
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
        startBackgroundMusic()
    }

    private fun setupViews() {
        binding.btnBack.setOnClickListener {
            it.animateBounce {
                finish()
            }
        }

        binding.btnClear.setOnClickListener {
            binding.btnClearCard.animateBounce {
                binding.drawingView.clearDrawing()
                Toast.makeText(this, R.string.try_again, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRepeat.setOnClickListener {
            binding.btnRepeatCard.animateBounce {
                currentLetter?.let { letter ->
                    soundPlayer.playSound(letter.soundFileName, letter.soundUrl)
                }
            }
        }

        // Music toggle button
        binding.btnMusic.setOnClickListener {
            binding.btnMusicCard.animateBounce {
                toggleBackgroundMusic()
            }
        }

        // Undo button
        binding.btnUndo.setOnClickListener {
            binding.btnUndoCard.animateBounce {
                if (binding.drawingView.canUndo()) {
                    binding.drawingView.undo()
                } else {
                    Toast.makeText(this, "Rien à annuler!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Redo button
        binding.btnRedo.setOnClickListener {
            binding.btnRedoCard.animateBounce {
                if (binding.drawingView.canRedo()) {
                    binding.drawingView.redo()
                } else {
                    Toast.makeText(this, "Rien à refaire!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Configuration de la vue de dessin
        binding.drawingView.apply {
            setDrawColor(Color.parseColor("#FF69B4")) // Pink default
            setStrokeWidth(20f) // Thicker for kids

            // 🎯 Set callback for when letter is traced correctly!
            onLetterTracedCorrectly = {
                onLetterTracedSuccessfully()
            }
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
                // Set the guide letter in the drawing view (big letter for tracing)
                binding.drawingView.setGuideLetter(letter.character)

                // Update progress
                repository.updateProgress(letter.id)
            }
        }
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
    }

    override fun onResume() {
        super.onResume()
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

    /**
     * 🎉 Called when the letter is traced correctly!
     */
    private fun onLetterTracedSuccessfully() {
        // Play a success sound
        currentLetter?.let { letter ->
            soundPlayer.playSound(letter.soundFileName, letter.soundUrl)
        }

        // Show congratulations dialog
        showCongratulationsDialog()
    }

    /**
     * 🎊 Show a beautiful congratulations dialog with Lottie animations!
     */
    private fun showCongratulationsDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_congratulations, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Make dialog background transparent so CardView shows properly
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Get views from dialog
        val lottieAnimation =
            dialogView.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieAnimation)
        val btnNextLetter =
            dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnNextLetter)
        val btnTryAgain =
            dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnTryAgain)

        // 🎨 Randomly select a Lottie animation for variety!
        val lottieAnimations = arrayOf(
            R.raw.butterfly_lottie_animation,
            R.raw.cute_tiger,
            R.raw.goldfish,
            R.raw.loading_cat,
            R.raw.monkey1
        )
        val randomAnimation = lottieAnimations.random()
        lottieAnimation.setAnimation(randomAnimation)
        lottieAnimation.playAnimation()

        // Next Letter button
        btnNextLetter.setOnClickListener {
            it.animateBounce {
                dialog.dismiss()
                loadNextLetter()
            }
        }

        // Try Again button
        btnTryAgain.setOnClickListener {
            it.animateBounce {
                dialog.dismiss()
                binding.drawingView.clearDrawing()
            }
        }

        dialog.show()

        // Add scale animation to dialog entrance
        dialog.window?.decorView?.apply {
            scaleX = 0.8f
            scaleY = 0.8f
            alpha = 0f
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(300)
                .start()
        }
    }

    /**
     * 📝 Load the next letter in sequence
     */
    private fun loadNextLetter() {
        lifecycleScope.launch {
            currentLetter?.let { letter ->
                val nextLetter = repository.getNextLetter(letter)

                if (nextLetter != null) {
                    // Load next letter
                    currentLetter = nextLetter
                    binding.drawingView.setGuideLetter(nextLetter.character)
                    repository.updateProgress(nextLetter.id)

                    // Play the sound for the new letter
                    soundPlayer.playSound(nextLetter.soundFileName, nextLetter.soundUrl)
                } else {
                    // No more letters! Show completion message
                    showCompletionMessage()
                }
            }
        }
    }

    /**
     * 🏆 Show GRAND FINALE completion message with multiple Lottie animations!
     */
    private fun showCompletionMessage() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_completion, null)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Make dialog background transparent
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Get all Lottie animation views
        val lottieCenter =
            dialogView.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieAnimationCenter)
        val lottieLeft =
            dialogView.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieAnimationLeft)
        val lottieRight =
            dialogView.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.lottieAnimationRight)
        val btnBackHome =
            dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBackHome)

        // 🎨 Set different animations for grand celebration!
        val allAnimations = arrayOf(
            R.raw.butterfly_lottie_animation,
            R.raw.cute_tiger,
            R.raw.goldfish,
            R.raw.loading_cat,
            R.raw.monkey1
        )

        // Use different animations for variety
        val shuffled = allAnimations.toMutableList().shuffled()
        lottieCenter.setAnimation(shuffled[0])
        lottieLeft.setAnimation(shuffled[1])
        lottieRight.setAnimation(shuffled[2])

        // Start all animations
        lottieCenter.playAnimation()
        lottieLeft.playAnimation()
        lottieRight.playAnimation()

        // Back button
        btnBackHome.setOnClickListener {
            it.animateBounce {
                dialog.dismiss()
                finish()
            }
        }

        dialog.show()

        // Add bouncing entrance animation to dialog
        dialog.window?.decorView?.apply {
            scaleX = 0.5f
            scaleY = 0.5f
            alpha = 0f
            animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(1f)
                .setDuration(300)
                .withEndAction {
                    animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()
        }
    }

    /**
     * 🎈 Extension function to add a fun bounce animation to any view
     */
    private fun android.view.View.animateBounce(onAnimationEnd: () -> Unit) {
        this.animate()
            .scaleX(0.8f)
            .scaleY(0.8f)
            .setDuration(100)
            .withEndAction {
                this.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(150)
                    .withEndAction {
                        this.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(100)
                            .withEndAction {
                                onAnimationEnd()
                            }
                            .start()
                    }
                    .start()
            }
            .start()
    }
}
