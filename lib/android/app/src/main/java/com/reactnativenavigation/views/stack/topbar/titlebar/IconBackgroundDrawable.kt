package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap
import com.reactnativenavigation.options.IconBackgroundOptions
import kotlin.math.max


class IconBackgroundDrawable(private val context: Context, private val wrapped: Drawable, private val iconBackground: IconBackgroundOptions, private val iconColor: Int?, private val backgroundColor: Int) : Drawable() {
    private val path: Path = Path()
    private val bitmapPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        colorFilter = iconColor?.let { PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN) }
    }
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        color = backgroundColor
    }

    private val cornerRadius = iconBackground.cornerRadius.get(0)
    private val bitmapWidth = wrapped.intrinsicWidth
    private val bitmapHeight = wrapped.intrinsicHeight
    private val backgroundWidth =  iconBackground.width.get(bitmapWidth).let { max(it, bitmapWidth) }
    private val backgroundHeight =  iconBackground.height.get(bitmapHeight).let { max(it, bitmapHeight) }
    private var backgroundRect = Rect()
    private var bitmapRect = Rect();

    override fun draw(canvas: Canvas) {
        drawPath(canvas)
        drawBackgroundColor(canvas)
        drawBitmap(canvas)
    }

    private fun drawBackgroundColor(canvas: Canvas) {
        canvas.drawRect(backgroundRect, backgroundPaint)
    }

    private fun drawPath(canvas: Canvas) {
        if (iconBackground.cornerRadius.hasValue()) {
            canvas.clipPath(path)
        }
    }

    private fun drawBitmap(canvas: Canvas) {
        canvas.drawBitmap(wrapped.toBitmap(), null, bitmapRect, bitmapPaint)
    }

    override fun setBounds(l: Int, t: Int, r: Int, b: Int) {
        updatePath(RectF(l.toFloat(), t.toFloat(), backgroundWidth.toFloat(), backgroundHeight.toFloat()))
        super.setBounds(l, t, backgroundWidth, backgroundHeight)
    }

    override fun setBounds(r: Rect) {
        r.right = backgroundWidth
        r.bottom = backgroundHeight
        updatePath(RectF(r))
        super.setBounds(r)
    }

    override fun onBoundsChange(bounds: Rect?) {
        if (bounds != null) {
            backgroundRect = Rect((bounds.width() - backgroundWidth) / 2,
                    (bounds.height() - backgroundHeight) / 2,
                    bounds.width() - (bounds.width() - backgroundWidth) / 2,
                    bounds.height() - (bounds.height() - backgroundHeight) / 2)
            bitmapRect = Rect((bounds.width()-bitmapWidth) / 2,
                    (bounds.height()-bitmapHeight) / 2,
                    bounds.width() - (bounds.width()-bitmapWidth) / 2,
                    bounds.height() - (bounds.height()-bitmapHeight) / 2)
        }
        super.onBoundsChange(bounds)
    }

    override fun setAlpha(alpha: Int) {
        wrapped.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        wrapped.colorFilter = colorFilter
    }

    private fun updatePath(r: RectF) {
        if (iconBackground.cornerRadius.hasValue()) {
            path.reset()
            path.addRoundRect(r, cornerRadius.toFloat(), cornerRadius.toFloat(), Path.Direction.CW)
        }
    }
}