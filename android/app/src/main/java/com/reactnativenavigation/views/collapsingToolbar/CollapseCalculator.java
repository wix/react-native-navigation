package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.ScrollDirection.Direction;

import static com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour.CollapseTogether;
import static com.reactnativenavigation.params.CollapsingTopBarParams.CollapseBehaviour.CollapseTopBarFirst;

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
    private CollapseBehaviour collapseBehaviour;
    protected ScrollView scrollView;
    private int scrollY = -1;
    private GestureDetector flingDetector;
    private ScrollListener flingListener;

    public CollapseCalculator(final CollapsingView collapsingView, CollapseBehaviour collapseBehaviour) {
        this.view = collapsingView;
        this.collapseBehaviour = collapseBehaviour;
        setFlingDetector(collapseBehaviour);
    }

    private void setFlingDetector(CollapseBehaviour collapseBehaviour) {
        if (collapseBehaviour == CollapseTogether) {
            flingDetector =
                    new GestureDetector(NavigationApplication.instance, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            final Direction direction = getScrollDirection(e1, e2);
                            if (canCollapse(direction)) {
                                flingListener.onFling(direction);
                            }
                            return false;
                        }

                        private Direction getScrollDirection(MotionEvent e1, MotionEvent e2) {
                            if (e1.getRawY() == e2.getRawY()) {
                                return Direction.None;
                            }
                            return e1.getRawY() - e2.getRawY() > 0 ? Direction.Up : Direction.Down;
                        }
                    });
        }
    }

    void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    void setFlingListener(ScrollListener flingListener) {
        this.flingListener = flingListener;
    }

    @NonNull
    CollapseAmount calculate(MotionEvent event) {
        updateInitialTouchY(event);
        if (collapseBehaviour == CollapseTogether) {
            flingDetector.onTouchEvent(event);
        }
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
        return canCollapse(getScrollDirection(event.getRawY()));
    }

    private boolean canCollapse(Direction direction) {
        checkCollapseLimits();
        return ((collapseBehaviour == CollapseTopBarFirst) || isScrolling()) &&
               isNotCollapsedOrExpended() ||
               (canCollapse && isExpendedAndScrollingUp(direction)) ||
               (canExpend && isCollapsedAndScrollingDown(direction));
    }

    private boolean isScrolling() {
        final int currentScrollY = scrollView.getScrollY();
        final boolean isScrolling = currentScrollY != scrollY;
        scrollY = currentScrollY;
        return isScrolling;
    }

    private Direction getScrollDirection(float y) {
        if (y == (previousCollapseY == -1 ? touchDownY : previousCollapseY)) {
            return Direction.None;
        }
        if (previousTouchEvent == null) {
            return Direction.None;
        }
        return y < previousTouchEvent.getRawY() ?
                Direction.Up :
                Direction.Down;
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
               (scrollView.getScrollY() == 0 || collapseBehaviour == CollapseBehaviour.CollapseTogether);
    }

    private boolean isCollapsedAndScrollingDown(Direction direction) {
        return isCollapsed && direction == Direction.Down;
    }

    private boolean isExpendedAndScrollingUp(Direction direction) {
        return isExpended && direction == Direction.Up;
    }

    private boolean isNotCollapsedOrExpended() {
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
