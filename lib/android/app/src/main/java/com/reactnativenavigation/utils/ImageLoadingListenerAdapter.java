package com.reactnativenavigation.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class ImageLoadingListenerAdapter implements ImageLoader.ImageLoadingListener {
    @Override
    public void onComplete(@NonNull Drawable drawable) {

    }

    @Override
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
