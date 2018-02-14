package com.reactnativenavigation.presentation;


import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reactnativenavigation.R;
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

    public void applyOptions(Options options, @NonNull ReactComponent component, @NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.component = component;
        applyFabOptions(options.fabOptions);
        applyFabMenuOptions(options.fabMenuOptions);
    }

    private void applyFabMenuOptions(FabOptions options) {
        if (options.id.hasValue()) {
            if (fabMenu == null) {
                fabMenu = new FabMenu(viewGroup.getContext());
                setParams(fabMenu, options);
                applyFabMenuOptions(fabMenu, options);
                viewGroup.addView(fabMenu);
            } else {
                setParams(fabMenu, options);
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
                setParams(fab, options);
                applyFabOptions(fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
                viewGroup.addView(fab);
            } else {
                setParams(fab, options);
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

    private void setParams(View fab, FabOptions options) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParamsRelative = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsRelative.bottomMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsRelative.rightMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsRelative.leftMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsRelative.topMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            if (options.alignHorizontally.hasValue()) {
                if ("right".equals(options.alignHorizontally.get())) {
                    layoutParamsRelative.removeRule(ALIGN_PARENT_LEFT);
                    layoutParamsRelative.addRule(ALIGN_PARENT_RIGHT);
                }
                if ("left".equals(options.alignHorizontally.get())) {
                    layoutParamsRelative.removeRule(ALIGN_PARENT_RIGHT);
                    layoutParamsRelative.addRule(ALIGN_PARENT_LEFT);
                }
            } else {
                layoutParamsRelative.addRule(ALIGN_PARENT_RIGHT);
            }
            if (options.alignVertically.hasValue()) {
                if ("top".equals(options.alignVertically.get())) {
                    layoutParamsRelative.removeRule(ALIGN_PARENT_BOTTOM);
                    layoutParamsRelative.addRule(ALIGN_PARENT_TOP);
                }
                if ("bottom".equals(options.alignVertically.get())) {
                    layoutParamsRelative.removeRule(ALIGN_PARENT_TOP);
                    layoutParamsRelative.addRule(ALIGN_PARENT_BOTTOM);
                }
            } else {
                layoutParamsRelative.addRule(ALIGN_PARENT_BOTTOM);
            }
            layoutParams = layoutParamsRelative;
        }
        if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParamsFrame = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFrame.bottomMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsFrame.rightMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsFrame.leftMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            layoutParamsFrame.topMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
            if (options.alignHorizontally.hasValue()) {
                if ("right".equals(options.alignHorizontally.get())) {
                    removeGravityParam(layoutParamsFrame, Gravity.LEFT);
                    setGravityParam(layoutParamsFrame, Gravity.RIGHT);
                }
                if ("left".equals(options.alignHorizontally.get())) {
                    removeGravityParam(layoutParamsFrame, Gravity.RIGHT);
                    setGravityParam(layoutParamsFrame, Gravity.LEFT);
                }
            } else {
                setGravityParam(layoutParamsFrame, Gravity.RIGHT);
            }
            if (options.alignVertically.hasValue()) {
                if ("top".equals(options.alignVertically.get())) {
                    removeGravityParam(layoutParamsFrame, Gravity.BOTTOM);
                    setGravityParam(layoutParamsFrame, Gravity.TOP);
                }
                if ("bottom".equals(options.alignVertically.get())) {
                    removeGravityParam(layoutParamsFrame, Gravity.TOP);
                    setGravityParam(layoutParamsFrame, Gravity.BOTTOM);
                }
            } else {
                setGravityParam(layoutParamsFrame, Gravity.BOTTOM);
            }
            layoutParams = layoutParamsFrame;
        }
        fab.setLayoutParams(layoutParams);
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
        if (options.hideOnScroll == True) {
            fabMenu.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll == False) {
            fabMenu.disableCollapse();
        }
    }

    private void setGravityParam(FrameLayout.LayoutParams params, int gravityParam) {
        params.gravity = params.gravity | gravityParam;
    }

    private void removeGravityParam(FrameLayout.LayoutParams params, int gravityParam) {
        if ((params.gravity & gravityParam) == gravityParam) {
            params.gravity = params.gravity & ~gravityParam;
        }
    }
}
