package com.reactnativenavigation.views.pip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ComponentName;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.util.Size;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.RequiresApi;

import com.reactnativenavigation.R;

import java.io.PrintWriter;

/**
 * Manages all the touch handling for PIP on the Phone, including moving, dismissing and expanding
 * the PIP.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PipTouchHandler {
    private static final String TAG = "PipTouchHandler";
    // Allow the PIP to be dragged to the edge of the screen to be minimized.
    private static final boolean ENABLE_MINIMIZE = false;
    // Allow the PIP to be flung from anywhere on the screen to the bottom to be dismissed.
    private static final boolean ENABLE_FLING_DISMISS = false;
    // These values are used for metrics and should never change
    private static final int METRIC_VALUE_DISMISSED_BY_TAP = 0;
    private static final int METRIC_VALUE_DISMISSED_BY_DRAG = 1;
    private static final int SHOW_DISMISS_AFFORDANCE_DELAY = 225;
    // Allow dragging the PIP to a location to close it
    private static final boolean ENABLE_DISMISS_DRAG_TO_EDGE = true;
    private PIPFloatingLayout mFloatingLayout;
    private final ViewConfiguration mViewConfig;
    private final PipMenuListener mMenuListener = new PipMenuListener();
    private final PipDismissViewController mDismissViewController;
    private final PipSnapAlgorithm mSnapAlgorithm;
    private final AccessibilityManager mAccessibilityManager;
    private boolean mShowPipMenuOnAnimationEnd = false;
    // The current movement bounds
    private Rect mMovementBounds = new Rect();
    // The reference inset bounds, used to determine the dismiss fraction
    private Rect mInsetBounds = new Rect();
    // The reference bounds used to calculate the normal/expanded target bounds
    private Rect mNormalBounds = new Rect();
    private Rect mNormalMovementBounds = new Rect();
    private Rect mExpandedBounds = new Rect();
    private Rect mExpandedMovementBounds = new Rect();

    // us to send stale bounds
    private int mDeferResizeToNormalBoundsUntilRotation = -1;

    private Handler mHandler = new Handler();
    private Runnable mShowDismissAffordance = new Runnable() {
        @Override
        public void run() {
            if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                mDismissViewController.showDismissTarget();
            }
        }
    };
    private ValueAnimator.AnimatorUpdateListener mUpdateScrimListener =
            new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    updateDismissFraction();
                }
            };
    // Behaviour states
    private PIPStates mPIPState = PIPStates.NOT_STARTED;
    private boolean mIsMinimized;
    private boolean mIsImeShowing;
    private int mImeHeight;
    private int mImeOffset;
    private float mSavedSnapFraction = -1f;
    private boolean mSendingHoverAccessibilityEvents;
    private boolean mMovementWithinMinimize;
    private boolean mMovementWithinDismiss;
    // Touch state
    private final PipTouchState mTouchState;
    private final FlingAnimationUtils mFlingAnimationUtils;
    private final PipTouchGesture[] mGestures;
    private final PIPMotionHelper mMotionHelper;
    // Temp vars
    private final Rect mTmpBounds = new Rect();

    /**
     * A listener for the PIP menu activity.
     */
    private class PipMenuListener implements PIPFloatingLayout.IPIPListener {
        @Override
        public void onPIPStateChanged(PIPStates oldPIPState, PIPStates pipState) {
            switch (pipState) {
                case CUSTOM_EXPANDED:
                    if (!mIsMinimized) {
                        mMotionHelper.expandPip();
                    }
                    break;
                case CUSTOM_COMPACT:
                    setMinimizedStateInternal(true);
                    mMotionHelper.animateToClosestMinimizedState(mMovementBounds, null /* updateListener */);
                    break;
                case NOT_STARTED:
                    mMotionHelper.dismissPip();

            }
        }
    }

    public PipTouchHandler(PIPFloatingLayout floatingLayout) {
        // Initialize the Pip input consumer
        mFloatingLayout = floatingLayout;
        mAccessibilityManager = floatingLayout.getContext().getSystemService(AccessibilityManager.class);
        mViewConfig = ViewConfiguration.get(floatingLayout.getContext());
        mDismissViewController = new PipDismissViewController(floatingLayout.getContext());
        mSnapAlgorithm = new PipSnapAlgorithm(floatingLayout.getContext());
        mFlingAnimationUtils = new FlingAnimationUtils(floatingLayout.getContext(), 2.5f);

        PipTouchGesture mDefaultMovementGesture = new PipTouchGesture() {
            // Whether the PiP was on the left side of the screen at the start of the gesture
            private boolean mStartedOnLeft;
            private final Point mStartPosition = new Point();
            private final PointF mDelta = new PointF();

            @Override
            public void onDown(PipTouchState touchState) {
                if (!touchState.isUserInteracting()) {
                    return;
                }
                Rect bounds = mMotionHelper.getBounds();
                mDelta.set(0f, 0f);
                mStartPosition.set(bounds.left, bounds.top);
                mStartedOnLeft = bounds.left < mMovementBounds.centerX();
                mMovementWithinMinimize = true;
                mMovementWithinDismiss = touchState.getDownTouchPosition().y >= mMovementBounds.bottom;
                if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                    mDismissViewController.createDismissTarget();
                    mHandler.postDelayed(mShowDismissAffordance, SHOW_DISMISS_AFFORDANCE_DELAY);
                }
            }

            @Override
            boolean onMove(PipTouchState touchState) {
                if (!touchState.isUserInteracting()) {
                    return false;
                }
                if (touchState.startedDragging()) {
                    mSavedSnapFraction = -1f;
                    if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                        mHandler.removeCallbacks(mShowDismissAffordance);
                        mDismissViewController.showDismissTarget();
                    }
                }
                if (touchState.isDragging()) {
                    // Move the pinned stack freely
                    final PointF lastDelta = touchState.getLastTouchDelta();
                    float lastX = mStartPosition.x + mDelta.x;
                    float lastY = mStartPosition.y + mDelta.y;
                    float left = lastX + lastDelta.x;
                    float top = lastY + lastDelta.y;
                    if (!touchState.allowDraggingOffscreen() || !ENABLE_MINIMIZE) {
                        left = Math.max(mMovementBounds.left, Math.min(mMovementBounds.right, left));
                    }
                    if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                        // Allow pip to move past bottom bounds
                        top = Math.max(mMovementBounds.top, top);
                    } else {
                        top = Math.max(mMovementBounds.top, Math.min(mMovementBounds.bottom, top));
                    }
                    // Add to the cumulative delta after bounding the position
                    mDelta.x += left - lastX;
                    mDelta.y += top - lastY;
                    mTmpBounds.set(mMotionHelper.getBounds());
                    mTmpBounds.offsetTo((int) left, (int) top);
                    mMotionHelper.movePip(mTmpBounds);
                    if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                        updateDismissFraction();
                    }
                    final PointF curPos = touchState.getLastTouchPosition();
                    if (mMovementWithinMinimize) {
                        // Track if movement remains near starting edge to identify swipes to minimize
                        mMovementWithinMinimize = mStartedOnLeft
                                ? curPos.x <= mMovementBounds.left + mTmpBounds.width()
                                : curPos.x >= mMovementBounds.right;
                    }
                    if (mMovementWithinDismiss) {
                        // Track if movement remains near the bottom edge to identify swipe to dismiss
                        mMovementWithinDismiss = curPos.y >= mMovementBounds.bottom;
                    }
                    return true;
                }
                return false;
            }

            @Override
            public boolean onUp(PipTouchState touchState) {
                if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                    // Clean up the dismiss target regardless of the touch state in case the touch
                    // enabled state changes while the user is interacting
                    cleanUpDismissTarget();
                }
                if (!touchState.isUserInteracting()) {
                    return false;
                }
                final PointF vel = touchState.getVelocity();
                final boolean isHorizontal = Math.abs(vel.x) > Math.abs(vel.y);
                final float velocity = PointF.length(vel.x, vel.y);
                final boolean isFling = velocity > mFlingAnimationUtils.getMinVelocityPxPerSecond();
                final boolean isUpWithinDimiss = ENABLE_FLING_DISMISS
                        && touchState.getLastTouchPosition().y >= mMovementBounds.bottom
                        && mMotionHelper.isGestureToDismissArea(mMotionHelper.getBounds(), vel.x,
                        vel.y, isFling);
                final boolean isFlingToBot = isFling && vel.y > 0 && !isHorizontal
                        && (mMovementWithinDismiss || isUpWithinDimiss);
                if (ENABLE_DISMISS_DRAG_TO_EDGE) {
                    // Check if the user dragged or flung the PiP offscreen to dismiss it
                    if (mMotionHelper.shouldDismissPip() || isFlingToBot) {
                        mMotionHelper.animateDismiss(mMotionHelper.getBounds(), vel.x,
                                vel.y, mUpdateScrimListener);
                        return true;
                    }
                }
                if (touchState.isDragging()) {
                    final boolean isFlingToEdge = isFling && isHorizontal && mMovementWithinMinimize
                            && (mStartedOnLeft ? vel.x < 0 : vel.x > 0);
                    if (ENABLE_MINIMIZE &&
                            !mIsMinimized && (mMotionHelper.shouldMinimizePip() || isFlingToEdge)) {
                        // Pip should be minimized
                        setMinimizedStateInternal(true);
                        if (mMenuState == MENU_STATE_FULL) {
                            // If the user dragged the expanded PiP to the edge, then hiding the menu
                            // will trigger the PiP to be scaled back to the normal size with the
                            // minimize offset adjusted
                            mMenuController.hideMenu();
                        } else {
                            mMotionHelper.animateToClosestMinimizedState(mMovementBounds,
                                    mUpdateScrimListener);
                        }
                        return true;
                    }
                    if (mIsMinimized) {
                        // If we're dragging and it wasn't a minimize gesture then we shouldn't be
                        // minimized.
                        setMinimizedStateInternal(false);
                    }
                    AnimatorListenerAdapter postAnimationCallback = null;

                    if (isFling) {
                        mMotionHelper.flingToSnapTarget(velocity, vel.x, vel.y, mMovementBounds,
                                mUpdateScrimListener, postAnimationCallback,
                                mStartPosition);
                    } else {
                        mMotionHelper.animateToClosestSnapTarget(mMovementBounds, mUpdateScrimListener,
                                postAnimationCallback);
                    }
                } else if (mIsMinimized) {
                    // This was a tap, so no longer minimized
                    mMotionHelper.animateToClosestSnapTarget(mMovementBounds, null /* updateListener */,
                            null /* animatorListener */);
                    setMinimizedStateInternal(false);
                } else if (mMenuState != MENU_STATE_FULL) {
                    if (mTouchState.isDoubleTap()) {
                        // Expand to fullscreen if this is a double tap
                        mMotionHelper.expandPip();
                    } else {
                        // Next touch event _may_ be the second tap for the double-tap, schedule a
                        // fallback runnable to trigger the menu if no touch event occurs before the
                        // next tap
                        mTouchState.scheduleDoubleTapTimeoutCallback();
                    }
                } else {
                    mMotionHelper.expandPip();
                }
                return true;
            }
        };
        mGestures = new PipTouchGesture[]{
                mDefaultMovementGesture
        };
        mMotionHelper = new PIPMotionHelper(floatingLayout,
                mSnapAlgorithm, mFlingAnimationUtils);
        mTouchState = new PipTouchState(mViewConfig, mHandler,
                () -> mMenuController.showMenu(MENU_STATE_FULL, mMotionHelper.getBounds(),
                        mMovementBounds, true /* allowMenuTimeout */, willResizeMenu()));
        Resources res = context.getResources();
        mImeOffset = res.getDimensionPixelSize(R.dimen.pip_ime_offset);
    }

    public void setTouchEnabled(boolean enabled) {
        mTouchState.setAllowTouches(enabled);
    }


    public void onActivityPinned() {
        cleanUp();
        mShowPipMenuOnAnimationEnd = true;
    }

    public void onActivityUnpinned(ComponentName topPipActivity) {
        if (topPipActivity == null) {
            // Clean up state after the last PiP activity is removed
            cleanUp();
        }
    }


    public void onImeVisibilityChanged(boolean imeVisible, int imeHeight) {
        mIsImeShowing = imeVisible;
        mImeHeight = imeHeight;
    }

    public void onMovementBoundsChanged(Rect insetBounds, Rect normalBounds, Rect animatingBounds,
                                        boolean fromImeAdjustement, int displayRotation, Size expandedSize) {
        // Re-calculate the expanded bounds
        mNormalBounds = normalBounds;
        Rect normalMovementBounds = new Rect();
        mSnapAlgorithm.getMovementBounds(mNormalBounds, insetBounds, normalMovementBounds,
                mIsImeShowing ? mImeHeight : 0);
        // Calculate the expanded size
        float aspectRatio = (float) normalBounds.width() / normalBounds.height();
        Point displaySize = new Point();
        mFloatingLayout.getRealSize(displaySize);
        mExpandedBounds.set(0, 0, expandedSize.getWidth(), expandedSize.getHeight());
        Rect expandedMovementBounds = new Rect();
        mSnapAlgorithm.getMovementBounds(mExpandedBounds, insetBounds, expandedMovementBounds,
                mIsImeShowing ? mImeHeight : 0);
        // If this is from an IME adjustment, then we should move the PiP so that it is not occluded
        // by the IME
        if (fromImeAdjustement) {
            if (mTouchState.isUserInteracting()) {
                // Defer the update of the current movement bounds until after the user finishes
                // touching the screen
            } else {
                final Rect bounds = new Rect(animatingBounds);
                final Rect toMovementBounds = mPIPState == PIPStates.CUSTOM_EXPANDED
                        ? expandedMovementBounds
                        : normalMovementBounds;
                if (mIsImeShowing) {
                    // IME visible, apply the IME offset if the space allows for it
                    final int imeOffset = toMovementBounds.bottom - Math.max(toMovementBounds.top,
                            toMovementBounds.bottom - mImeOffset);
                    if (bounds.top == mMovementBounds.bottom) {
                        // If the PIP is currently resting on top of the IME, then adjust it with
                        // the showing IME
                        bounds.offsetTo(bounds.left, toMovementBounds.bottom - imeOffset);
                    } else {
                        bounds.offset(0, Math.min(0, toMovementBounds.bottom - imeOffset
                                - bounds.top));
                    }
                } else {
                    // IME hidden
                    if (bounds.top >= (mMovementBounds.bottom - mImeOffset)) {
                        // If the PIP is resting on top of the IME, then adjust it with the hiding
                        // IME
                        bounds.offsetTo(bounds.left, toMovementBounds.bottom);
                    }
                }
                mMotionHelper.animateToIMEOffset(bounds);
            }
        }
        // Update the movement bounds after doing the calculations based on the old movement bounds
        // above
        mNormalMovementBounds = normalMovementBounds;
        mExpandedMovementBounds = expandedMovementBounds;
        mInsetBounds.set(insetBounds);
        updateMovementBounds(mMenuState);
        // If we have a deferred resize, apply it now
        if (mDeferResizeToNormalBoundsUntilRotation == displayRotation) {
            mMotionHelper.animateToUnexpandedState(normalBounds, mSavedSnapFraction,
                    mNormalMovementBounds, mMovementBounds, mIsMinimized,
                    true /* immediate */);
            mSavedSnapFraction = -1f;
            mDeferResizeToNormalBoundsUntilRotation = -1;
        }
    }


    private boolean handleTouchEvent(MotionEvent ev) {

        // Update the touch state
        mTouchState.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mMotionHelper.synchronizePinnedStackBounds();
                for (PipTouchGesture gesture : mGestures) {
                    gesture.onDown(mTouchState);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (PipTouchGesture gesture : mGestures) {
                    if (gesture.onMove(mTouchState)) {
                        break;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                // Update the movement bounds again if the state has changed since the user started
                // dragging (ie. when the IME shows)
                updateMovementBounds(mMenuState);
                for (PipTouchGesture gesture : mGestures) {
                    if (gesture.onUp(mTouchState)) {
                        break;
                    }
                }
                // Fall through to clean up
            }
            case MotionEvent.ACTION_CANCEL: {
                mTouchState.reset();
                break;
            }
        }
        return mMenuState == MENU_STATE_NONE;
    }


    /**
     * Sets the minimized state.
     */
    private void setMinimizedStateInternal(boolean isMinimized) {
        if (!ENABLE_MINIMIZE) {
            return;
        }
        setMinimizedState(isMinimized, false /* fromController */);
    }

    /**
     * Sets the minimized state.
     */
    void setMinimizedState(boolean isMinimized, boolean fromController) {
        if (!ENABLE_MINIMIZE) {
            return;
        }
        mIsMinimized = isMinimized;
        mSnapAlgorithm.setMinimized(isMinimized);
        if (fromController) {
            if (isMinimized) {
                // Move the PiP to the new bounds immediately if minimized
                mMotionHelper.movePip(mMotionHelper.getClosestMinimizedBounds(mNormalBounds,
                        mMovementBounds));
            }
        }
    }


    /**
     * @return the motion helper.
     */
    public PIPMotionHelper getMotionHelper() {
        return mMotionHelper;
    }

    /**
     * Updates the current movement bounds based on whether the menu is currently visible.
     */
    private void updateMovementBounds(PIPStates pipStates) {
        boolean isMenuExpanded = pipStates == PIPStates.CUSTOM_EXPANDED;
        mMovementBounds = isMenuExpanded
                ? mExpandedMovementBounds
                : mNormalMovementBounds;
    }

    /**
     * Removes the dismiss target and cancels any pending callbacks to show it.
     */
    private void cleanUpDismissTarget() {
        mHandler.removeCallbacks(mShowDismissAffordance);
        mDismissViewController.destroyDismissTarget();
    }

    /**
     * Resets some states related to the touch handling.
     */
    private void cleanUp() {
        if (mIsMinimized) {
            setMinimizedStateInternal(false);
        }
        cleanUpDismissTarget();
    }

    /**
     * @return whether the menu will resize as a part of showing the full menu.
     */
    private boolean willResizeMenu() {
        return mExpandedBounds.width() != mNormalBounds.width() ||
                mExpandedBounds.height() != mNormalBounds.height();
    }

    public void dump(PrintWriter pw, String prefix) {
        final String innerPrefix = prefix + "  ";
        pw.println(prefix + TAG);
        pw.println(innerPrefix + "mMovementBounds=" + mMovementBounds);
        pw.println(innerPrefix + "mNormalBounds=" + mNormalBounds);
        pw.println(innerPrefix + "mNormalMovementBounds=" + mNormalMovementBounds);
        pw.println(innerPrefix + "mExpandedBounds=" + mExpandedBounds);
        pw.println(innerPrefix + "mExpandedMovementBounds=" + mExpandedMovementBounds);
        pw.println(innerPrefix + "mPIPState=" + mPIPState);
        pw.println(innerPrefix + "mIsMinimized=" + mIsMinimized);
        pw.println(innerPrefix + "mIsImeShowing=" + mIsImeShowing);
        pw.println(innerPrefix + "mImeHeight=" + mImeHeight);
        pw.println(innerPrefix + "mSavedSnapFraction=" + mSavedSnapFraction);
        pw.println(innerPrefix + "mEnableDragToEdgeDismiss=" + ENABLE_DISMISS_DRAG_TO_EDGE);
        pw.println(innerPrefix + "mEnableMinimize=" + ENABLE_MINIMIZE);
        mSnapAlgorithm.dump(pw, innerPrefix);
        mTouchState.dump(pw, innerPrefix);
        mMotionHelper.dump(pw, innerPrefix);
    }
}