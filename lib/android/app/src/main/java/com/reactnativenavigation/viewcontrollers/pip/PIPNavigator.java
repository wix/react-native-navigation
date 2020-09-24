package com.reactnativenavigation.viewcontrollers.pip;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Rational;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.PIPActionButton;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.Functions.Func1;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.element.ElementTransitionManager;
import com.reactnativenavigation.views.pip.PIPContainer;
import com.reactnativenavigation.views.pip.PIPFloatingLayout;
import com.reactnativenavigation.views.pip.PIPStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PIPNavigator extends ParentController<PIPContainer> {
    private NavigationAnimator pipInAnimator;
    private ViewController childController;
    private PIPStates pipState = PIPStates.NOT_STARTED;
    private PIPFloatingLayout pipFloatingLayout;
    private String EXTRA_CONTROL_TYPE = "control_type";
    private boolean receiverRegistered = false;

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
        return new PIPContainer(getActivity());
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        if (childController != null) childController.sendOnNavigationButtonPressed(buttonId);
    }

    public void setContentLayout(ViewGroup contentLayout) {
        contentLayout.addView(getView());
        getView().setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public Collection<? extends ViewController> getChildControllers() {
        ArrayList<ViewController> children = new ArrayList<>();
        if (childController != null) children.add(childController);
        return children;
    }

    public void pushPIP(ViewController childController) {
        closePIP(null);
        getView().setVisibility(View.VISIBLE);
        this.childController = childController;
        this.childController.setParentController(this);
        View pipView = this.childController.getView();
        updatePIPStateInternal(PIPStates.MOUNT_START);
        PIPFloatingLayout floatingLayout = new PIPFloatingLayout(getActivity());
        floatingLayout.setCustomPIPDimensions(this.childController.options.pipOptions.customPIP);
        floatingLayout.addView(pipView);
        floatingLayout.setPipListener(new PIPFloatingLayout.IPIPListener() {
            @Override
            public void onPIPStateChanged(PIPStates oldState, PIPStates newState) {
                if (isChildControlledAvailable())
                    PIPNavigator.this.childController.sendOnPIPStateChanged(oldState.getValue(), newState.getValue());
            }
        });
        getView().addView(floatingLayout);
        if (this.childController.options.animations.pipIn.enabled.isTrueOrUndefined()) {
            this.pipInAnimator.pipIn(floatingLayout, this.childController, this.childController.options, () -> {
                this.pipFloatingLayout = floatingLayout;
                updatePIPStateInternal(PIPStates.CUSTOM_COMPACT);
            });
        } else {
            this.pipFloatingLayout = floatingLayout;
            updatePIPStateInternal(PIPStates.CUSTOM_COMPACT);
        }
    }

    public void restorePIP(Func1<ViewController> task) {
        if (this.childController != null) {
            pipFloatingLayout.cancelAnimations();
            pipFloatingLayout.initiateRestore();
            updatePIPStateInternal(PIPStates.RESTORE_START);
            if (this.childController.options.animations.pipOut.enabled.isTrueOrUndefined()) {
                this.pipInAnimator.pipOut(this.pipFloatingLayout, this.childController, this.childController.options, () -> {
                    updatePIPStateInternal(PIPStates.NOT_STARTED);
                    this.childController.detachView();
                    task.run(this.childController);
                    clearPIP();
                });
            } else {
                updatePIPStateInternal(PIPStates.NOT_STARTED);
                this.childController.detachView();
                task.run(this.childController);
                clearPIP();
            }

        }
    }

    @Override
    public void mergeChildOptions(Options options, ViewController child) {
        super.mergeChildOptions(options, child);
        if (pipFloatingLayout != null) {
            pipFloatingLayout.setCustomPIPDimensions(this.childController.options.pipOptions.customPIP);
            pipFloatingLayout.updatePIPState(this.pipState);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && pipState == PIPStates.NATIVE_MOUNTED) {
                updatePictureInPictureParams();
            }
        }
    }

    public void updatePIPState(PIPStates newPIPState) {
        switch (newPIPState) {
            case NATIVE_MOUNTED:
                if (isChildControlledAvailable()) {
                    receiverRegistered = true;
                    getActivity().registerReceiver(mReceiver, new IntentFilter(childController.options.pipOptions.actionControlGroup));
                }
                break;
            case CUSTOM_MOUNTED:
                if (receiverRegistered) {
                    receiverRegistered = false;
                    getActivity().unregisterReceiver(mReceiver);
                }
                break;
        }
        updatePIPStateInternal(newPIPState);
    }

    public PIPStates getPipStates() {
        return pipState;
    }

    public void closePIP(CommandListener listener) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (PIPNavigator.this.childController != null) {
                    updatePIPStateInternal(PIPStates.UNMOUNT_START);
                    PIPNavigator.this.childController.detachView();
                    PIPNavigator.this.childController.onViewWillDisappear();
                    PIPNavigator.this.childController.destroy();
                    updatePIPStateInternal(PIPStates.NOT_STARTED);
                    if (listener != null)
                        listener.onSuccess("PIP Closed");
                    clearPIP();
                } else {
                    if (listener != null)
                        listener.onSuccess("PIP is not available");
                }
            }
        });
    }

    private void clearPIP() {
        getView().removeAllViews();
        PIPNavigator.this.childController = null;
        PIPNavigator.this.pipFloatingLayout = null;
        getView().setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<RemoteAction> getPIPActionButtons() {
        List<RemoteAction> actionList = new ArrayList<RemoteAction>();
        if (childController != null) {
            PIPActionButton[] buttons = childController.options.pipOptions.actionButtons;
            if (buttons != null) {
                for (PIPActionButton button : buttons) {
                    PendingIntent intent = PendingIntent.getBroadcast(getActivity(), button.requestCode.intValue(), new Intent(childController.options.pipOptions.actionControlGroup).putExtra(EXTRA_CONTROL_TYPE, button.requestType.get()), 0);
                    int id = getActivity().getResources().getIdentifier(button.actionIcon.get(), "drawable", getActivity().getPackageName());
                    Icon icon = Icon.createWithResource(getActivity(), id);
                    actionList.add(new RemoteAction(icon, button.actionTitle.get(), button.actionTitle.get(), intent));
                }
            }
        }
        return actionList;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Rational getPIPAspectRatio() {
        return childController != null ? new Rational(childController.options.pipOptions.aspectRatio.numerator.get(), childController.options.pipOptions.aspectRatio.denominator.get()) : null;
    }

    private void updatePIPStateInternal(PIPStates newPIPState) {
        pipState = newPIPState;
        if (pipFloatingLayout != null) {
            if (newPIPState == PIPStates.CUSTOM_MOUNTED) {
                if (pipFloatingLayout.isStateAvailable()) {
                    pipFloatingLayout.updatePIPState(newPIPState);
                } else {
                    PIPNavigator.this.childController.sendOnPIPStateChanged(PIPStates.NATIVE_MOUNTED.getValue(), PIPStates.NOT_STARTED.getValue());
                    clearPIP();
                }
            } else {
                pipFloatingLayout.updatePIPState(newPIPState);
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && childController != null) {
                if (!childController.options.pipOptions.actionControlGroup.equals(intent.getAction())) {
                    return;
                }
                if (!childController.isDestroyed())
                    childController.sendOnPIPButtonPressed(intent.getStringExtra(EXTRA_CONTROL_TYPE));
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PictureInPictureParams getPictureInPictureParams() {
        PictureInPictureParams.Builder mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
        mPictureInPictureParamsBuilder.setActions(getPIPActionButtons());
        mPictureInPictureParamsBuilder.setAspectRatio(getPIPAspectRatio());
        return mPictureInPictureParamsBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePictureInPictureParams() {
        getActivity().setPictureInPictureParams(getPictureInPictureParams());
    }

    private boolean isChildControlledAvailable() {
        return PIPNavigator.this.childController != null && !PIPNavigator.this.childController.isDestroyed();
    }
}