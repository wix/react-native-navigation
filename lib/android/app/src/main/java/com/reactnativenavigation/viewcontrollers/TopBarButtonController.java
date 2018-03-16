package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.react.TopBarReactButtonView;

public class TopBarButtonController extends ViewController<TopBarReactButtonView> {

    private final String componentName;

    private final ReactViewCreator viewCreator;

    public TopBarButtonController(Activity activity, Button button, ReactViewCreator viewCreator) {
        super(activity, button.id, new Options());
        this.componentName = button.component.get();
        this.viewCreator = viewCreator;
    }

    @Override
    public void onViewAppeared() {
        view.sendComponentStart();
    }

    @Override
    public void onViewDisappear() {
        view.sendComponentStop();
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        getView().sendOnNavigationButtonPressed(buttonId);
    }

    @NonNull
    @Override
    protected TopBarReactButtonView createView() {
        view = (TopBarReactButtonView) viewCreator.create(getActivity(), getId(), componentName);
        return (TopBarReactButtonView) view.asView();
    }
}
