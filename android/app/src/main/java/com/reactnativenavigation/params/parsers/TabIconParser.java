package com.reactnativenavigation.params.parsers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.react.ImageLoader;
import com.reactnativenavigation.react.ResourceDrawableIdHelper;

class TabIconParser extends Parser {

    private Bundle params;

    TabIconParser(Bundle params) {
        this.params = params;
    }

    public Drawable parse() {
        Drawable tabIcon = null;
        if (hasKey(params, "icon")) {
            String uri = params.getString("icon");
            if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("file://")) {
                tabIcon = ImageLoader.loadImage(uri);
            } else {
                tabIcon = ResourceDrawableIdHelper.instance.getResourceDrawable(NavigationApplication.instance, uri);
            }
        }
        return tabIcon;
    }
}
