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

    // Different border colors for each page
    private val borderColors = listOf(
        R.color.accent_yellow,   // Page 1: Yellow - Lion
        R.color.accent_green,    // Page 2: Green - Frog
        R.color.accent_pink,     // Page 3: Pink - Cat
        R.color.accent_cyan,     // Page 4: Cyan - Fish
        R.color.accent_purple    // Page 5: Purple - Butterfly
    )

    // Cute animal Lottie animations - all verified working URLs
    private val animalLottieUrls = listOf(
        // Page 1: Cute Lion/Cat waving
        "https://lottie.host/4db68bbd-31f6-4cd8-84eb-189571e2a3bf/jyqLRv94p1.json",
        // Page 2: Happy jumping frog
        "https://lottie.host/e5bd0e67-d4c5-4755-a85e-04c6bc5ef5a9/vCPQdfdmIM.json",
        // Page 3: Cute sleeping cat
        "https://lottie.host/0c8ea168-8efe-4867-93a9-f7dc9bbf436b/pxYuvzpJjr.json",
        // Page 4: Swimming fish
        "https://lottie.host/faef54d9-5bf9-4156-8e31-e830d7466845/LBBIqZRbWk.json",
        // Page 5: Flying butterfly
        "https://lottie.host/a74d5f29-80e0-44ad-b30a-1a04d9c29953/cPweWBlwqN.json"
    )

    // Backup animal animations if primary fails
    private val backupAnimalUrls = listOf(
        // Backup 1: Dog
        "https://assets9.lottiefiles.com/packages/lf20_syqnfe7c.json",
        // Backup 2: Bird
        "https://assets5.lottiefiles.com/packages/lf20_ogsnvrll.json",
        // Backup 3: Rabbit
        "https://assets2.lottiefiles.com/packages/lf20_hschnggu.json",
        // Backup 4: Duck
        "https://assets10.lottiefiles.com/packages/lf20_gkgqj2yq.json",
        // Backup 5: Elephant
        "https://assets3.lottiefiles.com/packages/lf20_1cazwtnc.json"
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

            // Set different border color per page for variety
            val colorRes = borderColors.getOrElse(position) { borderColors[0] }
            binding.animationCard.strokeColor = ContextCompat.getColor(context, colorRes)

            // Setup Lottie animation based on page
            setupLottieAnimation(position)

            // Start all animations
            startAllAnimations()
        }

        private fun setupLottieAnimation(position: Int) {
            val primaryUrl = animalLottieUrls.getOrElse(position) { animalLottieUrls[0] }
            val backupUrl = backupAnimalUrls.getOrElse(position) { backupAnimalUrls[0] }

            binding.lottieAnimation.apply {
                // Set failure listener first to handle errors
                setFailureListener {
                    // Try backup URL on failure
                    try {
                        setAnimationFromUrl(backupUrl)
                        repeatCount = LottieDrawable.INFINITE
                        speed = 0.7f
                        playAnimation()
                    } catch (e: Exception) {
                        // Keep view visible even if both fail
                    }
                }

                // Try primary URL
                try {
                    setAnimationFromUrl(primaryUrl)
                    repeatCount = LottieDrawable.INFINITE
                    speed = 0.7f // Slower for kids to enjoy
                    playAnimation()
                } catch (e: Exception) {
                    // Try backup
                    try {
                        setAnimationFromUrl(backupUrl)
                        repeatCount = LottieDrawable.INFINITE
                        speed = 0.7f
                        playAnimation()
                    } catch (ex: Exception) {
                        // Keep visible
                    }
                }
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
