package com.kidslearning.app.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.kidslearning.app.R
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityMainBinding
import com.kidslearning.app.ui.arabic.ArabicAlphabetActivity
import com.kidslearning.app.ui.base.BaseActivity
import com.kidslearning.app.ui.french.FrenchAlphabetActivity
import com.kidslearning.app.utils.LanguageHelper
import kotlinx.coroutines.launch

/**
 * Main Activity - Home screen with animations and language switching
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: LetterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LetterRepository(this)

        // Initialize data from JSON
        lifecycleScope.launch {
            repository.initializeDataIfNeeded()
        }

        setupLanguageButton()
        setupClickListeners()
        startAnimations()
    }

    private fun setupLanguageButton() {
        // Update language label to show the OTHER language (what clicking will switch to)
        updateLanguageLabel()

        // Language switch button click
        binding.btnLanguage.setOnClickListener {
            switchLanguage()
        }

        // Also allow clicking the label
        binding.tvLanguageLabel.setOnClickListener {
            switchLanguage()
        }

        // Animate the language button
        val bounceButton = AnimationUtils.loadAnimation(this, R.anim.bounce_button)
        binding.btnLanguageCard.startAnimation(bounceButton)
    }

    private fun updateLanguageLabel() {
        // Show what language will be switched TO
        binding.tvLanguageLabel.text = LanguageHelper.getOppositeLanguageDisplayName(this)
    }

    private fun setupClickListeners() {
        // Arabic Alphabet Card with bounce animation
        binding.cardArabic.setOnClickListener {
            // Add click animation
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    val intent = Intent(this, ArabicAlphabetActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                }
                .start()
        }

        // French Alphabet Card with bounce animation
        binding.cardFrench.setOnClickListener {
            // Add click animation
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    val intent = Intent(this, FrenchAlphabetActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out)
                }
                .start()
        }

        // Video Learning Button (Circle) with bounce animation
        binding.cardVideos.setOnClickListener {
            // Add click animation
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(100)
                        .start()

                    val intent = Intent(
                        this,
                        com.kidslearning.app.ui.video.VideoLearningActivity::class.java
                    )
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                .start()
        }
    }

    private fun startAnimations() {
        // Load all animations
        val floatUpDown = AnimationUtils.loadAnimation(this, R.anim.float_up_down)
        val floatSlow = AnimationUtils.loadAnimation(this, R.anim.float_slow)
        val floatSideways = AnimationUtils.loadAnimation(this, R.anim.float_sideways)
        val pulseStar = AnimationUtils.loadAnimation(this, R.anim.pulse_star)
        val rotateSun = AnimationUtils.loadAnimation(this, R.anim.rotate_sun)
        val balloonSway = AnimationUtils.loadAnimation(this, R.anim.balloon_sway)
        val bubbleRise = AnimationUtils.loadAnimation(this, R.anim.bubble_rise)
        val bounceButton = AnimationUtils.loadAnimation(this, R.anim.bounce_button)
        val sparkle = AnimationUtils.loadAnimation(this, R.anim.sparkle)
        val heartBeat = AnimationUtils.loadAnimation(this, R.anim.heart_beat)

        // Sun rotation
        binding.ivSun.startAnimation(rotateSun)

        // Clouds floating
        binding.ivCloudLeft.startAnimation(floatSlow)
        binding.ivCloudMiddle.startAnimation(floatSideways)
        binding.ivBottomCloud.startAnimation(floatUpDown)

        // Balloon swaying
        binding.ivBalloon.startAnimation(balloonSway)

        // Stars pulsing
        binding.ivStar1.startAnimation(pulseStar)
        binding.ivStar2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
            startOffset = 400
        })
        binding.ivArabicStar.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
                startOffset = 200
            })
        binding.ivSubtitleStar1.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
                startOffset = 300
            })
        binding.ivSubtitleStar2.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
                startOffset = 600
            })

        // Sparkles twinkling
        binding.ivSparkle1.startAnimation(sparkle)
        binding.ivSparkle2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.sparkle).apply {
            startOffset = 300
        })

        // Heart beating
        binding.ivFrenchHeart.startAnimation(heartBeat)

        // Logo bouncing
        binding.logoCard.startAnimation(bounceButton)

        // Buttons bouncing
        binding.cardArabic.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bounce_button).apply {
                startOffset = 500
            })
        binding.cardFrench.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bounce_button).apply {
                startOffset = 800
            })

        // Video Learning Button bouncing
        binding.cardVideos.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bounce_button).apply {
                startOffset = 1100
            })

        // Bubbles rising
        binding.bubblePink.startAnimation(bubbleRise)
        binding.bubbleYellow.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bubble_rise).apply {
                startOffset = 500
            })
        binding.bubbleBlue.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bubble_rise).apply {
                startOffset = 1000
            })
    }

    override fun onResume() {
        super.onResume()
        startAnimations()
        updateLanguageLabel()
    }
}
