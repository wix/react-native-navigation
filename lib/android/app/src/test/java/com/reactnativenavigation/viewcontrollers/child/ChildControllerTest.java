package com.reactnativenavigation.viewcontrollers.child;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;

import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChildControllerTest extends BaseTest {

    private ChildController uut;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        childRegistry = spy(new ChildControllersRegistry());
        uut = new SimpleViewController(newActivity(), childRegistry, "childId", new Options());
    }

    @Test
    public void onViewAppeared() {
        uut.onViewAppeared();
        verify(childRegistry, times(1)).onViewAppeared(uut);
    }

    @Test
    public void onViewDisappear() {
        uut.onViewAppeared();

        uut.onViewDisappear();
        verify(childRegistry, times(1)).onViewDisappear(uut);
    }
}
