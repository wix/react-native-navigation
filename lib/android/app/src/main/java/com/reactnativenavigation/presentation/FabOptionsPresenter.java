package com.reactnativenavigation.presentation;


import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;

public class FabOptionsPresenter {
    private ViewGroup viewGroup;
    private ReactComponent component;

    private Fab fab;
    private FabMenu fabMenu;

    public FabOptionsPresenter(ViewGroup viewGroup, ReactComponent component) {
        this.viewGroup = viewGroup;
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
                fabMenu = new FabMenu(viewGroup.getContext(), options.fabMenuOptions, clickListener, component.getScrollEventListener());
                viewGroup.addView(fabMenu);
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
                fab = new Fab(viewGroup.getContext(), options.fabOptions, component.getScrollEventListener());
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.fabOptions.id.get()));
                viewGroup.addView(fab);
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
            viewGroup.removeView(fabMenu);
            fabMenu = null;
        }
    }

    private void removeFab() {
        if (fab != null) {
            if (fab.isShown()) {
                fab.hide(true);
            }
            viewGroup.removeView(fab);
            fab = null;
        }
    }
}
