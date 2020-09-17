package com.reactnativenavigation.utils

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import com.facebook.react.views.image.ReactImageView

class RoundCornersOutlineProvider(private val image: ReactImageView, radius: Float) : ViewOutlineProvider() {
    var radius: Float = radius
        private set

    override fun getOutline(view: View, outline: Outline) = outline.setRoundRect(
            0,
            0,
            image.clipBounds?.width() ?: image.width,
            image.clipBounds?.height() ?: image.height,
            radius
    )

    fun updateRadius(radius: Float) {
        this.radius = radius
        image.invalidateOutline()
    }
}