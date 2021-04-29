package com.reactnativenavigation.views

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.marginTop
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.NullColor
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonsBar
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN_PX
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleAndButtonsContainer
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import kotlin.math.roundToInt

private const val UUT_WIDTH = 1000
private const val UUT_HEIGHT = 100

class TitleAndButtonsContainerTest : BaseTest() {
    lateinit var uut: TitleAndButtonsContainer
    private lateinit var activity: Activity
    private lateinit var mockLeftBar: ButtonsBar;
    private lateinit var mockRightBar: ButtonsBar;
    private lateinit var mockComponent: View;
    override fun beforeEach() {
        super.beforeEach()
        setup()
    }

    private fun setup(
            mockUUT: Boolean = true,
            direction: Int = View.LAYOUT_DIRECTION_LTR,
            titleBarWidth: Int = 0,
            componentWidth: Int = 0,
            rightBarWidth: Int = 0,
            leftBarWidth: Int = 0,
            alignment: Alignment = Alignment.Default
    ) {
        activity = newActivity()
        val originalUUT = TitleAndButtonsContainer(activity)
        uut = if (mockUUT) spy(originalUUT) else originalUUT
        mockLeftBar = spy(ButtonsBar(activity))
        mockRightBar = spy(ButtonsBar(activity))
        mockComponent = spy(View(activity))
        val mockTitleSubtitleLayout = spy(TitleSubTitleLayout(activity))
        Mockito.doReturn(rightBarWidth).`when`(mockRightBar).measuredWidth
        Mockito.doReturn(leftBarWidth).`when`(mockLeftBar).measuredWidth
        if (mockUUT)
            Mockito.doReturn(direction).`when`(uut).layoutDirection
        Mockito.doReturn(titleBarWidth).`when`(mockTitleSubtitleLayout).measuredWidth
        Mockito.doReturn(componentWidth).`when`(mockComponent).measuredWidth
        uut.setTitleBarAlignment(alignment)
        if (rightBarWidth > 0 || leftBarWidth > 0)
            uut.setButtonsBars(mockLeftBar, mockRightBar)
        if (componentWidth > 0)
            uut.setComponent(mockComponent, alignment)
        uut.setTitleSubtitleLayout(mockTitleSubtitleLayout)

        activity.window.decorView.layoutDirection = direction
        activity.setContentView(FrameLayout(activity).apply {
            layoutDirection = direction
            addView(uut, ViewGroup.LayoutParams(UUT_WIDTH, UUT_HEIGHT))
        })
        idleMainLooper()
    }

