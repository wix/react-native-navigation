package com.reactnativenavigation.views.sharedElementTransition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

class SharedElementAnimatorCreator {
    private interface OrderedAnimationValues {
        float[] get(float... values);
    }

    private final SharedElementTransition from;
    private final SharedElementTransition to;
    private OrderedAnimationValues values = new OrderedAnimationValues() {
        @Override
        public float[] get(float... values) {
            return values;
        }
    };
    private OrderedAnimationValues reversedValues = new OrderedAnimationValues() {
        @Override
        public float[] get(float... values) {
            for(int i = 0; i < values.length / 2; i++) {
                float temp = values[i];
                values[i] = values[values.length - i - 1];
                values[values.length - i - 1] = temp;
            }
            return values;
        }
    };

    SharedElementAnimatorCreator(SharedElementTransition from, SharedElementTransition to) {
        this.from = from;
        this.to = to;
    }

    List<Animator> createReverse() {
        return create(reversedValues);
    }

    public List<Animator> create() {
        return create(values);
    }

    @NonNull
    private List<Animator> create(OrderedAnimationValues orderedAnimationValues) {
        List<Animator> result = new ArrayList<>();
        createXAnimator(result, orderedAnimationValues);
        createYAnimator(result, orderedAnimationValues);
//        createScaleXAnimator(result);
//        createScaleYAnimator(result);
        return result;
    }

    private void createXAnimator(List<Animator> result, OrderedAnimationValues values) {
        int[] fromXY = new int[2];
        int[] toXY = new int[2];
        from.getLocationOnScreen(fromXY);
        to.getLocationOnScreen(toXY);

        final float fromX = fromXY[0];
        final float toX = toXY[0];
        if (fromX != toX) {
            result.add(ObjectAnimator.ofFloat(to, View.TRANSLATION_X, values.get(fromX - toX, 0)));
        }
    }

    private void createYAnimator(List<Animator> result, OrderedAnimationValues orderedAnimationValues) {
        int[] fromXY = new int[2];
        int[] toXY = new int[2];
        from.getLocationOnScreen(fromXY);
        to.getLocationOnScreen(toXY);
        final int fromY = fromXY[1];
        final int toY = toXY[1];
        if (fromY != toY) {
            result.add(ObjectAnimator.ofFloat(to, View.TRANSLATION_Y, orderedAnimationValues.get(fromY - toY, 0)));
        }
    }

    private void createScaleXAnimator(List<Animator> result) {
        final int fromWidth = from.getWidth();
        final int toWidth = to.getWidth();
        if (fromWidth != toWidth) {
//            to.setPivotX(from.getX());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_X, (float) (fromWidth / toWidth), 1));
        }
    }

    private void createScaleYAnimator(List<Animator> result) {
        final int fromHeight = from.getWidth();
        final int toHeight = to.getWidth();
        if (fromHeight != toHeight) {
//            to.setPivotY(from.getY());
            result.add(ObjectAnimator.ofFloat(to, View.SCALE_Y, (float) (fromHeight / toHeight), 1));
        }
    }
}
