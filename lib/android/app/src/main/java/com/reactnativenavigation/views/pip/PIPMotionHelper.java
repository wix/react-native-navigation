package com.reactnativenavigation.views.pip;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.RectEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.animation.Interpolator;

import androidx.annotation.RequiresApi;

import java.io.PrintWriter;

import static com.reactnativenavigation.views.pip.Interpolators.FAST_OUT_LINEAR_IN;
import static com.reactnativenavigation.views.pip.Interpolators.FAST_OUT_SLOW_IN;
import static com.reactnativenavigation.views.pip.Interpolators.LINEAR_OUT_SLOW_IN;

/**
 * A helper to animate and manipulate the PiP.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PIPMotionHelper {
    private static final String TAG = "PipMotionHelper";
    private static final boolean DEBUG = false;
    private static final RectEvaluator RECT_EVALUATOR = new RectEvaluator(new Rect());
    private static final int DEFAULT_MOVE_STACK_DURATION = 225;
    private static final int SNAP_STACK_DURATION = 225;
    private static final int DRAG_TO_TARGET_DISMISS_STACK_DURATION = 375;
    private static final int DRAG_TO_DISMISS_STACK_DURATION = 175;
    private static final int SHRINK_STACK_FROM_MENU_DURATION = 250;
    private static final int EXPAND_STACK_TO_MENU_DURATION = 250;
    private static final int EXPAND_STACK_TO_FULLSCREEN_DURATION = 300;
    private static final int MINIMIZE_STACK_MAX_DURATION = 200;
    private static final int IME_SHIFT_DURATION = 300;
    // The fraction of the stack width that the user has to drag offscreen to minimize the PiP
    private static final float MINIMIZE_OFFSCREEN_FRACTION = 0.3f;
    // The fraction of the stack height that the user has to drag offscreen to dismiss the PiP
    private static final float DISMISS_OFFSCREEN_FRACTION = 0.3f;
    private static final int MSG_RESIZE_IMMEDIATE = 1;
    private static final int MSG_RESIZE_ANIMATE = 2;
    private PIPFloatingLayout mContext;
    private PipSnapAlgorithm mSnapAlgorithm;
    private FlingAnimationUtils mFlingAnimationUtils;
    private final Rect mBounds = new Rect();
    private final Rect mStableInsets = new Rect();
    private ValueAnimator mBoundsAnimator = null;

    public PIPMotionHelper(PIPFloatingLayout floatingLayout, PipSnapAlgorithm snapAlgorithm, FlingAnimationUtils flingAnimationUtils) {
        mContext = floatingLayout;
        mSnapAlgorithm = snapAlgorithm;
        mFlingAnimationUtils = flingAnimationUtils;
    }


    /**
     * Tries to the move the pinned stack to the given {@param bounds}.
     */
    void movePip(Rect toBounds) {
        cancelAnimations();
        resizePipUnchecked(toBounds);
        mBounds.set(toBounds);
    }


    /**
     * @return the PiP bounds.
     */
    Rect getBounds() {
        return mBounds;
    }

    /**
     * @return the closest minimized PiP bounds.
     */
    Rect getClosestMinimizedBounds(Rect stackBounds, Rect movementBounds) {
        Point displaySize = new Point();
        mContext.getDisplay().getRealSize(displaySize);
        Rect toBounds = mSnapAlgorithm.findClosestSnapBounds(movementBounds, stackBounds);
        mSnapAlgorithm.applyMinimizedOffset(toBounds, movementBounds, displaySize, mStableInsets);
        return toBounds;
    }

    /**
     * @return whether the PiP at the current bounds should be minimized.
     */
    boolean shouldMinimizePip() {
        Point displaySize = new Point();
        mContext.getDisplay().getRealSize(displaySize);
        if (mBounds.left < 0) {
            float offscreenFraction = (float) -mBounds.left / mBounds.width();
            return offscreenFraction >= MINIMIZE_OFFSCREEN_FRACTION;
        } else if (mBounds.right > displaySize.x) {
            float offscreenFraction = (float) (mBounds.right - displaySize.x) /
                    mBounds.width();
            return offscreenFraction >= MINIMIZE_OFFSCREEN_FRACTION;
        } else {
            return false;
        }
    }

    /**
     * @return whether the PiP at the current bounds should be dismissed.
     */
    boolean shouldDismissPip() {
        Point displaySize = new Point();
        mContext.getDisplay().getSize(displaySize);
        if (mBounds.bottom > displaySize.y) {
            float offscreenFraction = (float) (mBounds.bottom - displaySize.y) / mBounds.height();
            return offscreenFraction >= DISMISS_OFFSCREEN_FRACTION;
        }
        return false;
    }

    /**
     * Flings the minimized PiP to the closest minimized snap target.
     */
    Rect flingToMinimizedState(float velocityY, Rect movementBounds, Point dragStartPosition) {
        cancelAnimations();
        // We currently only allow flinging the minimized stack up and down, so just lock the
        // movement bounds to the current stack bounds horizontally
        movementBounds = new Rect(mBounds.left, movementBounds.top, mBounds.left,
                movementBounds.bottom);
        Rect toBounds = mSnapAlgorithm.findClosestSnapBounds(movementBounds, mBounds,
                0 /* velocityX */, velocityY, dragStartPosition);
        if (!mBounds.equals(toBounds)) {
            mBoundsAnimator = createAnimationToBounds(mBounds, toBounds, 0, FAST_OUT_SLOW_IN);
            mFlingAnimationUtils.apply(mBoundsAnimator, 0,
                    distanceBetweenRectOffsets(mBounds, toBounds),
                    velocityY);
            mBoundsAnimator.start();
        }
        return toBounds;
    }

    /**
     * Animates the PiP to the minimized state, slightly offscreen.
     */
    Rect animateToClosestMinimizedState(Rect movementBounds,
                                        AnimatorUpdateListener updateListener) {
        cancelAnimations();
        Rect toBounds = getClosestMinimizedBounds(mBounds, movementBounds);
        if (!mBounds.equals(toBounds)) {
            mBoundsAnimator = createAnimationToBounds(mBounds, toBounds,
                    MINIMIZE_STACK_MAX_DURATION, LINEAR_OUT_SLOW_IN);
            if (updateListener != null) {
                mBoundsAnimator.addUpdateListener(updateListener);
            }
            mBoundsAnimator.start();
        }
        return toBounds;
    }

    /**
     * Flings the PiP to the closest snap target.
     */
    Rect flingToSnapTarget(float velocity, float velocityX, float velocityY, Rect movementBounds,
                           AnimatorUpdateListener updateListener, AnimatorListener listener,
                           Point startPosition) {
        cancelAnimations();
        Rect toBounds = mSnapAlgorithm.findClosestSnapBounds(movementBounds, mBounds,
                velocityX, velocityY, startPosition);
        if (!mBounds.equals(toBounds)) {
            mBoundsAnimator = createAnimationToBounds(mBounds, toBounds, 0, FAST_OUT_SLOW_IN);
            mFlingAnimationUtils.apply(mBoundsAnimator, 0,
                    distanceBetweenRectOffsets(mBounds, toBounds),
                    velocity);
            if (updateListener != null) {
                mBoundsAnimator.addUpdateListener(updateListener);
            }
            if (listener != null) {
                mBoundsAnimator.addListener(listener);
            }
            mBoundsAnimator.start();
        }
        return toBounds;
    }

    /**
     * Animates the PiP to the closest snap target.
     */
    Rect animateToClosestSnapTarget(Rect movementBounds, AnimatorUpdateListener updateListener,
                                    AnimatorListener listener) {
        cancelAnimations();
        Rect toBounds = mSnapAlgorithm.findClosestSnapBounds(movementBounds, mBounds);
        if (!mBounds.equals(toBounds)) {
            mBoundsAnimator = createAnimationToBounds(mBounds, toBounds, SNAP_STACK_DURATION,
                    FAST_OUT_SLOW_IN);
            if (updateListener != null) {
                mBoundsAnimator.addUpdateListener(updateListener);
            }
            if (listener != null) {
                mBoundsAnimator.addListener(listener);
            }
            mBoundsAnimator.start();
        }
        return toBounds;
    }

    /**
     * Animates the PiP to the expanded state to show the menu.
     */
    float animateToExpandedState(Rect expandedBounds, Rect movementBounds,
                                 Rect expandedMovementBounds) {
        float savedSnapFraction = mSnapAlgorithm.getSnapFraction(new Rect(mBounds), movementBounds);
        mSnapAlgorithm.applySnapFraction(expandedBounds, expandedMovementBounds, savedSnapFraction);
        resizeAndAnimatePipUnchecked(expandedBounds, EXPAND_STACK_TO_MENU_DURATION);
        return savedSnapFraction;
    }

    /**
     * Animates the PiP from the expanded state to the normal state after the menu is hidden.
     */
    void animateToUnexpandedState(Rect normalBounds, float savedSnapFraction,
                                  Rect normalMovementBounds, Rect currentMovementBounds, boolean minimized,
                                  boolean immediate) {
        if (savedSnapFraction < 0f) {
            // If there are no saved snap fractions, then just use the current bounds
            savedSnapFraction = mSnapAlgorithm.getSnapFraction(new Rect(mBounds),
                    currentMovementBounds);
        }
        mSnapAlgorithm.applySnapFraction(normalBounds, normalMovementBounds, savedSnapFraction);
        if (minimized) {
            normalBounds = getClosestMinimizedBounds(normalBounds, normalMovementBounds);
        }
        if (immediate) {
            movePip(normalBounds);
        } else {
            resizeAndAnimatePipUnchecked(normalBounds, SHRINK_STACK_FROM_MENU_DURATION);
        }
    }

    /**
     * Animates the PiP to offset it from the IME.
     */
    void animateToIMEOffset(Rect toBounds) {
        cancelAnimations();
        resizeAndAnimatePipUnchecked(toBounds, IME_SHIFT_DURATION);
    }

    /**
     * Animates the dismissal of the PiP off the edge of the screen.
     */
    Rect animateDismiss(Rect pipBounds, float velocityX, float velocityY,
                        AnimatorUpdateListener listener) {
        cancelAnimations();
        final float velocity = PointF.length(velocityX, velocityY);
        final boolean isFling = velocity > mFlingAnimationUtils.getMinVelocityPxPerSecond();
        Point p = getDismissEndPoint(pipBounds, velocityX, velocityY, isFling);
        Rect toBounds = new Rect(pipBounds);
        toBounds.offsetTo(p.x, p.y);
        mBoundsAnimator = createAnimationToBounds(mBounds, toBounds, DRAG_TO_DISMISS_STACK_DURATION,
                FAST_OUT_LINEAR_IN);
        mBoundsAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //dismissPip();
            }
        });
        if (isFling) {
            mFlingAnimationUtils.apply(mBoundsAnimator, 0,
                    distanceBetweenRectOffsets(mBounds, toBounds), velocity);
        }
        if (listener != null) {
            mBoundsAnimator.addUpdateListener(listener);
        }
        mBoundsAnimator.start();
        return toBounds;
    }

    /**
     * Cancels all existing animations.
     */
    void cancelAnimations() {
        if (mBoundsAnimator != null) {
            mBoundsAnimator.cancel();
            mBoundsAnimator = null;
        }
    }

    /**
     * Creates an animation to move the PiP to give given {@param toBounds}.
     */
    private ValueAnimator createAnimationToBounds(Rect fromBounds, Rect toBounds, int duration,
                                                  Interpolator interpolator) {
        ValueAnimator anim = new ValueAnimator();
        anim.setObjectValues(fromBounds, toBounds);
        anim.setEvaluator(RECT_EVALUATOR);
        anim.setDuration(duration);
        anim.setInterpolator(interpolator);
        anim.addUpdateListener((ValueAnimator animation) -> {
            resizePipUnchecked((Rect) animation.getAnimatedValue());
        });
        return anim;
    }

    /**
     * Directly resizes the PiP to the given {@param bounds}.
     */
    private void resizePipUnchecked(Rect toBounds) {
        if (DEBUG) {
            Log.d(TAG, "resizePipUnchecked: toBounds=" + toBounds
                    + " callers=\n");
        }
        if (!toBounds.equals(mBounds)) {
        }
    }

    /**
     * Directly resizes the PiP to the given {@param bounds}.
     */
    private void resizeAndAnimatePipUnchecked(Rect toBounds, int duration) {
        if (DEBUG) {
            Log.d(TAG, "resizeAndAnimatePipUnchecked: toBounds=" + toBounds
                    + " duration=" + duration + " callers=\n");
        }
        if (!toBounds.equals(mBounds)) {

        }
    }

    /**
     * @return the coordinates the PIP should animate to based on the direction of velocity when
     * dismissing.
     */
    private Point getDismissEndPoint(Rect pipBounds, float velX, float velY, boolean isFling) {
        Point displaySize = new Point();
        mContext.getDisplay().getRealSize(displaySize);
        final float bottomBound = displaySize.y + pipBounds.height() * .1f;
        if (isFling && velX != 0 && velY != 0) {
            // Line is defined by: y = mx + b, m = slope, b = y-intercept
            // Find the slope
            final float slope = velY / velX;
            // Sub in slope and PiP position to solve for y-intercept: b = y - mx
            final float yIntercept = pipBounds.top - slope * pipBounds.left;
            // Now find the point on this line when y = bottom bound: x = (y - b) / m
            final float x = (bottomBound - yIntercept) / slope;
            return new Point((int) x, (int) bottomBound);
        } else {
            // If it wasn't a fling the velocity on 'up' is not reliable for direction of movement,
            // just animate downwards.
            return new Point(pipBounds.left, (int) bottomBound);
        }
    }

    /**
     * @return whether the gesture it towards the dismiss area based on the velocity when
     * dismissing.
     */
    public boolean isGestureToDismissArea(Rect pipBounds, float velX, float velY,
                                          boolean isFling) {
        Point endpoint = getDismissEndPoint(pipBounds, velX, velY, isFling);
        // Center the point
        endpoint.x += pipBounds.width() / 2;
        endpoint.y += pipBounds.height() / 2;
        // The dismiss area is the middle third of the screen, half the PIP's height from the bottom
        Point size = new Point();
        mContext.getDisplay().getRealSize(size);
        final int left = size.x / 3;
        Rect dismissArea = new Rect(left, size.y - (pipBounds.height() / 2), left * 2,
                size.y + pipBounds.height());
        return dismissArea.contains(endpoint.x, endpoint.y);
    }

    /**
     * @return the distance between points {@param p1} and {@param p2}.
     */
    private float distanceBetweenRectOffsets(Rect r1, Rect r2) {
        return PointF.length(r1.left - r2.left, r1.top - r2.top);
    }

    void synchronizePinnedStackBounds() {
        cancelAnimations();
      /*  try {
            StackInfo stackInfo = mActivityManager.getStackInfo(PINNED_STACK_ID);
            if (stackInfo != null) {
                mBounds.set(stackInfo.bounds);
            }
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to get pinned stack bounds");
        }*/
    }

    void expandPip() {

    }

    public void dump(PrintWriter pw, String prefix) {
        final String innerPrefix = prefix + "  ";
        pw.println(prefix + TAG);
        pw.println(innerPrefix + "mBounds=" + mBounds);
        pw.println(innerPrefix + "mStableInsets=" + mStableInsets);
    }

    public void dismissPip() {
    }
}