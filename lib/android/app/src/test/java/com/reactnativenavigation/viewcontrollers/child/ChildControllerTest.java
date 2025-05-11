package com.reactnativenavigation.viewcontrollers.child;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChildControllerTest extends BaseTest {

    private ChildController<?> uut;
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
        ParentController<?> parent = Mockito.mock(ParentController.class);
        Mockito.when(parent.resolveChildOptions(uut)).thenReturn(Options.EMPTY);
        uut.setParentController(parent);
    }

    @Test
    public void onViewAppeared() {
        uut.onViewWillAppear();
        verify(childRegistry, times(1)).onViewAppeared(uut);
    }

    @Test
    public void onViewDisappear() {
        uut.onViewWillAppear();

        uut.onViewDisappear();
        verify(childRegistry, times(1)).onViewDisappear(uut);
    }

    @Test
    public void mergeOptions() {
        newActivity().setContentView(uut.getView());

        Options options = new Options();
        uut.mergeOptions(options);
        verify(presenter).mergeOptions(uut, options);
    }

    @Test
    public void mergeOptions_emptyOptionsAreIgnored() {
        uut.mergeOptions(Options.EMPTY);
        verify(presenter, times(0)).mergeOptions(any(), any());
    }

    @Test
    public void mergeOptions_mergeWithParentViewController() {
        Options options = new Options();
        uut.mergeOptions(options);
        verify(uut.getParentController()).mergeChildOptions(options, uut);
    }

    @Test
    public void destroy() {
        uut.destroy();
        verify(childRegistry).onChildDestroyed(uut);
    }
}
