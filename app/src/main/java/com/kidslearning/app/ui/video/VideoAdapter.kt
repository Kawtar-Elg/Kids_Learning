package com.alphapals.app.ui.video

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alphapals.app.R
import com.alphapals.app.data.model.LearningVideo
import com.alphapals.app.data.model.VideoCategory
import com.alphapals.app.data.model.VideoLanguage
import com.alphapals.app.databinding.ItemVideoBinding

/**
 * Adapter for displaying educational videos in a RecyclerView
 */
class VideoAdapter(
    private var videos: List<LearningVideo>,
    private val isArabic: Boolean,
    private val onVideoClick: (LearningVideo) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    fun updateVideos(newVideos: List<LearningVideo>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    inner class VideoViewHolder(
        private val binding: ItemVideoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(video: LearningVideo) {
            val context = binding.root.context

            // Set title based on language
            binding.tvVideoTitle.text = if (isArabic) video.titleAr else video.title

            // Set description based on language
            binding.tvVideoDescription.text =
                if (isArabic) video.descriptionAr else video.description

            // Set duration
            if (video.duration.isBlank()) {
                binding.tvDuration.visibility = View.GONE
            } else {
                binding.tvDuration.visibility = View.VISIBLE
                binding.tvDuration.text = video.duration
            }

            // Load real thumbnail (YouTube)
            if (video.thumbnailUrl.isBlank()) {
                binding.ivThumbnail.setImageDrawable(null)
            } else {
                binding.ivThumbnail.load(video.thumbnailUrl) {
                    crossfade(true)
                    placeholder(R.drawable.bg_gradient_rainbow)
                    error(R.drawable.bg_gradient_rainbow)
                }
            }

            // Language chip (FR/AR)
            binding.tvLang.text = when (video.language) {
                VideoLanguage.FRENCH -> "FR"
                VideoLanguage.ARABIC -> "AR"
                VideoLanguage.BOTH -> "FR+AR"
            }

            // Set category badge
            val categoryText = when (video.category) {
                VideoCategory.ALPHABET_SONG -> if (isArabic) "أغنية" else "Chanson"
                VideoCategory.HOW_TO_WRITE -> if (isArabic) "كتابة" else "Écriture"
                VideoCategory.LETTER_SOUNDS -> if (isArabic) "أصوات" else "Sons"
                VideoCategory.FULL_TUTORIAL -> if (isArabic) "درس كامل" else "Tutoriel"
                VideoCategory.FUN_LEARNING -> if (isArabic) "تعلم ممتع" else "Amusant"
            }
            binding.tvCategory.text = categoryText

            // Set category color
            val categoryColor = when (video.category) {
                VideoCategory.ALPHABET_SONG -> R.color.accent_yellow
                VideoCategory.HOW_TO_WRITE -> R.color.accent_pink
                VideoCategory.LETTER_SOUNDS -> R.color.accent_cyan
                VideoCategory.FULL_TUTORIAL -> R.color.accent_orange
                VideoCategory.FUN_LEARNING -> R.color.accent_purple
            }
            val catColorInt = ContextCompat.getColor(context, categoryColor)
            binding.tvCategory.backgroundTintList = ColorStateList.valueOf(catColorInt)
            binding.tvCategory.setTextColor(ContextCompat.getColor(context, R.color.white))
            // Match card stroke to category for a colorful, unique look
            binding.cardVideo.strokeColor = catColorInt

            // Click listener
            binding.cardVideo.setOnClickListener {
                onVideoClick(video)
            }

            // Add animation on bind
            binding.cardVideo.alpha = 0f
            binding.cardVideo.scaleX = 0.9f
            binding.cardVideo.scaleY = 0.9f
            binding.cardVideo.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setStartDelay(((bindingAdapterPosition.coerceAtLeast(0)) % 3) * 50L)
                .start()
        }
    }
}
