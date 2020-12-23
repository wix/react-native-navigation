package com.reactnativenavigation.views.stack.topbar.titlebar

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.graphics.drawable.toBitmap
import com.reactnativenavigation.options.IconBackgroundOptions
import com.reactnativenavigation.utils.UiUtils


class IconBackgroundDrawable(private val context: Context, private val wrapped: Drawable, private val iconBackgroundOptions: IconBackgroundOptions, private val iconColor: Int?) : Drawable() {
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

    private val cornerRadius: Float =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconBackgroundOptions.cornerRadius.get().toFloat(), Resources
            .getSystem().displayMetrics).toFloat()

    override fun draw(canvas: Canvas) {
        drawPath(canvas)
        drawBackgroundColor(canvas)
        drawBitmap(canvas)
    }

    override fun getIntrinsicWidth(): Int {
        return UiUtils.dpToPx(context, getBackgroundWidth())
    }

    override fun getIntrinsicHeight(): Int {
        return UiUtils.dpToPx(context, getBackgroundHeight())
    }

    override fun setBounds(l: Int, t: Int, r: Int, b: Int) {
        updatePath(RectF(l.toFloat(), t.toFloat(), getBackgroundWidth().toFloat(), getBackgroundHeight().toFloat()))
        super.setBounds(l, t, getBackgroundWidth(), getBackgroundHeight())
    }

    override fun setBounds(r: Rect) {
        r.right = getBackgroundWidth()
        r.bottom = getBackgroundHeight()
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

    private fun getBackgroundWidth(): Int {
        val backgroundWidth = iconBackgroundOptions.width.get(getBitmapWidthDp())
        return if (backgroundWidth < getBitmapWidthDp()) UiUtils.dpToPx(context, getBitmapWidthDp())
        else UiUtils.dpToPx(context, backgroundWidth)
    }

    private fun getBackgroundHeight(): Int {
        val backgroundHeight = iconBackgroundOptions.height.get(getBitmapHeightDp())
        return if (backgroundHeight < getBitmapHeightDp()) UiUtils.dpToPx(context, getBitmapHeightDp())
        else UiUtils.dpToPx(context, backgroundHeight)
    }

    private fun getBitmapWidthDp(): Int {
        return UiUtils.pxToDp(context, getBitmapWidth().toFloat()).toInt()
    }

    private fun getBitmapHeightDp(): Int {
        return UiUtils.pxToDp(context, getBitmapHeight().toFloat()).toInt()
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

    private fun updatePath(r: RectF) {
        if (iconBackgroundOptions.cornerRadius.hasValue()) {
            path.reset()
            path.addRoundRect(r, cornerRadius, cornerRadius, Path.Direction.CW)
        }
    }
}