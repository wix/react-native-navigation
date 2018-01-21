package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.views.ComponentLayout;
import com.reactnativenavigation.views.ReactComponent;

public class ComponentViewController extends ViewController<ComponentLayout> implements NavigationOptionsListener {

    public interface ReactViewCreator {

        IReactView create(Activity activity, String componentId, String componentName);
    }

    public interface IReactView {

        boolean isReady();

        View asView();

        void destroy();

        void sendComponentStart();

        void sendComponentStop();

        void sendOnNavigationButtonPressed(String buttonId);

        ScrollEventListener getScrollEventListener();

        void dispatchTouchEventToJs(MotionEvent event);
    }

    private final String componentName;

    private final ReactViewCreator viewCreator;
    private ReactComponent component;

    public ComponentViewController(final Activity activity,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialNavigationOptions) {
        super(activity, id);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
        options = initialNavigationOptions;
    }

    @Override
    public void destroy() {
        super.destroy();
        if (component != null) component.destroy();
        component = null;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        component.applyOptions(options);
        applyOnParentController(parentController -> {
            parentController.clearOptions();
            parentController.applyOptions(options, component);
        });
        component.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        super.onViewDisappear();
        component.sendComponentStop();
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && component.isReady();
    }

    @NonNull
    @Override
    protected ComponentLayout createView() {
        component = (ReactComponent) viewCreator.create(getActivity(), getId(), componentName);
        return (ComponentLayout) component.asView();
    }

    @Override
    public void mergeOptions(Options options) {
        this.options.mergeWith(options);
        component.applyOptions(this.options);
        applyOnParentController(parentController -> parentController.applyOptions(this.options, component));
    }

    Options getOptions() {
        return options;
    }

    ReactComponent getComponent() {
        return component;
    }
}
