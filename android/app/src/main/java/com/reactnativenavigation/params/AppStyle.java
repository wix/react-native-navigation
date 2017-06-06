package com.reactnativenavigation.params;

import android.os.Bundle;

import com.reactnativenavigation.params.parsers.StyleParamsParser;

public class AppStyle {
    public static StyleParams appStyle;

    public static void setAppStyle(Bundle params) {
        appStyle = new StyleParamsParser(params.getBundle("appStyle")).parse();
    }
}
