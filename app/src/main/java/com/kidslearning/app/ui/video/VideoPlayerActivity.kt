package com.kidslearning.app.ui.video

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kidslearning.app.R
import com.kidslearning.app.databinding.ActivityVideoPlayerBinding
import com.kidslearning.app.ui.base.BaseActivity

/**
 * Activity for playing educational videos using ExoPlayer
 */
class VideoPlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private var player: ExoPlayer? = null
    private var videoUrl: String = ""
    private var videoTitle: String = ""
    private var isFullscreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen on during video playback
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get video data from intent
        videoUrl = intent.getStringExtra("VIDEO_URL") ?: ""
        videoTitle = intent.getStringExtra("VIDEO_TITLE") ?: ""

        if (videoUrl.isEmpty()) {
            showError(getString(R.string.video_error))
            return
        }

        setupPlayer()
        setupUI()
    }

    private fun setupPlayer() {
        // Initialize ExoPlayer
        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            // ExoPlayer can't play regular YouTube page URLs. Open those externally.
            if (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be")) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)))
                } catch (e: Exception) {
                    showError(getString(R.string.video_error))
                }
                finish()
                return
            }

            // Prepare media for direct stream/local URIs (mp4, m3u8, etc.)
            val mediaItem = MediaItem.fromUri(videoUrl)

            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            // Add player listener
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        Player.STATE_READY -> {
                            binding.progressBar.visibility = View.GONE
                            binding.errorLayout.visibility = View.GONE
                        }

                        Player.STATE_ENDED -> {
                            // Video ended, optionally show related videos or go back
                        }

                        Player.STATE_IDLE -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    showError("${getString(R.string.video_error)}: ${error.message}")
                }
            })

            // Auto-play
            exoPlayer.playWhenReady = true
        }
    }

    private fun setupUI() {
        // Find back button from custom controls
        val backButton = binding.playerView.findViewById<View>(R.id.exo_back)
        backButton?.setOnClickListener {
            finish()
        }

        // Find fullscreen button
        val fullscreenButton = binding.playerView.findViewById<View>(R.id.exo_fullscreen)
        fullscreenButton?.setOnClickListener {
            toggleFullscreen()
        }

        // Error retry button
        binding.btnRetry.setOnClickListener {
            binding.errorLayout.visibility = View.GONE
            releasePlayer()
            setupPlayer()
        }

        // Back to list button
        binding.btnBackToList.setOnClickListener {
            finish()
        }
    }

    private fun toggleFullscreen() {
        if (isFullscreen) {
            // Exit fullscreen
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        } else {
            // Enter fullscreen
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }
        isFullscreen = !isFullscreen
    }

    private fun showError(message: String) {
        binding.errorLayout.visibility = View.VISIBLE
        binding.tvError.text = message
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun releasePlayer() {
        player?.let {
            it.release()
            player = null
        }
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onResume() {
        super.onResume()
        if (player?.playWhenReady == true) {
            player?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onBackPressed() {
        if (isFullscreen) {
            toggleFullscreen()
        } else {
            super.onBackPressed()
        }
    }
}
