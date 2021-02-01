package com.reactnativenavigation.views.bottomtabs

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.nhaarman.mockitokotlin2.*
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.utils.UiUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BottomTabsContainerTest : BaseTest() {
    private lateinit var uut: BottomTabsContainer
    private lateinit var bottomTabs: BottomTabs

    private lateinit var activity: Activity

    override fun beforeEach() {
        this.bottomTabs = mock()
        this.activity = newActivity()
        uut = spy(BottomTabsContainer(activity,bottomTabs))
    }

    @Test
    fun `init - should have bottom tabs, outline, shadow children`() {
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(uut.getChildAt(0)).isInstanceOf(ShadowRectView::class.java)
        assertThat(uut.getChildAt(1)).isInstanceOf(TopOutlineView::class.java)
        assertThat(uut.getChildAt(2)).isInstanceOf(BottomTabs::class.java)
    }

    @Test
    fun `init - should have defaults set for shadow and topOutline`() {
        val topOutLineView = uut.topOutLineView
        val background = topOutLineView.background as? ColorDrawable
        assertThat(background?.color).isEqualTo(DEFAULT_TOP_OUTLINE_COLOR)
        assertThat(topOutLineView.layoutParams.height).isEqualTo(DEFAULT_TOP_OUTLINE_SIZE_PX)

        val shadowView = uut.shadowRectView
        assertThat(shadowView.layoutParams.height).isEqualTo(UiUtils.dpToPx(activity, SHADOW_HEIGHT_DP))
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




}