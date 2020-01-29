package com.reactnativenavigation.presentation;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class FabPresenter {
    private ViewGroup viewGroup;
    private ViewController component;

    private Fab fab;
    private FabMenu fabMenu;

    public void applyOptions(FabOptions options, @NonNull ViewController component, @NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.component = component;

        if (options.id.hasValue()) {
            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
                fabMenu.bringToFront();
                applyFabMenuOptions(fabMenu, options);
                setParams(fabMenu, options);
            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
                fab.bringToFront();
                applyFabOptions(fab, options);
                setParams(fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            } else {
                createFab(options);
            }
        } else {
            removeFab();
            removeFabMenu();
        }
    }

    public void mergeOptions(FabOptions options, @NonNull ViewController component, @NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.component = component;
        if (options.id.hasValue()) {
            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
                mergeParams(fabMenu, options);
                fabMenu.bringToFront();
                mergeFabMenuOptions(fabMenu, options);
            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
                mergeParams(fab, options);
                fab.bringToFront();
                mergeFabOptions(fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            } else {
                createFab(options);
            }
        }
    }

    private void createFab(FabOptions options) {
        if (options.actionsArray.size() > 0) {
            fabMenu = new FabMenu(viewGroup.getContext(), options.id.get());
            setParams(fabMenu, options);
            applyFabMenuOptions(fabMenu, options);
            viewGroup.addView(fabMenu);
        } else {
            fab = new Fab(viewGroup.getContext(), options.id.get());
            setParams(fab, options);
            applyFabOptions(fab, options);
            fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            viewGroup.addView(fab);
        }
    }

    private void removeFabMenu() {
        if (fabMenu != null) {
            fabMenu.hideMenuButton(true);
            viewGroup.removeView(fabMenu);
            fabMenu = null;
        }
    }

    private void removeFab() {
        if (fab != null) {
            fab.hide(true);
            viewGroup.removeView(fab);
            fab = null;
        }
    }

    private void setParams(View fab, FabOptions options) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.rightMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        lp.leftMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        lp.bottomMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        fab.setTag(R.id.fab_bottom_margin, lp.bottomMargin);
        lp.gravity = Gravity.BOTTOM;
        if (options.alignHorizontally.hasValue()) {
            if ("right".equals(options.alignHorizontally.get())) {
                lp.gravity |= Gravity.RIGHT;
            }
            if ("left".equals(options.alignHorizontally.get())) {
                lp.gravity |= Gravity.LEFT;
            }
        } else {
            lp.gravity |= Gravity.RIGHT;
        }
        fab.setLayoutParams(lp);
    }

    private void mergeParams(View fab, FabOptions options) {
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) perform(
                fab,
                new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT),
                View::getLayoutParams
        );
        fab.setTag(R.id.fab_bottom_margin, lp.leftMargin);
        lp.gravity = Gravity.BOTTOM;
        if (options.alignHorizontally.hasValue()) {
            if ("right".equals(options.alignHorizontally.get())) {
                lp.gravity |= Gravity.RIGHT;
            }
            if ("left".equals(options.alignHorizontally.get())) {
                lp.gravity |= Gravity.RIGHT;
            }
        } else {
            lp.gravity |= Gravity.RIGHT;
        }
        fab.setLayoutParams(lp);
    }

    private void applyFabOptions(Fab fab, FabOptions options) {
        if (options.visible.isTrueOrUndefined()) {
            fab.show(true);
        }
        if (options.visible.isFalse()) {
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
            fab.applyIcon(options.icon.get(), options.iconColor);
        }
        if (options.size.hasValue()) {
            fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        }
        if (options.hideOnScroll.isTrue()) {
            fab.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll.isFalseOrUndefined()) {
            fab.disableCollapse();
        }
    }

    private void mergeFabOptions(Fab fab, FabOptions options) {
        if (options.visible.isTrue()) {
            fab.show(true);
        }
        if (options.visible.isFalse()) {
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
            fab.applyIcon(options.icon.get(), options.iconColor);
        }
        if (options.size.hasValue()) {
            fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        }
        if (options.hideOnScroll.isTrue()) {
            fab.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll.isFalse()) {
            fab.disableCollapse();
        }
    }

    private void applyFabMenuOptions(FabMenu fabMenu, FabOptions options) {
        if (options.visible.isTrueOrUndefined()) {
            fabMenu.showMenuButton(true);
        }
        if (options.visible.isFalse()) {
            fabMenu.hideMenuButton(true);
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
        if (options.hideOnScroll.isTrue()) {
            fabMenu.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll.isFalseOrUndefined()) {
            fabMenu.disableCollapse();
        }
    }

    private void mergeFabMenuOptions(FabMenu fabMenu, FabOptions options) {
        if (options.visible.isTrue()) {
            fabMenu.showMenuButton(true);
        }
        if (options.visible.isFalse()) {
            fabMenu.hideMenuButton(true);
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
        if (options.actionsArray.size() > 0) {
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
        }
        if (options.hideOnScroll.isTrue()) {
            fabMenu.enableCollapse(component.getScrollEventListener());
        }
        if (options.hideOnScroll.isFalse()) {
            fabMenu.disableCollapse();
        }
    }
}
