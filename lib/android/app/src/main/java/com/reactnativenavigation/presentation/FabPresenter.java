package com.reactnativenavigation.presentation;


import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.utils.CoordinatorLayoutUtils;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;
import static com.reactnativenavigation.utils.CollectionUtils.*;

public class FabPresenter {
    private Fab fab;
    private FabMenu fabMenu;
    private final int margin;
    private Options defaultOptions;

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public FabPresenter(Context context, Options defaultOptions) {
        this.defaultOptions = defaultOptions;
        margin = UiUtils.dpToPx(context, 16);
    }

    public void applyOptions(ViewController view, FabOptions options) {
        FabOptions withDefault = options.copy().mergeWithDefault(defaultOptions.fabOptions);
        if (withDefault.hasValue() && view.getId().equals(withDefault.layoutId)) {
            if (fabMenu != null && fabMenu.getFabId().equals(withDefault.id.get())) {
                fabMenu.bringToFront();
                applyFabMenuOptions(fabMenu, withDefault, view);
                fabMenu.setLayoutParams(createLayoutParams(withDefault));
            } else if (fab != null && fab.getFabId().equals(withDefault.id.get())) {
                fab.bringToFront();
                applyFabOptions(fab, withDefault, view);
                fab.setLayoutParams(createLayoutParams(withDefault));
                fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(withDefault.id.get()));
            } else {
                view.getView().addView(createFab(withDefault, view));
            }
        } else {
            removeFab(view);
            removeFabMenu(view);
        }
    }

    public void mergeOptions(ViewController view, FabOptions options) {
        if (options.hasValue()) {
            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
                fabMenu.setLayoutParams(createLayoutParams(options));
                fabMenu.bringToFront();
                mergeFabMenuOptions(fabMenu, options, view);
            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
                fab.setLayoutParams(createLayoutParams(options));
                fab.bringToFront();
                mergeFabOptions(fab, options, view);
                fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(options.id.get()));
            } else {
                view.getView().addView(createFab(options, view));
            }
        }
    }

    private View createFab(FabOptions options, ViewController view) {
        if (options.actionsArray.size() > 0) {
            fabMenu = new FabMenu(view.getActivity(), options.id.get());
            fabMenu.setLayoutParams(createLayoutParams(options));
            applyFabMenuOptions(fabMenu, options, view);
            return fabMenu;
        } else {
            fab = new Fab(view.getActivity(), options.id.get());
            fab.setLayoutParams(createLayoutParams(options));
            applyFabOptions(fab, options, view);
            fab.setOnClickListener(v -> view.sendOnNavigationButtonPressed(options.id.get()));
            return fab;
        }
    }

    private void removeFabMenu(ViewController view) {
        if (fabMenu != null) {
            fabMenu.hideMenuButton(true);
            view.getView().removeView(fabMenu);
            fabMenu = null;
        }
    }

    private void removeFab(ViewController view) {
        if (fab != null) {
            fab.hide(true);
            view.getView().removeView(fab);
            fab = null;
        }
    }

    private CoordinatorLayout.LayoutParams createLayoutParams(FabOptions options) {
        CoordinatorLayout.LayoutParams lp = CoordinatorLayoutUtils.wrapContent(margin, Gravity.BOTTOM);
        FabOptions withDefault = options.copy().mergeWithDefault(defaultOptions.fabOptions);

        switch (withDefault.alignHorizontally.get("end")) {
            case "right":
                lp.gravity = lp.gravity | Gravity.RIGHT;
                break;
            case "left":
                lp.gravity = lp.gravity | Gravity.LEFT;
                break;
            case "center":
                lp.gravity = lp.gravity | Gravity.CENTER_HORIZONTAL;
                break;
            case "start":
                lp.gravity = lp.gravity | Gravity.START;
                break;
            default:
            case "end":
                lp.gravity = lp.gravity | Gravity.END;
                break;
        }
        return lp;
    }

    private void applyFabOptions(Fab fab, FabOptions options, ViewController view) {
        if (options.visible.isTrueOrUndefined()) fab.show(true);
        if (options.visible.isFalse()) fab.hide(true);
        if (options.backgroundColor.hasValue()) fab.setColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fab.setColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fab.setColorRipple(options.rippleColor.get());
        if (options.icon.hasValue()) fab.applyIcon(options.icon.get(), options.iconColor);
        if (options.size.hasValue()) fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        if (options.hideOnScroll.isTrue()) fab.enableCollapse(view.getScrollEventListener());
        if (options.hideOnScroll.isFalseOrUndefined()) fab.disableCollapse();
    }

    private void mergeFabOptions(Fab fab, FabOptions options, ViewController view) {
        if (options.visible.isTrue()) fab.show(true);
        if (options.visible.isFalse()) fab.hide(true);
        if (options.backgroundColor.hasValue()) fab.setColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fab.setColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fab.setColorRipple(options.rippleColor.get());
        if (options.icon.hasValue()) fab.applyIcon(options.icon.get(), options.iconColor);
        if (options.size.hasValue()) fab.setButtonSize("mini".equals(options.size.get()) ? SIZE_MINI : SIZE_NORMAL);
        if (options.hideOnScroll.isTrue()) fab.enableCollapse(view.getScrollEventListener());
        if (options.hideOnScroll.isFalse()) fab.disableCollapse();
    }

    private void applyFabMenuOptions(FabMenu fabMenu, FabOptions options, ViewController child) {
        if (options.visible.isTrueOrUndefined()) fabMenu.showMenu(true);
        if (options.visible.isFalse()) fabMenu.hideMenu(true);

        if (options.backgroundColor.hasValue()) fabMenu.setMenuButtonColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fabMenu.setMenuButtonColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fabMenu.setMenuButtonColorRipple(options.rippleColor.get());

        forEach(fabMenu.getActions(), fabMenu::removeMenuButton);
        fabMenu.getActions().clear();
        for (FabOptions fabOption : options.actionsArray) {
            Fab fab = new Fab(child.getActivity(), fabOption.id.get());
            applyFabOptions(fab, fabOption, child);
            fab.setOnClickListener(v -> child.sendOnNavigationButtonPressed(options.id.get()));

            fabMenu.getActions().add(fab);
            fabMenu.addMenuButton(fab);
        }
        if (options.hideOnScroll.isTrue()) fabMenu.enableCollapse(child.getScrollEventListener());
        if (options.hideOnScroll.isFalseOrUndefined()) fabMenu.disableCollapse();
    }

    private void mergeFabMenuOptions(FabMenu fabMenu, FabOptions options, ViewController child) {
        if (options.visible.isTrue()) fabMenu.showMenu(true);
        if (options.visible.isFalse()) fabMenu.hideMenu(true);

        if (options.backgroundColor.hasValue()) fabMenu.setMenuButtonColorNormal(options.backgroundColor.get());
        if (options.clickColor.hasValue()) fabMenu.setMenuButtonColorPressed(options.clickColor.get());
        if (options.rippleColor.hasValue()) fabMenu.setMenuButtonColorRipple(options.rippleColor.get());
        if (options.actionsArray.size() > 0) {
            forEach(fabMenu.getActions(), fabMenu::removeMenuButton);
            fabMenu.getActions().clear();

            for (FabOptions fabOption : options.actionsArray) {
                Fab fab = new Fab(child.getActivity(), fabOption.id.get());
                applyFabOptions(fab, fabOption, child);
                fab.setOnClickListener(v -> child.sendOnNavigationButtonPressed(options.id.get()));

                fabMenu.getActions().add(fab);
                fabMenu.addMenuButton(fab);
            }
        }
        if (options.hideOnScroll.isTrue()) fabMenu.enableCollapse(child.getScrollEventListener());
        if (options.hideOnScroll.isFalse()) fabMenu.disableCollapse();
    }

    public void applyBottomInset(int bottomInset) {
        if (fab != null) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            lp.bottomMargin = bottomInset + margin;
            fab.requestLayout();
        }
        if (fabMenu != null ) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fabMenu.getLayoutParams();
            lp.bottomMargin = bottomInset + margin;
            fabMenu.requestLayout();
        }
    }
}