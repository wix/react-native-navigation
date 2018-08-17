package com.reactnativenavigation;

import android.app.Activity;
import android.content.Context;

import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ReactViewCreator;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.stack.StackControllerBuilder;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

public class TestUtils {
    public static StackControllerBuilder newStackController(Activity activity) {
        return new StackControllerBuilder(activity)
                .setId("stack")
                .setChildRegistry(new ChildControllersRegistry())
                .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                .setTopBarController(new TopBarController() {
                    @Override
                    protected TopBar createTopBar(Context context, ReactViewCreator buttonCreator, TopBarBackgroundViewController topBarBackgroundViewController, TopBarButtonController.OnClickListener topBarButtonClickListener, StackLayout stackLayout, ImageLoader imageLoader) {
                        TopBar topBar = super.createTopBar(context, buttonCreator, topBarBackgroundViewController, topBarButtonClickListener, stackLayout, imageLoader);
                        topBar.layout(0, 0, 1000, 100);
                        return topBar;
                    }
                })
                .setStackPresenter(new StackOptionsPresenter(activity, new TitleBarReactViewCreatorMock(), new Options()))
                .setInitialOptions(new Options());
    }
}
