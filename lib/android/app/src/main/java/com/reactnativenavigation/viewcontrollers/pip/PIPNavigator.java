package com.reactnativenavigation.viewcontrollers.pip;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;
import com.reactnativenavigation.views.pip.PIPContainer;
import com.reactnativenavigation.views.pip.PIPFloatingLayout;
import com.reactnativenavigation.views.pip.PIPStates;

import java.util.ArrayList;
import java.util.Collection;

public class PIPNavigator extends ParentController<PIPContainer> {
    private NavigationAnimator pipInAnimator;
    private ViewController childController;
    private PIPStates pipState = PIPStates.NOT_STARTED;
    private PIPFloatingLayout pipFloatingLayout;

    public PIPNavigator(Activity activity, ChildControllersRegistry childRegistry, Presenter presenter, Options initialOptions) {
        super(activity, childRegistry, "PIP " + CompatUtils.generateViewId(), presenter, initialOptions);
        pipInAnimator = new NavigationAnimator(getActivity(), new ElementTransitionManager());
    }

    @Override
    protected ViewController getCurrentChild() {
        return childController;
    }

    @NonNull
    @Override
    protected PIPContainer createView() {
        PIPContainer containerLayout = new PIPContainer(getActivity());
        return containerLayout;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        if (childController != null) childController.sendOnNavigationButtonPressed(buttonId);
    }

    public void setContentLayout(ViewGroup contentLayout) {
        contentLayout.addView(getView());
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        ArrayList<ViewController> children = new ArrayList<>();
        if (childController != null) children.add(childController);
        return children;
    }

    public void pushPIP(ViewController childController) {
        this.childController = childController;
        this.childController.setParentController(this);
        View pipView = this.childController.getView();
        getView().removeAllViews();
        updatePIPState(this.childController, PIPStates.CUSTOM_MOUNT_START);
        PIPFloatingLayout floatingLayout = new PIPFloatingLayout(getActivity());
        floatingLayout.setCustomPIPDimensions(this.childController.options.pipOptions.customPIP);
        floatingLayout.addView(pipView);
        getView().addView(floatingLayout);
        if (this.childController.options.animations.pipIn.enabled.isTrueOrUndefined()) {
            this.pipInAnimator.pipIn(floatingLayout, this.childController, this.childController.options, () -> {
                this.pipFloatingLayout = floatingLayout;
                updatePIPState(this.childController, PIPStates.CUSTOM_MOUNTED);
            });
        } else {
            this.pipFloatingLayout = floatingLayout;
            updatePIPState(this.childController, PIPStates.CUSTOM_MOUNTED);
        }
    }

    public ViewController restorePIP() {
        if (this.childController != null) {
            if (this.childController.options.animations.pipOut.enabled.isTrueOrUndefined()) {
                this.pipInAnimator.pipOut(this.pipFloatingLayout, this.childController, this.childController.options, () -> {
                    updatePIPState(this.childController, PIPStates.CUSTOM_MOUNTED);
                });
            } else {
                updatePIPState(this.childController, PIPStates.CUSTOM_MOUNTED);
            }
        }
        return this.childController;
    }

    public void onPictureInPictureModeChanged(Boolean isInPictureInPictureMode, Configuration newConfig) {
        if (isInPictureInPictureMode) {
            updatePIPState(this.childController, PIPStates.NATIVE_MOUNTED);
        } else {
            updatePIPState(this.childController, PIPStates.CUSTOM_MOUNTED);
        }
    }

    public PIPStates getPipStates() {
        return pipState;
    }

    public void closePIP() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PIPNavigator.this.childController != null) {
                    updatePIPState(PIPNavigator.this.childController, PIPStates.CUSTOM_UNMOUNTED);
                    PIPNavigator.this.childController.onViewWillDisappear();
                    PIPNavigator.this.childController.destroy();
                }
            }
        });
    }

    private void updatePIPState(ViewController controller, PIPStates newPIPState) {
        if (pipFloatingLayout != null)
            pipFloatingLayout.updatePIPState(newPIPState);
        controller.sendOnPIPStateChanged(pipState.getValue(), newPIPState.getValue());
        pipState = newPIPState;
    }
}
