package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CollapseCalculator {
    private float collapse;
    private MotionEvent previousTouchEvent;
    private float touchDownY = -1;
    private float previousCollapseY = -1;
    private boolean isExpended;
    private boolean isCollapsed = true;
    private boolean canCollapse = true;
    private boolean canExpend = false;
    private CollapsingView view;
    protected ScrollView scrollView;

    public CollapseCalculator(final CollapsingView collapsingView) {
        this.view = collapsingView;
    }

    void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @NonNull
    CollapseAmount calculate(MotionEvent event) {
        updateInitialTouchY(event);
        if (!isMoveEvent(event)) {
            return CollapseAmount.None;
        }

        if (shouldCollapse(event)) {
            return calculateCollapse(event);
        } else {
            previousCollapseY = -1;
            previousTouchEvent = MotionEvent.obtain(event);
            return CollapseAmount.None;
        }
    }

    private boolean shouldCollapse(MotionEvent event) {
        checkCollapseLimits();
        ScrollDirection.Direction direction = getScrollDirection(event.getRawY());
        return isNotCollapsedOrExpended() ||
                (canCollapse && isExpendedAndScrollingUp(direction)) ||
                (canExpend && isCollapsedAndScrollingDown(direction));
    }

    private ScrollDirection.Direction getScrollDirection(float y) {
        if (y == (previousCollapseY == -1 ? touchDownY : previousCollapseY)) {
            return ScrollDirection.Direction.None;
        }
        if (previousTouchEvent == null) {
            return ScrollDirection.Direction.None;
        }
        return y < previousTouchEvent.getRawY() ?
                ScrollDirection.Direction.Up :
                ScrollDirection.Direction.Down;
    }

    private void checkCollapseLimits() {
        float currentCollapse = view.getCurrentCollapseValue();
        float finalExpendedTranslation = 0;
        isExpended = isExpended(currentCollapse, finalExpendedTranslation);
        isCollapsed = isCollapsed(currentCollapse, view.getFinalCollapseValue());
        canCollapse = calculateCanCollapse(currentCollapse, finalExpendedTranslation, view.getFinalCollapseValue());
        canExpend = calculateCanExpend(currentCollapse, finalExpendedTranslation, view.getFinalCollapseValue());
    }

    private boolean calculateCanCollapse(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation > finalCollapsedTranslation &&
               currentTopBarTranslation <= finalExpendedTranslation;
    }

    private boolean calculateCanExpend(float currentTopBarTranslation, float finalExpendedTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation >= finalCollapsedTranslation &&
               currentTopBarTranslation < finalExpendedTranslation &&
               scrollView.getScrollY() == 0;
    }

    private boolean isCollapsedAndScrollingDown(ScrollDirection.Direction direction) {
        return isCollapsed && direction == ScrollDirection.Direction.Down;
    }

    private boolean isExpendedAndScrollingUp(ScrollDirection.Direction direction) {
        return isExpended && direction == ScrollDirection.Direction.Up;
    }

    private  boolean isNotCollapsedOrExpended() {
        return canExpend && canCollapse;
    }

    private boolean isCollapsed(float currentTopBarTranslation, float finalCollapsedTranslation) {
        return currentTopBarTranslation == finalCollapsedTranslation;
    }

    private boolean isExpended(float currentTopBarTranslation, float finalExpendedTranslation) {
        return currentTopBarTranslation == finalExpendedTranslation;
    }

    private CollapseAmount calculateCollapse(MotionEvent event) {
        float y = event.getRawY();
        if (previousCollapseY == -1) {
            previousCollapseY = y;
        }
        collapse = calculateCollapse(y);
        previousCollapseY = y;
        previousTouchEvent = MotionEvent.obtain(event);
        return new CollapseAmount(collapse);
    }

    private float calculateCollapse(float y) {
        float translation = y - previousCollapseY + view.getCurrentCollapseValue();
        if (translation < view.getFinalCollapseValue()) {
            translation = view.getFinalCollapseValue();
        }
        final float expendedTranslation = 0;
        if (translation > expendedTranslation) {
            translation = expendedTranslation;
        }
        return translation;
    }


    private void updateInitialTouchY(MotionEvent event) {
        if (isTouchDown(previousTouchEvent) && isMoveEvent(event)) {
            saveInitialTouchY(previousTouchEvent);
        } else if (isTouchUp(event) && isMoveEvent(previousTouchEvent)) {
            clearInitialTouchY();
        }
    }

    private boolean isMoveEvent(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_MOVE;
    }

    private boolean isTouchDown(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_DOWN;
    }

    private boolean isTouchUp(@Nullable MotionEvent event) {
        return event != null && event.getActionMasked() == MotionEvent.ACTION_UP;
    }

    private void saveInitialTouchY(MotionEvent event) {
        touchDownY = event.getRawY();
        previousCollapseY = touchDownY;
    }

    private void clearInitialTouchY() {
        touchDownY = -1;
        previousCollapseY = -1;
        collapse = 0;
    }
}
