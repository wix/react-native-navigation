package com.reactnativenavigation.viewcontrollers;

import android.app.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.viewcontrollers.toptabs.*;

import org.junit.*;

import static org.mockito.Mockito.*;

public class TopTabControllerTest extends BaseTest {
    private TopTabController uut;
    private TopTabLayoutMock view;
    private ParentController parentController;
    private Options initialOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        view = spy(new TopTabLayoutMock(activity));
        initialOptions = new Options();
        uut = new TopTabController(activity,
                "componentId",
                "componentName",
                (activity1, componentId, componentName) -> view,
                initialOptions
        );
        parentController = spy(new TopTabsControllerMock(activity, "parentComponentId"));
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
        uut.mergeOptions(new Options());
        verify(parentController, times(2)).applyOptions(initialOptions);
    }

}
