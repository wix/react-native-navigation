package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.SideMenuOptions;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.SideMenuOptionsPresenter;
import com.reactnativenavigation.views.Component;

import java.util.ArrayList;
import java.util.Collection;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SideMenuController extends ParentController<DrawerLayout> {

	private ViewController centerController;
	private ViewController leftController;
	private ViewController rightController;

	public SideMenuController(Activity activity, ChildControllersRegistry childRegistry, String id, Options initialOptions, OptionsPresenter presenter) {
		super(activity, childRegistry, id, presenter, initialOptions);
	}

    @Override
    protected ViewController getCurrentChild() {
	    if (getView().isDrawerOpen(Gravity.LEFT)) {
            return leftController;
        } else if (getView().isDrawerOpen(Gravity.RIGHT)) {
            return rightController;
        }
        return centerController;
    }

    @NonNull
	@Override
	protected DrawerLayout createView() {
        return new DrawerLayout(getActivity());
	}

    @Override
    public void sendOnNavigationButtonPressed(String buttonId) {
        centerController.sendOnNavigationButtonPressed(buttonId);
    }

    @NonNull
	@Override
	public Collection<ViewController> getChildControllers() {
		ArrayList<ViewController> children = new ArrayList<>();
		if (centerController != null) children.add(centerController);
		if (leftController != null) children.add(leftController);
		if (rightController != null) children.add(rightController);
		return children;
	}

    @Override
    public void applyChildOptions(Options options, Component child) {
        super.applyChildOptions(options, child);
        performOnParentController(parentController ->
                ((ParentController) parentController).applyChildOptions(this.options, child)
        );
    }

    @Override
    public void mergeChildOptions(Options options, Component child) {
        super.mergeChildOptions(options, child);
        new SideMenuOptionsPresenter(getView()).present(options.sideMenuRootOptions);
        performOnParentController(parentController ->
                ((ParentController) parentController).mergeChildOptions(options.copy().clearSideMenuOptions(), child)
        );
    }

    @Override
    public void mergeOptions(Options options) {
        super.mergeOptions(options);
        new SideMenuOptionsPresenter(getView()).present(this.options.sideMenuRootOptions);
    }

    public void setCenterController(ViewController centerController) {
		this.centerController = centerController;
		View childView = centerController.getView();
		getView().addView(childView);
	}

    public void setLeftController(ViewController controller) {
        this.leftController = controller;
        int height = this.getHeight(options.sideMenuRootOptions.left);
        int width = this.getWidth(options.sideMenuRootOptions.left);
        getView().addView(controller.getView(), new LayoutParams(width, height, Gravity.LEFT));
    }

    public void setRightController(ViewController controller) {
        this.rightController = controller;
        int height = this.getHeight(options.sideMenuRootOptions.right);
        int width = this.getWidth(options.sideMenuRootOptions.right);
        getView().addView(controller.getView(), new LayoutParams(width, height, Gravity.RIGHT));
    }

    protected int getWidth(SideMenuOptions sideMenuOptions) {
        int width = MATCH_PARENT;
        if (sideMenuOptions.width.hasValue()) {
            width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sideMenuOptions.width.get(), Resources.getSystem().getDisplayMetrics());
        }
        return width;
    }

    protected int getHeight(SideMenuOptions sideMenuOptions) {
        int height = MATCH_PARENT;
        if (sideMenuOptions.height.hasValue()) {
            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sideMenuOptions.height.get(), Resources.getSystem().getDisplayMetrics());
        }
        return height;
    }
}
