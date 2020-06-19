package com.reactnativenavigation.views.pip;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public class PIPFloatingLayout extends CoordinatorLayout {
    private float dX, dY;
    private boolean nativePIPMode = false;
    private Activity activity;
    private int pipLayoutWidth = 0, pipLayoutHeight = 0, pipLayoutLeft = 0, pipLayoutTop = 0;

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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!nativePIPMode) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!nativePIPMode) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dX = getX() - event.getRawX();
                    dY = getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:
                    animate()
                            .x(event.getRawX() + dX)
                            .y(event.getRawY() + dY)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public void setNativePIPMode() {
        this.nativePIPMode = true;
        dX = 0;
        dY = 0;
        animate().cancel();
        FrameLayout.LayoutParams pipLayoutLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        pipLayoutLayoutParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        pipLayoutLayoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        pipLayoutLayoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(pipLayoutLayoutParams);
    }

    public void setCustomPIPMode() {
        FrameLayout.LayoutParams pipLayoutLayoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        pipLayoutLayoutParams.width = this.pipLayoutWidth;
        pipLayoutLayoutParams.height = this.pipLayoutHeight;
        pipLayoutLayoutParams.setMargins(this.pipLayoutLeft, this.pipLayoutTop, 0, 0);
        setLayoutParams(pipLayoutLayoutParams);
    }

    public void intializeCustomLayoutParams() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.pipLayoutWidth = (int) (displayMetrics.widthPixels * .5);
        this.pipLayoutHeight = (int) (displayMetrics.widthPixels * .5 * .56);
        this.pipLayoutTop = (int) (displayMetrics.heightPixels * .7);
        this.pipLayoutLeft = (int) (displayMetrics.widthPixels * .4);
    }

    public void resetPIPLayout() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.pipLayoutWidth = 0;
        this.pipLayoutHeight = 0;
        this.pipLayoutTop = 0;
        this.pipLayoutLeft = 0;
        FrameLayout.LayoutParams pipLayoutLayoutParams = new FrameLayout.LayoutParams(this.pipLayoutWidth, this.pipLayoutHeight);
        pipLayoutLayoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(pipLayoutLayoutParams);
        removeAllViews();
    }
}
