package com.reactnativenavigation.viewcontrollers.stack;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.R;
import com.reactnativenavigation.options.FabOptions;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewExtensionsKt;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.stack.fab.Fab;
import com.reactnativenavigation.views.stack.fab.FabMenu;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.clans.fab.FloatingActionButton.SIZE_MINI;
import static com.github.clans.fab.FloatingActionButton.SIZE_NORMAL;
import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class FabPresenter {
    private static final int DURATION = 200;

    private ViewGroup viewGroup;

    private Fab fab;
    private FabMenu fabMenu;

    public void applyOptions(FabOptions options, @NonNull ViewController<?> component, @NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;

        if (options.id.hasValue()) {
            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
                fabMenu.bringToFront();
                applyFabMenuOptions(component, fabMenu, options);
                setParams(component, fabMenu, options);
            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
                fab.bringToFront();
                setParams(component, fab, options);
                applyFabOptions(component, fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            } else {
                createFab(component, options);
            }
        } else {
            removeFab();
            removeFabMenu();
        }
    }

    public void mergeOptions(FabOptions options, @NonNull ViewController<?> component, @NonNull ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        if (options.id.hasValue()) {
            if (fabMenu != null && fabMenu.getFabId().equals(options.id.get())) {
                mergeParams(fabMenu, options);
                fabMenu.bringToFront();
                mergeFabMenuOptions(component, fabMenu, options);
            } else if (fab != null && fab.getFabId().equals(options.id.get())) {
                mergeParams(fab, options);
                fab.bringToFront();
                mergeFabOptions(component, fab, options);
                fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            } else {
                createFab(component, options);
            }
        }
    }

    private void createFab(ViewController<?> component, FabOptions options) {
        ViewExtensionsKt.removeFromParent(fabMenu);
        ViewExtensionsKt.removeFromParent(fab);
        if (options.actionsArray.size() > 0) {
            fabMenu = new FabMenu(viewGroup.getContext(), options.id.get());
            setParams(component, fabMenu, options);
            applyFabMenuOptions(component, fabMenu, options);
            viewGroup.addView(fabMenu);
        } else {
            fab = new Fab(viewGroup.getContext(), options.id.get());
            setParams(component, fab, options);
            applyFabOptions(component, fab, options);
            viewGroup.addView(fab);
            fab.setOnClickListener(v -> component.sendOnNavigationButtonPressed(options.id.get()));
            UiUtils.doOnLayout(fab, () -> {
                fab.setPivotX(fab.getWidth() / 2f);
                fab.setPivotY(fab.getHeight() / 2f);
            });
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
            animateHide(() -> {
                viewGroup.removeView(fab);
                fab = null;
            });
        }
    }

    public void animateHide(Runnable onAnimationEnd) {
        fab.animate()
                .scaleX(0f)
                .scaleY(0f)
                .setDuration(DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onAnimationEnd.run();
                    }
                });
    }

    private void setParams(ViewController<?> component, View fab, FabOptions options) {
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lp.rightMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        lp.leftMargin = (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        lp.bottomMargin = component.getBottomInset() + (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin);
        fab.setTag(R.id.fab_bottom_margin, (int) viewGroup.getContext().getResources().getDimension(R.dimen.margin));
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

    private void applyFabOptions(ViewController<?> component, Fab fab, FabOptions options) {
        if (options.visible.isTrueOrUndefined()) {
            fab.setScaleX(0.6f);
            fab.setScaleY(0.6f);
            fab.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(DURATION)
                    .start();
        }
        if (options.visible.isFalse()) {
            fab.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(DURATION)
                    .start();
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

    private void mergeFabOptions(ViewController<?> component, Fab fab, FabOptions options) {
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

    private void applyFabMenuOptions(ViewController<?> component, FabMenu fabMenu, FabOptions options) {
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
            applyFabOptions(component, fab, fabOption);
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

    private void mergeFabMenuOptions(ViewController<?> component, FabMenu fabMenu, FabOptions options) {
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
                applyFabOptions(component, fab, fabOption);
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

    public void onConfigurationChanged( Options options) {
        FabOptions fabOptions = options.fabOptions;
        if(fab!=null){
            if (fabOptions.backgroundColor.hasValue()) {
                fab.setColorNormal(fabOptions.backgroundColor.get());
            }
            if (fabOptions.clickColor.hasValue()) {
                fab.setColorPressed(fabOptions.clickColor.get());
            }
            if (fabOptions.rippleColor.hasValue()) {
                fab.setColorRipple(fabOptions.rippleColor.get());
            }
            if (fabOptions.icon.hasValue()) {
                fab.applyIcon(fabOptions.icon.get(), fabOptions.iconColor);
            }
        }

        if(fabMenu!=null){
            if (fabOptions.backgroundColor.hasValue()) {
                fabMenu.setMenuButtonColorNormal(fabOptions.backgroundColor.get());
            }
            if (fabOptions.clickColor.hasValue()) {
                fabMenu.setMenuButtonColorPressed(fabOptions.clickColor.get());
            }
            if (fabOptions.rippleColor.hasValue()) {
                fabMenu.setMenuButtonColorRipple(fabOptions.rippleColor.get());
            }
        }
    }
}
