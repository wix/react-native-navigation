package com.reactnativenavigation.views.pip;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.reactnativenavigation.views.touch.OnSwipeTouchListener;

public class PIPOnSwipeTouchListener extends OnSwipeTouchListener {
    private Activity activity;

    public PIPOnSwipeTouchListener(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public void onSwipeTop() {
        Toast.makeText(activity, "top", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeRight() {
        Toast.makeText(activity, "right", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeLeft() {
        Toast.makeText(activity, "left", Toast.LENGTH_SHORT).show();
    }

    public void onSwipeBottom() {
        Toast.makeText(activity, "bottom", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(activity, "onTouch", Toast.LENGTH_SHORT).show();
        return super.onTouch(v, event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(activity, "onDown", Toast.LENGTH_SHORT).show();
        return super.onDown(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(activity, "onDoubleTap", Toast.LENGTH_SHORT).show();
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Toast.makeText(activity, "onDoubleTapEvent", Toast.LENGTH_SHORT).show();
        return super.onDoubleTapEvent(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toast.makeText(activity, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show();
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(activity, "onSingleTapUp", Toast.LENGTH_SHORT).show();
        return super.onSingleTapUp(e);
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
        Toast.makeText(activity, "onLongPress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Toast.makeText(activity, "onScroll", Toast.LENGTH_SHORT).show();
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
        Toast.makeText(activity, "onShowPress", Toast.LENGTH_SHORT).show();
    }
}
