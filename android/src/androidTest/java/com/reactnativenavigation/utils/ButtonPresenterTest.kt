package com.reactnativenavigation.utils

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.ActionMenuView
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reactnativenavigation.BaseAndroidTest
import com.reactnativenavigation.TestActivity
import com.reactnativenavigation.fakes.IconResolverFake
import com.reactnativenavigation.mocks.ImageLoaderMock.mock
import com.reactnativenavigation.options.ButtonOptions
import com.reactnativenavigation.options.IconBackgroundOptions
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.options.params.Colour
import com.reactnativenavigation.options.params.Number
import com.reactnativenavigation.options.params.Text
import com.reactnativenavigation.options.params.ThemeColour
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar
import com.reactnativenavigation.views.stack.topbar.titlebar.IconBackgroundDrawable
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator
import org.assertj.core.api.Java6Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.Objects

@RunWith(AndroidJUnit4::class)
class ButtonPresenterTest : BaseAndroidTest() {
    private lateinit var titleBar: ButtonBar
    private lateinit var uut: ButtonPresenter
    private lateinit var buttonController: ButtonController
    private lateinit var button: ButtonOptions
    private lateinit var activity: Activity

    @get:Rule
    val rule = ActivityScenarioRule(TestActivity::class.java)

    @Before
    fun beforeEach() {
        rule.scenario.onActivity { activity: TestActivity ->
            this.activity =
                activity
            titleBar = ButtonBar(activity)
            activity.setContentView(titleBar)
            button = createButton()

            val imageLoaderMock =
                mock()
            initUUt(imageLoaderMock)
        }
    }

    private fun initUUt(imageLoaderMock: ImageLoader) {
        val iconResolver = IconResolverFake(
            activity, imageLoaderMock
        )
        uut = ButtonPresenter(activity, button, iconResolver)
        buttonController = ButtonController(
            activity,
            uut,
            button,
            Mockito.mock(TitleBarButtonCreator::class.java),
            Mockito.mock(
                ButtonController.OnClickListener::class.java
            )
        )
    }

