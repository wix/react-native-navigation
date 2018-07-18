package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarReactViewCreator;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;
import org.mockito.Mockito;

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
            protected TopBar createTopBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator titleBarReactViewCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout, ImageLoader imageLoader) {
                return new TopBar(context, buttonCreator, titleBarReactViewCreator, topBarBackgroundViewController, topBarButtonClickListener, stackLayout, imageLoader) {
                    @Override
                    protected TitleBar createTitleBar(Context context, ReactViewCreator buttonCreator, TitleBarReactViewCreator reactViewCreator, TopBarButtonController.OnClickListener onClickListener, ImageLoader imageLoader) {
                        titleBar[0] = spy(super.createTitleBar(context, buttonCreator, reactViewCreator, onClickListener, imageLoader));
                        return titleBar[0];
                    }
                };
            }
        };
        Activity activity = newActivity();
        uut.createView(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()), buttonId -> { }, Mockito.mock(StackLayout.class));
        uut.clear();
        verify(titleBar[0], times(1)).clear();
    }
}
