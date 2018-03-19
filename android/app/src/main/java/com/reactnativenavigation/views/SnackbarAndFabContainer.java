package com.reactnativenavigation.views;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;

import com.reactnativenavigation.events.Event;
import com.reactnativenavigation.events.EventBus;
import com.reactnativenavigation.events.FabSetEvent;
import com.reactnativenavigation.events.ScreenChangedEvent;
import com.reactnativenavigation.events.Subscriber;
import com.reactnativenavigation.layouts.Layout;
import com.reactnativenavigation.params.FabParams;
import com.reactnativenavigation.params.SnackbarParams;
import com.reactnativenavigation.screens.Screen;

import java.util.EmptyStackException;

public class SnackbarAndFabContainer extends CoordinatorLayout implements Snakbar.OnDismissListener, Subscriber{
    private Snakbar snakbar;
    private FloatingActionButtonCoordinator fabCoordinator;
    private Layout layout;

    public SnackbarAndFabContainer(Context context, Layout layout) {
        super(context);
        this.layout = layout;
        fabCoordinator = new FloatingActionButtonCoordinator(this);
        EventBus.instance.register(this);
    }

    public void showSnackbar(final String navigatorEventId, final SnackbarParams params) {
        snakbar = new Snakbar(this, navigatorEventId, params);
        snakbar.show();
    }

    @Override
    public void onDismiss(Snakbar snakbar) {
        if (this.snakbar == snakbar) {
            this.snakbar = null;
        }
    }

    public void destroy() {
        EventBus.instance.unregister(this);
    }

    @Override
    public void onEvent(Event event) {
        if (ScreenChangedEvent.TYPE.equals(event.getType())) {
            onScreenChange(((ScreenChangedEvent) event).fabParams);
        }
        if (FabSetEvent.TYPE.equals(event.getType())) {
            updateFab(((FabSetEvent) event).fabParams);
        }
    }

    private void onScreenChange(FabParams fabParams) {
        dismissSnackbar();
        updateFab(fabParams);
    }

    public void dismissSnackbar() {
        if (snakbar != null) {
            snakbar.dismiss();
            snakbar = null;
        }
    }

    private void updateFab(final FabParams fabParams) {
        fabCoordinator.remove(new Runnable() {
            @Override
            public void run() {
                if (fabParams != null) {
                    Screen currentScreen = null;
                    try {
                        currentScreen = layout.getCurrentScreen();
                    } catch(EmptyStackException exception) {
                        currentScreen = null;
                    }
                    if (currentScreen != null && currentScreen.getScreenInstanceId().equals(fabParams.screenInstanceId)) {
                        fabCoordinator.add(fabParams);
                    }
                }
            }
        });
    }
}
