package com.reactnativenavigation.views.pip;

/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.graphics.PointF;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import java.io.PrintWriter;

/**
 * This keeps track of the touch state throughout the current touch gesture.
 */
public class PipTouchState {
    private static final String TAG = "PipTouchHandler";
    private static final boolean DEBUG = false;

    static final long DOUBLE_TAP_TIMEOUT = 200;
    private final Handler mHandler;
    private final ViewConfiguration mViewConfig;
    private final Runnable mDoubleTapTimeoutCallback;
    private VelocityTracker mVelocityTracker;
    private long mDownTouchTime = 0;
    private long mLastDownTouchTime = 0;
    private long mUpTouchTime = 0;
    private final PointF mDownTouch = new PointF();
    private final PointF mDownDelta = new PointF();
    private final PointF mLastTouch = new PointF();
    private final PointF mLastDelta = new PointF();
    private final PointF mVelocity = new PointF();
    private boolean mAllowTouches = true;
    private boolean mIsUserInteracting = false;
    // Set to true only if the multiple taps occur within the double tap timeout
    private boolean mIsDoubleTap = false;
    // Set to true only if a gesture
    private boolean mIsWaitingForDoubleTap = false;
    private boolean mIsDragging = false;
    // The previous gesture was a drag
    private boolean mPreviouslyDragging = false;
    private boolean mStartedDragging = false;
    private boolean mAllowDraggingOffscreen = false;
    private int mActivePointerId;

    public PipTouchState(ViewConfiguration viewConfig, Handler handler,
                         Runnable doubleTapTimeoutCallback) {
        mViewConfig = viewConfig;
        mHandler = handler;
        mDoubleTapTimeoutCallback = doubleTapTimeoutCallback;
    }

    /**
     * Resets this state.
     */
    public void reset() {
        mAllowDraggingOffscreen = false;
        mIsDragging = false;
        mStartedDragging = false;
        mIsUserInteracting = false;
    }

