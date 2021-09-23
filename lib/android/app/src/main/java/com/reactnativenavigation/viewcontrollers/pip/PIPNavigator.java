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
import android.util.Log;
import android.util.Rational;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.PIPActionButton;
import com.reactnativenavigation.options.StackAnimationOptions;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.Functions;
import com.reactnativenavigation.utils.ILogger;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.stack.StackAnimator;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.pip.IPIPListener;
import com.reactnativenavigation.views.pip.PIPContainer;
import com.reactnativenavigation.views.pip.PIPFloatingLayout;
import com.reactnativenavigation.views.pip.PIPStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.reactnativenavigation.views.pip.PIPStates.MOUNT_START;
import static com.reactnativenavigation.views.pip.PIPStates.NATIVE_MOUNT_START;
import static com.reactnativenavigation.views.pip.PIPStates.NOT_STARTED;

public class PIPNavigator extends ParentController<PIPContainer> {
    private ViewController childController;
    private StackAnimator animator;
    private PIPStates pipState = NOT_STARTED;
    private PIPFloatingLayout pipFloatingLayout;
    private String EXTRA_CONTROL_TYPE = "control_type";
    private ILogger logger;
    private boolean receiverRegistered = false;
    private boolean wasDirectLaunchToNative = false;
    private String TAG = "PIPNavigator";
    private IPIPListener pipListener;

    public PIPNavigator(Activity activity, ChildControllersRegistry childRegistry, Presenter presenter, Options initialOptions, ILogger logger) {
        super(activity, childRegistry, "PIP " + CompatUtils.generateViewId(), presenter, initialOptions);
        this.logger = logger;
        animator = new StackAnimator(getActivity());
    }

    @Override
    public ViewController getCurrentChild() {
        return childController;
    }

    @NonNull
    @Override
    public PIPContainer createView() {
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

    public void pushPIP(ViewController childController, boolean toNative) {
        wasDirectLaunchToNative = toNative;
        closePIP(null);
        getView().setVisibility(View.VISIBLE);
        this.childController = childController;
        this.childController.setParentController(this);
        View pipView = this.childController.getView();
        PIPFloatingLayout floatingLayout = new PIPFloatingLayout(getActivity(), logger);
        floatingLayout.setCustomPIPDimensions(this.childController.options.pipOptions.customPIP);
        floatingLayout.addView(pipView);
        floatingLayout.setPIPListener(pipFloatingLayoutListener);
        getView().addView(floatingLayout);
        this.pipFloatingLayout = floatingLayout;
        if (this.childController != null) {
            if (toNative) {
                updatePIPStateInternal(NATIVE_MOUNT_START);
            } else {
                updatePIPStateInternal(MOUNT_START);
            }
        }
        if (this.childController.options.animations.pipIn.enabled.isTrueOrUndefined() && !toNative) {
            this.animator.pipIn(this.childController, this.childController.options, new ArrayList<>(), () -> {
                updatePIPStateInternal(PIPStates.CUSTOM_COMPACT);
            });
        } else {
            updatePIPStateInternal(PIPStates.CUSTOM_COMPACT);
        }
    }


    public void restorePIP(Functions.Func1<ViewController> task) {
        if (this.childController != null) {
            pipFloatingLayout.cancelAnimations();
            pipFloatingLayout.initiateRestore();
            updatePIPStateInternal(PIPStates.RESTORE_START);
            if (this.childController.options.animations.pipOut.enabled.isTrueOrUndefined() && !wasDirectLaunchToNative) {
                this.animator.pipOut(
                        this.childController,
                        this.childController.options,
                        new ArrayList<>(),
                        () -> {
                            updatePIPStateInternal(NOT_STARTED);
                            this.childController.detachView();
                            task.run(this.childController);
                            clearPIP();
                        });
            } else {
                updatePIPStateInternal(NOT_STARTED);
                this.childController.detachView();
                task.run(this.childController);
                clearPIP();
            }
            wasDirectLaunchToNative = false;
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
                updatePIPStateInternal(newPIPState);
                break;
            case CUSTOM_MOUNTED:
                if (receiverRegistered) {
                    receiverRegistered = false;
                    getActivity().unregisterReceiver(mReceiver);
                }
                updatePIPStateInternal(newPIPState);
                break;
            case UNMOUNT_START:
                if (receiverRegistered) {
                    receiverRegistered = false;
                    getActivity().unregisterReceiver(mReceiver);
                }
                closePIP(null);
                break;
            default:
                updatePIPStateInternal(newPIPState);
                break;
        }
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
                    updatePIPStateInternal(NOT_STARTED);
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

    public void setPipListener(IPIPListener pipListener) {
        this.pipListener = pipListener;
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
        logger.log(Log.INFO, TAG, "updatePIPStateInternal " + newPIPState.toString());
        pipState = newPIPState;
        if (pipFloatingLayout != null) {
            pipFloatingLayout.updatePIPState(newPIPState);
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

    public boolean wasDirectLaunchToNative() {
        return wasDirectLaunchToNative;
    }

    private IPIPListener pipFloatingLayoutListener = new IPIPListener() {
        @Override
        public void onPIPStateChanged(PIPStates oldState, PIPStates newState) {
            if (isChildControlledAvailable()) {
                logger.log(Log.INFO, TAG, "Old State " + oldState.toString() + " New State " + newState.toString());
                PIPNavigator.this.childController.sendOnPIPStateChanged(oldState.getValue(), newState.getValue());
            }
            if (pipListener != null)
                pipListener.onPIPStateChanged(oldState, newState);
        }

        @Override
        public void onCloseClick() {
            if (pipListener != null)
                pipListener.onCloseClick();
        }

        @Override
        public void onFullScreenClick() {
            if (pipListener != null)
                pipListener.onFullScreenClick();
        }
    };
}