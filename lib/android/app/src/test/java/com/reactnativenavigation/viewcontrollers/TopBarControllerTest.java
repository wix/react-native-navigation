package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.Context;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarControllerTest extends BaseTest {

    private TopBarController uut;
    private TitleBar titleBar;
    private TopBar topBar;
    private Activity activity;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = new TopBarController() {
            @Override
            protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                topBar = spy(new TopBar(context, stackLayout) {
                    @Override
                    protected TitleBar createTitleBar(Context context) {
                        titleBar = spy(super.createTitleBar(context));
                        return titleBar;
                    }
                });
                return topBar;
            }
        };
    }

    @Test
    public void createView_setElevationToCancelDefaultElevationAnimationWhichMightConflictWithElevationValueFromDefaultOptions() {
        uut.createView(activity, Mockito.mock(StackLayout.class));
        verify(topBar).setElevation(0);
    }

    @Test
    public void clear() {
        uut.createView(activity, Mockito.mock(StackLayout.class));
        uut.clear();
        verify(titleBar, times(1)).clear();
    }
}
