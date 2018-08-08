package com.reactnativenavigation.views.element.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.CallSuper;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Transition;
import com.reactnativenavigation.views.element.Element;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;

public abstract class PropertyAnimatorCreator<T> {

    protected Element from;
    protected Element to;

    PropertyAnimatorCreator(Element from, Element to) {
        this.from = from;
        this.to = to;
    }

    @CallSuper
    public boolean shouldAnimateProperty() {
        Class<T> type = getChildClass();
        return type.isInstance(from.getChild()) &&
               type.isInstance(to.getChild()) &&
               !excludedViews().contains(from.getChild().getClass()) &&
               !excludedViews().contains(to.getChild().getClass()) &&
               shouldAnimateProperty((T) from.getChild(), (T) to.getChild());
    }

    protected abstract boolean shouldAnimateProperty(T fromChild, T toChild);

    protected List<Class> excludedViews() {
        return Collections.EMPTY_LIST;
    }

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
