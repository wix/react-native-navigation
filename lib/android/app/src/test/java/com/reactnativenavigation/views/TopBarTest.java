package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarTest extends BaseTest {

    private TopBar uut;
    private TopBarAnimator animator;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        TopBarBackgroundViewController topBarBackgroundViewController = new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock());
        StackLayout parent = new StackLayout(activity, topBarBackgroundViewController, new TopBarController(), null);
        uut = new TopBar(activity, topBarBackgroundViewController, parent);
        animator = spy(new TopBarAnimator(uut));
        uut.setAnimator(animator);
        parent.addView(uut);
    }

    @Test
    public void title() {
        assertThat(uut.getTitle()).isEmpty();
        uut.setTitle("new title");
        assertThat(uut.getTitle()).isEqualTo("new title");
    }

    @Test
    public void hide_animate() {
        AnimationOptions options = new AnimationOptions();
        uut.hideAnimate(options);
        verify(animator, times(1)).hide(eq(options), any());
    }

    @Test
    public void show_animate() {
        AnimationOptions options = new AnimationOptions();
        uut.hide();
        uut.showAnimate(options);
        verify(animator, times(1)).show(options);
    }
}