    @Test
    fun applyOptions_buttonIsAddedToMenu() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            addButtonAndApplyOptions()
            Java6Assertions.assertThat(findButtonView().text.toString())
                .isEqualTo(BTN_TEXT)
        }
    }

    @Test
    fun applyOptions_appliesColorOnButtonTextView() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            addButtonAndApplyOptions()
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor).isEqualTo(Color.RED)
    }

    @Test
    fun applyOptions_appliesColorOnButtonTextViewOnDarkMode() {
        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as Application
        application.resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_NO
        button.color = ThemeColour(Colour(Color.RED), Colour(Color.BLACK))
        var menuItem: MenuItem? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            menuItem = addButtonAndApplyOptions()
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor).isEqualTo(Color.RED)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            application.resources.configuration.uiMode = Configuration.UI_MODE_NIGHT_YES
            uut.applyOptions(titleBar, menuItem!!) { buttonController.view }
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor).isEqualTo(Color.BLACK)
    }

    @Test
    fun apply_disabledColor() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            button.enabled = Bool(false)
            addButtonAndApplyOptions()
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor)
            .isEqualTo(ButtonPresenter.DISABLED_COLOR)
    }

    @Test
    fun applyColor_shouldChangeColor() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val menuItem = addMenuButton()
            uut.applyOptions(titleBar, menuItem) { buttonController.view }
            val color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            uut.applyColor(titleBar, menuItem, color)
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor).isEqualTo(Color.RED)
    }

    @Test
    fun applyBackgroundColor_shouldChangeBackgroundColor() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockD = Mockito.mock(
                IconBackgroundDrawable::class.java
            )
            initUUt(mock(mockD))
            button.enabled = Bool(true)
            button.icon = Text("icon")
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            val iconBackground = IconBackgroundOptions()
            iconBackground.color = ThemeColour(
                Colour(Color.GREEN),
                Colour(Color.GREEN)
            )
            button.iconBackground = iconBackground
            val menuItem = Mockito.spy(addMenuButton())
            uut.applyOptions(titleBar, menuItem) { buttonController.view }

            Java6Assertions.assertThat((menuItem.icon as IconBackgroundDrawable).backgroundColor)
                .isEqualTo(Color.GREEN)

            uut.applyBackgroundColor(
                titleBar, menuItem,
                ThemeColour(
                    Colour(Color.BLACK),
                    Colour(Color.BLACK)
                )
            )
            Java6Assertions.assertThat((menuItem.icon as IconBackgroundDrawable).backgroundColor)
                .isEqualTo(Color.BLACK)
        }
    }

    @Test
    fun applyOptions_shouldChangeIconColorTint() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockD = Mockito.mock(
                IconBackgroundDrawable::class.java
            )
            initUUt(mock(mockD))
            button.enabled = Bool(true)
            button.icon = Text("icon")
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            val menuItem = Mockito.spy(addMenuButton())
            uut.applyOptions(titleBar, menuItem) { buttonController.view }

            val icon = menuItem.icon
            Java6Assertions.assertThat(
                icon
            ).isNotNull()
            Mockito.verify(icon)?.colorFilter =
                PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
        }
    }

    @Test
    fun applyOptions_shouldChangeIconDisabledColorTint() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockD = Mockito.mock(
                IconBackgroundDrawable::class.java
            )
            initUUt(mock(mockD))
            button.enabled = Bool(false)
            button.icon = Text("icon")
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            button.disabledColor = ThemeColour(
                Colour(Color.YELLOW),
                Colour(Color.YELLOW)
            )
            val menuItem = Mockito.spy(addMenuButton())
            uut.applyOptions(titleBar, menuItem) { buttonController.view }

            val icon = menuItem.icon
            Java6Assertions.assertThat(
                icon
            ).isNotNull()
            Mockito.verify(icon)?.colorFilter =
                PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
        }
    }

    @Test
    fun applyOptions_shouldChangeIconColorBackground() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockD = Mockito.mock(
                IconBackgroundDrawable::class.java
            )
            initUUt(mock(mockD))
            button.enabled = Bool(true)
            button.icon = Text("icon")
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            val iconBackground = IconBackgroundOptions()
            iconBackground.color = ThemeColour(
                Colour(Color.GREEN),
                Colour(Color.GREEN)
            )
            button.iconBackground = iconBackground
            val menuItem = Mockito.spy(addMenuButton())
            uut.applyOptions(titleBar, menuItem) { buttonController.view }

            val icon = menuItem.icon
            Java6Assertions.assertThat(
                icon
            ).isNotNull()
            Java6Assertions.assertThat(
                icon
            ).isInstanceOf(
                IconBackgroundDrawable::class.java
            )
            val modifed = icon as IconBackgroundDrawable?
            Mockito.verify(modifed!!.getWrappedDrawable()).colorFilter =
                PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
            Java6Assertions.assertThat(modifed.backgroundColor)
                .isEqualTo(Color.GREEN)
        }
    }

    @Test
    fun applyOptions_shouldChangeIconDisabledColorBackground() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val mockD = Mockito.mock(
                IconBackgroundDrawable::class.java
            )
            initUUt(mock(mockD))
            button.enabled = Bool(false)
            button.icon = Text("icon")
            button.color =
                ThemeColour(Colour(Color.RED), Colour(Color.RED))
            button.disabledColor = ThemeColour(
                Colour(Color.YELLOW),
                Colour(Color.YELLOW)
            )
            val iconBackground = IconBackgroundOptions()
            iconBackground.color = ThemeColour(
                Colour(Color.GREEN),
                Colour(Color.GREEN)
            )
            iconBackground.disabledColor = ThemeColour(
                Colour(Color.CYAN),
                Colour(Color.CYAN)
            )
            button.iconBackground = iconBackground
            val menuItem = Mockito.spy(addMenuButton())
            uut.applyOptions(titleBar, menuItem) { buttonController.view }

            val icon = menuItem.icon
            Java6Assertions.assertThat(
                icon
            ).isNotNull()
            Java6Assertions.assertThat(
                icon
            ).isInstanceOf(
                IconBackgroundDrawable::class.java
            )
            val modifed = icon as IconBackgroundDrawable?
            Mockito.verify(modifed!!.getWrappedDrawable()).colorFilter =
                PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN)
            Java6Assertions.assertThat(modifed.backgroundColor)
                .isEqualTo(Color.CYAN)
        }
    }

    @Test
    fun applyColor_shouldChangeDisabledColor() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            button.enabled = Bool(false)
            val menuItem = addMenuButton()
            uut.applyOptions(titleBar, menuItem) { buttonController.view }
            val disabledColor = ThemeColour(
                Colour(Color.BLUE),
                Colour(Color.BLUE)
            )
            uut.applyDisabledColor(titleBar, menuItem, disabledColor)
        }
        Espresso.onIdle()
        Java6Assertions.assertThat(findButtonView().currentTextColor).isEqualTo(Color.BLUE)
    }

    @Test
    fun apply_allCaps() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            button.allCaps = Bool(false)
            addButtonAndApplyOptions()
            Java6Assertions.assertThat(findButtonView().isAllCaps)
                .isEqualTo(false)
        }
    }

    private fun addButtonAndApplyOptions(): MenuItem {
        val menuItem = addMenuButton()
        uut.applyOptions(titleBar, menuItem) { buttonController.view }
        return menuItem
    }

    private fun addMenuButton(): MenuItem {
        return titleBar.addButton(
            Menu.NONE,
            1,
            0,
            SpannableString.valueOf(button.text["text"])
        ) ?: throw IllegalStateException("MenuItem should not be null")
    }

    private fun findButtonView(): TextView {
        return ViewUtils.findChildrenByClass(
            Objects.requireNonNull(
                ViewUtils.findChildByClass(
                    titleBar,
                    ActionMenuView::class.java
                )
            ),
            TextView::class.java
        ) { child: Any? -> true }[0] as TextView
    }

    private fun createButton(): ButtonOptions {
        val b = ButtonOptions()
        b.id = "btn1"
        b.text = Text(BTN_TEXT)
        b.showAsAction = Number(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return b
    }

    companion object {
        private const val BTN_TEXT = "button1"
    }
}