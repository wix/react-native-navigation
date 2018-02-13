package com.reactnativenavigation.presentation;


import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.ReactComponent;

import static android.widget.RelativeLayout.ALIGN_PARENT_BOTTOM;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.ALIGN_PARENT_TOP;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;
import static com.reactnativenavigation.parse.Options.BooleanOptions.False;
import static com.reactnativenavigation.parse.Options.BooleanOptions.True;

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
        applyFabOptions(options.fabOptions);
        applyFabMenuOptions(options.fabMenuOptions);
    }

    private void applyFabMenuOptions(FabOptions options) {
        if (options.id.hasValue()) {
            if (fabMenu == null) {
                fabMenu = new FabMenu(viewGroup.getContext());
                applyFabMenuOptions(fabMenu, options);
                viewGroup.addView(fabMenu);
            } else {
                fabMenu.bringToFront();
                applyFabMenuOptions(fabMenu, options);
            }
        } else {
            removeFabMenu();
        }
    }

    private void applyFabOptions(FabOptions options) {
        if (options.id.hasValue()) {
            if (fab == null) {
                fab = new Fab(viewGroup.getContext(), options.id.get());
                applyFabOptions(fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
                viewGroup.addView(fab);
            } else {
                fab.bringToFront();
                applyFabOptions(fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
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

    private void applyFabOptions(Fab fab, FabOptions options) {
        if (options.visible == True) {
            fab.show(true);
        }
        if (options.visible == False) {
            fab.hide(true);
        }
        if (options.backgroundColor.hasValue()) {
            fab.setColorNormal(options.backgroundColor.get());
        }
        if (options.clickColor.hasValue()) {
            fab.setColorPressed(options.clickColor.get());
        }
        if (options.rippleColor.hasValue()) {
            fab.setColorRipple(options.rippleColor.get());
        }
        if (options.icon.hasValue()) {
            fab.applyIcon(options.icon.get());
        }
        if (options.alignHorizontally.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
            if ("right".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_LEFT);
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
            }
            if ("left".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                layoutParams.addRule(ALIGN_PARENT_LEFT);
            }
            fab.setLayoutParams(layoutParams);
        }
        if (options.alignVertically.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fab.getLayoutParams();
            if ("top".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(ALIGN_PARENT_TOP);
            }
            if ("bottom".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_TOP);
                layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            }
            fab.setLayoutParams(layoutParams);
        }
        if (options.size.hasValue()) {
            fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        }
        if (options.hideOnScroll == True) {
            fab.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll == False) {
            fab.disableCollapse();
        }
    }

    private void applyFabMenuOptions(FabMenu fabMenu, FabOptions options) {
        if (options.visible == True) {
            fabMenu.showMenu(true);
        }
        if (options.visible == False) {
            fabMenu.hideMenu(true);
        }

        if (options.backgroundColor.hasValue()) {
            fabMenu.setMenuButtonColorNormal(options.backgroundColor.get());
        }
        if (options.clickColor.hasValue()) {
            fabMenu.setMenuButtonColorPressed(options.clickColor.get());
        }
        if (options.rippleColor.hasValue()) {
            fabMenu.setMenuButtonColorRipple(options.rippleColor.get());
        }
        if (options.icon.hasValue()) {
            fabMenu.applyIcon(options.icon.get());
        }

        for (Fab fabStored : fabMenu.getActions()) {
            fabMenu.removeMenuButton(fabStored);
        }
        fabMenu.getActions().clear();
        for (FabOptions fabOption : options.actionsArray) {
            Fab fab = new Fab(viewGroup.getContext(), fabOption.id.get());
            applyFabOptions(fab, fabOption);
            fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));

            fabMenu.getActions().add(fab);
            fabMenu.addMenuButton(fab);
        }
        if (options.alignHorizontally.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fabMenu.getLayoutParams();
            if ("right".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_LEFT);
                layoutParams.addRule(ALIGN_PARENT_RIGHT);
            }
            if ("left".equals(options.alignHorizontally.get())) {
                layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                layoutParams.addRule(ALIGN_PARENT_LEFT);
            }
            fabMenu.setLayoutParams(layoutParams);
        }
        if (options.alignVertically.hasValue()) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) fabMenu.getLayoutParams();
            if ("top".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(ALIGN_PARENT_TOP);
            }
            if ("bottom".equals(options.alignVertically.get())) {
                layoutParams.removeRule(ALIGN_PARENT_TOP);
                layoutParams.addRule(ALIGN_PARENT_BOTTOM);
            }
            fabMenu.setLayoutParams(layoutParams);
        }
        if (options.hideOnScroll == True) {
            fabMenu.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll == False) {
            fabMenu.disableCollapse();
        }
    }
}
