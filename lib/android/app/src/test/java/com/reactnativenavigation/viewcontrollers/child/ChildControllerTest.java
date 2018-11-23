package com.reactnativenavigation.viewcontrollers.child;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChildControllerTest extends BaseTest {

    private ChildController uut;
    private ChildControllersRegistry childRegistry;
    private Presenter presenter;
    private Options resolvedOptions = new Options();

    @Override
    public void beforeEach() {
        childRegistry = spy(new ChildControllersRegistry());
        presenter = Mockito.mock(Presenter.class);
        uut = new SimpleViewController(newActivity(), childRegistry, "childId", presenter, new Options()) {
            @Override
            public Options resolveCurrentOptions() {
                return resolvedOptions;
            }
        };
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
        newActivity().setContentView(uut.getView());
        verify(presenter).applyOptions(uut.getView(), resolvedOptions);
        verify(presenter).applyRootOptions(uut.getView(), resolvedOptions);
    }

    @Test
    public void applyOptions_doesNotApplyRootOptionsIfHasParent() {
        Options options = new Options();
        uut.setParentController(Mockito.mock(ParentController.class));
        uut.applyOptions(options);
        verify(presenter, times(0)).applyRootOptions(uut.getView(), options);
    }

    @Test
    public void mergeOptions() {
        newActivity().setContentView(uut.getView());

        Options options = new Options();
        uut.mergeOptions(options);
        verify(presenter).mergeOptions(uut.getView(), options);
    }

    @Test
    public void mergeOptions_emptyOptionsAreIgnored() {
        uut.mergeOptions(Options.EMPTY);
        verify(presenter, times(0)).mergeOptions(any(), any());
    }
}