    @Test
    fun `should maintain stable ids for guest component`() {
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
    fun `setTitle - should be aligned left and take all the width when no buttons`() {
        setup(titleBarWidth = 200)

        val component = uut.getTitleComponent()
        assertThat(component.left).isEqualTo(DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(UUT_WIDTH - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `setTitle - RTL - should be aligned right and take all the width when no buttons`() {
        setup(direction = View.LAYOUT_DIRECTION_RTL, titleBarWidth = 200)

        val titleComponent = uut.getTitleComponent()
        assertThat(titleComponent.right).isEqualTo(UUT_WIDTH - DEFAULT_LEFT_MARGIN_PX)
        assertThat(titleComponent.left).isEqualTo(0)
    }

    @Test
    fun `Title - should place title between the toolbars`() {
        val leftBarWidth = 50
        val rightBarWidth = 100
        setup(leftBarWidth = leftBarWidth, rightBarWidth = rightBarWidth, titleBarWidth = 200)
        val titleSubTitleLayout = uut.getTitleComponent() as TitleSubTitleLayout

        idleMainLooper()
        assertThat(titleSubTitleLayout.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(titleSubTitleLayout.right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `Title - should place title between the toolbars at center`() {
        val leftBarWidth = 50
        val rightBarWidth = 100
        val titleBarWidth = 200
        setup(
                leftBarWidth = leftBarWidth,
                rightBarWidth = rightBarWidth,
                titleBarWidth = titleBarWidth,
                alignment = Alignment.Center
        )

        idleMainLooper()
        assertThat(uut.getTitleComponent().left).isEqualTo((UUT_WIDTH / 2f - titleBarWidth / 2f).roundToInt())
        assertThat(uut.getTitleComponent().right).isEqualTo((UUT_WIDTH / 2f + titleBarWidth / 2f).roundToInt())
    }

    @Test
    fun `Title - center should not overlap with toolbars and be resized to fit between`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val titleBarWidth = (0.75 * UUT_WIDTH).roundToInt()
        val spaceBetween = UUT_WIDTH - leftBarWidth - rightBarWidth
        setup(
                leftBarWidth = leftBarWidth,
                rightBarWidth = rightBarWidth,
                titleBarWidth = titleBarWidth,
                alignment = Alignment.Center
        )

        idleMainLooper()
        assertThat(uut.getTitleComponent().left).isEqualTo((UUT_WIDTH / 2f - spaceBetween / 2f).roundToInt())
        assertThat(uut.getTitleComponent().right).isEqualTo((UUT_WIDTH / 2f + spaceBetween / 2f).roundToInt())
    }

    @Test
    fun `Title - left should not overlap with toolbars and be resized to fit between`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val titleBarWidth = (0.75 * UUT_WIDTH).roundToInt()
        setup(
                leftBarWidth = leftBarWidth,
                rightBarWidth = rightBarWidth,
                titleBarWidth = titleBarWidth,
                alignment = Alignment.Default
        )

        idleMainLooper()
        assertThat(uut.getTitleComponent().left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(uut.getTitleComponent().right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `Component - center should not overlap with toolbars and be resized to fit between`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val componentWidth = (0.75 * UUT_WIDTH).roundToInt()
        val spaceBetween = UUT_WIDTH - leftBarWidth - rightBarWidth
        setup(
                leftBarWidth = leftBarWidth,
                rightBarWidth = rightBarWidth,
                componentWidth = componentWidth,
                alignment = Alignment.Center
        )

        idleMainLooper()
        assertThat(uut.getTitleComponent().left).isEqualTo((UUT_WIDTH / 2f - spaceBetween / 2f).roundToInt())
        assertThat(uut.getTitleComponent().right).isEqualTo((UUT_WIDTH / 2f + spaceBetween / 2f).roundToInt())
    }

    @Test
    fun `Component - left should not overlap with toolbars and be resized to fit between`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val componentWidth = (0.75 * UUT_WIDTH).roundToInt()
        setup(
                leftBarWidth = leftBarWidth,
                rightBarWidth = rightBarWidth,
                componentWidth = componentWidth,
                alignment = Alignment.Default
        )

        idleMainLooper()
        assertThat(uut.getTitleComponent().left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(uut.getTitleComponent().right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `Component - should place title between the toolbars`() {
        val leftBarWidth = 50
        val rightBarWidth = 100
        setup(leftBarWidth = leftBarWidth, rightBarWidth = rightBarWidth, titleBarWidth = 0, componentWidth = 200)
        val component = uut.getTitleComponent()

        idleMainLooper()
        assertThat(component.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `Component - should place title between the toolbars at center`() {
        val componentWidth = 200
        setup(leftBarWidth = 50, rightBarWidth = 100, titleBarWidth = 0, componentWidth = componentWidth,
                alignment = Alignment.Center)
        val component = uut.getTitleComponent()

        idleMainLooper()
        assertThat(component.left).isEqualTo((UUT_WIDTH / 2f - componentWidth / 2f).roundToInt())
        assertThat(component.right).isEqualTo((UUT_WIDTH / 2f + componentWidth / 2f).roundToInt())
    }

    @Test
    fun `setTitleBarAlignment - should measure and layout children when alignment changes`() {
        val titleBarWidth = 200
        setup(
                titleBarWidth = titleBarWidth,
                alignment = Alignment.Center
        )
        var component = uut.getTitleComponent()
        idleMainLooper()

        assertThat(component.left).isEqualTo((UUT_WIDTH / 2f - titleBarWidth / 2f).roundToInt())
        assertThat(component.right).isEqualTo((UUT_WIDTH / 2f + titleBarWidth / 2f).roundToInt())

        setup(
                titleBarWidth = titleBarWidth,
                alignment = Alignment.Fill
        )
        component = uut.getTitleComponent()
        idleMainLooper()
        assertThat(component.left).isEqualTo(DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(UUT_WIDTH - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `setComponent - should set dynamic width-height and center vertically`() {
        val component = View(activity).apply { id = 19 }
        uut.setComponent(component)
        idleMainLooper()
        assertThat(component.layoutParams.width).isEqualTo(ViewGroup.LayoutParams.WRAP_CONTENT)
        assertThat(component.layoutParams.height).isEqualTo(ViewGroup.LayoutParams.WRAP_CONTENT)
        assertThat((component.layoutParams as RelativeLayout.LayoutParams).rules[RelativeLayout.CENTER_VERTICAL])
                .isEqualTo(RelativeLayout.TRUE)
    }

    @Test
    fun setComponent_shouldChangeDifferentComponents() {
        setup(mockUUT = false)
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)

        val titleComponent = uut.getTitleComponent()
        assertThat(titleComponent).isEqualTo(component)

        uut.setComponent(component2, Alignment.Fill)
        assertThat(uut.findViewById<View?>(component.id)).isNull()
        assertThat(uut.findViewById<View?>(component2.id)).isEqualTo(component2)

    }


    @Test
    fun setComponent_shouldReplaceTitleViewIfExist() {
        setup(mockUUT = false)

        uut.setTitle("Title")
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)

        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        assertThat(uut.findViewById<View?>(component.id)).isEqualTo(component)
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.INVISIBLE)
    }


    @Test
    fun setComponent_doesNothingIfComponentIsAlreadyAdded() {
        setup(componentWidth = 100)
        idleMainLooper()
        assertThat(uut.getComponent()).isNotNull()
        uut.setComponent(mockComponent)
        idleMainLooper()
        assertThat(uut.getComponent()?.id).isEqualTo(mockComponent.id)
    }

    @Test
    fun setTitle_shouldChangeTheTitle() {
        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
    }

    @Test
    fun setTitle_shouldReplaceComponentIfExist() {
        setup(mockUUT = false)
        val compId = 19
        val component = View(activity).apply { id = compId }
        uut.setComponent(component)
        val id = component.id
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.INVISIBLE)

        uut.setTitle("Title")
        assertThat(uut.getTitleSubtitleBar().visibility).isEqualTo(View.VISIBLE)
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
        uut.layout(0, 0, UUT_WIDTH, UUT_HEIGHT)
        uut.height = UUT_HEIGHT / 2
        assertThat(uut.layoutParams.height).isEqualTo(UUT_HEIGHT / 2)
    }

    @Test
    fun setTopMargin_changesTitleBarTopMargin() {
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