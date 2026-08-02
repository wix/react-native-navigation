package com.reactnativenavigation.utils

import android.view.View
import com.reactnativenavigation.BaseRobolectricTest
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN_PX
import com.reactnativenavigation.views.stack.topbar.titlebar.makeTitleAtMostWidthMeasureSpec
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveHorizontalTitleBoundsLimit
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveLeftButtonsBounds
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveRightButtonsBounds
import org.junit.Test
import kotlin.test.assertEquals


class TitleAndButtonsMeasurer : BaseRobolectricTest() {
    private val parentWidth = 1080

    @Test
    fun `left buttons should be at parent start`() {
        val barWidth = 200
        val isRTL = false
        val (left, right) = resolveLeftButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(barWidth, right)
    }

    @Test
    fun `left buttons should not exceed parent width`() {
        val barWidth = parentWidth + 1
        val isRTL = false
        val (left, right) = resolveLeftButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `RTL - left buttons should be at parent end`() {
        val barWidth = 200
        val isRTL = true
        val (left, right) = resolveLeftButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(parentWidth - barWidth, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `RTL - left buttons should not exceed parent left`() {
        val barWidth = parentWidth + 1
        val isRTL = true
        val (left, right) = resolveLeftButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `right buttons should be at parent end`() {
        val barWidth = 200
        val isRTL = false
        val (left, right) = resolveRightButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(parentWidth - barWidth, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `right buttons should not exceed parent start`() {
        val barWidth = parentWidth + 1
        val isRTL = false
        val (left, right) = resolveRightButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `RTL - right buttons should be at parent start`() {
        val barWidth = 200
        val isRTL = true
        val (left, right) = resolveRightButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(barWidth, right)
    }

    @Test
    fun `RTL - right buttons should not exceed parent end`() {
        val barWidth = parentWidth + 1
        val isRTL = true
        val (left, right) = resolveRightButtonsBounds(parentWidth, barWidth, isRTL)

        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `No Buttons - Aligned start - Title should be at default left margin bar width and right margin`() {
        val barWidth = 200
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(DEFAULT_LEFT_MARGIN_PX + barWidth + DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `RTL - No Buttons - Aligned start - Title should be at the end with default margins`() {
        val barWidth = 200
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX - barWidth - DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `RTL - No Buttons - Aligned start - Title should not exceed boundaries`() {
        val barWidth = parentWidth + 1
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `No Buttons - Aligned start - Title should not exceed parent boundaries`() {
        val barWidth = parentWidth + 1
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX, right)
    }


    @Test
    fun `No Buttons - Aligned center - Title should not exceed parent boundaries`() {
        val barWidth = parentWidth + 1
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = false
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `No Buttons - Aligned center - Title should have no margin and in center`() {
        val barWidth = 200
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = false
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(parentWidth / 2 - barWidth / 2, left)
        assertEquals(parentWidth / 2 + barWidth / 2, right)
    }

    @Test
    fun `RTL - No Buttons - Aligned center - Title should have no effect`() {
        val barWidth = 200
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = true
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(parentWidth / 2 - barWidth / 2, left)
        assertEquals(parentWidth / 2 + barWidth / 2, right)
    }

    @Test
    fun `Left Buttons - Aligned start - Title should be after left buttons with default margins`() {
        val barWidth = 200
        val leftButtons = 100
        val rightButtons = 0
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX + barWidth + DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `Left Buttons - Aligned start - Title should not exceed boundaries`() {
        val barWidth = parentWidth + 1
        val leftButtons = 100
        val rightButtons = 0
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `RTL - Left Buttons - Aligned start - Title should be after left (right) buttons with default margins`() {
        val barWidth = 200
        val leftButtons = 100
        val rightButtons = 0
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX - leftButtons - barWidth - DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - DEFAULT_LEFT_MARGIN_PX - leftButtons, right)
    }

    @Test
    fun `RTL - Left Buttons - Aligned start - Title should not exceed boundaries`() {
        val barWidth = parentWidth + 1
        val leftButtons = 100
        val rightButtons = 0
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - leftButtons - DEFAULT_LEFT_MARGIN_PX, right)
    }


    @Test
    fun `Left Buttons - Aligned center - Title should be at center`() {
        val barWidth = 200
        val leftButtons = 100
        val rightButtons = 0
        val isRTL = false
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)

        assertEquals(parentWidth / 2 - barWidth / 2, left)
        assertEquals(parentWidth / 2 + barWidth / 2, right)
    }

    @Test
    fun `Left Buttons - Aligned center - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 0
        val isRTL = false
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        val expectedOverlap = leftButtons - (parentWidth / 2 - barWidth / 2)
        assertEquals(parentWidth / 2 - barWidth / 2 + expectedOverlap, left)
        assertEquals(parentWidth / 2 + barWidth / 2 - expectedOverlap, right)
    }

    @Test
    fun `RTL - Left Buttons - Aligned center - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 0
        val isRTL = true
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        val expectedOverlap = leftButtons - (parentWidth / 2 - barWidth / 2)
        assertEquals(parentWidth / 2 - barWidth / 2 + expectedOverlap, left)
        assertEquals(parentWidth / 2 + barWidth / 2 - expectedOverlap, right)
    }


    @Test
    fun `Left + Right Buttons - Aligned center - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 350
        val isRTL = false
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(leftButtons, left)
        assertEquals(parentWidth - rightButtons, right)
    }

    @Test
    fun `RTL - Left + Right Buttons - Aligned center - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 350
        val isRTL = true
        val center = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(rightButtons, left)
        assertEquals(parentWidth - leftButtons, right)
    }

    @Test
    fun `Left + Right Buttons - Aligned start - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 350
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - rightButtons - DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `Left + Right Buttons - Aligned start - Title should'nt take amount of needed width only between buttons only`() {
        val barWidth = 100
        val leftButtons = 300
        val rightButtons = 350
        val isRTL = false
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(leftButtons + DEFAULT_LEFT_MARGIN_PX + barWidth + DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `RTL - Left + Right Buttons - Aligned start - Title should not exceed boundaries`() {
        val parentWidth = 1000
        val barWidth = 500
        val leftButtons = 300
        val rightButtons = 350
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(rightButtons + DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - leftButtons - DEFAULT_LEFT_MARGIN_PX, right)
    }

    @Test
    fun `RTL - Left + Right Buttons - Aligned start - Title should take amount of needed width only`() {
        val parentWidth = 1000
        val barWidth = 100
        val leftButtons = 300
        val rightButtons = 100
        val isRTL = true
        val center = false
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL)
        assertEquals(parentWidth - leftButtons - DEFAULT_LEFT_MARGIN_PX - barWidth - DEFAULT_LEFT_MARGIN_PX, left)
        assertEquals(parentWidth - leftButtons - DEFAULT_LEFT_MARGIN_PX, right)
    }

    // ----- makeTitleAtMostWidthMeasureSpec -----

    @Test
    fun `makeTitleAtMostWidthMeasureSpec - Fill - no buttons - EXACTLY full container width`() {
        val spec = makeTitleAtMostWidthMeasureSpec(parentWidth, 0, 0, isCenter = false, isFill = true)
        assertEquals(View.MeasureSpec.EXACTLY, View.MeasureSpec.getMode(spec))
        assertEquals(parentWidth, View.MeasureSpec.getSize(spec))
    }

    @Test
    fun `makeTitleAtMostWidthMeasureSpec - Fill - with buttons - EXACTLY width minus bars`() {
        val leftBar = 120
        val rightBar = 80
        val spec = makeTitleAtMostWidthMeasureSpec(parentWidth, rightBar, leftBar, isCenter = false, isFill = true)
        assertEquals(View.MeasureSpec.EXACTLY, View.MeasureSpec.getMode(spec))
        assertEquals(parentWidth - leftBar - rightBar, View.MeasureSpec.getSize(spec))
    }

    @Test
    fun `makeTitleAtMostWidthMeasureSpec - Center - AT_MOST full container width minus bars`() {
        val leftBar = 100
        val rightBar = 100
        val spec = makeTitleAtMostWidthMeasureSpec(parentWidth, rightBar, leftBar, isCenter = true)
        assertEquals(View.MeasureSpec.AT_MOST, View.MeasureSpec.getMode(spec))
        assertEquals(parentWidth - leftBar - rightBar, View.MeasureSpec.getSize(spec))
    }

    @Test
    fun `makeTitleAtMostWidthMeasureSpec - default (left-aligned) - keeps 2 x DEFAULT_LEFT_MARGIN_PX reservation`() {
        val leftBar = 100
        val rightBar = 100
        val spec = makeTitleAtMostWidthMeasureSpec(parentWidth, rightBar, leftBar, isCenter = false, isFill = false)
        assertEquals(View.MeasureSpec.AT_MOST, View.MeasureSpec.getMode(spec))
        assertEquals(parentWidth - leftBar - rightBar - 2 * DEFAULT_LEFT_MARGIN_PX, View.MeasureSpec.getSize(spec))
    }

    // ----- resolveHorizontalTitleBoundsLimit - Fill alignment -----

    @Test
    fun `Fill - no buttons - Title should span full container width`() {
        val barWidth = 200 // measured width of the title component
        val leftButtons = 0
        val rightButtons = 0
        val isRTL = false
        val center = false
        val isFill = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL, isFill)
        assertEquals(0, left)
        assertEquals(parentWidth, right)
    }

    @Test
    fun `Fill - with buttons - Title should span between buttons without DEFAULT_LEFT_MARGIN_PX`() {
        val barWidth = 200
        val leftButtons = 120
        val rightButtons = 80
        val isRTL = false
        val center = false
        val isFill = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL, isFill)
        assertEquals(leftButtons, left)
        assertEquals(parentWidth - rightButtons, right)
    }

    @Test
    fun `RTL - Fill - with buttons - Title should span between buttons after RTL flip`() {
        val barWidth = 200
        val leftButtons = 120
        val rightButtons = 80
        val isRTL = true
        val center = false
        val isFill = true
        val (left, right) = resolveHorizontalTitleBoundsLimit(parentWidth, barWidth, leftButtons, rightButtons, center, isRTL, isFill)
        // RTL flips logical left/right: leftButtons render on the right edge, rightButtons on the left.
        assertEquals(rightButtons, left)
        assertEquals(parentWidth - leftButtons, right)
    }
}
