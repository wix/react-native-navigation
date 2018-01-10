package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TopTabLayoutMock;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;

import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopTabControllerTest extends BaseTest {
    private TopTabController uut;
    private TopTabLayoutMock view;
    private ParentController parentController;
    private NavigationOptions initialOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        view = spy(new TopTabLayoutMock(activity));
        initialOptions = new NavigationOptions();
        uut = new TopTabController(activity,
                "containerId",
                "containerName",
                (activity1, containerId, containerName) -> view,
                initialOptions
        );
        parentController = spy(new TopTabsControllerMock(activity, "parentContainerId"));
        uut.setParentController(parentController);
    }

    @Test
    public void styleIsAppliedOnParentControllerWhenTabIsVisible() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(parentController, times(1)).applyOptions(initialOptions);
    }

    @Test
    public void styleIsAppliedOnParentControllerWhenOptionsAreSetDynamically() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        uut.mergeNavigationOptions(new NavigationOptions());
        verify(parentController, times(2)).applyOptions(initialOptions);
    }

}
