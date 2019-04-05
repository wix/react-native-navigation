package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Config(qualifiers = "xxhdpi")
public class TopBarTest extends BaseTest {

    private TopBar uut;
    private TopBarAnimator animator;
    private Activity activity;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        activity = newActivity();
        StackLayout parent = new StackLayout(activity, new TopBarController(), null);
        uut = new TopBar(activity, parent);
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

    @Test
    public void setElevation_ignoreValuesNotSetByNavigation() {
        float initialElevation = uut.getElevation();
        uut.setElevation(1f);
        assertThat(uut.getElevation()).isEqualTo(initialElevation);

        uut.setElevation(Double.valueOf(2));
        assertThat(uut.getElevation()).isEqualTo(UiUtils.dpToPx(activity, 2));
    }
}
