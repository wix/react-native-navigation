package com.reactnativenavigation.views.stack.topbar.titlebar

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.nhaarman.mockitokotlin2.verify
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.NullColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.times

class TitleBarTest : BaseTest() {
    private lateinit var uut: TitleBar
    private lateinit var activity: Activity

    override fun beforeEach() {
        activity = newActivity()
        uut = Mockito.spy(TitleBar(activity))
    }

    @Test
    fun setComponent_shouldReplaceTitleViewIfExist() {
        uut = TitleBar(activity)
        uut.setTitle("Title")
        assertThat(uut.childCount).isEqualTo(1)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.VISIBLE)

        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.childCount).isEqualTo(2)
        assertThat(uut.findViewById<View?>(compId)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.GONE)
    }

    @Test
    fun setComponent_setWithComponentAlignedStartCenterVertical() {
        val component = View(activity)
        uut.setComponent(component)
        Mockito.verify(uut).addView(component)
        val layoutParams = component.layoutParams as ConstraintLayout.LayoutParams
        assertThat(layoutParams.verticalBias).isEqualTo(0.5f)
        assertThat(layoutParams.horizontalBias).isEqualTo(0f)
    }

    @Test
    fun setComponent_setWithComponentAlignedStartCenterVerticalRTL() {
        val component = View(activity)
        `when`(uut.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL)
        uut.setComponent(component)
        Mockito.verify(uut).addView(component)
        val layoutParams = component.layoutParams as ConstraintLayout.LayoutParams
        assertThat(layoutParams.verticalBias).isEqualTo(0.5f)
        assertThat(layoutParams.horizontalBias).isEqualTo(0f)
    }


    @Test
    fun setComponent_doesNothingIfComponentIsAlreadyAdded() {
        val component = View(activity)
        uut.setComponent(component)
        uut.setComponent(component)
        Mockito.verify(uut, times(1)).addView(component)
    }

    @Test
    fun setTitle_shouldReplaceComponentIfExist() {
        uut = TitleBar(activity)
        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.childCount).isEqualTo(2)
        assertThat(uut.findViewById<View?>(compId)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.GONE)

        uut.setTitle("Title")
        assertThat(uut.childCount).isEqualTo(1)
        assertThat(uut.findViewById<View?>(compId)).isNull()
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun setTitle_setTitleAtStartCenterHorizontal() {
        uut.setTitle("title")
        val passedView = uut.getTitleSubtitleBarView()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).horizontalBias).isEqualTo(0f)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).verticalBias).isEqualTo(0.5f)
    }

    @Test
    fun setTitle_setTitleAtStartCenterHorizontalRTL() {
        val passedView = uut.getTitleSubtitleBarView()
        uut.setTitle("title")
        `when`(uut.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL)
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).horizontalBias).isEqualTo(0f)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).verticalBias).isEqualTo(0.5f)
    }

    @Test
    fun setSubTitle_setTitleAtStartCenterHorizontal() {
        uut.setSubtitle("Subtitle")
        val passedView = uut.getTitleSubtitleBarView()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).horizontalBias).isEqualTo(0f)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).verticalBias).isEqualTo(0.5f)
    }

    @Test
    fun setSubTitle_setTitleAtStartCenterHorizontalRTL() {
        uut.setSubtitle("Subtitle")
        `when`(uut.getLayoutDirection()).thenReturn(View.LAYOUT_DIRECTION_RTL)
        val passedView = uut.getTitleSubtitleBarView()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).horizontalBias).isEqualTo(0f)
        assertThat((passedView.layoutParams as ConstraintLayout.LayoutParams).verticalBias).isEqualTo(0.5f)
    }

    @Test
    fun setBackgroundColor_changesTitleBarBgColor() {
        uut.setBackgroundColor(NullColor())
        verify(uut, times(0)).setBackgroundColor(Color.GRAY)
        uut.setBackgroundColor(Colour(Color.GRAY))
        verify(uut, times(1)).setBackgroundColor(Color.GRAY)
    }

    @Test
    fun setTitleFontSize_changesTitleFontSize() {
        uut.setTitleFontSize(1f)
        assertThat(uut.getTitleTxtView().textSize).isEqualTo(1f)
    }

    @Test
    fun setSubTitleFontSize_changesTitleFontSize() {
        uut.setSubtitleFontSize(1f)
        assertThat(uut.getSubTitleTxtView().textSize).isEqualTo(1f)
    }

    @Test
    fun setTitleColor_changesTitleColor() {
        uut.setTitleColor(Color.YELLOW)
        assertThat(uut.getTitleTxtView().currentTextColor).isEqualTo(Color.YELLOW)
    }

    @Test
    fun setSubTitleColor_changesTitleColor() {
        uut.setSubtitleColor(Color.YELLOW)
        assertThat(uut.getSubTitleTxtView().currentTextColor).isEqualTo(Color.YELLOW)
    }

    @Test
    fun setHeight_changesTitleBarHeight() {
        val parent = FrameLayout(activity)
        parent.addView(uut)
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT)
        uut.height = UUT_HEIGHT / 2
        assertThat(uut.layoutParams.height).isEqualTo(UUT_HEIGHT / 2)
    }

    @Test
    fun setTopMargin_changesTitleBarTopMargin() {
        val parent = FrameLayout(activity)
        parent.addView(uut)
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT)
        uut.setTopMargin(10)
        assertThat(uut.marginTop).isEqualTo(10)
    }

    @Test
    fun getTitle_returnCurrentTextInTitleTextView() {
        assertThat(uut.getTitle()).isEmpty()
        uut.setTitle("TiTle")
        assertThat(uut.getTitle()).isEqualTo("TiTle")
    }

    @Test
    fun clear_shouldHideTitleAndRemoveComponent() {
        uut = TitleBar(activity)
        uut.setTitle("Title")
        assertThat(uut.childCount).isEqualTo(1)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.VISIBLE)
        uut.clear()
        assertThat(uut.childCount).isEqualTo(1)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.GONE)

        uut.setComponent(View(activity))
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.GONE)
        assertThat(uut.childCount).isEqualTo(2)
        uut.clear()
        assertThat(uut.childCount).isEqualTo(1)
        assertThat(uut.getTitleSubtitleBarView().visibility).isEqualTo(View.GONE)

    }

    companion object {
        private const val UUT_WIDTH = 1000
        private const val UUT_HEIGHT = 100
    }
}