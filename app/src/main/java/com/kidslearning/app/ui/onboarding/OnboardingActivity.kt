package com.kidslearning.app.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.kidslearning.app.R
import com.kidslearning.app.databinding.ActivityOnboardingBinding
import com.kidslearning.app.ui.base.BaseActivity
import com.kidslearning.app.ui.main.MainActivity
import kotlin.math.abs

/**
 * Onboarding Activity with Lottie animations and localization support
 */
class OnboardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter
    private val indicators = mutableListOf<ImageView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if onboarding was already shown
        if (isOnboardingCompleted()) {
            navigateToMain()
            return
        }

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboardingPages()
        setupIndicators()
        setupClickListeners()
        startBackgroundAnimations()
        playEntranceAnimations()
    }

    private fun setupOnboardingPages() {
        // Animal-themed onboarding pages
        val pages = listOf(
            OnboardingPage(
                title = getString(R.string.onboarding_title_1),  // Lion
                description = getString(R.string.onboarding_desc_1),
                lottieUrl = "",
                lottieAsset = "lion.json"
            ),
            OnboardingPage(
                title = getString(R.string.onboarding_title_2),  // Frog
                description = getString(R.string.onboarding_desc_2),
                lottieUrl = "",
                lottieAsset = "frog.json"
            ),
            OnboardingPage(
                title = getString(R.string.onboarding_title_3),  // Cat
                description = getString(R.string.onboarding_desc_3),
                lottieUrl = "",
                lottieAsset = "cat.json"
            ),
            OnboardingPage(
                title = getString(R.string.onboarding_title_4),  // Fish
                description = getString(R.string.onboarding_desc_4),
                lottieUrl = "",
                lottieAsset = "fish.json"
            ),
            OnboardingPage(
                title = getString(R.string.onboarding_title_5),  // Butterfly
                description = getString(R.string.onboarding_desc_5),
                lottieUrl = "",
                lottieAsset = "butterfly.json"
            )
        )

        adapter = OnboardingAdapter(pages)
        binding.viewPager.adapter = adapter

        // Add fun page transformation effect
        binding.viewPager.setPageTransformer(FunPageTransformer())

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateIndicators(position)
                updateButtonText(position)
            }
        })
    }

    private fun setupIndicators() {
        val pages = adapter.itemCount
        binding.indicatorLayout.removeAllViews()
        indicators.clear()

        for (i in 0 until pages) {
            val indicator = ImageView(this).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        this@OnboardingActivity,
                        if (i == 0) R.drawable.indicator_active else R.drawable.indicator_inactive
                    )
                )
                val params = android.widget.LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.indicator_size),
                    resources.getDimensionPixelSize(R.dimen.indicator_size)
                ).apply {
                    marginStart = resources.getDimensionPixelSize(R.dimen.indicator_margin)
                    marginEnd = resources.getDimensionPixelSize(R.dimen.indicator_margin)
                }
                layoutParams = params
            }
            indicators.add(indicator)
            binding.indicatorLayout.addView(indicator)
        }
    }

    private fun updateIndicators(position: Int) {
        indicators.forEachIndexed { index, imageView ->
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (index == position) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
            )

            // Add scale animation to active indicator
            if (index == position) {
                imageView.animate()
                    .scaleX(1.4f)
                    .scaleY(1.4f)
                    .setDuration(250)
                    .start()
            } else {
                imageView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(250)
                    .start()
            }
        }
    }

    private fun updateButtonText(position: Int) {
        val isLastPage = position == adapter.itemCount - 1

        // Animate button text change
        binding.btnNext.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.btnNext.text = if (isLastPage) {
                    getString(R.string.get_started)
                } else {
                    getString(R.string.next)
                }
                binding.btnNext.animate()
                    .alpha(1f)
                    .setDuration(150)
                    .start()
            }
            .start()

        // Hide/show skip button with animation
        if (isLastPage) {
            binding.btnSkip.animate()
                .alpha(0f)
                .setDuration(200)
                .withEndAction { binding.btnSkip.visibility = View.INVISIBLE }
                .start()
        } else {
            binding.btnSkip.visibility = View.VISIBLE
            binding.btnSkip.animate()
                .alpha(1f)
                .setDuration(200)
                .start()
        }
    }

    private fun setupClickListeners() {
        binding.btnNext.setOnClickListener {
            val currentPage = binding.viewPager.currentItem
            if (currentPage < adapter.itemCount - 1) {
                binding.viewPager.currentItem = currentPage + 1
            } else {
                completeOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            completeOnboarding()
        }
    }

    private fun playEntranceAnimations() {
        // Animate button entrance
        val fadeSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up)
        binding.btnNext.startAnimation(fadeSlideUp)
        binding.btnSkip.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.fade_slide_up).apply {
                startOffset = 200
            })
        binding.indicatorLayout.startAnimation(
            AnimationUtils.loadAnimation(
                this,
                R.anim.fade_slide_up
            ).apply {
                startOffset = 100
            })
    }

    private fun startBackgroundAnimations() {
        // Load animations
        val floatSlow = AnimationUtils.loadAnimation(this, R.anim.float_slow)
        val floatUpDown = AnimationUtils.loadAnimation(this, R.anim.float_up_down)
        val pulseStar = AnimationUtils.loadAnimation(this, R.anim.pulse_star)
        val bubbleRise = AnimationUtils.loadAnimation(this, R.anim.bubble_rise)

        // Apply animations to decorative elements
        binding.ivCloud1.startAnimation(floatSlow)
        binding.ivCloud2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_sideways))
        binding.ivStar1.startAnimation(pulseStar)
        binding.ivStar2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
            startOffset = 400
        })
        binding.bubblePink.startAnimation(bubbleRise)
        binding.bubbleYellow.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bubble_rise).apply {
                startOffset = 600
            })
    }

    private fun completeOnboarding() {
        // Save that onboarding is completed
        getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("onboarding_completed", true)
            .apply()

        navigateToMain()
    }

    private fun isOnboardingCompleted(): Boolean {
        return getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getBoolean("onboarding_completed", false)
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
        startBackgroundAnimations()
    }
}

/**
 * Fun page transformer with scale and fade effects
 */
class FunPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            val pageWidth = width
            val pageHeight = height

            when {
                position < -1 -> {
                    // Page is way off-screen to the left
                    alpha = 0f
                }

                position <= 1 -> {
                    // Page is visible or becoming visible
                    val scaleFactor = 0.85f.coerceAtLeast(1 - abs(position) * 0.15f)
                    val verticalMargin = pageHeight * (1 - scaleFactor) / 2
                    val horizontalMargin = pageWidth * (1 - scaleFactor) / 2

                    translationX = if (position < 0) {
                        horizontalMargin - verticalMargin / 2
                    } else {
                        -horizontalMargin + verticalMargin / 2
                    }

                    // Scale the page
                    scaleX = scaleFactor
                    scaleY = scaleFactor

                    // Fade based on position
                    alpha = 0.5f.coerceAtLeast(1 - abs(position) * 0.5f)
                }

                else -> {
                    // Page is way off-screen to the right
                    alpha = 0f
                }
            }
        }
    }
}

/**
 * Data class for onboarding page content
 */
data class OnboardingPage(
    val title: String,
    val description: String,
    val lottieUrl: String,
    val lottieAsset: String
)
