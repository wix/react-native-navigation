package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.support.annotation.*;
import android.view.*;

import com.reactnativenavigation.interfaces.ScrollEventListener;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.presentation.*;
import com.reactnativenavigation.views.*;

public class ComponentViewController extends ViewController implements NavigationOptionsListener {

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
    }

    private final String componentName;

    private final ReactViewCreator viewCreator;
    private Options options;
    private ReactComponent component;

    public ComponentViewController(final Activity activity,
                                   final String id,
                                   final String componentName,
                                   final ReactViewCreator viewCreator,
                                   final Options initialNavigationOptions) {
        super(activity, id);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
        this.options = initialNavigationOptions;
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar() {
        return component.getTopBar();
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
        ensureViewIsCreated();
        component.applyOptions(options);
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
    protected View createView() {
        component = (ReactComponent) viewCreator.create(getActivity(), getId(), componentName);
        return component.asView();
    }

    @Override
    public void mergeOptions(Options options) {
        this.options.mergeWith(options);
        component.applyOptions(this.options);
    }

    Options getOptions() {
        return options;
    }

    ReactComponent getComponent() {
        return component;
    }
}
