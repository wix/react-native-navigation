package com.reactnativenavigation.views.bottomtabs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.graphics.ColorUtils
import org.mockito.kotlin.*
import com.reactnativenavigation.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.roundToInt

class BottomTabsContainerTest : BaseTest() {
    private lateinit var uut: BottomTabsContainer
    private lateinit var bottomTabs: BottomTabs

    private lateinit var activity: Activity

    override fun beforeEach() {
        this.bottomTabs = mock()
        this.activity = newActivity()
        uut = spy(BottomTabsContainer(activity, bottomTabs))
    }

    @Test
    fun `init - should have only one child as vertical LinearLayout with border and bottom tabs`() {
        assertThat(uut.childCount).isEqualTo(1)
        val childAt = uut.getChildAt(0)
        assertThat(childAt).isInstanceOf(LinearLayout::class.java)
        val linearLayout = childAt as LinearLayout
        assertThat(linearLayout.getChildAt(0)).isInstanceOf(TopOutlineView::class.java)
        assertThat(linearLayout.getChildAt(1)).isInstanceOf(BottomTabs::class.java)
    }

    @Test
    fun `init - should have defaults set for shadow and topOutline`() {
        val topOutLineView = uut.topOutLineView
        val background = topOutLineView.background as? ColorDrawable
        assertThat(background?.color).isEqualTo(DEFAULT_TOP_OUTLINE_COLOR)
        assertThat(topOutLineView.layoutParams.height).isEqualTo(DEFAULT_TOP_OUTLINE_SIZE_PX)
        assertThat(uut.shadowColor).isEqualTo(DEFAULT_SHADOW_COLOR)
        assertThat(uut.shadowDistance).isEqualTo(DEFAULT_SHADOW_DISTANCE)
        assertThat(uut.shadowAngle).isEqualTo(DEFAULT_SHADOW_ANGLE)

    }

    @Test
    fun `should change top outline color, no visible changes`() {
        uut.setTopOutLineColor(Color.RED)
        val topOutLineView = uut.topOutLineView
        val background = topOutLineView.background as? ColorDrawable
        assertThat(background?.color).isEqualTo(Color.RED)
        assertThat(topOutLineView.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun `should change top outline width, no visible changes`() {
        uut.setTopOutlineWidth(10)
        val topOutLineView = uut.topOutLineView
        assertThat(topOutLineView.layoutParams.height).isEqualTo(10)
        assertThat(topOutLineView.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun `should show top outline when calling show`() {
        val topOutLineView = uut.topOutLineView
        assertThat(topOutLineView.visibility).isEqualTo(View.GONE)

        uut.showTopLine()
        assertThat(topOutLineView.visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun `should clear top outline when calling show`() {
        val topOutLineView = uut.topOutLineView
        uut.showTopLine()
        assertThat(topOutLineView.visibility).isEqualTo(View.VISIBLE)

        uut.clearTopOutline()
        assertThat(topOutLineView.visibility).isEqualTo(View.GONE)
    }

    @Test
    fun `should update layout upon shadow color change`() {
        uut.shadowColor = Color.RED
        verify(uut, times(1)).requestLayout()
        assertThat(uut.shadowColor).isEqualTo(Color.RED)
    }

    @Test
    fun `should update color alpha upon changing opacity`() {
        uut.shadowColor = Color.RED
        assertThat(uut.shadowColor).isEqualTo(Color.RED)
        uut.setShadowOpacity(0.5f)
        assertThat(uut.shadowColor).isEqualTo(ColorUtils.setAlphaComponent(Color.RED, (0.5f * 0xFF).roundToInt()))
    }


}