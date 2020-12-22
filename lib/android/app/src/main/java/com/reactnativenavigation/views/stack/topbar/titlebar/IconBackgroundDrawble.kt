package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.graphics.drawable.toBitmap
import com.reactnativenavigation.options.IconBackgroundOptions


class IconBackgroundDrawable(private val wrapped: Drawable, private val iconBackgroundOptions: IconBackgroundOptions, private val iconColor: Int?) : Drawable() {
    private val path: Path = Path()
    private val bitmapPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        colorFilter = iconColor?.let { PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN) }
    }
    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        isFilterBitmap = true
        color = iconBackgroundOptions.color.get(Color.TRANSPARENT)

    }

    private val intrinsicWidth: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconBackgroundOptions.width.get(wrapped.intrinsicWidth).toFloat(), Resources
            .getSystem().displayMetrics).toInt()
    private val intrinsicHeight: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconBackgroundOptions.height.get(wrapped.intrinsicHeight).toFloat(), Resources
            .getSystem().displayMetrics).toInt()
    private val cornerRadius: Float =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconBackgroundOptions.cornerRadius.get().toFloat(), Resources
            .getSystem().displayMetrics).toFloat()

    override fun draw(canvas: Canvas) {
        drawPath(canvas)
        drawBackgroundColor(canvas)

        canvas.drawBitmap(wrapped.toBitmap(), null, bounds, bitmapPaint)
    }

    private fun drawBackgroundColor(canvas: Canvas) {
        canvas.drawRect(bounds, backgroundPaint)
    }

    private fun drawPath(canvas: Canvas) {
        if (iconBackgroundOptions.cornerRadius.hasValue()) {
            canvas.clipPath(path)
        }
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return intrinsicHeight
    }

    override fun setBounds(l: Int, t: Int, r: Int, b: Int) {
        updatePath(RectF(l.toFloat(), t.toFloat(), r.toFloat(), b.toFloat()))
        super.setBounds(l, t, r, b)
    }

    override fun setBounds(r: Rect) {
        updatePath(RectF(r))
        super.setBounds(r)
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
        if (iconBackgroundOptions.cornerRadius.hasValue()) {
            path.reset()
            path.addRoundRect(r, cornerRadius, cornerRadius, Path.Direction.CW)
        }
    }
}