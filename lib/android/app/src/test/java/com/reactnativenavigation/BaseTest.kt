package com.reactnativenavigation

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.facebook.react.internal.featureflags.ReactNativeFeatureFlags
import com.reactnativenavigation.options.params.Bool
import com.reactnativenavigation.utils.CollectionUtils
import com.reactnativenavigation.utils.Functions
import com.reactnativenavigation.utils.SystemUiUtils
import com.reactnativenavigation.utils.SystemUiUtils.getStatusBarHeight
import com.reactnativenavigation.utils.SystemUiUtils.getStatusBarHeightDp
import com.reactnativenavigation.utils.ViewUtils
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController
import org.assertj.core.api.Java6Assertions
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper
import java.util.Arrays

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], application = TestApplication::class)
abstract class BaseTest {
    private val handler = Handler(Looper.getMainLooper())
    private val shadowMainLooper: ShadowLooper = Shadows.shadowOf(Looper.getMainLooper())

    protected lateinit var mockConfiguration: Configuration

    private var mockReactNativeFeatureFlags: MockedStatic<ReactNativeFeatureFlags>? = null

    @Before
    open fun beforeEach() {
        mockReactNativeFeatureFlags = mockStatic(ReactNativeFeatureFlags::class.java)
        mockReactNativeFeatureFlags?.close()

        NavigationApplication.instance = Mockito.mock(NavigationApplication::class.java)
        mockConfiguration = Mockito.mock(Configuration::class.java)
        val res = Mockito.mock(Resources::class.java)
        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO
        Mockito.`when`(res.configuration).thenReturn(mockConfiguration)
        Mockito.`when`(NavigationApplication.instance.resources).thenReturn(res)
        Mockito.`when`(res.getColor(ArgumentMatchers.anyInt())).thenReturn(0x00000)
        Mockito.`when`(res.getColor(ArgumentMatchers.anyInt(), ArgumentMatchers.any()))
            .thenReturn(0x00000)

        RNNFeatureToggles.init()
    }


    fun mockSystemUiUtils(
        statusBarHeight: Int,
        statusBarHeightDp: Int,
        mockedBlock: Functions.Func1<MockedStatic<SystemUiUtils?>?>
    ) {
        Mockito.mockStatic(SystemUiUtils::class.java).use { theMock ->
            theMock.`when`<Any> {
                getStatusBarHeight(ArgumentMatchers.any())
            }.thenReturn(statusBarHeight)
            theMock.`when`<Any> {
                getStatusBarHeightDp(ArgumentMatchers.any())
            }.thenReturn(statusBarHeightDp)
            mockedBlock.run(theMock)
        }
    }

    @After
    @CallSuper
    fun afterEach() {
        idleMainLooper()
        mockReactNativeFeatureFlags?.close()
        RNNFeatureToggles.clear();
    }

    fun newActivity(): Activity {
        return Robolectric.setupActivity(AppCompatActivity::class.java)
    }

    fun <T : AppCompatActivity?> newActivityController(clazz: Class<T>): ActivityController<T> {
        return Robolectric.buildActivity(clazz)
    }

    fun assertIsChild(parent: ViewGroup?, vararg children: ViewController<*>?) {
        CollectionUtils.forEach(
            Arrays.asList(*children)
        ) { c: ViewController<*> -> assertIsChild(parent, c.view) }
    }

    fun assertIsChild(parent: ViewGroup?, child: View) {
        Java6Assertions.assertThat(parent).isNotNull()
        Java6Assertions.assertThat(child).isNotNull()
        Java6Assertions.assertThat(ViewUtils.isChildOf(parent, child)).isTrue()
    }

    fun assertNotChildOf(parent: ViewGroup?, vararg children: ViewController<*>?) {
        CollectionUtils.forEach(
            Arrays.asList(*children)
        ) { c: ViewController<*> -> assertNotChildOf(parent, c.view) }
    }

    fun assertNotChildOf(parent: ViewGroup?, child: View) {
        Java6Assertions.assertThat(parent).isNotNull()
        Java6Assertions.assertThat(child).isNotNull()
        Java6Assertions.assertThat(ViewUtils.isChildOf(parent, child)).isFalse()
    }

    fun assertMatchParent(view: View) {
        Java6Assertions.assertThat(view.layoutParams.width)
            .isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
        Java6Assertions.assertThat(view.layoutParams.height)
            .isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT)
    }

    protected fun disablePushAnimation(vararg controllers: ViewController<*>) {
        for (controller in controllers) {
            controller.options.animations.push.enabled = Bool(false)
        }
    }

    protected fun disablePopAnimation(vararg controllers: ViewController<*>) {
        for (controller in controllers) {
            controller.options.animations.pop.enabled = Bool(false)
        }
    }

    protected fun disableModalAnimations(vararg modals: ViewController<*>) {
        disableShowModalAnimation(*modals)
        disableDismissModalAnimation(*modals)
    }

    protected fun disableShowModalAnimation(vararg modals: ViewController<*>) {
        for (modal in modals) {
            modal.options.animations.showModal.toggle(Bool(false))
        }
    }

    protected fun disableDismissModalAnimation(vararg modals: ViewController<*>) {
        for (modal in modals) {
            modal.options.animations.dismissModal.toggle(Bool(false))
        }
    }

    protected fun dispatchPreDraw(view: View) {
        view.viewTreeObserver.dispatchOnPreDraw()
    }

    protected fun dispatchOnGlobalLayout(view: View) {
        view.viewTreeObserver.dispatchOnGlobalLayout()
    }

    protected fun addToParent(context: Context, vararg controllers: ViewController<*>) {
        for (controller in controllers) {
            CoordinatorLayout(context).addView(controller.view)
        }
    }

    protected fun mockView(activity: Activity): View {
        val mock = Mockito.mock(View::class.java)
        Mockito.`when`(mock.context).thenReturn(activity)
        return mock
    }

    protected fun assertVisible(view: View) {
        Java6Assertions.assertThat(view.visibility).isEqualTo(View.VISIBLE)
    }

    protected fun assertGone(view: View) {
        Java6Assertions.assertThat(view.visibility).isEqualTo(View.GONE)
    }

    protected fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    protected fun idleMainLooper() {
        shadowMainLooper.idle()
    }
}
