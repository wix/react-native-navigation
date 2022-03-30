package com.reactnativenavigation;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.utils.RenderChecker;
import com.reactnativenavigation.viewcontrollers.stack.StackPresenter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.stack.StackControllerBuilder;
import com.reactnativenavigation.viewcontrollers.stack.topbar.TopBarController;
import com.reactnativenavigation.views.stack.StackLayout;
import com.reactnativenavigation.views.stack.topbar.TopBar;

import org.mockito.Mockito;

public class TestUtils {
    public static StackControllerBuilder newStackController(Activity activity) {
        TopBarController topBarController = new TopBarController() {
            @Override
            protected TopBar createTopBar(@NonNull Context context, @NonNull StackLayout stackLayout) {
                TopBar topBar = super.createTopBar(context, stackLayout);
                topBar.layout(0, 0, 1000, UiUtils.getTopBarHeight(context));
                return topBar;
            }
        };
        return new StackControllerBuilder(activity, Mockito.mock(EventEmitter.class))
                .setId("stack" + CompatUtils.generateViewId())
                .setChildRegistry(new ChildControllersRegistry())
                .setTopBarController(topBarController)
                .setStackPresenter(new StackPresenter(activity, new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), new TitleBarButtonCreatorMock(), new IconResolver(activity, new ImageLoader()), new TypefaceLoaderMock(), new RenderChecker(), new Options()))
                .setInitialOptions(new Options());
    }

    public static void hideBackButton(ViewController<?> viewController) {
        viewController.options.topBar.buttons.back.visible = new Bool(false);
    }

    public static <T extends View> T spyOn(T child) {
        ViewGroup parent = (ViewGroup) child.getParent();
        int indexOf = parent.indexOfChild(child);
        parent.removeView(child);
        T spy = Mockito.spy(child);
        parent.addView(spy, indexOf);
        return spy;
    }
}
