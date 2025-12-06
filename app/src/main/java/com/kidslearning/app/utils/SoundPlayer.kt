package com.kidslearning.app.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.IOException

/**
 * Gestionnaire pour jouer les sons des lettres
 */
class SoundPlayer(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * Joue un son depuis une URL ou un fichier local
     */
    fun playSound(soundFileName: String, soundUrl: String? = null, onComplete: (() -> Unit)? = null) {
        try {
            release()
            
            // Essayer d'abord l'URL si disponible
            if (!soundUrl.isNullOrEmpty()) {
                playFromUrl(soundUrl, onComplete)
                return
            }
            
            // Retirer l'extension si présente
            val resourceName = soundFileName.replace(".mp3", "").replace(".wav", "")
            val resId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
            
            if (resId != 0) {
                mediaPlayer = MediaPlayer.create(context, resId)
                mediaPlayer?.setOnCompletionListener {
                    onComplete?.invoke()
                    release()
                }
                mediaPlayer?.start()
            } else {
                // Essayer de charger depuis assets
                playFromAssets(soundFileName, onComplete)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun playFromUrl(url: String, onComplete: (() -> Unit)?) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener {
                    start()
                }
                setOnCompletionListener {
                    onComplete?.invoke()
                    release()
                }
                setOnErrorListener { _, _, _ ->
                    release()
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun playFromAssets(fileName: String, onComplete: (() -> Unit)?) {
        try {
            val afd = context.assets.openFd("sounds/$fileName")
            mediaPlayer = MediaPlayer().apply {
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                setOnCompletionListener {
                    onComplete?.invoke()
                    release()
                }
                start()
            }
            afd.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    
    /**
     * Arrête et libère le MediaPlayer
     */
    fun release() {
        try {
            mediaPlayer?.apply {
                try {
                    if (isPlaying) {
                        stop()
                    }
                } catch (e: IllegalStateException) {
                    // MediaPlayer already released
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
     * Vérifie si un son est en cours de lecture
     */
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}
