package com.reactnativenavigation.views

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.reactnativenavigation.TestActivity
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleSubTitleLayout
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val UUT_WIDTH = 1000
private const val UUT_HEIGHT = 100

@RunWith(AndroidJUnit4::class)
class TitleSubTitleLayoutTest {
    private val testId = "mock-testId"

    private lateinit var uut: TitleSubTitleLayout

    @get:Rule
    val rule = ActivityScenarioRule(TestActivity::class.java)

    @Before
    fun setup() {
        rule.scenario.onActivity { activity ->
            uut = TitleSubTitleLayout(activity)
            activity.setContentView(FrameLayout(activity).apply {
                addView(uut, ViewGroup.LayoutParams(UUT_WIDTH, UUT_HEIGHT))
            })
        }
    }

    @Test
    fun shouldSetTestIdCanonically() {
        uut.setTestId(testId)
        assertThat(uut.getTitleTxtView().tag).isEqualTo("$testId.title")
        assertThat(uut.getSubTitleTxtView().tag).isEqualTo("$testId.subtitle")
    }

    @Test
    fun shouldClearTestId() {
        uut.setTestId(testId)
        uut.setTestId("")
        assertThat(uut.getTitleTxtView().tag).isNull()
        assertThat(uut.getSubTitleTxtView().tag).isNull()
    }
} 