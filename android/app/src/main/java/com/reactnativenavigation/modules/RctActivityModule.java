package com.reactnativenavigation.modules;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeMap;
import com.reactnativenavigation.activities.BaseReactActivity;
import com.reactnativenavigation.activities.BottomTabActivity;
import com.reactnativenavigation.activities.SingleScreenActivity;
import com.reactnativenavigation.controllers.ModalController;
import com.reactnativenavigation.core.objects.Screen;
import com.reactnativenavigation.modal.RnnModal;
import com.reactnativenavigation.utils.BridgeUtils;
import com.reactnativenavigation.utils.ContextProvider;

import java.util.ArrayList;

/**
 * Created by guyc on 10/03/16.
 */
public class RctActivityModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RctActivity";

    public RctActivityModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactMethod
    public void startTabBasedApp(ReadableArray screens, ReadableMap style) {
        Activity context = ContextProvider.getActivityContext();
        if (context != null && !context.isFinishing()) {
            Intent intent = new Intent(context, BottomTabActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle extras = new Bundle();
            extras.putSerializable(BottomTabActivity.EXTRA_SCREENS, createScreens(screens));
            if (style != null) {
                BridgeUtils.addMapToBundle(((ReadableNativeMap) style).toHashMap(), extras);
            }
            intent.putExtras(extras);

            context.startActivity(intent);
        }
    }

    private ArrayList<Screen> createScreens(ReadableArray screens) {
        ArrayList<Screen> ret = new ArrayList<>();
        for(int i = 0; i < screens.size(); i++) {
            ret.add(new Screen(screens.getMap(i)));
        }
        return ret;
    }

    @ReactMethod
    public void startSingleScreenApp(ReadableMap screen) {
        BaseReactActivity context = ContextProvider.getActivityContext();
        if (context != null && !context.isFinishing()) {
            Intent intent = new Intent(context, SingleScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle extras = new Bundle();
            extras.putSerializable(SingleScreenActivity.EXTRA_SCREEN, new Screen(screen));
            intent.putExtras(extras);

            context.startActivity(intent);
        }
    }

    @ReactMethod
    public void setNavigatorButtons(final ReadableMap buttons) {
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context == null || context.isFinishing()) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setNavigationButtons(buttons);
            }
        });
    }

    @ReactMethod
    public void setNavigatorTitle(final ReadableMap title) {
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context == null || context.isFinishing()) {
            return;
        }
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setNavigationTitle(title);
            }
        });
    }

    @ReactMethod
    public void navigatorPush(final ReadableMap skreen) {
        final Screen screen = new Screen(skreen);
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context == null || context.isFinishing()) {
            return;
        }

        // First, check if the screen should be pushed to a Modal
        ModalController modalController = ModalController.getInstance();
        if (modalController.isModalDisplayed()) {
            final RnnModal modal = modalController.get();
            if (modal != null) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        modal.push(screen);
                    }
                });
            }
            return;
        }

        // No Modal is displayed, Push to activity
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.push(screen);
            }
        });
    }

    @ReactMethod
    public void navigatorPop(final ReadableMap navigator) {
        final String navigatorId = navigator.getString("navigatorID");
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context == null || context.isFinishing()) {
            return;
        }

        // First, check if the screen should be popped from a Modal
        ModalController modalController = ModalController.getInstance();
        if (modalController.isModalDisplayed()) {
            final RnnModal modal = modalController.get();
            if (modal != null) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        modal.pop();
                    }
                });
            }
            return;
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    context.pop(navigatorId);
                }
            });
        }

    }

    @ReactMethod
    public void showModal(final ReadableMap screen) {
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context != null && !context.isFinishing()) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new RnnModal(context, new Screen(screen)).show();
                }
            });
        }
    }

    @ReactMethod
    public void dismissAllModals(final ReadableMap params) {
        final BaseReactActivity context = ContextProvider.getActivityContext();
        if (context != null && !context.isFinishing()) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ModalController modalController = ModalController.getInstance();
                if (modalController.isModalDisplayed()) {
                    modalController.dismissAllModals();
                }
            }
        });
        }
    }


    /**
     * Dismisses the top modal (the last modal pushed).
     */
    @ReactMethod
    public void dismissModal() {
        ModalController modalController = ModalController.getInstance();
        if (modalController.isModalDisplayed()) {
            modalController.dismissModal();
        }
    }
}
