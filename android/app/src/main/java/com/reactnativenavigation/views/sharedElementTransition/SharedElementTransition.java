package com.reactnativenavigation.views.sharedElementTransition;

import android.content.Context;
import android.support.annotation.Keep;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.views.image.ReactImageView;
import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.react.ReactViewHacks;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.PathPoint;

public class SharedElementTransition extends ViewGroup {
    public InterpolationParams showInterpolation;
    public InterpolationParams hideInterpolation;

    public void setShowInterpolation(InterpolationParams showInterpolation) {
        this.showInterpolation = showInterpolation;
    }

    public void setHideInterpolation(InterpolationParams hideInterpolation) {
        this.hideInterpolation = hideInterpolation;
    }

    public SharedElementTransition(Context context) {
        super(context);
    }

    public void registerSharedElementTransition(final String key) {
        ViewUtils.runOnPreDraw(this, new Runnable() {
            @Override
            public void run() {
                ViewUtils.performOnParentScreen(SharedElementTransition.this, new Task<Screen>() {
                    @Override
                    public void run(Screen screen) {
                        screen.registerSharedView(SharedElementTransition.this, key);
                    }
                });
            }
        });
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    public void onViewAdded(View child) {
        if (child instanceof ReactImageView) {
            ReactViewHacks.disableReactImageViewRemoteImageFadeInAnimation((ReactImageView) child);
        }
        super.onViewAdded(child);
    }

    @Keep
    public void setCurvedMotion(PathPoint xy) {
        setTranslationX(xy.mX);
        setTranslationY(xy.mY);
    }
}
