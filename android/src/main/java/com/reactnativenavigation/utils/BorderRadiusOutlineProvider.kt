package com.reactnativenavigation.utils

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class BorderRadiusOutlineProvider(private val image: ImageView, initialRadius: Float) : ViewOutlineProvider() {
    var radius: Float = initialRadius
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