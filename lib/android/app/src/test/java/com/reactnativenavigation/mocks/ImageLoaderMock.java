package com.reactnativenavigation.mocks;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.reactnativenavigation.utils.ImageLoader;

import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
                    int urlCount = ((Collection) invocation.getArguments()[1]).size();
                    List<Drawable> drawables = Collections.nCopies(urlCount, mockDrawable);
                    ((ImageLoader.ImagesLoadingListener) invocation.getArguments()[2]).onComplete(drawables);
                    return null;
                }
        ).when(imageLoader).loadIcons(any(), any(), any());
        doAnswer(
                invocation -> {
                    ((ImageLoader.ImagesLoadingListener) invocation.getArguments()[2]).onComplete(mockDrawable);
                    return null;
                }
        ).when(imageLoader).loadIcon(any(), any(), any());
        return imageLoader;
    }
}
