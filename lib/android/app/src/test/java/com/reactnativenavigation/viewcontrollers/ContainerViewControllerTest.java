package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TestContainerLayout;
import com.reactnativenavigation.parse.NavigationOptions;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContainerViewControllerTest extends BaseTest {
	private ContainerViewController uut;
	private ContainerViewController.IReactView view;

	@Override
	public void beforeEach() {
		super.beforeEach();
		Activity activity = newActivity();
		view = spy(new TestContainerLayout(activity));
		uut = new ContainerViewController(activity, "containerId1", "containerName", new ContainerViewController.ReactViewCreator() {
			@Override
			public ContainerViewController.IReactView create(final Activity activity1, final String containerId, final String containerName) {
				return view;
			}
		}, new NavigationOptions());
	}

	@Test
	public void createsViewFromContainerViewCreator() throws Exception {
		assertThat(uut.getView()).isSameAs(view);
	}

	@Test
	public void containerViewDestroyedOnDestroy() throws Exception {
		uut.ensureViewIsCreated();
		verify(view, times(0)).destroy();
		uut.destroy();
		verify(view, times(1)).destroy();
	}

	@Test
	public void lifecycleMethodsSentToContainerView() throws Exception {
		uut.ensureViewIsCreated();
		verify(view, times(0)).sendContainerStart();
		verify(view, times(0)).sendContainerStop();
		uut.onViewAppeared();
		verify(view, times(1)).sendContainerStart();
		verify(view, times(0)).sendContainerStop();
		uut.onViewDisappear();
		verify(view, times(1)).sendContainerStart();
		verify(view, times(1)).sendContainerStop();
	}

	@Test
	public void isViewShownOnlyIfContainerViewIsReady() throws Exception {
		assertThat(uut.isViewShown()).isFalse();
		uut.ensureViewIsCreated();
		when(view.asView().isShown()).thenReturn(true);
		assertThat(uut.isViewShown()).isFalse();
		when(view.isReady()).thenReturn(true);
		assertThat(uut.isViewShown()).isTrue();
	}
}
