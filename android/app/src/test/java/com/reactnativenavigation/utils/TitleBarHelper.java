package com.reactnativenavigation.utils;

import android.app.Activity;

import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;

public class TitleBarHelper {
    public static ButtonOptions textualButton(String text) {
        ButtonOptions button = new ButtonOptions();
        button.id = text + CompatUtils.generateViewId();
        button.text = new Text(text);
        return button;
    }

    public static ButtonOptions reactViewButton(String name) {
        ButtonOptions button = new ButtonOptions();
        button.id = name + CompatUtils.generateViewId();
        button.component = new ComponentOptions();
        button.component.name = new Text("com.example" + name + CompatUtils.generateViewId());
        button.component.componentId = new Text(name + CompatUtils.generateViewId());
        return button;
    }

    public static ComponentOptions titleComponent(String componentId) {
        ComponentOptions component = new ComponentOptions();
        component.componentId = new Text(componentId);
        component.name = new Text(componentId);
        return component;
    }

    public static ButtonOptions iconButton(String id, String icon) {
        ButtonOptions button = new ButtonOptions();
        button.id = "someButton";
        button.icon = new Text(icon);
        return button;
    }


    public static ButtonController createButtonController(Activity activity, ButtonOptions button) {
        return new ButtonController(activity,
                new ButtonPresenter(activity, button, new IconResolver(activity, ImageLoaderMock.mock())),
                button,
                new TitleBarButtonCreatorMock(),
                buttonId -> {}
        );
    }
}
