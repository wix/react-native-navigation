package com.reactnativenavigation.viewcontrollers.child;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

import com.reactnativenavigation.BaseAndroidTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.options.Options;

import org.assertj.core.api.Java6Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ChildControllersRegistryTest extends BaseAndroidTest {
    private ChildControllersRegistry uut;
    private ChildController<?> child1;
    private ChildController<?> child2;

    @Rule
    public ActivityScenarioRule<TestActivity> rule = new ActivityScenarioRule<>(TestActivity.class);


    @Before
    public void beforeEach() {
        rule.getScenario().onActivity(activity -> {
            uut = new ChildControllersRegistry();
            child1 = Mockito.spy(new SimpleViewController(activity, uut, "child1", new Options()));
            child2 = Mockito.spy(new SimpleViewController(activity, uut, "child2", new Options()));
        });
    }

    @Test
    public void onViewAppeared() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> child1.onViewWillAppear());
        Mockito.verify(child1, Mockito.times(0)).onViewBroughtToFront();
        Java6Assertions.assertThat(uut.size()).isOne();
    }

    @Test
    public void onViewDisappear() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> {
            child1.onViewWillAppear();
            child2.onViewWillAppear();
        });
        Java6Assertions.assertThat(uut.size()).isEqualTo(2);
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> child2.onViewDisappear());
        Mockito.verify(child1, Mockito.times(1)).onViewBroughtToFront();
        Java6Assertions.assertThat(uut.size()).isOne();
    }

    @Test
    public void onChildDestroyed() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> child1.destroy());
        Java6Assertions.assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void onViewDisappear_doesNotCrashIfNoViewsHaveAppeared() {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(() -> uut.onViewDisappear(child1));
    }
}
