package com.reactnativenavigation.views

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginTop
import com.nhaarman.mockitokotlin2.verify
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.NullColor
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN
import com.reactnativenavigation.views.stack.topbar.titlebar.MainToolBar
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times

private const val UUT_WIDTH = 1000
private const val UUT_HEIGHT = 100

class MainToolbarTest : BaseTest() {
    lateinit var uut: MainToolBar
    private lateinit var activity: Activity

    override fun beforeEach() {
        activity = newActivity()
        uut = MainToolBar(activity)
    }

    @Test
    fun `should maintain stable ids for guest component`(){
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)
        assertThat(component.id).isEqualTo(19)

        uut.setComponent(component2)
        assertThat(component.id).isEqualTo(19)
        assertThat(component2.id).isEqualTo(29)

        uut.clear()
        assertThat(component.id).isEqualTo(19)
        assertThat(component2.id).isEqualTo(29)

        uut.setComponent(component2)
        assertThat(component.id).isEqualTo(19)
        assertThat(component2.id).isEqualTo(29)

        uut.setTitle("title")
        assertThat(component.id).isEqualTo(19)
        assertThat(component2.id).isEqualTo(29)
    }
    @Test
    fun `init- should Have Left Bar At Start`() {
        val layoutParams = uut.leftButtonsBar.layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.ALIGN_PARENT_LEFT]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
    }


    @Test
    fun `init- should Have Right Bar At End`() {
        val layoutParams = uut.rightButtonsBar.layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.ALIGN_PARENT_RIGHT]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
    }

    @Test
    fun `should change alignment of the title bar, start with margin, center no margin`() {
        uut.setTitleBarAlignment(Alignment.Center)
        var layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isNotEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isNotEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isNotEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isNotEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))


        uut.setTitleBarAlignment(Alignment.Fill)
        layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isNotEqualTo(RelativeLayout.TRUE)

    }

    @Test
    fun `RTL - should change alignment of the title bar to be right, right with margin, center no margin`() {
        uut.setTitleBarAlignment(Alignment.Center)
        var layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isNotEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isNotEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isNotEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isNotEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))


        uut.setTitleBarAlignment(Alignment.Fill)
        layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isNotEqualTo(RelativeLayout.TRUE)

    }

    @Test
    fun setComponent_shouldChangeDifferentComponents() {
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)

        var layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isNotEqualTo(RelativeLayout.TRUE)

        uut.setComponent(component2, Alignment.Fill)
        layoutParams = uut.getTitleComponent().layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isNotEqualTo(RelativeLayout.TRUE)
        assertThat(uut.findViewById<View?>(component.id)).isNull()
        assertThat(uut.findViewById<View?>(component2.id)).isEqualTo(component2)

        uut.setComponent(component, Alignment.Center)
        val flayoutParams = uut.getTitleComponent().layoutParams as FrameLayout.LayoutParams
        assertThat(flayoutParams.gravity).isEqualTo(Gravity.CENTER)
    }

    @Test
    fun setComponent_shouldAlignDefaultorPassedAligment() {
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)
        assertThat(uut.findViewById<View?>(component.id)).isEqualTo(component)

        uut.setComponent(component2)
        assertThat(uut.findViewById<View?>(component.id)).isNull()
        assertThat(uut.findViewById<View?>(component2.id)).isEqualTo(component2)

    }

    @Test
    fun setComponent_shouldReplaceTitleViewIfExist() {
        uut.setTitle("Title")
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)

        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.findViewById<View?>(component.id)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.INVISIBLE)
    }

    @Test
    fun setComponent_setWithComponentAlignedStartCenterVerticalBetweenLeftAndRightButtons() {
        uut = Mockito.spy(uut)
        val component = View(activity)
        uut.setComponent(component)
        val layoutParams = component.layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        assertThat(layoutParams.rules[RelativeLayout.CENTER_IN_PARENT]).isNotEqualTo(RelativeLayout.TRUE)
    }


    @Test
    fun setComponent_doesNothingIfComponentIsAlreadyAdded() {
        val component = View(activity)
        component.id = 19
        uut.setComponent(component)
        idleMainLooper()
        val firstCompId = component.id
        assertThat(uut.findViewById<View?>(firstCompId)).isNotNull()
        uut.setComponent(component)
        assertThat(uut.findViewById<View?>(component.id)).isNotNull()
        assertThat(firstCompId).isEqualTo(component.id)
    }

    @Test
    fun setTitle_shouldChangeTheTitle() {
        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
    }

    @Test
    fun setTitle_shouldReplaceComponentIfExist() {
        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        val id = component.id
        assertThat(uut.findViewById<View?>(id)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.INVISIBLE)

        uut.setTitle("Title")
        assertThat(uut.findViewById<View?>(id)).isNull()
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun setTitle_setTitleAtStartCenterHorizontal() {
        uut.setTitle("title")

        val passedView = uut.getTitleSubtitleBar()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)

        val layoutParams = passedView.layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))

        assertThat(passedView.getTitleTxtView().text).isEqualTo("title")
    }

    @Test
    fun setTitle_setTitleAtStartCenterHorizontalRTL() {
        uut.layoutDirection = View.LAYOUT_DIRECTION_RTL
        uut.setTitle("title")

        val passedView = uut.getTitleSubtitleBar()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)

        val layoutParams = passedView.layoutParams as RelativeLayout.LayoutParams
        assertThat(layoutParams.rules[RelativeLayout.CENTER_VERTICAL]).isEqualTo(RelativeLayout.TRUE)
        assertThat(layoutParams.rules[RelativeLayout.RIGHT_OF]).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.rules[RelativeLayout.LEFT_OF]).isEqualTo(uut.rightButtonsBar.id)
        assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))

        assertThat(passedView.getTitleTxtView().text).isEqualTo("title")
    }


    @Test
    fun setSubTitle_textShouldBeAlignedAtStartCenterVertical() {
        uut.setSubtitle("Subtitle")
        val passedView = uut.getTitleSubtitleBar()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)
        assertThat(passedView.getSubTitleTxtView().text).isEqualTo("Subtitle")
        assertThat((passedView.getSubTitleTxtView().layoutParams as LinearLayout.LayoutParams).gravity).isEqualTo(Gravity.START or Gravity.CENTER_VERTICAL)
    }

    @Test
    fun setBackgroundColor_changesTitleBarBgColor() {
        uut = Mockito.spy(uut)
        uut.setBackgroundColor(NullColor())
        verify(uut, times(0)).setBackgroundColor(Color.GRAY)
        uut.setBackgroundColor(Colour(Color.GRAY))
        verify(uut, times(1)).setBackgroundColor(Color.GRAY)
    }

    @Test
    fun setTitleFontSize_changesTitleFontSize() {
        uut.setTitleFontSize(1f)
        Assertions.assertThat(getTitleSubtitleView().getTitleTxtView().textSize).isEqualTo(1f)
    }

    @Test
    fun setSubTitleFontSize_changesTitleFontSize() {
        uut.setSubtitleFontSize(1f)
        Assertions.assertThat(getTitleSubtitleView().getSubTitleTxtView().textSize).isEqualTo(1f)
    }

    @Test
    fun setTitleColor_changesTitleColor() {
        uut.setTitleColor(Color.YELLOW)
        assertThat(getTitleSubtitleView().getTitleTxtView().currentTextColor).isEqualTo(Color.YELLOW)
    }

    @Test
    fun setSubTitleColor_changesTitleColor() {
        uut.setSubtitleColor(Color.YELLOW)
        assertThat(getTitleSubtitleView().getSubTitleTxtView().currentTextColor).isEqualTo(Color.YELLOW)
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
        uut.setTitle("Title")
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.VISIBLE)
        uut.clear()
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.INVISIBLE)

        uut.setComponent(View(activity))
        assertThat(uut.getComponent()?.visibility).isEqualTo(View.VISIBLE)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.INVISIBLE)
        uut.clear()
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.INVISIBLE)

    }

    private fun getTitleSubtitleView() = (uut.getTitleComponent() as TitleSubTitleLayout)
}