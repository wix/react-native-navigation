/*
 * Copyright 2016 Google Inc.
 * Copyright 2017 Shazam Entertainment Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.reactnativenavigation.views.element.animators.reflow;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Property;

import javax.annotation.Nonnull;

/**
 * A drawable which shows (a portion of) one of two given bitmaps, switching between them once
 * a progress property passes a threshold.
 * <p>
 * This is helpful when animating text size change as small text scaled up is blurry but larger
 * text scaled down has different kerning. Instead we use images of both states and switch
 * during the transition. We use images as animating text size thrashes the font cache.
 */
class SwitchDrawable extends Drawable {

    static final Property<SwitchDrawable, PointF> TOP_LEFT =
            new Property<SwitchDrawable, PointF>(PointF.class, "topLeft") {
                @Override
                public void set(SwitchDrawable drawable, PointF topLeft) {
                    drawable.setTopLeft(topLeft);
                }

                @Override
                public PointF get(SwitchDrawable drawable) {
                    return drawable.getTopLeft();
                }
            };

    static final Property<SwitchDrawable, Integer> WIDTH =
            new Property<SwitchDrawable, Integer>(Integer.class, "width") {
                @Override
                public void set(SwitchDrawable drawable, Integer width) {
                    drawable.setWidth(width);
                }

                @Override
                public Integer get(SwitchDrawable drawable) {
                    return drawable.getWidth();
                }
            };

    static final Property<SwitchDrawable, Integer> HEIGHT =
            new Property<SwitchDrawable, Integer>(Integer.class, "height") {
                @Override
                public void set(SwitchDrawable drawable, Integer height) {
                    drawable.setHeight(height);
                }

                @Override
                public Integer get(SwitchDrawable drawable) {
                    return drawable.getHeight();
                }
            };

    static final Property<SwitchDrawable, Integer> ALPHA =
            new Property<SwitchDrawable, Integer>(Integer.class, "alpha") {
                @Override
                public void set(SwitchDrawable drawable, Integer alpha) {
                    drawable.setAlpha(alpha);
                }

                @Override
                public Integer get(SwitchDrawable drawable) {
                    return DrawableCompat.getAlpha(drawable);
                }
            };

    static final Property<SwitchDrawable, Float> PROGRESS =
            new Property<SwitchDrawable, Float>(Float.class, "progress") {
                @Override
                public void set(SwitchDrawable drawable, Float progress) {
                    drawable.setProgress(progress);
                }

                @Override
                public Float get(SwitchDrawable drawable) {
                    return 0f;
                }
            };

    private final Paint paint;
    private final float switchThreshold;
    private Bitmap currentBitmap;
    private final Bitmap endBitmap;
    private Rect currentBitmapSrcBounds;
    private final Rect endBitmapSrcBounds;
    private boolean hasSwitched = false;
    private PointF topLeft;
    private int width, height;

    SwitchDrawable(
            @Nonnull Bitmap startBitmap,
            @Nonnull Rect startBitmapSrcBounds,
            float startFontSize,
            @Nonnull Bitmap endBitmap,
            @Nonnull Rect endBitmapSrcBounds,
            float endFontSize) {
        currentBitmap = startBitmap;
        currentBitmapSrcBounds = startBitmapSrcBounds;
        this.endBitmap = endBitmap;
        this.endBitmapSrcBounds = endBitmapSrcBounds;
        switchThreshold = startFontSize / (startFontSize + endFontSize);
        paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
    }

    @Override
    public void draw(@Nonnull Canvas canvas) {
        canvas.drawBitmap(currentBitmap, currentBitmapSrcBounds, getBounds(), paint);
    }

    @Override
    public int getAlpha() {
        return paint.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public ColorFilter getColorFilter() {
        return paint.getColorFilter();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    void setProgress(float progress) {
        if (!hasSwitched && progress >= switchThreshold) {
            currentBitmap = endBitmap;
            currentBitmapSrcBounds = endBitmapSrcBounds;
            hasSwitched = true;
        }
    }

    PointF getTopLeft() {
        return topLeft;
    }

    void setTopLeft(PointF topLeft) {
        this.topLeft = topLeft;
        updateBounds();
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
        updateBounds();
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
        updateBounds();
    }

    private void updateBounds() {
        int left = Math.round(topLeft.x);
        int top = Math.round(topLeft.y);
        setBounds(left, top, left + width, top + height);
    }

}
