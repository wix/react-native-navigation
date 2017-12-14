package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.views.Container;
import com.reactnativenavigation.views.TopBar;

public class ContainerViewController extends ViewController implements NavigationOptionsListener {

    public interface ReactViewCreator {

        IReactView create(Activity activity, String containerId, String containerName);
	}

	public interface IReactView {

		boolean isReady();

		View asView();

		void destroy();

		void sendContainerStart();

		void sendContainerStop();

	}

	private final String containerName;

	private final ReactViewCreator viewCreator;
	private NavigationOptions navigationOptions;
	private IReactView containerView;

	private TopBar topBar;

    public void setTitle(String title) {
        topBar.setTitle(title);
    }

    public void setBackgroundColor(int backgroundColor) {
        topBar.setBackgroundColor(backgroundColor);
    }

    public void setTitleTextColor(int textColor) {
        topBar.setTitleTextColor(textColor);
    }

    public void setTitleFontSize(float textFontSize) {
        topBar.setTitleFontSize(textFontSize);
    }

	public ContainerViewController(final Activity activity,
								   final String id,
								   final String containerName,
								   final ReactViewCreator viewCreator,
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
		if (containerView instanceof Container) {
			topBar = ((Container) containerView).getTopBar();
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

	public IReactView getContainerView() {
		return containerView;
	}
}
