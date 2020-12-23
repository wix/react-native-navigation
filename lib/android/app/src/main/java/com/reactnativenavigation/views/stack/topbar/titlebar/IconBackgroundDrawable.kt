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

    private val mIntrinsicWidth: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getBackgroundWidth().toFloat(), Resources
            .getSystem().displayMetrics).toInt()
    private val mIntrinsicHeight: Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getBackgroundHeight().toFloat(), Resources
            .getSystem().displayMetrics).toInt()
    private val cornerRadius: Float =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconBackgroundOptions.cornerRadius.get().toFloat(), Resources
            .getSystem().displayMetrics).toFloat()

    override fun draw(canvas: Canvas) {
        drawPath(canvas)
        drawBackgroundColor(canvas)
        drawBitmap(canvas)
    }

    private fun getBackgroundWidth(): Int {
        return if (iconBackgroundOptions.width.get(getBitmapWidth()) < getBitmapWidth()) getBitmapWidth()
        else iconBackgroundOptions.width.get(getBitmapWidth())
    }

    private fun getBackgroundHeight(): Int {
        return if (iconBackgroundOptions.height.get(getBitmapHeight()) < getBitmapHeight()) getBitmapHeight()
        else iconBackgroundOptions.height.get(getBitmapHeight())
    }

    private fun getBitmapWidth(): Int {
        return wrapped.intrinsicWidth
    }

    private fun getBitmapHeight(): Int {
        return wrapped.intrinsicHeight
    }

    private fun drawBackgroundColor(canvas: Canvas) {
        val r = Rect((bounds.width() - getBackgroundWidth()) / 2,
                (bounds.height() - getBackgroundHeight()) / 2,
                bounds.width() - (bounds.width() - getBackgroundWidth()) / 2,
                bounds.height() - (bounds.height() - getBackgroundHeight()) / 2)
        canvas.drawRect(r, backgroundPaint)
    }

    private fun drawPath(canvas: Canvas) {
        if (iconBackgroundOptions.cornerRadius.hasValue()) {
            canvas.clipPath(path)
        }
    }

    private fun drawBitmap(canvas: Canvas) {
        val bitmapRect = Rect((bounds.width()-getBitmapWidth()) / 2,
                (bounds.height()-getBitmapHeight()) / 2,
                bounds.width() - (bounds.width()-getBitmapWidth()) / 2,
                bounds.height() - (bounds.height()-getBitmapHeight()) / 2)
        canvas.drawBitmap(wrapped.toBitmap(), null, bitmapRect, bitmapPaint)
    }

    override fun getIntrinsicWidth(): Int {
        return mIntrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mIntrinsicHeight
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