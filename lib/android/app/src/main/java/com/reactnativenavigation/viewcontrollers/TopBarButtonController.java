package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.react.TopBarReactButtonView;

public class TopBarButtonController extends ViewController<TopBarReactButtonView> {

    private final String componentName;

    private final ReactViewCreator viewCreator;

    public TopBarButtonController(final Activity activity,
                                  final String id,
                                  final String componentName,
                                  final ReactViewCreator viewCreator,
                                  final Options initialOptions) {
        super(activity, id, initialOptions);
        this.componentName = componentName;
        this.viewCreator = viewCreator;
    }

    @Override
    public void onViewAppeared() {
        super.onViewAppeared();
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
        super.onViewDisappear();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @Override
    protected boolean isViewShown() {
        return super.isViewShown() && view.isReady();
    }

    @NonNull
    @Override
    protected TopBarReactButtonView createView() {
        view = (TopBarReactButtonView) viewCreator.create(getActivity(), getId(), componentName);
        return (TopBarReactButtonView) view.asView();
    }
}
