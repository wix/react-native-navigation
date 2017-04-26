package com.reactnativenavigation.react;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.reactnativenavigation.NavigationApplication;

import java.lang.reflect.Constructor;

public class ImageLoader {
    private static final String FILE_SCHEME = "file";

    public static Drawable loadImage(String iconSource) {
        if (NavigationApplication.instance.isDebug()) {
            return JsDevImageLoader.loadIcon(iconSource);
        } else {
            Uri uri = Uri.parse(iconSource);
            if (isLocalFile(uri)) {
                return loadFile(uri);
            } else {
                return loadResource(iconSource);
            }
        }
    }

    /**
     * Loads Drawable class, and returns new instance created with Context constructor.
     *
     * @param className Full qualified name of class.
     * @return Newly created Drawable instance.
     */
    public static Drawable loadClass(String className) {
        Class clazz;
        try {
            clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(Context.class);
            return (Drawable) constructor.newInstance(NavigationApplication.instance);
        } catch (Exception e) {
            Log.e("ImageLoader", "error loading Drawable class '" + className + "'. " + e.getCause().getMessage(), e);
            return null;
        }
    }

    private static boolean isLocalFile(Uri uri) {
        return FILE_SCHEME.equals(uri.getScheme());
    }

    private static Drawable loadFile(Uri uri) {
        return new BitmapDrawable(NavigationApplication.instance.getResources(), uri.getPath());
    }

    private static Drawable loadResource(String iconSource) {
        return ResourceDrawableIdHelper.instance.getResourceDrawable(NavigationApplication.instance, iconSource);
    }
}
