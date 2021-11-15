package com.reactnativenavigation.viewcontrollers.sidemenu;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.SideMenuRootOptions;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.sidemenu.SideMenu;
import com.reactnativenavigation.views.sidemenu.SideMenuRoot;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.drawerlayout.widget.DrawerLayout.LayoutParams;

import static com.reactnativenavigation.utils.ObjectUtils.perform;

public class SideMenuController extends ParentController<SideMenuRoot> implements DrawerLayout.DrawerListener {

    private ViewController<?> center;
    private ViewController<?> left;
    private ViewController<?> right;
    private SideMenuPresenter presenter;
    private float prevLeftSlideOffset = 0;
    private float prevRightSlideOffset = 0;

    public SideMenuController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions, SideMenuPresenter sideMenuOptionsPresenter, Presenter presenter) {
        super(activity, childRegistry, id, presenter, initialOptions);
        this.presenter = sideMenuOptionsPresenter;
    }

    @Override
    public ViewController<?> getCurrentChild() {
        if (!isDestroyed()) {
            if (getView().isDrawerOpen(Gravity.LEFT)) {
                return left;
            } else if (getView().isDrawerOpen(Gravity.RIGHT)) {
                return right;
            }
        }
        return center;
    }

    @NonNull
    @Override
    public SideMenuRoot createView() {
        SideMenu sideMenu = new SideMenu(getActivity());
        presenter.bindView(sideMenu);
        sideMenu.addDrawerListener(this);

        SideMenuRoot root = new SideMenuRoot(getActivity());
        root.addSideMenu(sideMenu, this);
        return root;
    }

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        center.sendOnNavigationButtonPressed(buttonId);
    }

    @NonNull
    @Override
    public Collection<ViewController<?>> getChildControllers() {
        ArrayList<ViewController<?>> children = new ArrayList<>();
        if (center != null) children.add(center);
        if (left != null) children.add(left);
        if (right != null) children.add(right);
        return children;
    }

    @Override
    public void applyOptions(Options options) {
        super.applyOptions(options);
        presenter.applyOptions(options);
    }

    @Override
    public void applyChildOptions(Options options, ViewController<?> child) {
        super.applyChildOptions(options, child);
        presenter.applyChildOptions(resolveCurrentOptions());
        performOnParentController(parent -> parent.applyChildOptions(this.options, child));
    }

    @Override
    public void mergeChildOptions(Options options, ViewController<?> child) {
        super.mergeChildOptions(options, child);
        presenter.mergeOptions(options.sideMenuRootOptions);
        mergeLockMode(this.initialOptions, options.sideMenuRootOptions);
        performOnParentController(parent -> parent.mergeChildOptions(options, child));
    }

    @Override
    public void onViewWillAppear() {
        super.onViewWillAppear();
        if (left != null) left.performOnView(view -> ((View) view).requestLayout());
        if (right != null) right.performOnView(view -> ((View) view).requestLayout());
    }

    @Override
    public void mergeOptions(Options options) {
        super.mergeOptions(options);
        presenter.mergeOptions(options.sideMenuRootOptions);
    }

    @Override
    public Options resolveCurrentOptions() {
        Options options = super.resolveCurrentOptions();
        if (isDrawerOpen(Gravity.LEFT) || isDrawerOpen(Gravity.RIGHT)) {
            options = options.mergeWith(center.resolveCurrentOptions());
        }
        return options;
    }

    private boolean isDrawerOpen(int gravity) {
        return !isDestroyed() && getView().isDrawerOpen(gravity);
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {
        ViewController<?> view = this.getMatchingView(drawerView);
        view.mergeOptions(this.getOptionsWithVisibility(isLeftMenu(drawerView), true));
    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {
        ViewController<?> view = this.getMatchingView(drawerView);
        view.mergeOptions(this.getOptionsWithVisibility(isLeftMenu(drawerView), false));
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, @FloatRange(from = 0, to = 1) float slideOffset) {
        int gravity = getSideMenuGravity(drawerView);
        if (gravity == Gravity.LEFT) {
            dispatchSideMenuVisibilityEvents(left, prevLeftSlideOffset, slideOffset);
            prevLeftSlideOffset = slideOffset;
        } else if (gravity == Gravity.RIGHT) {
            dispatchSideMenuVisibilityEvents(right, prevRightSlideOffset, slideOffset);
            prevRightSlideOffset = slideOffset;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public boolean handleBack(CommandListener listener) {
        return presenter.handleBack() || center.handleBack(listener) || super.handleBack(listener);
    }

    @Nullable
    @Override
    public ViewController<?> findController(View child) {
        return getView().isSideMenu(child) ? this : super.findController(child);
    }

    public void setCenterController(ViewController<?> centerController) {
        center = centerController;
        getView().setCenter(center);
    }

    public void setLeftController(ViewController<?> controller) {
        left = controller;
        getView().setLeft(left, options);
        presenter.bindLeft(left);
    }

    public void setRightController(ViewController<?> controller) {
        right = controller;
        getView().setRight(right, options);
        presenter.bindRight(right);
    }

    private ViewController<?> getMatchingView(View drawerView) {
        return this.isLeftMenu(drawerView) ? left : right;
    }

    private boolean isLeftMenu(View drawerView) {
        return (left != null && drawerView.equals(left.getView()));
    }

    private int getSideMenuGravity(View drawerView) {
        return ((LayoutParams) drawerView.getLayoutParams()).gravity;
    }

    private Options getOptionsWithVisibility(boolean isLeft, boolean visible) {
        Options options = new Options();
        if (isLeft) {
            options.sideMenuRootOptions.left.visible = new Bool(visible);
        } else {
            options.sideMenuRootOptions.right.visible = new Bool(visible);
        }
        return options;
    }

    private void dispatchSideMenuVisibilityEvents(ViewController<?> drawer, float prevOffset, float offset) {
        if (prevOffset < 1 && offset == 1) {
            drawer.onViewDidAppear();
        } else if (prevOffset == 0 && offset > 0) {
            drawer.onViewWillAppear();
        } else if (prevOffset > 0 && offset == 0) {
            drawer.onViewDisappear();
        }
    }

    private void mergeLockMode(Options out, SideMenuRootOptions sideMenu) {
        perform(sideMenu.left.enabled.get(null), enabled -> out.sideMenuRootOptions.left.enabled = new Bool(enabled));
        perform(sideMenu.right.enabled.get(null), enabled -> out.sideMenuRootOptions.right.enabled = new Bool(enabled));
    }

    @RestrictTo(RestrictTo.Scope.TESTS)
    SideMenu getSideMenu() {
        return presenter.getSideMenu();
    }
}
