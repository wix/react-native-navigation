package com.reactnativenavigation.utils

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import com.reactnativenavigation.views.element.animators.ViewOutline
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class OutlineProvider(
        private val view: View,
        private var outline: ViewOutline
) : ViewOutlineProvider() {
    val radius: Float
        get() = outline.radius

    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(
                0,
                0,
                this.outline.width.roundToInt(),
                this.outline.height.roundToInt(),
                this.outline.radius
        )
    }

    fun update(outline: ViewOutline) {
        this.outline = outline
        view.invalidateOutline()
    }
}