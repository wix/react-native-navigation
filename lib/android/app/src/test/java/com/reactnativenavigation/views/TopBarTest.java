package com.reactnativenavigation.views;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;

@Config(qualifiers = "xxhdpi")
public class TopBarTest extends BaseTest {

    private TopBar uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        StackLayout parent = new StackLayout(activity, new TopBarController(), null);
        uut = new TopBar(activity);
        parent.addView(uut);
    }

    @Test
    public void title() {
        assertThat(uut.getTitle()).isEmpty();
        uut.setTitle("new title");
        assertThat(uut.getTitle()).isEqualTo("new title");
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
