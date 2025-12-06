package com.kidslearning.app.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Vue personnalisée pour dessiner avec le doigt
 */
class DrawingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private val drawPath = Path()
    private val drawPaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        strokeWidth = 12f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }
    
    private val canvasPaint = Paint(Paint.DITHER_FLAG)
    private var canvasBitmap: Bitmap? = null
    private var drawCanvas: Canvas? = null
    
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
        
        // Dessiner le dessin de l'utilisateur
        canvasBitmap?.let { canvas.drawBitmap(it, 0f, 0f, canvasPaint) }
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
                drawCanvas?.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> return false
        }
        
        invalidate()
        return true
    }
    
    /**
     * Efface le dessin
     */
    fun clearDrawing() {
        drawPath.reset()
        canvasBitmap?.eraseColor(Color.TRANSPARENT)
        invalidate()
    }
    
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
}
