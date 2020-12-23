package com.reactnativenavigation.views.pip;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.R;
import com.reactnativenavigation.options.CustomPIPDimension;
import com.reactnativenavigation.utils.ILogger;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.ViewExtension;

public class PIPFloatingLayout extends FrameLayout {
    private float dX, dY;
    private Activity activity;
    private int pipLayoutLeft = 0, pipLayoutTop = 0;
    private FrameLayout.LayoutParams layoutParams;
    private PIPStates pipState = PIPStates.NOT_STARTED;
    private CustomPIPDimension pipDimension;
    private IPIPListener pipListener;
    private AnimatorSet runningAnimation;
    private boolean mDownTouch = false;
    private int mTouchSlop;
    private float mPrevRawX = 0;
    private boolean mIsMoving = false;
    private int mMoveCount = 0;
    private String TAG = "PIPFloatingLayout";
    private ILogger logger;
    private PIPButtonsLayout pipButtonsLayout;
    private int statusBarHeight = 0;

    public PIPFloatingLayout(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
        pipButtonsLayout = new PIPButtonsLayout(activity);
        pipButtonsLayout.setPipButtonListener(buttonListener);
        ViewConfiguration vc = ViewConfiguration.get(this.getContext());
        mTouchSlop = vc.getScaledTouchSlop() * 3;
        statusBarHeight = getStatusBarHeight();
        setBackgroundResource(R.drawable.pip_layout_bg);
    }

    public PIPFloatingLayout(@NonNull Activity activity, ILogger logger) {
        this(activity);
        this.logger = logger;
    }

