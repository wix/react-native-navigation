package com.reactnativenavigation.viewcontrollers.child;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChildControllerTest extends BaseTest {

    private ChildController uut;
    private ChildControllersRegistry childRegistry;
    private OptionsPresenter presenter;

    @Override
    public void beforeEach() {
        childRegistry = spy(new ChildControllersRegistry());
        presenter = Mockito.mock(OptionsPresenter.class);
        uut = new SimpleViewController(newActivity(), childRegistry, "childId", presenter, new Options());
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

    @Test
    public void applyOptions_applyRootOptionsIfRoot() {
        addToParent(newActivity(), uut);
        Options options = new Options();
        uut.applyOptions(options);
        verify(presenter, times(1)).applyRootOptions(uut.getView(), options);
    }

    @Test
    public void applyOptions_doesNotApplyRootOptionsIfHasParent() {
        Options options = new Options();
        uut.setParentController(Mockito.mock(ParentController.class));
        uut.applyOptions(options);
        verify(presenter, times(0)).applyRootOptions(uut.getView(), options);
    }
}
