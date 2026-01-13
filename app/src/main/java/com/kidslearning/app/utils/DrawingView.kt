package com.alphapals.app.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 🎨 UPGRADED DrawingView - With undo/redo, better drawing, and letter validation!
 */
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val drawPath = Path()
    private val drawPaint = Paint().apply {
        color = Color.parseColor("#FF69B4") // Pink default
        isAntiAlias = true
        strokeWidth = 20f // Thicker for kids
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val canvasPaint = Paint(Paint.DITHER_FLAG)
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null

    // Undo/Redo support
    private val paths = mutableListOf<Pair<Path, DrawingStyle>>()
    private val undoPaths = mutableListOf<Pair<Path, DrawingStyle>>()

    data class DrawingStyle(
        val color: Int,
        val strokeWidth: Float
    )

    // Guide de la lettre
    private var guideText: String = ""
    private var isLetterCorrect: Boolean = false
    private val guidePaint = Paint().apply {
        color = Color.LTGRAY
        textSize = 600f  // 🔤 MUCH BIGGER for easier tracing!
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        alpha = 100
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    // Callback for when letter is traced correctly
    var onLetterTracedCorrectly: (() -> Unit)? = null

    // Validation settings
    private var validationEnabled = true
    private var hasBeenValidated = false
    private val requiredCoveragePercentage = 30f // 30% of letter area needs to be covered
    
    init {
        setBackgroundColor(Color.WHITE)
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }
    
    override fun onDraw(canvas: Canvas) {
        // Dessiner le guide de la lettre - CENTERED PERFECTLY for big letters!
        if (guideText.isNotEmpty()) {
            // Change color to green if letter is correct! 🎉
            if (isLetterCorrect) {
                guidePaint.color = Color.parseColor("#4CAF50") // Beautiful green!
                guidePaint.alpha = 200 // More visible
            } else {
                guidePaint.color = Color.LTGRAY
                guidePaint.alpha = 100
            }

            // Calculate vertical center accounting for text metrics
            val textBounds = Rect()
            guidePaint.getTextBounds(guideText, 0, guideText.length, textBounds)
            val textHeight = textBounds.height()
            canvas.drawText(guideText, width / 2f, height / 2f + textHeight / 2f, guidePaint)
        }
        
        // Dessiner tous les paths précédents
        for ((path, drawStyle) in paths) {
            val paint = Paint().apply {
                color = drawStyle.color
                isAntiAlias = true
                strokeWidth = drawStyle.strokeWidth
                style = Paint.Style.STROKE
                strokeJoin = Paint.Join.ROUND
                strokeCap = Paint.Cap.ROUND
            }
            canvas.drawPath(path, paint)
        }

        // Dessiner le path actuel
        canvas.drawPath(drawPath, drawPaint)
    }
    
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                // Save current path with its style
                val pathCopy = Path(drawPath)
                val styleCopy = DrawingStyle(drawPaint.color, drawPaint.strokeWidth)
                paths.add(Pair(pathCopy, styleCopy))
                undoPaths.clear() // Clear redo history
                drawPath.reset()

                // Validate letter tracing if enabled and not already validated
                if (validationEnabled && !hasBeenValidated && guideText.isNotEmpty()) {
                    validateLetterTracing()
                }
            }

            else -> return false
        }

        invalidate()
        return true
    }

    /**
     * 🎯 Validates if the letter has been traced correctly
     */
    private fun validateLetterTracing() {
        // Need at least 5 strokes to consider validation
        if (paths.size < 3) return

        // Calculate coverage percentage
        val coveragePercentage = calculateLetterCoverage()

        // Check if coverage meets requirement
        if (coveragePercentage >= requiredCoveragePercentage) {
            // Letter traced correctly! 🎉
            isLetterCorrect = true
            hasBeenValidated = true
            invalidate()

            // Notify success after a short delay for visual feedback
            postDelayed({
                onLetterTracedCorrectly?.invoke()
            }, 500) // 0.5 second delay to show green color
        }
    }

    /**
     * 📊 Calculates what percentage of the letter area is covered by drawing
     * Simple heuristic: checks if drawing has good distribution across letter area
     */
    private fun calculateLetterCoverage(): Float {
        if (paths.isEmpty() || guideText.isEmpty()) return 0f

        // Get letter bounds
        val textBounds = Rect()
        guidePaint.getTextBounds(guideText, 0, guideText.length, textBounds)
        val letterCenterX = width / 2f
        val letterCenterY = height / 2f
        val letterWidth = textBounds.width()
        val letterHeight = textBounds.height()

        // Calculate how many strokes are near the letter area
        var strokesNearLetter = 0
        val totalPoints = paths.size

        for ((path, _) in paths) {
            val pathMeasure = PathMeasure(path, false)
            val midPoint = FloatArray(2)

            // Sample middle point of the stroke
            if (pathMeasure.length > 0) {
                pathMeasure.getPosTan(pathMeasure.length / 2, midPoint, null)

                // Check if point is within letter area (with some margin)
                val dx = kotlin.math.abs(midPoint[0] - letterCenterX)
                val dy = kotlin.math.abs(midPoint[1] - letterCenterY)
                val marginFactor = 1.2f // Allow 20% margin

                if (dx < letterWidth * marginFactor / 2 && dy < letterHeight * marginFactor / 2) {
                    strokesNearLetter++
                }
            }
        }

        // Calculate coverage percentage
        return if (totalPoints > 0) {
            (strokesNearLetter.toFloat() / totalPoints) * 100f
        } else {
            0f
        }
    }
    
    /**
     * Efface tout le dessin
     */
    fun clearDrawing() {
        drawPath.reset()
        paths.clear()
        undoPaths.clear()
        canvasBitmap?.eraseColor(Color.TRANSPARENT)
        isLetterCorrect = false
        hasBeenValidated = false
        invalidate()
    }

    /**
     * Annule le dernier trait (Undo)
     */
    fun undo() {
        if (paths.isNotEmpty()) {
            val lastPath = paths.removeAt(paths.size - 1)
            undoPaths.add(lastPath)
            invalidate()
        }
    }

    /**
     * Refait le dernier trait annulé (Redo)
     */
    fun redo() {
        if (undoPaths.isNotEmpty()) {
            val lastUndoPath = undoPaths.removeAt(undoPaths.size - 1)
            paths.add(lastUndoPath)
            invalidate()
        }
    }

    /**
     * Vérifie si undo est possible
     */
    fun canUndo(): Boolean = paths.isNotEmpty()

    /**
     * Vérifie si redo est possible
     */
    fun canRedo(): Boolean = undoPaths.isNotEmpty()

    /**
     * Définit la lettre guide
     */
    fun setGuideLetter(letter: String) {
        guideText = letter
        isLetterCorrect = false
        hasBeenValidated = false
        clearDrawing() // Clear previous drawing when new letter is set
        invalidate()
    }

    /**
     * Définit la couleur du trait
     */
    fun setDrawColor(color: Int) {
        drawPaint.color = color
    }

    /**
     * Définit l'épaisseur du trait
     */
    fun setStrokeWidth(width: Float) {
        drawPaint.strokeWidth = width
    }

    /**
     * Obtient la couleur actuelle
     */
    fun getCurrentColor(): Int = drawPaint.color

    /**
     * Obtient l'épaisseur actuelle
     */
    fun getCurrentStrokeWidth(): Float = drawPaint.strokeWidth
}
