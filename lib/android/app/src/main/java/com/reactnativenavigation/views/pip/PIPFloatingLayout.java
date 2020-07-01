package com.reactnativenavigation.views.pip;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.parse.CustomPIPDimension;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ViewExtension;

public class PIPFloatingLayout extends CoordinatorLayout {
    private float dX, dY;
    private Activity activity;
    private int pipLayoutLeft = 0, pipLayoutTop = 0;
    private FrameLayout.LayoutParams layoutParams;
    private PIPStates pipState;
    private CustomPIPDimension pipDimension;

    public PIPFloatingLayout(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public PIPFloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PIPFloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomPIPDimensions(CustomPIPDimension dimension) {
        this.pipDimension = dimension;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (shouldInterceptTouchEvent()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    return true;
                case MotionEvent.ACTION_UP:
                    return this.pipState != PIPStates.CUSTOM_EXPANDED;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandled = false;
        if (shouldInterceptTouchEvent()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = getX() - event.getRawX();
                    dY = getY() - event.getRawY();
                    isHandled = true;
                    break;

                case MotionEvent.ACTION_MOVE:
                    animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    isHandled = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (this.pipState != PIPStates.CUSTOM_EXPANDED) {
                        isHandled = true;
                        animateToExpand();
                    }
                    break;
            }
        }
        return isHandled;
    }

    private void setNativePIPMode() {
        dX = 0;
        dY = 0;
        animate().x(0).y(0).setDuration(0).start();
        Point loc = ViewUtils.getLocationOnScreen(this);
        this.pipLayoutLeft = loc.x;
        this.pipLayoutTop = loc.y;
        this.layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        FrameLayout.LayoutParams pipLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        pipLayoutLayoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(pipLayoutLayoutParams);
    }

    private void setCustomPIPMode() {
        animate().x(this.pipLayoutLeft).y(this.pipLayoutTop).setDuration(0).start();
        setLayoutParams(layoutParams);
    }

    private void initializeCustomLayoutParams() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutParams = new FrameLayout.LayoutParams(this.pipDimension.compact.width.get(), this.pipDimension.compact.height.get());
        this.pipLayoutTop = 0;
        this.pipLayoutLeft = 0;
    }

    private void resetPIPLayout() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.pipLayoutTop = 0;
        this.pipLayoutLeft = 0;
        layoutParams = new FrameLayout.LayoutParams(0, 0);
        layoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(layoutParams);
        removeAllViews();
    }

    private boolean shouldInterceptTouchEvent() {
        return this.pipState == PIPStates.CUSTOM_MOUNTED || this.pipState == PIPStates.CUSTOM_COMPACT;
    }

    private void setCustomCompactState() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = UiUtils.dpToPx(activity, pipDimension.compact.width.get());
        params.height = UiUtils.dpToPx(activity, pipDimension.compact.height.get());
        setLayoutParams(params);

    }

    private void setCustomExpandedState() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = UiUtils.dpToPx(activity, pipDimension.expanded.width.get());
        params.height = UiUtils.dpToPx(activity, pipDimension.expanded.height.get());
        setLayoutParams(params);
        animateToCompact(5000);
    }

    private AnimatorSet createViewSizeAnimation(float currentHeight, float targetHeight, float currentWidth, float targetWidth, int duration) {
        ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(this, ViewExtension.HEIGHT, UiUtils.dpToPx(activity, currentHeight), UiUtils.dpToPx(activity, targetHeight));
        ObjectAnimator widthAnimator = ObjectAnimator.ofFloat(this, ViewExtension.WIDTH, UiUtils.dpToPx(activity, currentWidth), UiUtils.dpToPx(activity, targetWidth));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(heightAnimator, widthAnimator);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    private void animateToCompact(int delay) {
        AnimatorSet animatorSet = createViewSizeAnimation(pipDimension.expanded.height.get(), pipDimension.compact.height.get(), pipDimension.expanded.width.get(), pipDimension.compact.width.get(), 100);
        animatorSet.setStartDelay(delay);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updatePIPState(PIPStates.CUSTOM_COMPACT);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animateToExpand() {
        AnimatorSet animatorSet = createViewSizeAnimation(pipDimension.compact.height.get(), pipDimension.expanded.height.get(), pipDimension.compact.width.get(), pipDimension.expanded.width.get(), 100);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updatePIPState(PIPStates.CUSTOM_EXPANDED);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void updatePIPState(PIPStates pipState) {
        this.pipState = pipState;
        switch (this.pipState) {
            case NOT_STARTED:
                resetPIPLayout();
                break;
            case CUSTOM_MOUNT_START:
                resetPIPLayout();
                initializeCustomLayoutParams();
                setCustomPIPMode();
                break;
            case CUSTOM_COMPACT:
                setCustomCompactState();
                break;
            case CUSTOM_EXPANDED:
                setCustomExpandedState();
                break;
            case NATIVE_MOUNTED:
                setNativePIPMode();
                break;
            case CUSTOM_UNMOUNTED:
                resetPIPLayout();
                break;

        }
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        child.setLayoutParams(params);
    }
}
