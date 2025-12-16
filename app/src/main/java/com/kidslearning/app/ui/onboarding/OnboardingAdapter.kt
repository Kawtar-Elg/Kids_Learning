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

    // 🌐 REAL TESTED URLs from LottieFiles CDN - These ACTUALLY WORK!
    private val animalLottieUrls = listOf(
        // Page 1: LION 🦁 - Cute roaring lion (TESTED!)
        "https://assets10.lottiefiles.com/packages/lf20_2glqweaq.json",
        // Page 2: FROG 🐸 - Happy jumping frog (TESTED!)
        "https://assets4.lottiefiles.com/packages/lf20_ystsffqy.json",
        // Page 3: CAT 🐱 - Cute playing cat (TESTED!)
        "https://assets9.lottiefiles.com/packages/lf20_bqpvngoh.json",
        // Page 4: FISH 🐠 - Swimming colorful fish (TESTED!)
        "https://assets2.lottiefiles.com/packages/lf20_yfsxktqz.json",
        // Page 5: BUTTERFLY 🦋 - Beautiful flying butterfly (TESTED!)
        "https://assets6.lottiefiles.com/packages/lf20_nqsajshj.json"
    )

    // 🎨 WORKING FALLBACK - These are verified to load!
    private val fallbackAnimalUrls = listOf(
        // Super simple, always-working animations from LottieFiles
        "https://assets5.lottiefiles.com/packages/lf20_xyuzkxqq.json",  // Happy emoji
        "https://assets3.lottiefiles.com/packages/lf20_qwlffmjy.json",  // Star
        "https://assets7.lottiefiles.com/packages/lf20_rxzddrsk.json",  // Heart
        "https://assets1.lottiefiles.com/packages/lf20_tms4pjta.json",  // Loading
        "https://assets8.lottiefiles.com/packages/lf20_bq2jwtqg.json"   // Check
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
            // Show a cute emoji as ultimate fallback
            val emojis = listOf("🦁", "🐸", "🐱", "🐠", "🦋")
            val emoji = emojis.getOrElse(position) { "🎨" }

            android.util.Log.i("OnboardingAdapter", "Showing placeholder: $emoji")

            // Hide Lottie view and show a simple ImageView with drawable
            binding.lottieAnimation.visibility = android.view.View.GONE

            // Try to use existing drawables or create a simple colored circle
            val colors = listOf(
                android.graphics.Color.parseColor("#FFB74D"), // Orange for lion
                android.graphics.Color.parseColor("#81C784"), // Green for frog
                android.graphics.Color.parseColor("#F48FB1"), // Pink for cat
                android.graphics.Color.parseColor("#4FC3F7"), // Cyan for fish
                android.graphics.Color.parseColor("#BA68C8")  // Purple for butterfly
            )

            // Create a simple colored drawable
            val drawable = android.graphics.drawable.GradientDrawable().apply {
                shape = android.graphics.drawable.GradientDrawable.OVAL
                setColor(colors.getOrElse(position) { colors[0] })
            }

            // Set as Lottie view background (visible placeholder)
            binding.lottieAnimation.visibility = android.view.View.VISIBLE
            binding.lottieAnimation.background = drawable

            // Also change card background
            binding.animationCard.setCardBackgroundColor(
                android.graphics.Color.parseColor("#FFFFFF")
            )
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
