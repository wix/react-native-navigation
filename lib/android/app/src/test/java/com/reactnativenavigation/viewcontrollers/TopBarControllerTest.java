package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarControllerTest extends BaseTest {

    private TopBarController uut;

    @Override
    public void beforeEach() {
        uut = new TopBarController();
    }

    @Test
    public void clear() {
        final TitleBar[] titleBar = new TitleBar[1];
        uut = new TopBarController() {
            @NonNull
            @Override
            protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                return new TopBar(context, stackLayout) {
                    @Override
                    protected TitleBar createTitleBar(Context context) {
                        titleBar[0] = spy(super.createTitleBar(context));
                        return titleBar[0];
                    }
                };
            }
        };
        Activity activity = newActivity();
        uut.createView(activity, Mockito.mock(StackLayout.class));
        uut.clear();
        verify(titleBar[0], times(1)).clear();
    }

    @Test
    public void destroy() {
        uut.createView(newActivity(), mock(StackLayout.class));
        uut.clear();
    }

    @Test
    public void destroy_canBeCalledBeforeViewIsCreated() {
        uut.clear();
    }
}
