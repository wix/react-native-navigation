package com.reactnativenavigation.views

import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.ThemeColour
import com.reactnativenavigation.options.params.NullThemeColour
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN_PX
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleAndButtonsContainer
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.Assertions
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import kotlin.math.roundToInt
import kotlin.test.assertFalse

private const val UUT_WIDTH = 1000
private const val UUT_HEIGHT = 100

class TitleAndButtonsContainerTest : BaseTest() {
    lateinit var uut: TitleAndButtonsContainer
    private lateinit var activity: Activity
    private lateinit var mockLeftBar: ButtonBar
    private lateinit var mockRightBar: ButtonBar
    private lateinit var mockComponent: View
    override fun beforeEach() {
        super.beforeEach()
        setup()
    }

    private fun setup(
            mockUUT: Boolean = true,
            direction: Int = View.LAYOUT_DIRECTION_LTR,
            titleBarWidth: Int = 0,
            titleBarHeight: Int = 0,
            componentWidth: Int = 0,
            componentHeight: Int = 0,
            rightBarWidth: Int = 0,
            leftBarWidth: Int = 0,
            alignment: Alignment = Alignment.Default
    ) {
        activity = newActivity()
        val originalUUT = TitleAndButtonsContainer(activity)
        uut = if (mockUUT) spy(originalUUT) else originalUUT
        mockLeftBar = spy(ButtonBar(activity))
        mockRightBar = spy(ButtonBar(activity))
        mockComponent = spy(View(activity))
        val mockTitleSubtitleLayout = spy(TitleSubTitleLayout(activity))
        Mockito.doReturn(rightBarWidth).`when`(mockRightBar).measuredWidth
        Mockito.doReturn(leftBarWidth).`when`(mockLeftBar).measuredWidth
        if (mockUUT)
            Mockito.doReturn(direction).`when`(uut).layoutDirection
        Mockito.doReturn(titleBarWidth).`when`(mockTitleSubtitleLayout).measuredWidth
        Mockito.doReturn(titleBarHeight).`when`(mockTitleSubtitleLayout).measuredHeight
        Mockito.doReturn(componentWidth).`when`(mockComponent).measuredWidth
        Mockito.doReturn(componentHeight).`when`(mockComponent).measuredHeight
        uut.setTitleBarAlignment(alignment)
        if (rightBarWidth > 0 || leftBarWidth > 0)
            uut.setButtonBars(mockLeftBar, mockRightBar)
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
    fun `animateLeftRightButtons - should be false as default`(){
        assertFalse(mockLeftBar.shouldAnimate)
        assertFalse(mockRightBar.shouldAnimate)
    }
    @Test
    fun `animateLeftRightButtons - should change corresponding button bar`(){
        setup(rightBarWidth = 10,leftBarWidth = 20)

        uut.animateLeftButtons(true)
        verify(mockLeftBar).shouldAnimate=true

        uut.animateLeftButtons(false)
        verify(mockLeftBar).shouldAnimate=false

        uut.animateRightButtons(true)
        verify(mockRightBar).shouldAnimate=true

        uut.animateRightButtons(false)
        verify(mockRightBar).shouldAnimate=false
    }

    @Test
    fun `setComponent - should not change component id`() {
        val component = View(activity).apply { id = 19 }
        val component2 = View(activity).apply { id = 29 }
        uut.setComponent(component)
        assertThat(component.id).isEqualTo(19)

        uut.setComponent(component2)
        assertThat(component.id).isEqualTo(19)
        assertThat(component2.id).isEqualTo(29)

        uut.clearTitle()
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
    fun `onLayout - should set title component center vertically`() {
        val componentHeight = (UUT_HEIGHT / 4f).roundToInt()
        setup(componentWidth = 100, componentHeight = componentHeight)

        val component = uut.getTitleComponent()
        assertThat(component.top).isEqualTo((UUT_HEIGHT / 2f - componentHeight / 2f).roundToInt())
        assertThat(component.bottom).isEqualTo((UUT_HEIGHT / 2f + componentHeight / 2f).roundToInt())
    }

    @Test
    fun `onLayout - set title text should take all parent height`() {
        setup(titleBarWidth = 100, titleBarHeight = UUT_HEIGHT)

        val component = uut.getTitleComponent()

        assertThat(component.layoutParams.height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
        assertThat(component.top).isEqualTo(0)
        assertThat(component.bottom).isEqualTo(UUT_HEIGHT)
    }

    @Test
    fun `onLayout - title should not overlap with toolbar and have to re-layout`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val titleBarWidth = 3 * (UUT_WIDTH / 4f).roundToInt()
        setup(titleBarWidth = titleBarWidth, titleBarHeight = UUT_HEIGHT, leftBarWidth = leftBarWidth, rightBarWidth = rightBarWidth)

        val component = uut.getTitleComponent()
        assertThat(component.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `onLayout - title component should not overlap with toolbar and have to re-layout`() {
        val leftBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val rightBarWidth = (UUT_WIDTH / 4f).roundToInt()
        val titleBarWidth = 3 * (UUT_WIDTH / 4f).roundToInt()
        setup(componentWidth = titleBarWidth, componentHeight = UUT_HEIGHT, leftBarWidth = leftBarWidth, rightBarWidth =
        rightBarWidth)

        val component = uut.getTitleComponent()
        assertThat(component.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(UUT_WIDTH - rightBarWidth - DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `onLayout - title should be aligned left and take needed the width + margins when no buttons`() {
        val titleBarWidth = 200
        setup(titleBarWidth = titleBarWidth)

        val component = uut.getTitleComponent()
        assertThat(component.left).isEqualTo(DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(titleBarWidth + 2 * DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `setTitle - RTL - should be aligned right and take available width when no buttons`() {
        val titleBarWidth = 200
        setup(direction = View.LAYOUT_DIRECTION_RTL, titleBarWidth = titleBarWidth)

        val titleComponent = uut.getTitleComponent()
        assertThat(titleComponent.right).isEqualTo(UUT_WIDTH - DEFAULT_LEFT_MARGIN_PX)
        assertThat(titleComponent.left).isEqualTo(UUT_WIDTH- DEFAULT_LEFT_MARGIN_PX-titleBarWidth-DEFAULT_LEFT_MARGIN_PX )
    }

    @Test
    fun `Title - should place title between the toolbars`() {
        val leftBarWidth = 50
        val rightBarWidth = 100
        val titleBarWidth = 200
        setup(leftBarWidth = leftBarWidth, rightBarWidth = rightBarWidth, titleBarWidth = titleBarWidth)
        val titleSubTitleLayout = uut.getTitleComponent() as TitleSubTitleLayout

        idleMainLooper()
        assertThat(titleSubTitleLayout.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(titleSubTitleLayout.right).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX + titleBarWidth + DEFAULT_LEFT_MARGIN_PX)
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
        val componentWidth = 200
        setup(leftBarWidth = leftBarWidth, rightBarWidth = rightBarWidth, titleBarWidth = 0, componentWidth = componentWidth)
        val component = uut.getTitleComponent()

        idleMainLooper()
        assertThat(component.left).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX)
        assertThat(component.right).isEqualTo(leftBarWidth + DEFAULT_LEFT_MARGIN_PX + componentWidth + DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `onLayout - should place title component between the toolbars at center`() {
        val componentWidth = 200
        setup(leftBarWidth = 50, rightBarWidth = 100, componentWidth = componentWidth,
                alignment = Alignment.Center)
        val component = uut.getTitleComponent()

        idleMainLooper()
        assertThat(component.left).isEqualTo((UUT_WIDTH / 2f - componentWidth / 2f).roundToInt())
        assertThat(component.right).isEqualTo((UUT_WIDTH / 2f + componentWidth / 2f).roundToInt())
    }

    @Test
    fun `onLayout - should measure and layout children when alignment changes`() {
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
        assertThat(component.right).isEqualTo(titleBarWidth + 2 * DEFAULT_LEFT_MARGIN_PX)
    }

    @Test
    fun `setComponent - should set dynamic width-height`() {
        val component = View(activity).apply { id = 19 }
        uut.setComponent(component)
        idleMainLooper()
        assertThat(component.layoutParams.width).isEqualTo(ViewGroup.LayoutParams.WRAP_CONTENT)
        assertThat(component.layoutParams.height).isEqualTo(ViewGroup.LayoutParams.WRAP_CONTENT)
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
    fun setComponent_shouldResetTextTitleWhenSettingComponent() {
        setup(mockUUT = false)
        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
        val component = View(activity)
        uut.setComponent(component)
        assertThat(uut.getTitle()).isNullOrEmpty()
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
    fun setTitle_shouldResetTitleComponentWhenSettingTextTitle() {
        setup(mockUUT = false)
        val component = View(activity)
        uut.setComponent(component)
        assertThat(uut.getTitle()).isNullOrEmpty()

        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
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
        uut.setBackgroundColor(NullThemeColour())
        verify(uut, times(0)).setBackgroundColor(Color.GRAY)
        uut.setBackgroundColor(ThemeColour(Colour(Color.GRAY)))
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
    fun getTitle_returnCurrentTextInTitleTextView() {
        assertThat(uut.getTitle()).isEmpty()
        uut.setTitle("TiTle")
        assertThat(uut.getTitle()).isEqualTo("TiTle")
    }

    @Test
    fun clearCurrentTitle_shouldCleatTextAndRemoveComponent() {
        uut.setTitle("Title")
        assertThat(uut.getTitle()).isEqualTo("Title")
        uut.clearTitle()
        assertThat(uut.getTitle()).isNullOrEmpty()

        val component = View(activity)
        uut.setComponent(component)
        assertThat(uut.getTitleComponent()).isEqualTo(component)

        uut.clearTitle()
        assertThat(uut.getTitleComponent()).isNotEqualTo(component)

    }

    private fun getTitleSubtitleView() = (uut.getTitleComponent() as TitleSubTitleLayout)
}