package com.alphapals.app.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.alphapals.app.R
import com.alphapals.app.databinding.ItemOnboardingPageBinding

/**
 * Adapter for onboarding ViewPager with cute animal Lottie animations
 */
class OnboardingAdapter(
    private val pages: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    // 🌈 Vibrant Border Colors for Each Animal - Super Eye-Catching!
    private val borderColors = listOf(
        R.color.accent_orange,   // Page 1: Orange - Lion (King of the jungle!)
        R.color.accent_green,    // Page 2: Green - Gorilla (Jungle mighty!)
        R.color.accent_pink,     // Page 3: Pink - Cat (Cute and cuddly!)
        R.color.accent_cyan,     // Page 4: Cyan - Fish (Ocean blue!)
        R.color.accent_purple    // Page 5: Purple - Butterfly (Magical!)
    )

    // 🎨 Matching Background Colors - Soft & Attractive
    private val backgroundColors = listOf(
        "#FFF9E6",  // Page 1: Soft yellow/cream for Lion
        "#E6F9F0",  // Page 2: Soft mint green for Gorilla
        "#FFE6F2",  // Page 3: Soft pink for Cat
        "#E6F9FF",  // Page 4: Soft cyan for Fish
        "#F2E6FF"   // Page 5: Soft purple for Butterfly
    )

    // 🦁 LOCAL Raw Resources - Primary source (USING LOCAL FILES!)
    private val animalRawResources = listOf(
        R.raw.cute_tiger,                    // Page 1: LION 🦁 (using tiger)
        R.raw.monkey1,                       // Page 2: GORILLA 🦍
        R.raw.loading_cat,                   // Page 3: CAT 🐱
        R.raw.goldfish,                      // Page 4: FISH 🐠
        R.raw.butterfly_lottie_animation     // Page 5: BUTTERFLY 🦋
    )

    // 🌐 RELIABLE URLs from LottieFiles CDN - TESTED & WORKING!
    private val animalLottieUrls = listOf(
        // Page 1: LION 🦁 - Cute roaring lion
        "https://assets10.lottiefiles.com/packages/lf20_2glqweaq.json",
        // Page 2: GORILLA 🦍 - Happy jumping frog/monkey (kid-friendly)
        "https://assets4.lottiefiles.com/packages/lf20_ystsffqy.json",
        // Page 3: CAT 🐱 - Cute playing cat
        "https://assets9.lottiefiles.com/packages/lf20_bqpvngoh.json",
        // Page 4: FISH 🐠 - Swimming colorful fish
        "https://assets2.lottiefiles.com/packages/lf20_yfsxktqz.json",
        // Page 5: BUTTERFLY 🦋 - Beautiful flying butterfly
        "https://assets6.lottiefiles.com/packages/lf20_nqsajshj.json"
    )

    // 🎨 SECONDARY FALLBACK - Alternative working animations
    private val fallbackAnimalUrls = listOf(
        // Simple, reliable fallback animations
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

            // Setup Lottie animation based on page with crash protection
            try {
                setupLottieAnimation(position)
            } catch (e: Exception) {
                android.util.Log.e(
                    "OnboardingAdapter",
                    "Critical error in setupLottieAnimation for position $position: ${e.message}",
                    e
                )
                // Show placeholder as last resort
                showColoredPlaceholder(position)
            }

            // Start all animations
            try {
                startAllAnimations()
            } catch (e: Exception) {
                android.util.Log.e(
                    "OnboardingAdapter",
                    "Error in startAllAnimations: ${e.message}",
                    e
                )
            }
        }

        private fun setupLottieAnimation(position: Int) {
            val context = binding.root.context
            val rawResourceId = animalRawResources.getOrElse(position) { animalRawResources[0] }
            val fallbackUrl = fallbackAnimalUrls.getOrElse(position) { fallbackAnimalUrls[0] }

            binding.lottieAnimation.apply {
                var animationLoaded = false

                // Enhanced failure listener with fallback to URL
                setFailureListener { throwable ->
                    android.util.Log.e(
                        "OnboardingAdapter",
                        "Lottie failed for position $position: ${throwable.message}",
                        throwable
                    )

                    if (!animationLoaded) {
                        // Try fallback URL if raw resource fails
                        try {
                            android.util.Log.w(
                                "OnboardingAdapter",
                                "Trying fallback URL for position $position: $fallbackUrl"
                            )
                            setAnimationFromUrl(fallbackUrl)
                            repeatCount = LottieDrawable.INFINITE
                            speed = 0.8f
                            playAnimation()
                            animationLoaded = true
                            android.util.Log.i(
                                "OnboardingAdapter",
                                "Fallback URL animation loaded for position $position"
                            )
                        } catch (e: Exception) {
                            android.util.Log.e(
                                "OnboardingAdapter",
                                "All animations failed for position $position: ${e.message}",
                                e
                            )
                            // Show colored placeholder as last resort
                            showColoredPlaceholder(position)
                        }
                    }
                }

                // PRIMARY STRATEGY: Load from LOCAL RAW RESOURCES (FAST & OFFLINE!)
                try {
                    android.util.Log.i(
                        "OnboardingAdapter",
                        "Loading animation from raw resource for position $position"
                    )

                    setAnimation(rawResourceId)
                    repeatCount = LottieDrawable.INFINITE
                    speed = 0.8f
                    animationLoaded = true

                    android.util.Log.i(
                        "OnboardingAdapter",
                        "✅ Successfully loaded from raw resource for position $position"
                    )

                    // Add entrance animation
                    addEntranceAnimation()
                    playAnimation()

                } catch (e: Exception) {
                    android.util.Log.e(
                        "OnboardingAdapter",
                        "Failed to load raw resource for position $position: ${e.message}",
                        e
                    )
                    // Failure listener will handle fallback to URL
                    showColoredPlaceholder(position)
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
            try {
                // Show a cute emoji as ultimate fallback
                val emojis = listOf("🦁", "🦍", "🐱", "🐠", "🦋")
                val emoji = emojis.getOrElse(position) { "🎨" }

                android.util.Log.i(
                    "OnboardingAdapter",
                    "Showing placeholder for position $position: $emoji"
                )

                // Try to use existing drawables or create a simple colored circle
                val colors = listOf(
                    android.graphics.Color.parseColor("#FFB74D"), // Orange for lion
                    android.graphics.Color.parseColor("#81C784"), // Green for gorilla
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
                binding.lottieAnimation.apply {
                    visibility = android.view.View.VISIBLE
                    cancelAnimation()
                    background = drawable
                    alpha = 1f
                }

                // Keep card background with original color
                android.util.Log.i(
                    "OnboardingAdapter",
                    "Placeholder displayed successfully for position $position"
                )
            } catch (e: Exception) {
                android.util.Log.e(
                    "OnboardingAdapter",
                    "Error showing placeholder: ${e.message}",
                    e
                )
                // Last resort - just make lottie view visible
                binding.lottieAnimation.visibility = android.view.View.VISIBLE
            }
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
