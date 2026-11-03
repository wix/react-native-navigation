package com.reactnativenavigation.views.bottomtabs

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import com.reactnativenavigation.BaseTest
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ShadowLayoutTest : BaseTest() {
    @Test
    fun regeneratingShadowRecyclesPreviousBitmap() {
        val view = ShadowLayout(newActivity()).apply { isShadowed = true }
        val output = Bitmap.createBitmap(40, 40, Bitmap.Config.ARGB_8888)

        measureAndDraw(view, output)
        val firstShadow = shadowBitmap(view)

        view.requestLayout()
        measureAndDraw(view, output)
        val secondShadow = shadowBitmap(view)

        assertNotSame(firstShadow, secondShadow)
        assertTrue(firstShadow.isRecycled)
        output.recycle()
    }

    private fun measureAndDraw(view: ShadowLayout, output: Bitmap) {
        val size = View.MeasureSpec.makeMeasureSpec(40, View.MeasureSpec.EXACTLY)
        view.measure(size, size)
        view.layout(0, 0, 40, 40)
        view.draw(Canvas(output))
    }

    private fun shadowBitmap(view: ShadowLayout): Bitmap {
        val field = ShadowLayout::class.java.getDeclaredField("bitmap")
        field.isAccessible = true
        return field.get(view) as Bitmap
    }
}
