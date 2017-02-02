package com.reactnativenavigation.views.collapsingToolbar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import com.reactnativenavigation.NavigationApplication;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.CollapseTitleBarBehaviour;
import com.reactnativenavigation.views.collapsingToolbar.behaviours.TitleBarHideOnScrollBehaviour;

public class CollapseCalculator {
    public enum Direction {
        Up, Down, None
    }

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
    private GestureDetector flingDetector;
    private OnFlingListener flingListener;
    private int scrollY = 0;
    private int totalCollapse = 0;
    private final int scaledTouchSlop;

    public CollapseCalculator(final CollapsingView collapsingView, CollapseBehaviour collapseBehaviour) {
        this.view = collapsingView;
        this.collapseBehaviour = collapseBehaviour;
        scaledTouchSlop = ViewConfiguration.get(NavigationApplication.instance).getScaledTouchSlop();
        setFlingDetector();
    }

    private void setFlingDetector() {
        if (collapseBehaviour.shouldCollapseOnFling()) {
            flingDetector =
                    new GestureDetector(NavigationApplication.instance, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            final Direction direction = getScrollDirection(e1, e2);
                            if (canCollapse(direction) && totalCollapse != 0) {
                                flingListener.onFling(new CollapseAmount(direction));
                                return true;
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

    void setFlingListener(OnFlingListener flingListener) {
        this.flingListener = flingListener;
    }

    @NonNull
    CollapseAmount calculate(MotionEvent event) {
        updateInitialTouchY(event);
        CollapseAmount touchUpCollapse = shouldCollapseOnTouchUp(event);
        if (touchUpCollapse != CollapseAmount.None) {
            previousTouchEvent = MotionEvent.obtain(event);
            return touchUpCollapse;
        }

        if (!isMoveEvent(event)) {
            previousTouchEvent = MotionEvent.obtain(event);
            return CollapseAmount.None;
        }

        final Direction direction = calculateScrollDirection(event.getRawY());
        if (shouldCollapseAfterMoveEvent(direction)) {
            return calculateCollapse(event);
        } else {
            previousCollapseY = -1;
            previousTouchEvent = MotionEvent.obtain(event);
            return CollapseAmount.None;
        }
    }

    private CollapseAmount shouldCollapseOnTouchUp(MotionEvent event) {
        if ((collapseBehaviour.shouldCollapseOnTouchUp())
            && !flingDetector.onTouchEvent(event) && isTouchUp(event)) {
            final float visibilityPercentage = view.getCurrentCollapseValue() / view.getFinalCollapseValue();
            Direction direction = visibilityPercentage >= 0.5f ? Direction.Up : Direction.Down;
            if (canCollapse(direction) && totalCollapse != 0) {
                return new CollapseAmount(direction);
            }
        }
        return CollapseAmount.None;
    }

    private boolean shouldCollapseAfterMoveEvent(Direction direction) {
        if (collapseBehaviour instanceof TitleBarHideOnScrollBehaviour && !isScrolling()) {
            return false;
        }
        return canCollapse(direction);
    }

    private boolean canCollapse(Direction direction) {
        if (view == null) {
            return false;
        }
        checkCollapseLimits();
        return (isNotCollapsedOrExpended() ||
               (canCollapse && isExpendedAndScrollingUp(direction)) ||
               (canExpend && isCollapsedAndScrollingDown(direction)));
    }

    private boolean isScrolling() {
        final int currentScrollY = scrollView.getScrollY();
        final boolean isScrolling = currentScrollY != scrollY;
        scrollY = currentScrollY;
        return isScrolling;
    }

    private Direction calculateScrollDirection(float y) {
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
               (scrollView.getScrollY() == 0 || (collapseBehaviour instanceof TitleBarHideOnScrollBehaviour || collapseBehaviour instanceof CollapseTitleBarBehaviour));
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
        totalCollapse += collapse;
        previousCollapseY = y;
        previousTouchEvent = MotionEvent.obtain(event);
        return Math.abs(totalCollapse) < scaledTouchSlop ? CollapseAmount.None : new CollapseAmount(collapse);
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
        totalCollapse = 0;
        touchDownY = event.getRawY();
        scrollY = scrollView.getScrollY();
        previousCollapseY = touchDownY;
    }

    private void clearInitialTouchY() {
        previousCollapseY = -1;
        collapse = 0;
    }
}
