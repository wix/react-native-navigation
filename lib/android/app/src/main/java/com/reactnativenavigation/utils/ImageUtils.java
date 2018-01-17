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

import com.reactnativenavigation.NavigationApplication;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {

	public interface ImageLoadingListener {
		void onComplete(@NonNull Drawable drawable);

		void onError(Throwable error);
	}

	public static void loadIcon(final Context context, final String uri, final ImageLoadingListener listener) {
        try {
            StrictMode.ThreadPolicy threadPolicy = adjustThreadPolicyDebug();
            
            InputStream is = openStream(context, uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            listener.onComplete(drawable);

            restoreThreadPolicyDebug(threadPolicy);
        } catch (IOException e) {
            listener.onError(e);
        }
    }

    private static StrictMode.ThreadPolicy adjustThreadPolicyDebug() {
        StrictMode.ThreadPolicy threadPolicy = null;
        if (NavigationApplication.instance.isDebug()) {
            threadPolicy = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        }
        return threadPolicy;
    }

    private static void restoreThreadPolicyDebug(@Nullable StrictMode.ThreadPolicy threadPolicy) {
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
