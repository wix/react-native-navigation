package com.reactnativenavigation.views.pip;


/**
 * A generic interface for a touch gesture.
 */
public abstract class PipTouchGesture {
    /**
     * Handle the touch down.
     */
    void onDown(PipTouchState touchState) {}
    /**
     * Handle the touch move, and return whether the event was consumed.
     */
    boolean onMove(PipTouchState touchState) {
        return false;
    }
    /**
     * Handle the touch up, and return whether the gesture was consumed.
     */
    boolean onUp(PipTouchState touchState) {
        return false;
    }
}
