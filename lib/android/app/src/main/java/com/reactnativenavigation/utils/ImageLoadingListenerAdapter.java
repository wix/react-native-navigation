package com.reactnativenavigation.utils;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.List;

public class ImageLoadingListenerAdapter implements ImageLoader.ImagesLoadingListener {
    @Override
    public void onComplete(@NonNull List<Drawable> drawables) {

    }

    @Override
    public void onComplete(@NonNull Drawable drawable) {

    }

    @Override
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
