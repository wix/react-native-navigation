package com.reactnativenavigation.views.stack.topbar.titlebar

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.RestrictTo
import androidx.core.graphics.drawable.toBitmap
import com.reactnativenavigation.options.params.DensityPixel


class IconBackgroundDrawable(
        private val wrapped: Drawable,
        private val cornerRadius: DensityPixel,
        private val backgroundWidth: Int,
        private val backgroundHeight: Int,
        private val iconColor: Int?,
        val backgroundColor: Int?
) : Drawable() {
    private val path: Path = Path()
    private val bitmapPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        colorFilter = iconColor?.let { PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN) }
    }
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        backgroundColor?.let {
            color = it
        }
    }

    private val bitmapWidth = wrapped.intrinsicWidth
    private val bitmapHeight = wrapped.intrinsicHeight
    private var backgroundRect = Rect()
    private var bitmapRect = Rect();

    override fun draw(canvas: Canvas) {
        drawPath(canvas)
        drawBackgroundColor(canvas)
        drawBitmap(canvas)
    }

    private fun drawBackgroundColor(canvas: Canvas) {
        backgroundColor?.let {
            canvas.drawRect(backgroundRect, backgroundPaint)
        }
    }

    private fun drawPath(canvas: Canvas) {
        if (cornerRadius.hasValue()) {
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
            bitmapRect = Rect((bounds.width() - bitmapWidth) / 2,
                    (bounds.height() - bitmapHeight) / 2,
                    bounds.width() - (bounds.width() - bitmapWidth) / 2,
                    bounds.height() - (bounds.height() - bitmapHeight) / 2)
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
        if (cornerRadius.hasValue()) {
            path.reset()
            val radius = cornerRadius.get(0).toFloat()
            path.addRoundRect(r, radius, radius, Path.Direction.CW)
        }
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun getWrappedDrawable(): Drawable = this.wrapped
}