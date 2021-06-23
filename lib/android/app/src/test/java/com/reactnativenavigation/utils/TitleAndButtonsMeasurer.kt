package com.reactnativenavigation.utils

import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN_PX
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveLeftButtonsBounds
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveRightButtonsBounds
import com.reactnativenavigation.views.stack.topbar.titlebar.resolveHorizontalTitleBoundsLimit
import org.junit.Test
import kotlin.test.assertEquals

class TitleAndButtonsMeasurer : BaseTest() {
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
}