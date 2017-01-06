package com.reactnativenavigation.views.sharedElementTransition;

import android.content.Context;
import android.support.annotation.Keep;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.RelativeLayout;

import com.facebook.react.views.image.ReactImageView;
import com.reactnativenavigation.params.InterpolationParams;
import com.reactnativenavigation.react.ReactViewHacks;
import com.reactnativenavigation.screens.Screen;
import com.reactnativenavigation.utils.Task;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.utils.PathPoint;
import com.reactnativenavigation.views.utils.Point;

public class SharedElementTransition extends ViewGroup {
    public InterpolationParams showInterpolation;
    public InterpolationParams hideInterpolation;
    public ViewGroup.LayoutParams childLayoutParams;

    private View child;
    private int childLeft;
    private int childTop;
    private int childWidth = -1;
    private int childHeight = -1;

    public void setShowInterpolation(InterpolationParams showInterpolation) {
        this.showInterpolation = showInterpolation;
    }

    public void setHideInterpolation(InterpolationParams hideInterpolation) {
        this.hideInterpolation = hideInterpolation;
    }

    public View getSharedView() {
        return child;
    }

    public SharedElementTransition(Context context) {
        super(context);
        setContentDescription("SET");
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
        this.child = child;
        if (child instanceof ReactImageView) {
            ReactViewHacks.disableReactImageViewRemoteImageFadeInAnimation((ReactImageView) child);
        }
        super.onViewAdded(child);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
    }

    @Keep
    public void setCurvedMotion(PathPoint xy) {
        child.setTranslationX(xy.mX);
        child.setTranslationY(xy.mY);
    }

    public void attachChildToScreen() {
        ViewUtils.performOnParentScreen(this, new Task<Screen>() {
            @Override
            public void run(Screen screen) {
                SharedElementTransition sharedElementTransition = SharedElementTransition.this;
                View child = sharedElementTransition.getChildAt(0);

                saveChildParams(child);
                Point childLocationInScreen = ViewUtils.getLocationOnScreen(child);

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(childWidth, childHeight);
                sharedElementTransition.removeView(child);
                lp.leftMargin = childLocationInScreen.x;
                lp.topMargin = childLocationInScreen.y;

                screen.addView(child, lp);
            }
        });
    }

    private void saveChildParams(View child) {
        childLayoutParams = child.getLayoutParams();
        childLeft = child.getLeft();
        childTop = child.getTop();
        if (childWidth == -1) {
            childWidth = child.getWidth();
        }
        if (childHeight == -1) {
            childHeight = child.getHeight();
        }
    }

    public void attachChildToSelf() {
        if (child instanceof SharedElementTransition) {
            return;
        }
        ((ViewManager) child.getParent()).removeView(child);
        child.setLeft(childLeft);
        child.setTop(childTop);
        addView(child, new LayoutParams(childLayoutParams));
    }
}
