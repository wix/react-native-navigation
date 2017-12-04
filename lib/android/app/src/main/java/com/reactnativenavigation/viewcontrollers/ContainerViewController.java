package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.TopBar;
import com.reactnativenavigation.views.TopbarContainerView;

public class ContainerViewController extends ViewController implements NavigationOptionsListener {

	public interface ContainerViewCreator {

		ContainerView create(Activity activity, String containerId, String containerName);
	}

	public interface ContainerView {

		boolean isReady();

		View asView();

		void destroy();

		void sendContainerStart();

		void sendContainerStop();

	}

	private final String containerName;

	private final ContainerViewCreator viewCreator;
	private NavigationOptions navigationOptions;
	private ContainerView containerView;

	private TopBar topBar;

	public ContainerViewController(final Activity activity,
								   final String id,
								   final String containerName,
								   final ContainerViewCreator viewCreator,
								   final NavigationOptions initialNavigationOptions) {
		super(activity, id);
		this.containerName = containerName;
		this.viewCreator = viewCreator;
		this.navigationOptions = initialNavigationOptions;
	}

	@Override
	public void destroy() {
		super.destroy();
		if (containerView != null) containerView.destroy();
		containerView = null;
	}

	@Override
	public void onViewAppeared() {
		super.onViewAppeared();
		ensureViewIsCreated();
		applyOptions();
		containerView.sendContainerStart();
	}

	@Override
	public void onViewDisappear() {
		super.onViewDisappear();
		containerView.sendContainerStop();
	}

	@Override
	protected boolean isViewShown() {
		return super.isViewShown() && containerView.isReady();
	}

	@NonNull
	@Override
	protected View createView() {
		containerView = viewCreator.create(getActivity(), getId(), containerName);
		if (containerView instanceof TopbarContainerView) {
			topBar = ((TopbarContainerView) containerView).getTopBar();
		}
		return containerView.asView();
	}

	@Override
	public void mergeNavigationOptions(NavigationOptions options) {
		navigationOptions.mergeWith(options);
		applyOptions();
	}

	NavigationOptions getNavigationOptions() {
		return navigationOptions;
	}

	private void applyOptions() {
		OptionsPresenter presenter = new OptionsPresenter(this);
		presenter.applyOptions(navigationOptions);
	}

	public TopBar getTopBar() {
		return topBar;
	}

	public ContainerView getContainerView() {
		return containerView;
	}
}
