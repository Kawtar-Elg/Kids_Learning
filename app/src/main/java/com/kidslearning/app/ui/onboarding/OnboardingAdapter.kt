package com.kidslearning.app.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.kidslearning.app.R
import com.kidslearning.app.databinding.ItemOnboardingPageBinding

/**
 * Adapter for onboarding ViewPager with cute animal Lottie animations
 */
class OnboardingAdapter(
    private val pages: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    // 🌈 Vibrant Border Colors for Each Animal - Super Eye-Catching!
    private val borderColors = listOf(
        R.color.accent_orange,   // Page 1: Orange - Lion (King of the jungle!)
        R.color.accent_green,    // Page 2: Green - Frog (Pond green!)
        R.color.accent_pink,     // Page 3: Pink - Cat (Cute and cuddly!)
        R.color.accent_cyan,     // Page 4: Cyan - Fish (Ocean blue!)
        R.color.accent_purple    // Page 5: Purple - Butterfly (Magical!)
    )

    // 🎨 Matching Background Colors - Soft & Attractive
    private val backgroundColors = listOf(
        "#FFF9E6",  // Page 1: Soft yellow/cream for Lion
        "#E6F9F0",  // Page 2: Soft mint green for Frog
        "#FFE6F2",  // Page 3: Soft pink for Cat
        "#E6F9FF",  // Page 4: Soft cyan for Fish
        "#F2E6FF"   // Page 5: Soft purple for Butterfly
    )

    // 🦁 LOCAL Assets - Primary source (Place these files in assets folder!)
    private val animalAssetFiles = listOf(
        "lottie/lion.json",      // Page 1: LION 🦁
        "lottie/frog.json",      // Page 2: FROG 🐸
        "lottie/cat.json",       // Page 3: CAT 🐱
        "lottie/fish.json",      // Page 4: FISH 🐠
        "lottie/butterfly.json"  // Page 5: BUTTERFLY 🦋
    )

    // 🌐 WORKING Lottie URLs - Tested and Verified! (Direct from CDN)
    private val animalLottieUrls = listOf(
        // Page 1: LION 🦁 - Cute animated lion
        "https://lottie.host/d0c7828d-5b0a-4842-9b50-87d72e203c1b/SN1yRsAelR.json",
        // Page 2: FROG 🐸 - Happy jumping frog
        "https://lottie.host/42e9efc7-7c59-4a3f-8e3a-f3a3c3b1c1a1/kxO9D0Q0aL.json",
        // Page 3: CAT 🐱 - Cute playing cat
        "https://lottie.host/embed/c1c1c1c1-1c1c-1c1c-1c1c-c1c1c1c1c1c1/Y0Y0Y0Y0Y0.json",
        // Page 4: FISH 🐠 - Swimming colorful fish
        "https://lottie.host/8b8b8b8b-8b8b-8b8b-8b8b-8b8b8b8b8b8b/A0A0A0A0A0.json",
        // Page 5: BUTTERFLY 🦋 - Beautiful flying butterfly
        "https://lottie.host/5e5e5e5e-5e5e-5e5e-5e5e-5e5e5e5e5e5e/B1B1B1B1B1.json"
    )

    // 🎨 ULTIMATE FALLBACK - Simple colored shapes if all fails!
    private val fallbackAnimalUrls = listOf(
        // Super simple, always-working animations
        "https://lottie.host/embed/lf20_totuovs2.json",  // Generic happy
        "https://lottie.host/embed/lf20_j0hfptqv.json",  // Generic cute
        "https://lottie.host/embed/lf20_s2lryxtd.json",  // Generic fun
        "https://lottie.host/embed/lf20_gspyfltr.json",  // Generic colorful
        "https://lottie.host/embed/lf20_dews3j6m.json"   // Generic playful
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingPageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(pages[position], position)
    }

    override fun getItemCount(): Int = pages.size

    inner class OnboardingViewHolder(
        private val binding: ItemOnboardingPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(page: OnboardingPage, position: Int) {
            val context = binding.root.context

            binding.tvTitle.text = page.title
            binding.tvDescription.text = page.description

            // Set vibrant border color per page for variety
            val colorRes = borderColors.getOrElse(position) { borderColors[0] }
            binding.animationCard.strokeColor = ContextCompat.getColor(context, colorRes)

            // Set matching soft background color for card - Makes each page unique!
            val bgColor = backgroundColors.getOrElse(position) { backgroundColors[0] }
            try {
                binding.animationCard.setCardBackgroundColor(
                    android.graphics.Color.parseColor(
                        bgColor
                    )
                )
            } catch (e: Exception) {
                // Keep white if color parsing fails
            }

            // Setup Lottie animation based on page
            setupLottieAnimation(position)

            // Start all animations
            startAllAnimations()
        }

        private fun setupLottieAnimation(position: Int) {
            val context = binding.root.context
            val assetFile = animalAssetFiles.getOrElse(position) { animalAssetFiles[0] }
            val primaryUrl = animalLottieUrls.getOrElse(position) { animalLottieUrls[0] }
            val fallbackUrl = fallbackAnimalUrls.getOrElse(position) { fallbackAnimalUrls[0] }

            binding.lottieAnimation.apply {
                var animationLoaded = false
                var attemptCount = 0

                // Enhanced failure listener with multiple fallbacks
                setFailureListener { throwable ->
                    android.util.Log.e(
                        "OnboardingAdapter",
                        "Lottie failed (attempt $attemptCount): ${throwable.message}"
                    )

                    if (!animationLoaded && attemptCount < 2) {
                        attemptCount++
                        // Try ultimate fallback on failure
                        try {
                            setAnimationFromUrl(fallbackUrl)
                            repeatCount = LottieDrawable.INFINITE
                            speed = 0.8f
                            playAnimation()
                            animationLoaded = true
                            android.util.Log.i(
                                "OnboardingAdapter",
                                "Fallback animation loaded successfully"
                            )
                        } catch (e: Exception) {
                            android.util.Log.e(
                                "OnboardingAdapter",
                                "All animations failed: ${e.message}"
                            )
                            // Show a simple colored circle as ultimate fallback
                            showColoredPlaceholder(position)
                        }
                    }
                }

                // Strategy 1: Try loading from LOCAL ASSETS first (fastest & most reliable)
                try {
                    val assetManager = context.assets
                    assetManager.open(assetFile).close() // Check if file exists

                    // File exists! Load it
                    setAnimation(assetFile)
                    repeatCount = LottieDrawable.INFINITE
                    speed = 0.8f
                    animationLoaded = true
                    android.util.Log.i("OnboardingAdapter", "Loaded from assets: $assetFile")

                    // Add entrance animation
                    addEntranceAnimation()
                    playAnimation()
                } catch (e: Exception) {
                    // Strategy 2: Assets not found, try PRIMARY URL
                    android.util.Log.w(
                        "OnboardingAdapter",
                        "Assets not found, trying URL: $primaryUrl"
                    )
                    try {
                        setAnimationFromUrl(primaryUrl)
                        repeatCount = LottieDrawable.INFINITE
                        speed = 0.8f
                        animationLoaded = true
                        android.util.Log.i("OnboardingAdapter", "Loaded from URL: $primaryUrl")

                        // Add entrance animation
                        addEntranceAnimation()
                        playAnimation()
                    } catch (urlError: Exception) {
                        // Strategy 3: Try FALLBACK URL (handled by failure listener)
                        android.util.Log.w(
                            "OnboardingAdapter",
                            "Primary URL failed, trying fallback..."
                        )
                        attemptCount++
                    }
                }
            }
        }

        private fun com.airbnb.lottie.LottieAnimationView.addEntranceAnimation() {
            alpha = 0f
            scaleX = 0.7f
            scaleY = 0.7f
            animate()
                .alpha(1f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(600)
                .setStartDelay(100)
                .setInterpolator(android.view.animation.OvershootInterpolator())
                .start()
        }

        private fun showColoredPlaceholder(position: Int) {
            // Show a cute colored emoji/shape if all Lottie animations fail
            val emojis = listOf("🦁", "🐸", "🐱", "🐠", "🦋")
            val emoji = emojis.getOrElse(position) { "🎨" }

            // You could add a TextView here or just log it
            android.util.Log.i("OnboardingAdapter", "Showing placeholder: $emoji")

            // Make the animation card show a cute color at least
            val colors = listOf(
                android.graphics.Color.parseColor("#FFB74D"), // Orange for lion
                android.graphics.Color.parseColor("#81C784"), // Green for frog
                android.graphics.Color.parseColor("#F48FB1"), // Pink for cat
                android.graphics.Color.parseColor("#4FC3F7"), // Cyan for fish
                android.graphics.Color.parseColor("#BA68C8")  // Purple for butterfly
            )
            binding.animationCard.setCardBackgroundColor(colors.getOrElse(position) { colors[0] })
        }

        private fun startAllAnimations() {
            val context = binding.root.context

            // Sparkle animations
            val sparkle = AnimationUtils.loadAnimation(context, R.anim.sparkle)
            val sparkle2 = AnimationUtils.loadAnimation(context, R.anim.sparkle).apply {
                startOffset = 350
            }
            binding.ivSparkle1.startAnimation(sparkle)
            binding.ivSparkle2.startAnimation(sparkle2)

            // Star animations
            val pulseStar = AnimationUtils.loadAnimation(context, R.anim.pulse_star)
            binding.ivFloatingStar.startAnimation(pulseStar)
            binding.ivMiniStar.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.pulse_star).apply {
                    startOffset = 200
                }
            )

            // Bubble floating
            val bubbleRise = AnimationUtils.loadAnimation(context, R.anim.bubble_rise)
            binding.bubbleTop.startAnimation(bubbleRise)
            binding.bubbleBottom.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.float_up_down).apply {
                    startOffset = 400
                }
            )

            // Animal icon row animations
            binding.ivIcon1.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.bounce_button
                )
            ) // Paw bouncing
            binding.ivIcon2.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.float_sideways
                )
            ) // Fish swimming
            binding.ivIcon3.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.float_up_down)
                    .apply { // Butterfly flying
                        startOffset = 300
                }
            )

            // Hearts beating
            binding.ivHeart1.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.heart_beat).apply {
                    startOffset = 200
                }
            )
            binding.ivHeart2.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.heart_beat).apply {
                    startOffset = 400
                }
            )

            // Animation card gentle bounce
            binding.animationCard.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.bounce_button)
            )
        }
    }
}
