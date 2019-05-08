package com.reactnativenavigation;

import android.app.Activity;
import android.content.Context;

import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.presentation.RenderChecker;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.stack.StackControllerBuilder;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.topbar.TopBar;

public class TestUtils {
    public static StackControllerBuilder newStackController(Activity activity) {
        return new StackControllerBuilder(activity)
                .setId("stack")
                .setChildRegistry(new ChildControllersRegistry())
                .setTopBarController(new TopBarController() {
                    @Override
                    protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                        TopBar topBar = super.createTopBar(context, stackLayout);
                        topBar.layout(0, 0, 1000, UiUtils.getTopBarHeight(context));
                        return topBar;
                    }
                })
                .setStackPresenter(new StackPresenter(activity, new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), new TopBarButtonCreatorMock(), new ImageLoader(), new RenderChecker(), new Options()))
                .setInitialOptions(new Options());
    }

    public static void hideBackButton(ViewController viewController) {
        viewController.options.topBar.buttons.back.visible = new Bool(false);
    }
}
