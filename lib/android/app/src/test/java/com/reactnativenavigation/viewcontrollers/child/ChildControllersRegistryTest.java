package com.reactnativenavigation.viewcontrollers.child;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChildControllersRegistryTest extends BaseTest {
    private ChildControllersRegistry uut;
    private ChildController child1;
    private ChildController child2;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        uut = new ChildControllersRegistry();
        child1 = spy(new SimpleViewController(activity, uut, "child1", new Options()));
        child2 = spy(new SimpleViewController(activity, uut, "child2", new Options()));
    }

    @Test
    public void onViewAppeared() {
        child1.onViewWillAppear();
        verify(child1, times(0)).onViewBroughtToFront();
        assertThat(uut.size()).isOne();
    }

    @Test
    public void onViewDisappear() {
        child1.onViewWillAppear();
        child2.onViewWillAppear();
        assertThat(uut.size()).isEqualTo(2);
        child2.onViewDisappear();
        verify(child1, times(1)).onViewBroughtToFront();
        assertThat(uut.size()).isOne();
    }

    @Test
    public void onChildDestroyed() {
        child1.destroy();
        assertThat(uut.size()).isEqualTo(0);
    }

    @Test
    public void onViewDisappear_doesNotCrashIfNoViewsHaveAppeared() {
        uut.onViewDisappear(child1);
    }
}
