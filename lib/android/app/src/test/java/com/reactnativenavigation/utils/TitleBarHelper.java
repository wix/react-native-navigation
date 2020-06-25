package com.reactnativenavigation.utils;

import android.app.Activity;

import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;

public class TitleBarHelper {
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

    public static Component titleComponent(String componentId) {
        Component component = new Component();
        component.componentId = new Text(componentId);
        component.name = new Text(componentId);
        return component;
    }

    public static Button iconButton(String id, String icon) {
        Button button = new Button();
        button.id = "someButton";
        button.icon = new Text(icon);
        return button;
    }


    public static TitleBarButtonController createButtonController(Activity activity, Button button) {
        return new TitleBarButtonController(activity,
                new ButtonPresenter(button, new IconResolver(activity, ImageLoaderMock.mock())),
                button,
                new TitleBarButtonCreatorMock(),
                buttonId -> {}
        );
    }
}
