package com.reactnativenavigation.utils;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;

public class OptionHelper {
    public static Options createBottomTabOptions() {
        Options options = new Options();
        options.bottomTabOptions.title = new Text("Tab");
        options.bottomTabOptions.icon = new Text("http://127.0.0.1/icon.png");
        return options;
    }
}
