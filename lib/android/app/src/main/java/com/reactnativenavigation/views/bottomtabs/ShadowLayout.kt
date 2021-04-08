package com.reactnativenavigation.views.bottomtabs

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.view.View
import android.widget.FrameLayout
import kotlin.math.cos
import kotlin.math.sin

private const val MAX_ALPHA = 255
private const val MAX_ANGLE = 360.0f
private const val MIN_RADIUS = 0.1f
private const val MIN_ANGLE = 0.0f

open class ShadowLayout constructor(context: Context) : FrameLayout(context) {
    private val paint: Paint = Paint(ANTI_ALIAS_FLAG).apply {
        isDither = true
        isFilterBitmap = true
    }
    private var bitmap: Bitmap? = null
    private val mainCanvas: Canvas = Canvas()
    private val bounds = Rect()
    private var invalidateShadow = true
    private var shadowAlpha = 0
    var shadowDx = 0f
        private set
    var shadowDy = 0f
        private set


    init {
        super.setWillNotDraw(false)
        super.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        bitmap?.recycle()
        bitmap = null
    }

    var isShadowed: Boolean = false
        set(isShadowed) {
            field = isShadowed
            this.updatePadding()
            postInvalidate()
        }

    var shadowDistance: Float = 0f
        set(shadowDistance) {
            field = shadowDistance
            resetShadow()
        }

    var shadowAngle: Float = 0f
        set(shadowAngle) {
            field = MIN_ANGLE.coerceAtLeast(shadowAngle.coerceAtMost(MAX_ANGLE))
            resetShadow()
        }

    open var shadowRadius: Float = 0f
        set(shadowRadius) {
            field = MIN_RADIUS.coerceAtLeast(shadowRadius)
            paint.maskFilter = BlurMaskFilter(field, BlurMaskFilter.Blur.NORMAL)
            resetShadow()
        }

    var shadowColor: Int = 0
        set(shadowColor) {
            field = shadowColor
            shadowAlpha = Color.alpha(shadowColor)
            resetShadow()
        }

    private fun resetShadow() {
        shadowDx = (shadowDistance * cos(shadowAngle / 180.0f * Math.PI)).toFloat()
        shadowDy = (shadowDistance * sin(shadowAngle / 180.0f * Math.PI)).toFloat()
        updatePadding()
        requestLayout()
    }

    private fun updatePadding() {
        val padding = if (isShadowed) (shadowDistance + shadowRadius).toInt() else 0
        setPadding(0, padding, 0, 0)
    }

    private fun adjustShadowAlpha(adjust: Boolean): Int {
        return Color.argb(
                if (adjust) MAX_ALPHA else shadowAlpha,
                Color.red(shadowColor),
                Color.green(shadowColor),
                Color.blue(shadowColor)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bounds[0, 0, measuredWidth] = measuredHeight
    }

    override fun requestLayout() {
        invalidateShadow = true
        super.requestLayout()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (isShadowed) {
            if (invalidateShadow) {
                if (bounds.width() != 0 && bounds.height() != 0) {
                    bitmap = Bitmap.createBitmap(
                            bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888
                    )
                    bitmap?.let {
                        mainCanvas.setBitmap(bitmap)
                        invalidateShadow = false

                        super.dispatchDraw(mainCanvas)
                        val extractedAlpha = it.extractAlpha()
                        mainCanvas.drawColor(0, PorterDuff.Mode.CLEAR)

                        paint.color = adjustShadowAlpha(false)
                        mainCanvas.drawBitmap(extractedAlpha, shadowDx, shadowDy, paint)

                        extractedAlpha.recycle()
                    }

                } else {
                    bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
                }
            }
            paint.color = adjustShadowAlpha(true)
            if (bitmap != null && bitmap?.isRecycled == false) canvas.drawBitmap(bitmap!!, 0.0f, 0.0f, paint)
        }

        super.dispatchDraw(canvas)
    }

}
