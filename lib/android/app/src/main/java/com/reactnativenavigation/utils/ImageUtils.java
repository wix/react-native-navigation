package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.URL;

public class ImageUtils {
    @NonNull
    public static Drawable tryLoadIcon(Context currentContext, String uri) {

        StrictMode.ThreadPolicy threadPolicy = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        Drawable drawable = null;
        try {
            drawable = loadIcon(currentContext, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StrictMode.setThreadPolicy(threadPolicy);

        return drawable;
    }

    private static Drawable loadIcon(Context context, String uri) throws IOException {
        URL url = new URL(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());

        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
