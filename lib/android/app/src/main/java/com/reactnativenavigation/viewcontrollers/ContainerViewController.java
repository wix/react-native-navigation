package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.view.View;

import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.presentation.NavigationOptionsListener;
import com.reactnativenavigation.views.ReactContainer;
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
	private ReactContainer container;

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

    @RestrictTo(RestrictTo.Scope.TESTS)
    TopBar getTopBar() {
        return container.getTopBar();
    }

	@Override
	public void destroy() {
		super.destroy();
		if (container != null) container.destroy();
		container = null;
	}

	@Override
	public void onViewAppeared() {
		super.onViewAppeared();
		ensureViewIsCreated();
		container.applyOptions(navigationOptions);
		container.sendContainerStart();
	}

	@Override
	public void onViewDisappear() {
		super.onViewDisappear();
		container.sendContainerStop();
	}

	@Override
	protected boolean isViewShown() {
		return super.isViewShown() && container.isReady();
	}

	@NonNull
	@Override
	protected View createView() {
		container = (ReactContainer) viewCreator.create(getActivity(), getId(), containerName);
        return container.asView();
	}

	@Override
	public void mergeNavigationOptions(NavigationOptions options) {
		navigationOptions.mergeWith(options);
        container.applyOptions(navigationOptions);
	}

	NavigationOptions getNavigationOptions() {
		return navigationOptions;
	}
}
