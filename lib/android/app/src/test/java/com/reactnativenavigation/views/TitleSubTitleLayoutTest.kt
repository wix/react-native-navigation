package com.reactnativenavigation.views

import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.reactnativenavigation.BaseTest
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.AssertionsForInterfaceTypes.*
import org.junit.Test

private const val UUT_WIDTH = 1000
private const val UUT_HEIGHT = 100


class TitleSubTitleLayoutTest : BaseTest() {
    private val testId = "mock-testId"

    lateinit var uut: TitleSubTitleLayout
    private lateinit var activity: Activity

    override fun beforeEach() {
        super.beforeEach()
        setup()
    }

    private fun setup() {
        activity = newActivity()

        uut = TitleSubTitleLayout(activity)

        activity.setContentView(FrameLayout(activity).apply {
            addView(uut, ViewGroup.LayoutParams(UUT_WIDTH, UUT_HEIGHT))
        })
        idleMainLooper()
    }

    @Test
    fun `should set Test ID canonically`(){
        uut.setTestId(testId)
        assertThat(uut.getTitleTxtView().tag).isEqualTo("$testId.title")
        assertThat(uut.getSubTitleTxtView().tag).isEqualTo("$testId.subtitle")
    }

    @Test
    fun `should clear test ID`() {
        uut.setTestId(testId)
        uut.setTestId("")
        assertThat(uut.getTitleTxtView().tag).isNull()
        assertThat(uut.getSubTitleTxtView().tag).isNull()
    }
}
