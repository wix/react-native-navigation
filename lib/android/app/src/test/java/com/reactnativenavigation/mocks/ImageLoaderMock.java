package com.reactnativenavigation.mocks;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.reactnativenavigation.utils.ImageLoader;

import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class ImageLoaderMock {
    private static Drawable mockDrawable = new Drawable() {
        @Override
        public void draw(@NonNull Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@android.support.annotation.Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    };

    public static ImageLoader mock() {
        ImageLoader imageLoader = Mockito.mock(ImageLoader.class);
        doAnswer(
                invocation -> {
                    ((ImageLoader.ImageLoadingListener) invocation.getArguments()[2]).onComplete(mockDrawable);
                    return null;
                }
        ).when(imageLoader).loadIcon(any(), anyString(), any());
        return imageLoader;
    }
}
