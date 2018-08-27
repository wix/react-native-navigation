package com.reactnativenavigation.utils;

import android.app.Activity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;

import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.viewcontrollers.button.NavigationIconResolver;
import com.reactnativenavigation.views.titlebar.TitleBar;

public class TitleBarHelper {
    public static ActionMenuItemView getRightButton(Toolbar toolbar, int index) {
        return (ActionMenuItemView) ViewUtils.findChildrenByClassRecursive(toolbar, ActionMenuItemView.class).get(toolbar.getMenu().size() - index - 1);
    }

    public static Button textualButton(String text) {
        Button button = new Button();
        button.id = text + CompatUtils.generateViewId();
        button.text = new Text(text);
        return button;
    }

    public static Button reactViewButton(String name) {
        Button button = new Button();
        button.id = name + CompatUtils.generateViewId();
        button.component = new Component();
        button.component.name = new Text("com.example" + name + CompatUtils.generateViewId());
        button.component.componentId = new Text(name + CompatUtils.generateViewId());
        return button;
    }

    public static Button iconButton(String id, String icon) {
        Button button = new Button();
        button.id = "someButton";
        button.icon = new Text(icon);
        return button;
    }


    public static TitleBarButtonController createButtonController(Activity activity, TitleBar titleBar, Button button) {
        return new TitleBarButtonController(activity,
                new NavigationIconResolver(activity, ImageLoaderMock.mock()),
                ImageLoaderMock.mock(),
                new ButtonOptionsPresenter(titleBar, button),
                button,
                new TopBarButtonCreatorMock(),
                buttonId -> {}
        );
    }
}
