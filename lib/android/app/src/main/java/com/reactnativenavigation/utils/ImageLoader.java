package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.react.views.imagehelper.ResourceDrawableIdHelper;
import com.reactnativenavigation.BuildConfig;
import com.reactnativenavigation.NavigationApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImageLoader {

    public interface ImagesLoadingListener {
        void onComplete(@NonNull List<Drawable> drawable);

        void onComplete(@NonNull Drawable drawable);

        void onError(Throwable error);
    }

    private static final String FILE_SCHEME = "file";

    public void loadIcon(Context context, String uri, ImagesLoadingListener listener) {
        try {
            listener.onComplete(getDrawable(context, uri));
        } catch (IOException e) {
            listener.onError(e);
        }
    }

    public void loadIcons(final Context context, List<String> uris, ImagesLoadingListener listener) {
        try {
            List<Drawable> drawables = new ArrayList<>();
            for (String uri : uris) {
                Drawable drawable = getDrawable(context, uri);
                drawables.add(drawable);
            }
            listener.onComplete(drawables);
        } catch (IOException e) {
            listener.onError(e);
        }
    }

    @NonNull
    private Drawable getDrawable(Context context, String source) throws IOException {
        if (BuildConfig.DEBUG) {
            return readJsDevImage(context, source);
        } else if (isLocalFile(Uri.parse(source))) {
            return loadFile(source);
        } else {
            return loadResource(source);
        }
    }

    @NonNull
    private Drawable readJsDevImage(Context context, String source) throws IOException {
        StrictMode.ThreadPolicy threadPolicy = adjustThreadPolicyDebug();
        InputStream is = openStream(context, source);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        restoreThreadPolicyDebug(threadPolicy);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    private boolean isLocalFile(Uri uri) {
        return FILE_SCHEME.equals(uri.getScheme());
    }

    private Drawable loadFile(String uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(Uri.parse(uri).getPath());
        return new BitmapDrawable(NavigationApplication.instance.getResources(), bitmap);
    }

    private static Drawable loadResource(String iconSource) {
        return ResourceDrawableIdHelper.getInstance().getResourceDrawable(NavigationApplication.instance, iconSource);
    }

    private StrictMode.ThreadPolicy adjustThreadPolicyDebug() {
        StrictMode.ThreadPolicy threadPolicy = null;
        if (NavigationApplication.instance.isDebug()) {
            threadPolicy = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        }
        return threadPolicy;
    }

    private void restoreThreadPolicyDebug(@Nullable StrictMode.ThreadPolicy threadPolicy) {
        if (NavigationApplication.instance.isDebug() && threadPolicy != null) {
            StrictMode.setThreadPolicy(threadPolicy);
        }
    }

    private static InputStream openStream(Context context, String uri) throws IOException {
        return uri.contains("http") ? remoteUrl(uri) : localFile(context, uri);
    }

    private static InputStream remoteUrl(String uri) throws IOException {
        return new URL(uri).openStream();
    }

    private static InputStream localFile(Context context, String uri) throws FileNotFoundException {
        return context.getContentResolver().openInputStream(Uri.parse(uri));
    }
}
