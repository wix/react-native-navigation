package com.reactnativenavigation.views

import android.app.Activity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.options.Alignment
import com.reactnativenavigation.utils.UiUtils
import com.reactnativenavigation.views.stack.topbar.titlebar.DEFAULT_LEFT_MARGIN
import com.reactnativenavigation.views.stack.topbar.titlebar.MainToolBar
import org.assertj.core.api.Assertions
import org.junit.Test

class MainToolbarTest : BaseTest() {
    lateinit var uut: MainToolBar
    private lateinit var activity: Activity

    override fun beforeEach() {
        activity = newActivity()
        uut = MainToolBar(activity)
    }

    @Test
    fun `should Have Left Bar At Start, Dynamic Content Size`() {
        val layoutParams = uut.leftButtonsBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.startToStart).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
    }

    @Test
    fun `should Have Right Bar At End, Dynamic Content Size`() {
        val layoutParams = uut.rightButtonsBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.endToEnd).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
    }

    @Test
    fun `should Have Title subtitle Bar next to left buttons bar, before right buttons, Dynamic Content Size, default margin`() {
        val layoutParams = uut.titleBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.topToTop).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.bottomToBottom).isEqualTo(ConstraintSet.PARENT_ID)
        Assertions.assertThat(layoutParams.startToEnd).isEqualTo(uut.leftBarTitleBarBarrierID)
        Assertions.assertThat(layoutParams.endToStart).isEqualTo(uut.titleBarRightBarBarrierID)
        Assertions.assertThat(layoutParams.width).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.height).isEqualTo(ConstraintSet.WRAP_CONTENT)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0f)
    }

    @Test
    fun `should change alignment of the title bar, start with margin, center no margin`() {
        uut.setTitleBarAlignment(Alignment.Center)
        var layoutParams = uut.titleBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0.5f)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(0)

        uut.setTitleBarAlignment(Alignment.Fill)
        layoutParams = uut.titleBar.layoutParams as ConstraintLayout.LayoutParams
        Assertions.assertThat(layoutParams.horizontalBias).isEqualTo(0f)
        Assertions.assertThat(layoutParams.marginStart).isEqualTo(UiUtils.dpToPx(activity, DEFAULT_LEFT_MARGIN))

    }
}