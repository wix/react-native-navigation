package com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.modes

import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.reactnativenavigation.TestActivity
import com.reactnativenavigation.mocks.SimpleViewController
import com.reactnativenavigation.options.Options
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import com.reactnativenavigation.views.bottomtabs.BottomTabsBehaviour
import org.assertj.core.api.AssertionsForInterfaceTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.spy

@RunWith(AndroidJUnit4::class)
abstract class AttachModeTest {
    private val INITIAL_TAB = 1

    private lateinit var childRegistry: ChildControllersRegistry
    protected lateinit var parent: ViewGroup
    protected lateinit var tab1: ViewController<*>
    protected lateinit var tab2: ViewController<*>
    protected lateinit var tabs: List<ViewController<*>>
    protected lateinit var options: Options
    protected lateinit var presenter: BottomTabsPresenter
    protected lateinit var uut: AttachMode

    @get:Rule
    val rule = ActivityScenarioRule(TestActivity::class.java)

    @Before
    open fun setup() {
        rule.scenario.onActivity { activity ->
            childRegistry = ChildControllersRegistry()
            parent = CoordinatorLayout(activity)
            tabs = createTabs(activity)
        }
        options = Options()
        options.bottomTabsOptions.currentTabIndex = Number(INITIAL_TAB)
        presenter = Mockito.mock(BottomTabsPresenter::class.java)
    }

    @Test
    fun attach_layoutOptionsAreApplied() {
        uut.attach(tab1)
        val lp = tab1.getView().layoutParams as CoordinatorLayout.LayoutParams
        assertThat(lp).isNotNull
        assertThat(lp.behavior).isInstanceOf(BottomTabsBehaviour::class.java)
    }

    @Test
    fun attach_initialTabIsVisible() {
        uut.attach(initialTab())
        assertThat(initialTab().getView().visibility).isEqualTo(View.VISIBLE)
    }

    @Test
    fun attach_otherTabsAreInvisibleWhenAttached() {
        otherTabs().forEach { t -> uut.attach(t) }
        otherTabs().forEach { t -> assertThat(t.getView().visibility).isEqualTo(View.INVISIBLE) }
    }

    protected fun otherTabs(): Array<ViewController<*>> {
        return tabs.filter { t -> t != initialTab() }.toTypedArray()
    }

    protected fun initialTab(): ViewController<*> {
        return tabs[INITIAL_TAB]
    }

    private fun createTabs(activity: android.app.Activity): List<ViewController<*>> {
        tab1 = SimpleViewController(activity, childRegistry, "child1", Options())
        tab2 = spy(SimpleViewController(activity, childRegistry, "child2", Options()))
        val tab3 = SimpleViewController(activity, childRegistry, "child3", Options())
        return listOf(tab1, tab2, tab3)
    }

    protected fun assertIsChild(parent: ViewGroup, vararg children: ViewController<*>) {
        children.forEach { child ->
            assertThat(child.getView().parent).isEqualTo(parent)
        }
    }

    protected fun assertNotChildOf(parent: ViewGroup, vararg children: ViewController<*>) {
        children.forEach { child ->
            assertThat(child.getView().parent).isNotEqualTo(parent)
        }
    }
} 