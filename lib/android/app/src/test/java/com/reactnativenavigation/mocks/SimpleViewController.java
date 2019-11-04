package com.reactnativenavigation.mocks;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.views.ReactComponent;

import org.mockito.Mockito;

import androidx.annotation.NonNull;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class SimpleViewController extends ChildController<SimpleViewController.SimpleView> {


    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Options options) {
        this(activity, childRegistry, id, new Presenter(activity, new Options()), options);
    }

    public SimpleViewController(Activity activity, ChildControllersRegistry childRegistry, String id, Presenter presenter, Options options) {
        super(activity, childRegistry, id, presenter, options);
    }

    @Override
    protected SimpleView createView() {
        return new SimpleView(getActivity());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    public void destroy() {
        if (!isDestroyed()) performOnParentController(parent -> parent.onChildDestroyed(this));
        super.destroy();
    }

    @NonNull
    @Override
    public String toString() {
        return "SimpleViewController " + getId();
    }

    @Override
    public int getTopInset() {
        int statusBarInset = resolveCurrentOptions().statusBar.drawBehind.isTrue() ? 0 : 63;
        return statusBarInset + perform(getParentController(), 0, p -> p.getTopInset(this));
    }

    public static class SimpleView extends ReactView implements ReactComponent {

        public SimpleView(@NonNull Context context) {
            super(context, Mockito.mock(ReactInstanceManager.class), "compId", "compName");
        }

        @Override
        public boolean isRendered() {
            return getChildCount() >= 1;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public ReactView asView() {
            return this;
        }

        @Override
        public void destroy() {

        }

        @Override
        public void sendComponentStart() {

        }

        @Override
        public void sendComponentStop() {

        }

        @Override
        public void sendOnNavigationButtonPressed(String buttonId) {

        }

        @Override
        public ScrollEventListener getScrollEventListener() {
            return null;
        }

        @Override
        public void dispatchTouchEventToJs(MotionEvent event) {

        }
    }
}
