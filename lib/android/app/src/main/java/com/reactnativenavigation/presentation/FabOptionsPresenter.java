package com.reactnativenavigation.presentation;


import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;

public class FabOptionsPresenter {
    private StackLayout stackLayout;
    private ReactComponent component;

    private Fab fab;
    private FabMenu fabMenu;

    public FabOptionsPresenter(StackLayout stackLayout, ReactComponent component) {
        this.stackLayout = stackLayout;
        this.component = component;
    }

    public void applyOptions(Options options) {
        applyFabOptions(options);
        applyFabMenuOptions(options);
    }

    private void applyFabMenuOptions(Options options) {
        if (options.fabMenuOptions.id.hasValue()) {
            FabMenu.FabClickListener clickListener = component::sendOnNavigationButtonPressed;
            if (fabMenu == null) {
                fabMenu = new FabMenu(stackLayout.getContext(), options.fabMenuOptions, clickListener, component.getScrollEventListener());
                stackLayout.addView(fabMenu);
            } else {
                fabMenu.bringToFront();
                fabMenu.applyOptions(options.fabMenuOptions, clickListener, component.getScrollEventListener());
            }
        } else {
            removeFabMenu();
        }
    }

    private void applyFabOptions(Options options) {
        if (options.fabOptions.id.hasValue()) {
            if (fab == null) {
                fab = new Fab(stackLayout.getContext(), options.fabOptions, component.getScrollEventListener());
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.fabOptions.id.get()));
                stackLayout.addView(fab);
            } else {
                fab.bringToFront();
                fab.applyOptions(options.fabOptions, component.getScrollEventListener());
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.fabOptions.id.get()));
            }
        } else {
            removeFab();
        }
    }

    private void removeFabMenu() {
        if (fabMenu != null) {
            if (fabMenu.isShown()) {
                fabMenu.hideMenu(true);
            }
            stackLayout.removeView(fabMenu);
            fabMenu = null;
        }
    }

    private void removeFab() {
        if (fab != null) {
            if (fab.isShown()) {
                fab.hide(true);
            }
            stackLayout.removeView(fab);
            fab = null;
        }
    }
}
