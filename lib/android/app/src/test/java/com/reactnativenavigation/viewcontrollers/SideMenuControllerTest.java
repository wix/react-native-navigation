package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.Gravity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SideMenuControllerTest extends BaseTest {
    private SideMenuController uut;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = new SideMenuController(activity, "sideMenu", new Options());
    }

    @Test
    public void mergeOptions_openLeftSideMenu() throws Exception {
        uut.setLeftController(new SimpleComponentViewController(activity, "left", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isTrue();
    }

    @Test
    public void mergeOptions_openRightSideMenu() throws Exception {
        uut.setRightController(new SimpleComponentViewController(activity, "right", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isTrue();
    }

    @Test
    public void mergeOptions_optionsAreClearedAfterMerge() throws Exception {
        Options initialOptions = uut.options;
        Options options = new Options();
        uut.mergeOptions(options);
        assertThat(uut.options.sideMenuRootOptions).isNotEqualTo(initialOptions.sideMenuRootOptions);
    }
}
