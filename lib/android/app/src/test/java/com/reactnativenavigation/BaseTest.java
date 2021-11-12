package com.reactnativenavigation;

import static com.reactnativenavigation.utils.CollectionUtils.forEach;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.Functions;
import com.reactnativenavigation.utils.SystemUiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = TestApplication.class)
public abstract class BaseTest {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ShadowLooper shadowMainLooper = Shadows.shadowOf(Looper.getMainLooper());
    protected Configuration mockConfiguration;

    @Before
    public void beforeEach() {
        NavigationApplication.instance = Mockito.mock(NavigationApplication.class);
        mockConfiguration = Mockito.mock(Configuration.class);
        Resources res = mock(Resources.class);
        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO;
        when(res.getConfiguration()).thenReturn(mockConfiguration);
        when(NavigationApplication.instance.getResources()).thenReturn(res);
        when(res.getColor(ArgumentMatchers.anyInt())).thenReturn(0x00000);
        when(res.getColor(ArgumentMatchers.anyInt(),any())).thenReturn(0x00000);
    }


    public void mockSystemUiUtils(int statusBarHeight, int statusBarHeightDp, Functions.Func1<MockedStatic<SystemUiUtils>> mockedBlock) {
        try (MockedStatic<SystemUiUtils> theMock = Mockito.mockStatic(SystemUiUtils.class)) {
            theMock.when(() -> {
                SystemUiUtils.getStatusBarHeight(any());
            }).thenReturn(statusBarHeight);
            theMock.when(() -> {
                SystemUiUtils.getStatusBarHeightDp(any());
            }).thenReturn(statusBarHeightDp);
            mockedBlock.run(theMock);
        }
    }

    @After
    @CallSuper
    public void afterEach() {
        idleMainLooper();
    }

    public Activity newActivity() {
        return Robolectric.setupActivity(AppCompatActivity.class);
    }

    public <T extends AppCompatActivity> ActivityController<T> newActivityController(Class<T> clazz) {
        return Robolectric.buildActivity(clazz);
    }

    public void assertIsChild(ViewGroup parent, ViewController<?>... children) {
        forEach(Arrays.asList(children), c -> assertIsChild(parent, c.getView()));
    }

    public void assertIsChild(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(ViewUtils.isChildOf(parent, child)).isTrue();
    }

    public void assertNotChildOf(ViewGroup parent, ViewController<?>... children) {
        forEach(Arrays.asList(children), c -> assertNotChildOf(parent, c.getView()));
    }

    public void assertNotChildOf(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(ViewUtils.isChildOf(parent, child)).isFalse();
    }

    public void assertMatchParent(View view) {
        assertThat(view.getLayoutParams().width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
        assertThat(view.getLayoutParams().height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    protected void disablePushAnimation(ViewController<?>... controllers) {
        for (ViewController<?> controller : controllers) {
            controller.options.animations.push.enabled = new Bool(false);
        }
    }

    protected void disablePopAnimation(ViewController<?>... controllers) {
        for (ViewController<?> controller : controllers) {
            controller.options.animations.pop.enabled = new Bool(false);
        }
    }

    protected void disableModalAnimations(ViewController<?>... modals) {
        disableShowModalAnimation(modals);
        disableDismissModalAnimation(modals);
    }

    protected void disableShowModalAnimation(ViewController<?>... modals) {
        for (ViewController<?> modal : modals) {
            modal.options.animations.showModal.toggle(new Bool(false));
        }
    }

    protected void disableDismissModalAnimation(ViewController<?>... modals) {
        for (ViewController<?> modal : modals) {
            modal.options.animations.dismissModal.toggle(new Bool(false));
        }
    }

    protected void dispatchPreDraw(View view) {
        view.getViewTreeObserver().dispatchOnPreDraw();
    }

    protected void dispatchOnGlobalLayout(View view) {
        view.getViewTreeObserver().dispatchOnGlobalLayout();
    }

    protected void addToParent(Context context, ViewController<?>... controllers) {
        for (ViewController<?> controller : controllers) {
            new CoordinatorLayout(context).addView(controller.getView());
        }
    }

    protected View mockView(Activity activity) {
        View mock = Mockito.mock(View.class);
        when(mock.getContext()).thenReturn(activity);
        return mock;
    }

    protected void assertVisible(View view) {
        assertThat(view.getVisibility()).isEqualTo(View.VISIBLE);
    }

    protected void assertGone(View view) {
        assertThat(view.getVisibility()).isEqualTo(View.GONE);
    }

    protected void post(Runnable runnable) {
        handler.post(runnable);
    }

    protected void idleMainLooper() {
        shadowMainLooper.idle();
    }
}
