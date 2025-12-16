package com.kidslearning.app.utils

import android.content.Context
import android.media.MediaPlayer
import java.io.IOException

/**
 * Background Music Player for playing relaxing/fun music while children learn
 * Perfect for creating an exciting and motivating learning environment
 *
 * ⚡ SINGLETON - Music persists across activities!
 */
class BackgroundMusicPlayer private constructor(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var isLooping = true
    private var currentVolume = 0.5f // 50% volume for background music

    companion object {
        @Volatile
        private var INSTANCE: BackgroundMusicPlayer? = null

        fun getInstance(context: Context): BackgroundMusicPlayer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: BackgroundMusicPlayer(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    /**
     * Available music tracks for kids learning
     */
    enum class MusicTrack(val resourceName: String, val displayName: String) {
        HAPPY_LEARNING("happy_learning_music", "Happy Learning"),
        GENTLE_PIANO("gentle_piano_music", "Gentle Piano"),
        UPLIFTING_MELODY("uplifting_melody", "Uplifting Melody"),
        CHEERFUL_BELLS("cheerful_bells", "Cheerful Bells"),
        RAINBOW_NOTES("rainbow_notes", "Rainbow Notes");

        companion object {
            fun fromString(value: String?): MusicTrack {
                return try {
                    MusicTrack.valueOf(value?.uppercase() ?: "HAPPY_LEARNING")
                } catch (e: IllegalArgumentException) {
                    HAPPY_LEARNING
                }
            }
        }
    }

    /**
     * Start playing background music
     * ⚠️ Falls back silently if music files don't exist
     */
    fun startMusic(track: MusicTrack = MusicTrack.HAPPY_LEARNING, volume: Float = 0.5f) {
        try {
            release()
            currentVolume = volume.coerceIn(0f, 1f)

            val resId =
                context.resources.getIdentifier(track.resourceName, "raw", context.packageName)

            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(context, resId)
                mediaPlayer?.apply {
                    setVolume(currentVolume, currentVolume)
                    isLooping = this@BackgroundMusicPlayer.isLooping
                    start()
                }
            } else {
                // Try loading from assets as fallback
                try {
                    playFromAssets("music/${track.resourceName}.mp3", volume)
                } catch (e: Exception) {
                    // Silent fallback - app works without music files
                    e.printStackTrace()
                    android.util.Log.w(
                        "BackgroundMusicPlayer",
                        "Music files not found. Add MP3 files to res/raw/ folder. " +
                                "App will work without music."
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.w(
                "BackgroundMusicPlayer",
                "Failed to start music: ${e.message}. App continues without music.")
        }
    }

    /**
     * Play from assets folder
     */
    private fun playFromAssets(fileName: String, volume: Float) {
        try {
            val afd = context.assets.openFd(fileName)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                setVolume(volume, volume)
                isLooping = this@BackgroundMusicPlayer.isLooping
                start()
            }
            afd.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Pause the background music
     */
    fun pauseMusic() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Resume the background music
     */
    fun resumeMusic() {
        try {
            mediaPlayer?.let {
                if (!it.isPlaying) {
                    it.start()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Stop and release the music player
     */
    fun stopMusic() {
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: IllegalStateException) {
            // Player already released
        } finally {
            mediaPlayer = null
        }
    }

    /**
     * Release resources
     */
    fun release() {
        try {
            mediaPlayer?.apply {
                try {
                    if (isPlaying) {
                        stop()
                    }
                } catch (e: IllegalStateException) {
                    // Already stopped
                }
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaPlayer = null
        }
    }

    /**
     * Check if music is currently playing
     */
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    /**
     * Set volume (0f to 1f)
     */
    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(currentVolume, currentVolume)
    }

    /**
     * Get current volume
     */
    fun getVolume(): Float {
        return currentVolume
    }

    /**
     * Set whether music should loop
     */
    fun setLooping(loop: Boolean) {
        isLooping = loop
        mediaPlayer?.isLooping = loop
    }

    /**
     * Check if music is set to loop
     */
    fun isLooping(): Boolean {
        return isLooping
    }
}
