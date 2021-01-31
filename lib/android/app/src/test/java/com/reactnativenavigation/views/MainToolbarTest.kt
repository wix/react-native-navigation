package com.reactnativenavigation.views

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginTop
import com.nhaarman.mockitokotlin2.spy
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
import kotlin.math.roundToInt

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
    fun `init- should Have Left Bar At Start, Dynamic Content Size, limited by half of the topBar`() {
        val layoutParams = uut.leftButtonsBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.startToStart).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.matchConstraintMaxWidth).isEqualTo((UiUtils.getWindowWidth(activity) / 2f).roundToInt())
    }

    @Test
    fun `init- should Have Right Bar At End, Dynamic Content Size, limited by half of the topBar`() {
        val layoutParams = uut.rightButtonsBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.endToEnd).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.matchConstraintMaxWidth).isEqualTo((UiUtils.getWindowWidth(activity) / 2f).roundToInt())
    }

    @Test
    fun `init- should Have Title subtitle Bar next to left buttons bar, before right buttons, Dynamic Content Size, default margin`() {
        val layoutParams = uut.getTitleComponent().layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.startToEnd).isEqualTo(uut.leftButtonsBar.id)
        Assertions.assertThat(layoutParams.endToStart).isEqualTo(uut.rightButtonsBar.id)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0f)
    }

    @Test
    fun `should change alignment of the title bar, start with margin, center no margin`() {
        uut.setTitleBarAlignment(Alignment.Center)
        var layoutParams = uut.getTitleComponent().layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0.5f)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(0)
        Assertions.assertThat(layoutParams.startToStart).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.endToEnd).isEqualTo(ConstraintSet.PARENT_ID)

        uut.setTitleBarAlignment(Alignment.Fill)
        layoutParams = uut.getTitleComponent().layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0f)
        Assertions.assertThat(layoutParams.startToEnd).isEqualTo(uut.leftButtonsBar.id)
        Assertions.assertThat(layoutParams.endToStart).isEqualTo(uut.rightButtonsBar.id)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
    }

    @Test
    fun setComponent_shouldChangeDifferentComponents(){
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)
        assertThat(uut.childCount).isEqualTo(4)

        assertThat(uut.findViewById<View?>(component.id)).isEqualTo(component)

        uut.setComponent(component2)
        assertThat(uut.childCount).isEqualTo(4)
        assertThat(uut.findViewById<View?>(component.id)).isNull()
        assertThat(uut.findViewById<View?>(component2.id)).isEqualTo(component2)

    }
    @Test
    fun setComponent_shouldReplaceTitleViewIfExist() {
        uut.setTitle("Title")
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)

        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.childCount).isEqualTo(4)
        assertThat(uut.findViewById<View?>(compId)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.GONE)
    }

    @Test
    fun setComponent_setWithComponentAlignedStartCenterVerticalBetweenLeftAndRightButtons() {
        uut = Mockito.spy(uut)
        val component = View(activity)
        uut.setComponent(component)
        Mockito.verify(uut).addView(component)
        val layoutParams = component.layoutParams as ConstraintLayout.LayoutParams
        assertThat(layoutParams.verticalBias).isEqualTo(0.5f)
        assertThat(layoutParams.horizontalBias).isEqualTo(0f)
        assertThat(layoutParams.startToEnd).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.endToStart).isEqualTo(uut.rightButtonsBar.id)
    }

    @Test
    fun setComponent_doesNothingIfComponentIsAlreadyAdded() {
        uut = Mockito.spy(uut)
        val component = View(activity)
        uut.setComponent(component)
        uut.setComponent(component)
        Mockito.verify(uut, times(1)).addView(component)
    }

    @Test
    fun setTitle_shouldChangeTheTitle(){
        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
    }

    @Test
    fun setTitle_shouldReplaceComponentIfExist() {
        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.childCount).isEqualTo(4)
        assertThat(uut.findViewById<View?>(compId)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.GONE)

        uut.setTitle("Title")
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(uut.findViewById<View?>(compId)).isNull()
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun setTitle_setTitleAtStartCenterHorizontal() {
        uut.setTitle("title")

        val passedView = uut.getTitleSubtitleBar()
        assertThat(passedView.visibility).isEqualTo(View.VISIBLE)

        val layoutParams = passedView.layoutParams as ConstraintLayout.LayoutParams
        assertThat(layoutParams.horizontalBias).isEqualTo(0f)
        assertThat(layoutParams.verticalBias).isEqualTo(0.5f)
        assertThat(layoutParams.startToEnd).isEqualTo(uut.leftButtonsBar.id)
        assertThat(layoutParams.endToStart).isEqualTo(uut.rightButtonsBar.id)

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
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.VISIBLE)
        uut.clear()
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.GONE)

        uut.setComponent(View(activity))
        assertThat(uut.getComponent()?.visibility).isEqualTo(View.VISIBLE)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.GONE)
        assertThat(uut.childCount).isEqualTo(4)
        uut.clear()
        assertThat(uut.childCount).isEqualTo(3)
        assertThat(getTitleSubtitleView().visibility).isEqualTo(View.GONE)

    }

    private fun getTitleSubtitleView() = (uut.getTitleComponent() as TitleSubTitleLayout)
}