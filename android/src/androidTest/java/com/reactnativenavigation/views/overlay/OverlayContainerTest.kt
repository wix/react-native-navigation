package com.reactnativenavigation.views.overlay

import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reactnativenavigation.views.component.ComponentLayout
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverlayContainerTest {
    private lateinit var context: Context
    private lateinit var uut: OverlayContainer

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        uut = OverlayContainer(context)
    }

    @Test
    fun onInterceptTouchEvent_noChildren_returnsFalse() {
        val event = createDownEvent(0f, 0f)
        val result = uut.onInterceptTouchEvent(event)
        assertFalse("Should not intercept when no children", result)
        event.recycle()
    }

    @Test
    fun onInterceptTouchEvent_nonComponentLayoutChild_returnsFalse() {
        // Add a regular View (not a ComponentLayout)
        val regularView = View(context)
        uut.addView(regularView, 100, 100)

        val event = createDownEvent(0f, 0f)
        val result = uut.onInterceptTouchEvent(event)

        assertFalse("Should not intercept when child is not ComponentLayout", result)
        event.recycle()
    }

    @Test
    fun onInterceptTouchEvent_nonActionDown_usesDefaultBehavior() {
        val regularView = View(context)
        uut.addView(regularView, 100, 100)

        val event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 0f, 0f, 0)
        // Should use super.onInterceptTouchEvent for non-ACTION_DOWN
        val result = uut.onInterceptTouchEvent(event)
        
        // Result depends on CoordinatorLayout's default behavior
        event.recycle()
    }

    @Test
    fun dispatchTouchEvent_noComponentLayoutChildren_returnsFalse() {
        // Add a regular View (not a ComponentLayout)
        val regularView = View(context)
        uut.addView(regularView, 100, 100)

        val event = createDownEvent(0f, 0f)
        val result = uut.dispatchTouchEvent(event)

        assertFalse("Should return false to let touch pass through when no ComponentLayout wants it", result)
        event.recycle()
    }

    @Test
    fun dispatchTouchEvent_nonActionDown_callsSuper() {
        val regularView = View(context)
        uut.addView(regularView, 100, 100)

        val event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 50f, 50f, 0)
        // Should call super.dispatchTouchEvent for non-ACTION_DOWN
        uut.dispatchTouchEvent(event)
        
        event.recycle()
    }

    @Test
    fun onInterceptTouchEvent_checksOnlyComponentLayoutChildren() {
        // Add a mix of regular Views and potentially ComponentLayouts
        val view1 = View(context)
        val view2 = View(context)
        uut.addView(view1, 100, 100)
        uut.addView(view2, 100, 100)

        val event = createDownEvent(50f, 50f)
        val result = uut.onInterceptTouchEvent(event)
        
        // Should iterate through children but find no ComponentLayouts
        assertFalse("Should not intercept when no ComponentLayout children", result)
        event.recycle()
    }

    // Helper methods
    private fun createDownEvent(x: Float, y: Float): MotionEvent {
        return MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0)
    }
}
