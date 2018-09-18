package com.reactnativenavigation;

import android.app.Activity;
import android.content.Context;

import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.presentation.StackOptionsPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
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
                    protected TopBar createTopBar(Context context, TopBarBackgroundViewController topBarBackgroundViewController, StackLayout stackLayout) {
                        TopBar topBar = super.createTopBar(context, topBarBackgroundViewController, stackLayout);
                        topBar.layout(0, 0, 1000, 100);
                        return topBar;
                    }
                })
                .setStackPresenter(new StackOptionsPresenter(activity, new TitleBarReactViewCreatorMock(), new TopBarButtonCreatorMock(), new ImageLoader(), new Options())                )
                .setInitialOptions(new Options());
    }

    public static void hideBackButton(ViewController viewController) {
        viewController.options.topBar.buttons.back.visible = new Bool(false);
    }
}
