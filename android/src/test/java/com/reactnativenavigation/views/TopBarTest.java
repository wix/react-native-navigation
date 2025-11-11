package com.reactnativenavigation.views;

import android.app.Activity;
import android.view.ViewGroup;

import com.google.android.material.appbar.AppBarLayout;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ScrollEventListener;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.views.stack.StackLayout;
import com.reactnativenavigation.views.stack.topbar.TopBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleAndButtonsContainer;

import org.junit.Test;
import org.mockito.Mockito;
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

    @Test
    public void setTitleHeight_changesTitleBarHeight() {
        int width = 1080;
        int height = 150;
        uut.layout(0, 0, width, height);
        uut.setTitleHeight(height / 2);
        TitleAndButtonsContainer titleAndButtonsContainer = uut.getTitleAndButtonsContainer();
        assertThat(titleAndButtonsContainer.getLayoutParams().height).isEqualTo(UiUtils.dpToPx(activity, height / 2));
    }

    @Test
    public void setTopMargin_changesTitleBarTopMargin() {
        int width = 1080;
        int height = 150;
        uut.layout(0, 0, width, height);
        int topMargin = 10;
        uut.setTitleTopMargin(topMargin);
        TitleAndButtonsContainer titleAndButtonsContainer = uut.getTitleAndButtonsContainer();
        assertThat(((ViewGroup.MarginLayoutParams) titleAndButtonsContainer.getLayoutParams()).topMargin).isEqualTo(UiUtils.dpToPx(activity, topMargin));
    }

    @Test
    public void disableCollapse_scrollIsDisabled() {
        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) uut.getChildAt(0).getLayoutParams();
        uut.enableCollapse(Mockito.mock(ScrollEventListener.class));
        assertThat(lp.getScrollFlags()).isEqualTo(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL);

        uut.disableCollapse();
        assertThat(lp.getScrollFlags()).isZero();
    }
}