    public PIPFloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PIPFloatingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCustomPIPDimensions(CustomPIPDimension dimension) {
        this.pipDimension = dimension;
        pipButtonsLayout.setPIPHeight(UiUtils.dpToPx(activity, pipDimension.compact.height.get()));
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean shouldIntercept = false;
        if (pipButtonsLayout.isWithinBounds(event)) {
            return shouldIntercept;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevRawX = event.getRawX();
                mIsMoving = false;
                shouldIntercept = shouldInterceptTouchEvent();
                break;
            case MotionEvent.ACTION_UP:
                mPrevRawX = Integer.MIN_VALUE;
                mIsMoving = false;
                shouldIntercept = shouldInterceptTouchEvent();
                break;
            case MotionEvent.ACTION_CANCEL:
                mPrevRawX = Integer.MIN_VALUE;
                mIsMoving = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsMoving) {
                    shouldIntercept = true;
                } else if (mPrevRawX != Integer.MIN_VALUE) {
                    int xDiff = (int) (event.getRawX() - mPrevRawX);
                    if (xDiff < 0) xDiff = -xDiff;
                    if (xDiff > mTouchSlop) {
                        mIsMoving = true;
                        shouldIntercept = true;
                    }
                }
                break;
        }
        logger.log(Log.VERBOSE, TAG, "onInterceptTouchEvent " + shouldIntercept + "PIPState " + pipState.toString() + "touchEvent " + event.getAction() + " isMoving " + mIsMoving);
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isHandled = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (shouldInterceptTouchEvent()) {
                    dX = getX() - event.getRawX();
                    dY = getY() - event.getRawY();
                    isHandled = true;
                    mDownTouch = true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                mMoveCount++;
                if (mMoveCount > 5) {
                    float halfWidth = getWidth() / 2.0f;
                    float halfHeight = getHeight() / 2.0f;
                    float destX = event.getRawX() + dX - halfWidth;
                    float destY = event.getRawY() + dY - halfHeight;
                    if (destX < 0) {
                        destX = 0;
                    }
                    if ((destX + getWidth()) > UiUtils.getWindowWidth(getContext())) {
                        destX = UiUtils.getWindowWidth(getContext()) - getWidth();
                    }
                    if (destY < statusBarHeight) {
                        destY = statusBarHeight;
                    }
                    if ((destY + getHeight()) > UiUtils.getWindowHeight(getContext())) {
                        destY = UiUtils.getWindowHeight(getContext()) - getHeight();
                    }
                    animate().x(destX).y(destY).setDuration(0).start();
                    mDownTouch = false;
                }
                isHandled = true;
                break;
            case MotionEvent.ACTION_UP:
                mMoveCount = 0;
                if (mDownTouch && shouldInterceptTouchEvent()) {
                    mDownTouch = false;
                    isHandled = true;
                    animateToExpand();
                }
                break;
        }
        return isHandled;
    }

    private void setNativePIPMode() {
        this.layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        FrameLayout.LayoutParams pipLayoutLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        pipLayoutLayoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(pipLayoutLayoutParams);
        this.pipButtonsLayout.hide();
    }

    private void setCustomPIPMode() {
        if (layoutParams != null) {
            setLayoutParams(layoutParams);
        }
        logger.log(Log.INFO, TAG, " setCustomPIPMode  pipLayoutLeft " + this.pipLayoutLeft + " pipLayoutTop " + this.pipLayoutTop);
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
        cancelAnimations();
    }

    public void initiateRestore() {
        Point loc = ViewUtils.getLocationOnScreen(this);
        animate().x(0).y(0).setDuration(0).start();
        this.setX(UiUtils.pxToDp(activity, loc.x));
        this.setY(UiUtils.pxToDp(activity, loc.y));
    }

    private boolean shouldInterceptTouchEvent() {
        return this.pipState == PIPStates.CUSTOM_MOUNTED || this.pipState == PIPStates.CUSTOM_COMPACT;
    }

    private void setCustomCompactState() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = UiUtils.dpToPx(activity, pipDimension.compact.width.get());
        params.height = UiUtils.dpToPx(activity, pipDimension.compact.height.get());
        setLayoutParams(params);
        pipButtonsLayout.makeShortTimeVisible();
    }

    private void setCustomExpandedState() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = UiUtils.dpToPx(activity, pipDimension.expanded.width.get());
        params.height = UiUtils.dpToPx(activity, pipDimension.expanded.height.get());
        setLayoutParams(params);
        pipButtonsLayout.makePermanentVisible();
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

    private AnimatorSet createXYAnimation(float targetStartX, float targetStartY, Point loc, int duration) {
        ObjectAnimator XAnimator = ObjectAnimator.ofFloat(this, View.X, loc.x, targetStartX);
        ObjectAnimator YAnimator = ObjectAnimator.ofFloat(this, View.Y, loc.y, targetStartY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(XAnimator, YAnimator);
        animatorSet.setDuration(duration);
        return animatorSet;
    }

    private float getTargetX(float currentWidth, float targetWidth, Point loc) {
        float targetStartX = loc.x;
        float targetEndX = UiUtils.pxToDp(activity, loc.x) + targetWidth;
        if (targetEndX > UiUtils.getWindowWidthDP(activity)) {
            float diffWidth = targetWidth - currentWidth;
            targetStartX = loc.x - UiUtils.dpToPx(activity, diffWidth);
        }
        return targetStartX;
    }

    private float getTargetY(float currentHeight, float targetHeight, Point loc) {
        float targetStartY = loc.y;
        float targetEndY = UiUtils.pxToDp(activity, loc.y) + targetHeight;
        if (targetEndY > UiUtils.getWindowHeightDP(activity)) {
            float diffHeight = targetHeight - currentHeight;
            targetStartY = loc.y - UiUtils.dpToPx(activity, diffHeight);
        }
        return targetStartY;
    }

    private void animateToCompact(int delay) {
        runningAnimation = createViewSizeAnimation(pipDimension.expanded.height.get(), pipDimension.compact.height.get(), pipDimension.expanded.width.get(), pipDimension.compact.width.get(), 100);
        runningAnimation.setStartDelay(delay);
        runningAnimation.start();
        runningAnimation.addListener(new Animator.AnimatorListener() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationStart(Animator animation) {
                wasCancelled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled) {
                    updatePIPState(PIPStates.CUSTOM_COMPACT);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void animateToExpand() {
        Point loc = ViewUtils.getLocationOnScreen(this);
        float targetStartX = getTargetX(pipDimension.compact.width.get(), pipDimension.expanded.width.get(), loc);
        float targetStartY = getTargetY(pipDimension.compact.height.get(), pipDimension.expanded.height.get(), loc);
        AnimatorSet expandAnimation = createViewSizeAnimation(pipDimension.compact.height.get(), pipDimension.expanded.height.get(), pipDimension.compact.width.get(), pipDimension.expanded.width.get(), 100);
        AnimatorSet xyAnimation = createXYAnimation(targetStartX, targetStartY, loc, 100);
        expandAnimation.playTogether(xyAnimation);
        runningAnimation = expandAnimation;
        expandAnimation.start();
        expandAnimation.addListener(new Animator.AnimatorListener() {
            boolean wasCancelled = false;

            @Override
            public void onAnimationStart(Animator animation) {
                wasCancelled = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!wasCancelled) {
                    setX(targetStartX);
                    setY(targetStartY);
                    updatePIPState(PIPStates.CUSTOM_EXPANDED);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                wasCancelled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void updatePIPState(PIPStates pipState) {
        PIPStates oldState = this.pipState;
        this.pipState = pipState;
        if (oldState != pipState) {
            switch (this.pipState) {
                case NOT_STARTED:
                    resetPIPLayout();
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
                case CUSTOM_MOUNTED:
                    setCustomPIPMode();
                    setCustomCompactState();
                    animateToExpand();
                    break;
                case NATIVE_MOUNT_START:
                    cancelAnimations();
                    Point loc = ViewUtils.getLocationOnScreen(this);
                    this.pipLayoutLeft = loc.x;
                    this.pipLayoutTop = loc.y;
                    dX = 0;
                    dY = 0;
                    animate().x(0).y(0).setDuration(0).start();
                    logger.log(Log.INFO, TAG, " NATIVE_MOUNT_START  pipLayoutLeft " + this.pipLayoutLeft + " pipLayoutTop " + this.pipLayoutTop);
                    break;

            }
            if (this.pipListener != null) {
                this.pipListener.onPIPStateChanged(oldState, pipState);
            }
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        child.setLayoutParams(params);
        super.addView(pipButtonsLayout);
    }

    public void setPIPListener(IPIPListener pipListener) {
        this.pipListener = pipListener;
    }

    public void cancelAnimations() {
        if (runningAnimation != null) {
            runningAnimation.cancel();
            runningAnimation = null;
        }
    }

    private PIPButtonsLayout.IPIPButtonListener buttonListener = new PIPButtonsLayout.IPIPButtonListener() {
        @Override
        public void onFullScreenClick() {
            if (pipListener != null) {
                pipListener.onFullScreenClick();
            }
        }

        @Override
        public void onCloseClick() {
            if (pipListener != null) {
                pipListener.onCloseClick();
            }
        }
    };

}