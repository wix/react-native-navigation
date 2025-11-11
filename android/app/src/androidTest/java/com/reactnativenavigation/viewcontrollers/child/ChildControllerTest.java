package com.reactnativenavigation.viewcontrollers.child;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;

import com.reactnativenavigation.BaseAndroidTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ChildControllerTest extends BaseAndroidTest {
    private ChildController<?> uut;
    private ChildControllersRegistry childRegistry;
    private Presenter presenter;
    private Options resolvedOptions = new Options();

    @Rule
    public ActivityScenarioRule<TestActivity> rule = new ActivityScenarioRule<>(TestActivity.class);

    @Before
    public void beforeEach() {
        rule.getScenario().onActivity(activity -> {
            childRegistry = spy(new ChildControllersRegistry());
            presenter = Mockito.mock(Presenter.class);
            uut = new SimpleViewController(activity, childRegistry, "childId", presenter, new Options()) {
                @Override
                public Options resolveCurrentOptions() {
                    return resolvedOptions;
                }
            };
            ParentController<?> parent = Mockito.mock(ParentController.class);
            Mockito.when(parent.resolveChildOptions(uut)).thenReturn(Options.EMPTY);
            uut.setParentController(parent);
        });
    }

    @Test
    public void onViewAppeared() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> uut.onViewWillAppear());
        verify(childRegistry, times(1)).onViewAppeared(uut);
    }

    @Test
    public void onViewDisappear() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            uut.onViewWillAppear();
            uut.onViewDisappear();
        });
        verify(childRegistry, times(1)).onViewDisappear(uut);
    }

    @Test
    public void mergeOptions() {
        try (ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class)) {
            scenario.onActivity(activity -> {
                activity.setContentView(uut.getView());
                Options options = new Options();
                uut.mergeOptions(options);
                verify(presenter).mergeOptions(uut, options);
            });
        }
    }

    @Test
    public void mergeOptions_emptyOptionsAreIgnored() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            uut.mergeOptions(Options.EMPTY);
            verify(presenter, times(0)).mergeOptions(any(), any());
        });
    }

    @Test
    public void mergeOptions_mergeWithParentViewController() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            Options options = new Options();
            uut.mergeOptions(options);
            verify(uut.getParentController()).mergeChildOptions(options, uut);
        });
    }

    @Test
    public void destroy() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> uut.destroy());
        verify(childRegistry).onChildDestroyed(uut);
    }
}