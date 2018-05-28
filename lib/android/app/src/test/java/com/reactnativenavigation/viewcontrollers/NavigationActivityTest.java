package com.reactnativenavigation.viewcontrollers;

import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.TestActivity;

import org.junit.Test;
import org.robolectric.android.controller.ActivityController;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NavigationActivityTest extends BaseTest {
    private ActivityController<? extends NavigationActivity> controller;
    private NavigationActivity uut;

    @Override
    public void beforeEach() {
        controller = newActivityController(TestActivity.class);
        uut = controller.get();
    }

    @Test
    public void onCreate_setSystemUiVisibility() {
        controller.setup();
        assertThat(uut
                .getNavigator()
                .getView()
                .getSystemUiVisibility()).isEqualTo(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
