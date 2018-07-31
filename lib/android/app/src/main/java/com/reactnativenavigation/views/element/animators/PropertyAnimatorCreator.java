package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.Element;

import java.lang.reflect.ParameterizedType;

public abstract class PropertyAnimatorCreator<T> {

    protected Element from;
    protected Element to;

    PropertyAnimatorCreator(Element from, Element to) {
        this.from = from;
        this.to = to;
    }

    public boolean shouldAnimateProperty() {
        Class<T> type = getChildClass();
        return type.isInstance(from.getChild()) &&
               type.isInstance(to.getChild()) &&
               shouldAnimateProperty((T) from.getChild(), (T) to.getChild());
    }

    protected abstract boolean shouldAnimateProperty(T fromChild, T toChild);

    public Animator create(Transition transition) {
        Animator animator = create().setDuration(transition.duration.get());
        animator.addListener(new AnimatorListenerAdapter() {
            private final boolean originalClipChildren = ((ViewGroup) to.getParent()).getClipChildren();
            @Override
            public void onAnimationStart(Animator animation) {
                ((ViewGroup) to.getParent()).setClipChildren(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((ViewGroup) to.getParent()).setClipChildren(originalClipChildren);
            }
        });
        return animator;
    }

    protected abstract Animator create();

    private Class<T> getChildClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
