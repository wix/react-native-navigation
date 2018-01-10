package com.reactnativenavigation.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageUtils {

	public interface ImageLoadingListener {
		void onComplete(@NonNull Drawable drawable);

		void onError(Throwable error);
	}

	public static void tryLoadIcon(final Context context, final String uri, final ImageLoadingListener listener) {
		if (uri == null) {
			if (listener != null) {
				listener.onError(new IllegalArgumentException("Uri is null"));
			}
			return;
		}
		runWorkerThread(new Runnable() {
			@Override
			public void run() {
				loadIcon(context, uri, listener);
			}
		});
	}

	private static void loadIcon(Context context, String uri, final ImageLoadingListener listener) {
		try {
			InputStream is = null;
			if (uri.contains("http")) {
				URL url = new URL(uri);
				is = url.openStream();
			} else {
				is = context.getContentResolver().openInputStream(Uri.parse(uri));
			}

			Bitmap bitmap = BitmapFactory.decodeStream(is);
			Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
			if (listener != null) {
				listener.onComplete(drawable);
			}
		} catch (IOException e) {
			if (listener != null) {
				listener.onError(e);
			} else {
				e.printStackTrace();
			}
		}
	}

	private static void runWorkerThread(Runnable runnable) {
		new Thread(runnable).start();
	}
}
