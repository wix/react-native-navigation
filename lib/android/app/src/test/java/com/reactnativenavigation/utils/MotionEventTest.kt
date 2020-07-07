package com.reactnativenavigation.utils

import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.widget.FrameLayout
import androidx.core.view.marginTop
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class MotionEventTest : BaseTest() {
    private lateinit var uut: MotionEvent
    private lateinit var activity: Activity
    private lateinit var parent: FrameLayout
    private val x = 173f
    private val y = 249f

    override fun beforeEach() {
        uut = MotionEvent.obtain(0L, 0, 0, x, y, 0)
        activity = newActivity()
        parent = FrameLayout(activity)
        activity.setContentView(parent)
    }

    @Test
    fun coordinatesInsideView() {
        val v: View = mock()
        assertThat(uut.coordinatesInsideView(v)).isFalse()
    }

    @Test
    fun coordinatesInsideView_inside() {
        val view = View(activity)
        parent.addView(view, 200, 300)
        assertThat(uut.coordinatesInsideView(view)).isTrue()
    }

    @Test
    fun coordinatesInsideView_outside() {
        val view = View(activity)
        parent.addView(view, 200, 300)
        view.top = (y + 1).toInt()
        assertThat(uut.coordinatesInsideView(view)).isFalse()
    }
}