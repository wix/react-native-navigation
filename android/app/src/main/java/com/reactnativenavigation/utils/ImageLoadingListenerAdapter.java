package com.reactnativenavigation.utils;

import android.graphics.drawable.Drawable;

import java.util.List;

import androidx.annotation.NonNull;

public class ImageLoadingListenerAdapter implements ImageLoader.ImagesLoadingListener {
    @Override
    public void onComplete(@NonNull List<? extends Drawable> drawables) {

    }

    @Override
    public void onComplete(@NonNull Drawable drawable) {

    }

    @Override
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
