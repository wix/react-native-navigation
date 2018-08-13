package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.res.Resources;
import android.view.ViewGroup.LayoutParams;
import android.util.TypedValue;
import android.view.Gravity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.SideMenuOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.presentation.OptionsPresenter;

import org.junit.Assert;
import org.junit.Test;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class SideMenuControllerTest extends BaseTest {
    private SideMenuController uut;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        OptionsPresenter presenter = new OptionsPresenter(activity, new Options());
        uut = new SideMenuController(activity, childRegistry, "sideMenu", new Options(), presenter);
    }

    @Test
    public void mergeOptions_openLeftSideMenu() {
        uut.setLeftController(new SimpleComponentViewController(activity, childRegistry, "left", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isTrue();
    }

    @Test
    public void mergeOptions_openRightSideMenu() {
        uut.setRightController(new SimpleComponentViewController(activity, childRegistry, "right", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isTrue();
    }

    @Test
    public void mergeOptions_optionsAreClearedAfterMerge() {
        Options initialOptions = uut.options;
        Options options = new Options();
        uut.mergeOptions(options);
        assertThat(uut.options.sideMenuRootOptions).isNotEqualTo(initialOptions.sideMenuRootOptions);
    }

    @Test
    public void setLeftController_matchesParentByDefault() {
        SideMenuOptions options = new SideMenuOptions();
        assertThat(options.width.hasValue()).isFalse();
        assertThat(options.height.hasValue()).isFalse();
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(MATCH_PARENT);
        assertThat(params.height).isEqualTo(MATCH_PARENT);
    }
    @Test
    public void setLeftController_setHeightAndWidthWithOptions() {
        SideMenuOptions options = new SideMenuOptions();
        options.height = new Number(100);
        options.width = new Number(200);
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, Resources.getSystem().getDisplayMetrics());
        int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, Resources.getSystem().getDisplayMetrics());

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(widthInDp);
        assertThat(params.height).isEqualTo(heightInDp);
    }
    @Test
    public void setRightController_matchesParentByDefault() {
        SideMenuOptions options = new SideMenuOptions();
        assertThat(options.width.hasValue()).isFalse();
        assertThat(options.height.hasValue()).isFalse();
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "right", new Options());
        uut.setRightController(componentViewController);

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(MATCH_PARENT);
        assertThat(params.height).isEqualTo(MATCH_PARENT);
    }
    @Test
    public void setRightController_setHeightAndWidthWithOptions() {
        SideMenuOptions options = new SideMenuOptions();
        options.height = new Number(100);
        options.width = new Number(200);
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, Resources.getSystem().getDisplayMetrics());
        int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, Resources.getSystem().getDisplayMetrics());

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(widthInDp);
        assertThat(params.height).isEqualTo(heightInDp);
    }
}
