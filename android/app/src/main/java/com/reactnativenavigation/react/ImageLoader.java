package com.reactnativenavigation.react;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;

import com.reactnativenavigation.NavigationApplication;

import java.io.IOException;
import java.net.URL;

import com.reactnativenavigation.NavigationApplication;

public class ImageLoader {
    private static final String FILE_SCHEME = "file";

    public static Drawable loadImage(String iconSource) {
        if (NavigationApplication.instance.isDebug()) {
            return JsDevImageLoader.loadIcon(iconSource);
        } else {
            Uri uri = Uri.parse(iconSource);
            if (isLocalFile(uri)) {
                return loadFile(uri);
            } else if (uri.toString().startsWith("http")
             || uri.toString().startsWith("https")) {
                return loadIcon(uri.toString());
            } else {
                return loadResource(iconSource);
            }
        }
    }

    private static boolean isLocalFile(Uri uri) {
        return FILE_SCHEME.equals(uri.getScheme());
    }

    private static Drawable loadFile(Uri uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
        return new BitmapDrawable(NavigationApplication.instance.getResources(), bitmap);
    }

    private static Drawable loadResource(String iconSource) {
        return ResourceDrawableIdHelper.instance.getResourceDrawable(NavigationApplication.instance, iconSource);
    }
    private static Drawable loadIcon(String iconSource) {
        try {
            StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

            Drawable drawable = tryLoadIcon(iconSource);

            StrictMode.setThreadPolicy(threadPolicy);
            return drawable;
        } catch (Exception e) {
            Log.e("loadTabIcon", "Unable to load icon: " + iconSource);
            return new BitmapDrawable();
        }
    }

    @NonNull
    private static Drawable tryLoadIcon(String iconSource) throws IOException {
        URL url = new URL(iconSource);
        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
        return new BitmapDrawable(NavigationApplication.instance.getResources(), bitmap);
    }
}
