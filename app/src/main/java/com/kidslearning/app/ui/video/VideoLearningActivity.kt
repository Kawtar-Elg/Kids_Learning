package com.alphapals.app.ui.video

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.alphapals.app.R
import com.alphapals.app.data.model.LearningVideo
import com.alphapals.app.data.model.VideoLanguage
import com.alphapals.app.data.model.VideoRepository
import com.alphapals.app.databinding.ActivityVideoLearningBinding
import com.alphapals.app.ui.base.BaseActivity

/**
 * Activity for browsing and selecting educational videos
 */
class VideoLearningActivity : BaseActivity() {

    private lateinit var binding: ActivityVideoLearningBinding
    private lateinit var videoAdapter: VideoAdapter
    private var currentLanguage = VideoLanguage.FRENCH
    private var isArabicUI = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoLearningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check current app language
        isArabicUI = getCurrentLanguage() == "ar"

        setupUI()
        setupRecyclerView()
        setupTabs()
        loadVideos(currentLanguage)
        startAnimations()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Set title based on language
        binding.tvTitle.text = getString(R.string.video_learning)
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(
            videos = emptyList(),
            isArabic = isArabicUI,
            onVideoClick = ::openVideo
        )

        binding.recyclerViewVideos.apply {
            layoutManager = LinearLayoutManager(this@VideoLearningActivity)
            adapter = videoAdapter

            // Add layout animation for items - makes videos appear with fall-down effect
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                this@VideoLearningActivity,
                R.anim.layout_animation_fall_down
            )
        }
    }

    private fun setupTabs() {
        // Add tabs for French and Arabic
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.french_videos))
        )
        binding.tabLayout.addTab(
            binding.tabLayout.newTab().setText(getString(R.string.arabic_videos))
        )

        // Tab selection listener with smooth transitions
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        if (currentLanguage != VideoLanguage.FRENCH) {
                            animateLanguageTransition(
                                from = VideoLanguage.ARABIC,
                                to = VideoLanguage.FRENCH,
                                slideDirection = "right"
                            )
                        }
                    }

                    1 -> {
                        if (currentLanguage != VideoLanguage.ARABIC) {
                            animateLanguageTransition(
                                from = VideoLanguage.FRENCH,
                                to = VideoLanguage.ARABIC,
                                slideDirection = "left"
                            )
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun loadVideos(language: VideoLanguage) {
        val videos = VideoRepository.getVideosByLanguage(language)
        videoAdapter.updateVideos(videos)

        // Animate RecyclerView
        binding.recyclerViewVideos.alpha = 0f
        binding.recyclerViewVideos.animate()
            .alpha(1f)
            .setDuration(400)
            .start()
    }

    /**
     * Animate smooth transition between French and Arabic videos
     */
    private fun animateLanguageTransition(
        from: VideoLanguage,
        to: VideoLanguage,
        slideDirection: String
    ) {
        currentLanguage = to

        // Slide out old content
        val slideOutAnim = if (slideDirection == "left") {
            AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
        } else {
            AnimationUtils.loadAnimation(this, R.anim.slide_out_right)
        }

        binding.recyclerViewVideos.startAnimation(slideOutAnim)

        // After slide out, load new videos and slide in
        binding.recyclerViewVideos.postDelayed({
            loadVideos(to)

            val slideInAnim = if (slideDirection == "left") {
                AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
            } else {
                AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
            }

            binding.recyclerViewVideos.startAnimation(slideInAnim)

            // Add fun decorative animations
            animateDecorations()
        }, 400)
    }

    /**
     * Animate decorative elements during language transition
     */
    private fun animateDecorations() {
        // Bounce the bubbles
        binding.bubblePink.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(200)
            .withEndAction {
                binding.bubblePink.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }
            .start()

        binding.bubbleYellow.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(200)
            .setStartDelay(100)
            .withEndAction {
                binding.bubbleYellow.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }

    private fun openVideo(video: LearningVideo) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("VIDEO_ID", video.id)
            putExtra("VIDEO_TITLE", if (isArabicUI) video.titleAr else video.title)
            putExtra("VIDEO_URL", video.videoUrl)
        }
        startActivity(intent)
    }

    private fun startAnimations() {
        // Animate decorative bubbles
        val floatUpDown = AnimationUtils.loadAnimation(this, R.anim.float_up_down)
        val bubbleRise = AnimationUtils.loadAnimation(this, R.anim.bubble_rise)

        binding.bubblePink.startAnimation(bubbleRise)
        binding.bubbleYellow.startAnimation(floatUpDown)

        // Animate toolbar entrance
        binding.toolbar.alpha = 0f
        binding.toolbar.translationY = -50f
        binding.toolbar.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .start()

        // Animate tabs entrance
        binding.tabLayout.alpha = 0f
        binding.tabLayout.translationY = 50f
        binding.tabLayout.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(500)
            .setStartDelay(200)
            .start()
    }

    override fun onResume() {
        super.onResume()
        startAnimations()
    }
}
