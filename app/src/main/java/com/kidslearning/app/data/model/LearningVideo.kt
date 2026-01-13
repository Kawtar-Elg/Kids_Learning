package com.alphapals.app.data.model

/**
 * Data class representing an educational video
 */
data class LearningVideo(
    val id: Int,
    val title: String,
    val titleAr: String,
    val description: String,
    val descriptionAr: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: String, // Format: "5:30"
    val language: VideoLanguage,
    val category: VideoCategory,
    val isOnline: Boolean = true // true = YouTube/Vimeo, false = local file
)

enum class VideoLanguage {
    FRENCH,
    ARABIC,
    BOTH
}

enum class VideoCategory {
    ALPHABET_SONG,      // Chansons de l'alphabet
    HOW_TO_WRITE,       // Comment écrire les lettres
    LETTER_SOUNDS,      // Sons des lettres
    FULL_TUTORIAL,      // Tutoriel complet
    FUN_LEARNING        // Apprentissage amusant
}

/**
 * Repository of educational videos for kids
 */
object VideoRepository {

    private fun ytThumb(videoId: String): String =
        "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

    // 🎥 FRENCH ALPHABET VIDEOS
    val frenchVideos = listOf(
        LearningVideo(
            id = 1,
            title = "Chanson de l'Alphabet Français",
            titleAr = "أغنية الحروف الفرنسية",
            description = "Apprends l'alphabet français en chantant!",
            descriptionAr = "تعلم الحروف الفرنسية بالغناء!",
            videoUrl = "https://www.youtube.com/watch?v=_LYy3P2okyw",
            thumbnailUrl = ytThumb("_LYy3P2okyw"),
            duration = "",
            language = VideoLanguage.FRENCH,
            category = VideoCategory.ALPHABET_SONG
        ),
        LearningVideo(
            id = 2,
            title = "Comment écrire l'Alphabet",
            titleAr = "كيف تكتب الحروف",
            description = "Apprends à écrire chaque lettre étape par étape",
            descriptionAr = "تعلم كتابة كل حرف خطوة بخطوة",
            videoUrl = "https://www.youtube.com/watch?v=U-fV8HY6xrg",
            thumbnailUrl = ytThumb("U-fV8HY6xrg"),
            duration = "",
            language = VideoLanguage.FRENCH,
            category = VideoCategory.HOW_TO_WRITE
        ),
        LearningVideo(
            id = 3,
            title = "Sons des Lettres Françaises",
            titleAr = "أصوات الحروف الفرنسية",
            description = "Découvre le son de chaque lettre",
            descriptionAr = "اكتشف صوت كل حرف",
            videoUrl = "https://www.youtube.com/watch?v=g7Ly2Wayhdg",
            thumbnailUrl = ytThumb("g7Ly2Wayhdg"),
            duration = "",
            language = VideoLanguage.FRENCH,
            category = VideoCategory.LETTER_SOUNDS
        ),
        LearningVideo(
            id = 4,
            title = "Alphabet Français Complet",
            titleAr = "الحروف الفرنسية الكاملة",
            description = "Tutoriel complet de A à Z!",
            descriptionAr = "درس كامل من A إلى Z!",
            videoUrl = "https://www.youtube.com/watch?v=NTSl9DDvf4E",
            thumbnailUrl = ytThumb("NTSl9DDvf4E"),
            duration = "",
            language = VideoLanguage.FRENCH,
            category = VideoCategory.FULL_TUTORIAL
        )
    )

    // 🎥 ARABIC ALPHABET VIDEOS  
    val arabicVideos = listOf(
        LearningVideo(
            id = 11,
            title = "Chanson de l'Alphabet Arabe",
            titleAr = "أغنية الحروف العربية",
            description = "Apprends l'alphabet arabe en chantant!",
            descriptionAr = "تعلم الحروف العربية بالغناء!",
            videoUrl = "https://www.youtube.com/watch?v=kqoRd7WmRHg",
            thumbnailUrl = ytThumb("kqoRd7WmRHg"),
            duration = "",
            language = VideoLanguage.ARABIC,
            category = VideoCategory.ALPHABET_SONG
        ),
        LearningVideo(
            id = 12,
            title = "Comment écrire les Lettres Arabes",
            titleAr = "كيف تكتب الحروف العربية",
            description = "Apprends à écrire chaque lettre arabe",
            descriptionAr = "تعلم كتابة كل حرف عربي",
            videoUrl = "https://www.youtube.com/watch?v=aNNUdNhpSB8",
            thumbnailUrl = ytThumb("aNNUdNhpSB8"),
            duration = "",
            language = VideoLanguage.ARABIC,
            category = VideoCategory.HOW_TO_WRITE
        ),
        LearningVideo(
            id = 13,
            title = "Sons des Lettres Arabes",
            titleAr = "أصوات الحروف العربية",
            description = "Découvre la prononciation de chaque lettre",
            descriptionAr = "اكتشف نطق كل حرف",
            videoUrl = "https://www.youtube.com/watch?v=i1TI07BCSIk",
            thumbnailUrl = ytThumb("i1TI07BCSIk"),
            duration = "",
            language = VideoLanguage.ARABIC,
            category = VideoCategory.LETTER_SOUNDS
        ),
        LearningVideo(
            id = 14,
            title = "Alphabet Arabe Complet",
            titleAr = "الحروف العربية الكاملة",
            description = "Tutoriel complet de ا à ي!",
            descriptionAr = "درس كامل من ا إلى ي!",
            videoUrl = "https://www.youtube.com/watch?v=HKJA87xnA9g",
            thumbnailUrl = ytThumb("HKJA87xnA9g"),
            duration = "",
            language = VideoLanguage.ARABIC,
            category = VideoCategory.FULL_TUTORIAL
        )
    )

    // 🌟 Suggested YouTube Video IDs (Replace with real educational videos)
    // For French Alphabet:
    // - "Alain Le Lait - L'alphabet" 
    // - "Monde des Titounis - ABC chanson"
    // - "Pinpin et Lili - Alphabet français"

    // For Arabic Alphabet:
    // - "Arabic Alphabet Song - أغنية الحروف العربية"
    // - "Learn Arabic Letters - تعلم الحروف"
    // - "Arabic ABC Song for Kids"

    fun getAllVideos() = frenchVideos + arabicVideos

    fun getVideosByLanguage(language: VideoLanguage) = when (language) {
        VideoLanguage.FRENCH -> frenchVideos
        VideoLanguage.ARABIC -> arabicVideos
        VideoLanguage.BOTH -> getAllVideos()
    }

    fun getVideosByCategory(category: VideoCategory) =
        getAllVideos().filter { it.category == category }

    fun getVideoById(id: Int) = getAllVideos().find { it.id == id }
}
