package com.kidslearning.app.ui.arabic

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kidslearning.app.R
import com.kidslearning.app.data.repository.LetterRepository
import com.kidslearning.app.databinding.ActivityAlphabetBinding
import com.kidslearning.app.ui.base.BaseActivity
import com.kidslearning.app.ui.drawing.DrawingActivity
import com.kidslearning.app.utils.SoundPlayer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Arabic Alphabet Activity with animations and localization support
 */
class ArabicAlphabetActivity : BaseActivity() {

    private lateinit var binding: ActivityAlphabetBinding
    private lateinit var repository: LetterRepository
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var adapter: LetterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlphabetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = LetterRepository(this)
        soundPlayer = SoundPlayer(this)

        setupToolbar()
        setupRecyclerView()
        observeLetters()
        startAnimations()
    }

    private fun setupToolbar() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = LetterAdapter { letter ->
            // Jouer le son de la lettre
            soundPlayer.playSound(letter.soundFileName, letter.soundUrl)

            // Ouvrir l'activité de dessin après un court délai
            binding.root.postDelayed({
                val intent = Intent(this, DrawingActivity::class.java).apply {
                    putExtra("LETTER_ID", letter.id)
                }
                startActivity(intent)
            }, 500)
        }

        binding.rvLetters.apply {
            layoutManager = GridLayoutManager(this@ArabicAlphabetActivity, 3)
            this.adapter = this@ArabicAlphabetActivity.adapter
            setHasFixedSize(true)

            // Add beautiful layout animation - letters fall down one by one!
            layoutAnimation = android.view.animation.AnimationUtils.loadLayoutAnimation(
                this@ArabicAlphabetActivity,
                R.anim.layout_animation_fall_down
            )
        }
    }

    private fun observeLetters() {
        lifecycleScope.launch {
            repository.getArabicLetters().collectLatest { letters ->
                adapter.submitList(letters)
            }
        }
    }

    private fun startAnimations() {
        // Load animations
        val floatUpDown = AnimationUtils.loadAnimation(this, R.anim.float_up_down)
        val floatSlow = AnimationUtils.loadAnimation(this, R.anim.float_slow)
        val pulseStar = AnimationUtils.loadAnimation(this, R.anim.pulse_star)
        val bubbleRise = AnimationUtils.loadAnimation(this, R.anim.bubble_rise)
        val heartBeat = AnimationUtils.loadAnimation(this, R.anim.heart_beat)

        // Title stars pulsing
        binding.ivTitleStar1.startAnimation(pulseStar)
        binding.ivTitleStar2.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
                startOffset = 400
            })

        // Floating star
        binding.ivFloatingStar.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.pulse_star).apply {
                startOffset = 200
            })

        // Cloud floating
        binding.ivBottomCloud.startAnimation(floatSlow)

        // Bubbles rising
        binding.bubbleYellow.startAnimation(floatUpDown)
        binding.bubblePink.startAnimation(bubbleRise)
        binding.bubbleBlue.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.bubble_rise).apply {
                startOffset = 600
            })

        // Heart beating
        binding.ivFloatingHeart.startAnimation(heartBeat)
    }

    override fun onResume() {
        super.onResume()
        startAnimations()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
    }
}
