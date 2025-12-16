package com.kidslearning.app.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 🎨 UPGRADED DrawingView - With undo/redo and better drawing!
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
    private val guidePaint = Paint().apply {
        color = Color.LTGRAY
        textSize = 300f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        alpha = 100
    }
    
    init {
        setBackgroundColor(Color.WHITE)
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap!!)
    }
    
    override fun onDraw(canvas: Canvas) {
        // Dessiner le guide de la lettre
        if (guideText.isNotEmpty()) {
            canvas.drawText(guideText, width / 2f, height / 2f + 100f, guidePaint)
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
            }
            else -> return false
        }
        
        invalidate()
        return true
    }
    
    /**
     * Efface tout le dessin
     */
    fun clearDrawing() {
        drawPath.reset()
        paths.clear()
        undoPaths.clear()
        canvasBitmap?.eraseColor(Color.TRANSPARENT)
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
