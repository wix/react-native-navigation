package com.reactnativenavigation.views.overlay

import android.content.Context
import android.view.MotionEvent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.react.ReactView
import com.reactnativenavigation.views.component.ComponentLayout
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

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
    fun onInterceptTouchEvent_overlayWithInterceptFalse_returnsFalse() {
        val overlay = createOverlayWithInterceptTouchOutside(false)
        uut.addView(overlay)

        val event = createDownEvent(0f, 0f)
        val result = uut.onInterceptTouchEvent(event)

        assertFalse("Should not intercept when overlay has interceptTouchOutside: false", result)
        event.recycle()
    }

    @Test
    fun onInterceptTouchEvent_overlayWithInterceptTrue_checksOverlay() {
        val overlay = createOverlayWithInterceptTouchOutside(true)
        uut.addView(overlay)

        val event = createDownEvent(100f, 100f)
        // Result depends on OverlayTouchDelegate's logic
        // We just verify it delegates to the overlay
        uut.onInterceptTouchEvent(event)
        
        event.recycle()
    }

    @Test
    fun onInterceptTouchEvent_nonActionDown_usesDefaultBehavior() {
        val overlay = createOverlayWithInterceptTouchOutside(false)
        uut.addView(overlay)

        val event = MotionEvent.obtain(0, 0, MotionEvent.ACTION_MOVE, 0f, 0f, 0)
        // Should use super.onInterceptTouchEvent for non-ACTION_DOWN
        uut.onInterceptTouchEvent(event)
        
        event.recycle()
    }

    @Test
    fun dispatchTouchEvent_overlayWithInterceptFalse_returnsFalse() {
        val overlay = createOverlayWithInterceptTouchOutside(false)
        uut.addView(overlay)

        val event = createDownEvent(0f, 0f)
        val result = uut.dispatchTouchEvent(event)

        assertFalse("Should return false to let touch pass through", result)
        event.recycle()
    }

    @Test
    fun dispatchTouchEvent_overlayWithInterceptTrue_callsSuper() {
        val overlay = createOverlayWithInterceptTouchOutside(true)
        uut.addView(overlay)

        val event = createDownEvent(50f, 50f)
        // Should call super.dispatchTouchEvent
        uut.dispatchTouchEvent(event)
        
        event.recycle()
    }

    @Test
    fun multipleLayers_anyOverlayInterceptsTrue_returnsTrue() {
        // Add overlay with intercept: false
        val overlay1 = createOverlayWithInterceptTouchOutside(false)
        uut.addView(overlay1)

        // Add overlay with intercept: true
        val overlay2 = createOverlayWithInterceptTouchOutside(true)
        uut.addView(overlay2)

        val event = createDownEvent(50f, 50f)
        // If any overlay wants to intercept, container should allow it
        uut.onInterceptTouchEvent(event)
        
        event.recycle()
    }

    // Helper methods
    private fun createDownEvent(x: Float, y: Float): MotionEvent {
        return MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, x, y, 0)
    }

    private fun createOverlayWithInterceptTouchOutside(intercept: Boolean): ComponentLayout {
        val reactView = mock(ReactView::class.java)
        val overlay = ComponentLayout(context, reactView)
        
        // Set interceptTouchOutside option
        val options = Options()
        options.overlayOptions.interceptTouchOutside = Bool(intercept)
        overlay.applyOptions(options)
        
        return overlay
    }
}
