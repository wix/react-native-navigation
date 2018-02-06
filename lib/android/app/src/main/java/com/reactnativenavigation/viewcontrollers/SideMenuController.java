package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.Options;

import java.util.ArrayList;
import java.util.Collection;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ListPopupWindow.WRAP_CONTENT;

public class SideMenuController extends ParentController {

	private ViewController centerController;
	private ViewController leftController;
	private ViewController rightController;

	public SideMenuController(final Activity activity, final String id, Options initialOptions) {
		super(activity, id, initialOptions);
	}

	@NonNull
	@Override
	protected ViewGroup createView() {
        return new DrawerLayout(getActivity());
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

	public void setCenterController(ViewController centerController) {
		this.centerController = centerController;
		View childView = centerController.getView();
		getView().addView(childView);
	}

	public void setLeftController(ViewController leftController) {
		this.leftController = leftController;
		View childView = leftController.getView();
		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		lp.gravity = Gravity.LEFT;
		childView.setLayoutParams(lp);
		getView().addView(childView);
	}

	public void setRightController(ViewController rightController) {
		this.rightController = rightController;
		View childView = rightController.getView();
		DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
		lp.gravity = Gravity.RIGHT;
		childView.setLayoutParams(lp);
		getView().addView(childView);
	}
}
