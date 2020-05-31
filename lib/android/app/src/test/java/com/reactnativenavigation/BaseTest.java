package com.reactnativenavigation;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28, application = TestApplication.class)
public abstract class BaseTest {
    @Before
    public void beforeEach() {
        //
    }

    @After
    public void afterEach() {
        //
    }

    public Activity newActivity() {
        return Robolectric.setupActivity(AppCompatActivity.class);
    }

    public <T extends AppCompatActivity> ActivityController<T> newActivityController(Class<T> clazz) {
        return Robolectric.buildActivity(clazz);
    }

    public void assertIsChild(ViewGroup parent, ViewController... children) {
        forEach(Arrays.asList(children),c -> assertIsChild(parent, c.getView()));
    }

    public void assertIsChild(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(ViewUtils.isChildOf(parent, child)).isTrue();
    }

    public void assertNotChildOf(ViewGroup parent, ViewController... children) {
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

    protected void disablePushAnimation(ViewController... controllers) {
        for (ViewController controller : controllers) {
            controller.options.animations.push.enabled = new Bool(false);
        }
    }

    protected void disablePopAnimation(ViewController... controllers) {
        for (ViewController controller : controllers) {
            controller.options.animations.pop.enabled = new Bool(false);
        }
    }

    protected void disableModalAnimations(ViewController... modals) {
        disableShowModalAnimation(modals);
        disableDismissModalAnimation(modals);
    }

    protected void disableShowModalAnimation(ViewController... modals) {
        for (ViewController modal : modals) {
            modal.options.animations.showModal.enabled = new Bool(false);
        }
    }

    protected void disableDismissModalAnimation(ViewController... modals) {
        for (ViewController modal : modals) {
            modal.options.animations.dismissModal.enabled = new Bool(false);
        }
    }

    protected void dispatchPreDraw(View view) {
        view.getViewTreeObserver().dispatchOnPreDraw();
    }

    protected void dispatchOnGlobalLayout(View view) {
        view.getViewTreeObserver().dispatchOnGlobalLayout();
    }

    protected void addToParent(Context context, ViewController... controllers) {
        for (ViewController controller : controllers) {
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
}