    /**
     * Processes a given touch event and updates the state.
     */
    public void onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (!mAllowTouches) {
                    return;
                }
                // Initialize the velocity tracker
                initOrResetVelocityTracker();
                mActivePointerId = ev.getPointerId(0);
                if (DEBUG) {
                    Log.e(TAG, "Setting active pointer id on DOWN: " + mActivePointerId);
                }
                mLastTouch.set(ev.getX(), ev.getY());
                mDownTouch.set(mLastTouch);
                mAllowDraggingOffscreen = true;
                mIsUserInteracting = true;
                mDownTouchTime = ev.getEventTime();
                mIsDoubleTap = !mPreviouslyDragging &&
                        (mDownTouchTime - mLastDownTouchTime) < DOUBLE_TAP_TIMEOUT;
                mIsWaitingForDoubleTap = false;
                mLastDownTouchTime = mDownTouchTime;
                if (mDoubleTapTimeoutCallback != null) {
                    mHandler.removeCallbacks(mDoubleTapTimeoutCallback);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Skip event if we did not start processing this touch gesture
                if (!mIsUserInteracting) {
                    break;
                }
                // Update the velocity tracker
                mVelocityTracker.addMovement(ev);
                int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid active pointer id on MOVE: " + mActivePointerId);
                    break;
                }
                float x = ev.getX(pointerIndex);
                float y = ev.getY(pointerIndex);
                mLastDelta.set(x - mLastTouch.x, y - mLastTouch.y);
                mDownDelta.set(x - mDownTouch.x, y - mDownTouch.y);
                boolean hasMovedBeyondTap = mDownDelta.length() > mViewConfig.getScaledTouchSlop();
                if (!mIsDragging) {
                    if (hasMovedBeyondTap) {
                        mIsDragging = true;
                        mStartedDragging = true;
                    }
                } else {
                    mStartedDragging = false;
                }
                mLastTouch.set(x, y);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                // Skip event if we did not start processing this touch gesture
                if (!mIsUserInteracting) {
                    break;
                }
                // Update the velocity tracker
                mVelocityTracker.addMovement(ev);
                int pointerIndex = ev.getActionIndex();
                int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // Select a new active pointer id and reset the movement state
                    final int newPointerIndex = (pointerIndex == 0) ? 1 : 0;
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                    if (DEBUG) {
                        Log.e(TAG, "Relinquish active pointer id on POINTER_UP: " +
                                mActivePointerId);
                    }
                    mLastTouch.set(ev.getX(newPointerIndex), ev.getY(newPointerIndex));
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                // Skip event if we did not start processing this touch gesture
                if (!mIsUserInteracting) {
                    break;
                }
                // Update the velocity tracker
                mVelocityTracker.addMovement(ev);
                mVelocityTracker.computeCurrentVelocity(1000,
                        mViewConfig.getScaledMaximumFlingVelocity());
                mVelocity.set(mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity());
                int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid active pointer id on UP: " + mActivePointerId);
                    break;
                }
                mUpTouchTime = ev.getEventTime();
                mLastTouch.set(ev.getX(pointerIndex), ev.getY(pointerIndex));
                mPreviouslyDragging = mIsDragging;
                mIsWaitingForDoubleTap = !mIsDoubleTap && !mIsDragging &&
                        (mUpTouchTime - mDownTouchTime) < DOUBLE_TAP_TIMEOUT;
                // Fall through to clean up
            }
            case MotionEvent.ACTION_CANCEL: {
                recycleVelocityTracker();
                break;
            }
        }
    }

    /**
     * @return the velocity of the active touch pointer at the point it is lifted off the screen.
     */
    public PointF getVelocity() {
        return mVelocity;
    }

    /**
     * @return the last touch position of the active pointer.
     */
    public PointF getLastTouchPosition() {
        return mLastTouch;
    }

    /**
     * @return the movement delta between the last handled touch event and the previous touch
     * position.
     */
    public PointF getLastTouchDelta() {
        return mLastDelta;
    }

    /**
     * @return the down touch position.
     */
    public PointF getDownTouchPosition() {
        return mDownTouch;
    }

    /**
     * @return the movement delta between the last handled touch event and the down touch
     * position.
     */
    public PointF getDownTouchDelta() {
        return mDownDelta;
    }

    /**
     * @return whether the user has started dragging.
     */
    public boolean isDragging() {
        return mIsDragging;
    }

    /**
     * @return whether the user is currently interacting with the PiP.
     */
    public boolean isUserInteracting() {
        return mIsUserInteracting;
    }

    /**
     * @return whether the user has started dragging just in the last handled touch event.
     */
    public boolean startedDragging() {
        return mStartedDragging;
    }

    /**
     * Sets whether touching is currently allowed.
     */
    public void setAllowTouches(boolean allowTouches) {
        mAllowTouches = allowTouches;
        // If the user happens to touch down before this is sent from the system during a transition
        // then block any additional handling by resetting the state now
        if (mIsUserInteracting) {
            reset();
        }
    }

    /**
     * Disallows dragging offscreen for the duration of the current gesture.
     */
    public void setDisallowDraggingOffscreen() {
        mAllowDraggingOffscreen = false;
    }

    /**
     * @return whether dragging offscreen is allowed during this gesture.
     */
    public boolean allowDraggingOffscreen() {
        return mAllowDraggingOffscreen;
    }

    /**
     * @return whether this gesture is a double-tap.
     */
    public boolean isDoubleTap() {
        return mIsDoubleTap;
    }

    /**
     * @return whether this gesture will potentially lead to a following double-tap.
     */
    public boolean isWaitingForDoubleTap() {
        return mIsWaitingForDoubleTap;
    }

    /**
     * Schedules the callback to run if the next double tap does not occur.  Only runs if
     * isWaitingForDoubleTap() is true.
     */
    public void scheduleDoubleTapTimeoutCallback() {
        if (mIsWaitingForDoubleTap) {
            long delay = getDoubleTapTimeoutCallbackDelay();
            mHandler.removeCallbacks(mDoubleTapTimeoutCallback);
            mHandler.postDelayed(mDoubleTapTimeoutCallback, delay);
        }
    }


    long getDoubleTapTimeoutCallbackDelay() {
        if (mIsWaitingForDoubleTap) {
            return Math.max(0, DOUBLE_TAP_TIMEOUT - (mUpTouchTime - mDownTouchTime));
        }
        return -1;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public void dump(PrintWriter pw, String prefix) {
        final String innerPrefix = prefix + "  ";
        pw.println(prefix + TAG);
        pw.println(innerPrefix + "mAllowTouches=" + mAllowTouches);
        pw.println(innerPrefix + "mActivePointerId=" + mActivePointerId);
        pw.println(innerPrefix + "mDownTouch=" + mDownTouch);
        pw.println(innerPrefix + "mDownDelta=" + mDownDelta);
        pw.println(innerPrefix + "mLastTouch=" + mLastTouch);
        pw.println(innerPrefix + "mLastDelta=" + mLastDelta);
        pw.println(innerPrefix + "mVelocity=" + mVelocity);
        pw.println(innerPrefix + "mIsUserInteracting=" + mIsUserInteracting);
        pw.println(innerPrefix + "mIsDragging=" + mIsDragging);
        pw.println(innerPrefix + "mStartedDragging=" + mStartedDragging);
        pw.println(innerPrefix + "mAllowDraggingOffscreen=" + mAllowDraggingOffscreen);
    }
}