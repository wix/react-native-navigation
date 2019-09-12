package com.reactnativenavigation.presentation;


import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.reactnativenavigation.R;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.utils.*;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.ReactComponent;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;

public class FabPresenter {
    private ReactComponent component;
    private CoordinatorLayout parent;

    private Fab fab;
    private FabMenu fabMenu;

    public void applyOptions(FabOptions options, @NonNull ViewController child, @NonNull ParentController parent) {
        bindView(child, parent);


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

    public void mergeOptions(FabOptions options, @NonNull ViewController child, @NonNull ParentController parent) {
        bindView(child, parent);

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
            fabMenu = new FabMenu(parent.getContext(), options.id.get());
            setParams(fabMenu, options);
            applyFabMenuOptions(fabMenu, options);
            parent.addView(fabMenu);
        } else {
            fab = new Fab(parent.getContext(), options.id.get());
            setParams(fab, options);
            applyFabOptions(fab, options);
            fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            parent.addView(fab);
        }
    }

    private void removeFabMenu() {
        if (fabMenu != null) {
            fabMenu.hideMenuButton(true);
            parent.removeView(fabMenu);
            fabMenu = null;
        }
    }

    private void removeFab() {
        if (fab != null) {
            fab.hide(true);
            parent.removeView(fab);
            fab = null;
        }
    }

    private void setParams(View fab, FabOptions options) {
        CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        CoordinatorLayout.LayoutParams layoutParamsCoordinator = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        layoutParamsCoordinator.setMargins(
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16)
        );

        layoutParamsCoordinator.gravity =  Gravity.BOTTOM + Gravity.END;

        if (options.alignHorizontally.hasValue()) {
            if ("right".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.RIGHT;
            }
            else if ("left".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.LEFT;
            }
            else if ("center".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.CENTER_HORIZONTAL;
            }
            else if ("start".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.START;

            }
            else {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.END;
            }
        }
        else {
            layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.END;
        }

        if (options.alignVertically.hasValue()) {
            if ("top".equals(options.alignVertically.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.TOP;
            }
            else {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.BOTTOM;
            }
        }
        else {
            layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.BOTTOM;
        }

        layoutParams = layoutParamsCoordinator;

        fab.setLayoutParams(layoutParams);
    }

    private void mergeParams(View fab, FabOptions options) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();

        CoordinatorLayout.LayoutParams layoutParamsCoordinator = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);

        layoutParamsCoordinator.setMargins(
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16),
                UiUtils.dpToPx(fab.getContext(), 16)
        );

        if (options.alignHorizontally.hasValue()) {
            if ("right".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.RIGHT;
            }
            else if ("left".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.LEFT;
            }
            else if ("center".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.CENTER_HORIZONTAL;
            }
            else if ("start".equals(options.alignHorizontally.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.START;

            }
            else {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.END;
            }
        }
        else {
            layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.END;
        }

        if (options.alignVertically.hasValue()) {
            if ("top".equals(options.alignVertically.get())) {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.TOP;
            }
            else {
                layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.BOTTOM;
            }
        }
        else {
            layoutParamsCoordinator.gravity = layoutParamsCoordinator.gravity | Gravity.BOTTOM;
        }

        layoutParams = layoutParamsCoordinator;

        fab.setLayoutParams(layoutParams);
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
            fabMenu.showMenu(true);
        }
        if (options.visible.isFalse()) {
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
        for (Fab fabStored : fabMenu.getActions()) {
            fabMenu.removeMenuButton(fabStored);
        }
        fabMenu.getActions().clear();
        for (FabOptions fabOption : options.actionsArray) {
            Fab fab = new Fab(parent.getContext(), fabOption.id.get());
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
            fabMenu.showMenu(true);
        }
        if (options.visible.isFalse()) {
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
        if (options.actionsArray.size() > 0) {
            for (Fab fabStored : fabMenu.getActions()) {
                fabMenu.removeMenuButton(fabStored);
            }
            fabMenu.getActions().clear();
            for (FabOptions fabOption : options.actionsArray) {
                Fab fab = new Fab(parent.getContext(), fabOption.id.get());
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

    public void applyTopInset(int topInset) {
        if (parent == null) return;

        int statusBarHeight = StatusBarUtils.getStatusBarHeight(parent.getContext());

        if (fab != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            lp.topMargin = topInset + statusBarHeight + (int) UiUtils.dpToPx(fab.getContext(), 16);
            fab.requestLayout();
        }
        if (fabMenu != null ) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fabMenu.getLayoutParams();
            lp.topMargin = topInset + statusBarHeight + (int) UiUtils.dpToPx(fab.getContext(), 16);
            fabMenu.requestLayout();
        }
    }

    public void applyBottomInset(int bottomInset) {
        if (parent == null) return;

        if (fab != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            lp.bottomMargin = bottomInset + (int) UiUtils.dpToPx(fab.getContext(), 16);
            fab.requestLayout();
        }
        if (fabMenu != null ) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fabMenu.getLayoutParams();
            lp.bottomMargin = bottomInset + (int) UiUtils.dpToPx(fab.getContext(), 16);
            fabMenu.requestLayout();
        }
    }

    public void bindView(ViewController child, ParentController parent) {
        this.component = (ReactComponent) child.getView();
        this.parent = (CoordinatorLayout) parent.getView();
    }
}