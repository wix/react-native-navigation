package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.ChildController;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BackButtonHelperTest extends BaseTest {
    private BackButtonHelper uut;
    private StackController stack;
    private ChildController child1;
    private ChildController child2;

    @Override
    public void beforeEach() {
        uut = new BackButtonHelper();
        Activity activity = newActivity();
        ChildControllersRegistry childRegistry = new ChildControllersRegistry();
        stack = TestUtils.newStackController(activity)
                .setChildRegistry(childRegistry)
                .setBackButtonHelper(uut)
                .build();
        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        stack.ensureViewIsCreated();
    }

    @Test
    public void addToChild() {
        uut.addToPushedChild(child1);
        verify(child1).mergeOptions(any());
    }

    @Test
    public void addToChild_addsIfStackContainsMoreThenOneChild() {
        disablePushAnimation(child1, child2);
        stack.push(child1, new CommandListenerAdapter());
        stack.push(child2, new CommandListenerAdapter());

        ArgumentCaptor<Options> optionWithBackButton = ArgumentCaptor.forClass(Options.class);
        verify(child2, times(1)).mergeOptions(optionWithBackButton.capture());
        assertThat(optionWithBackButton.getValue().topBar.buttons.back.visible.get()).isTrue();
    }

    @Test
    public void addToChild_doesNotAddIfBackButtonHidden() {
        disablePushAnimation(child1, child2);
        stack.push(child1, new CommandListenerAdapter());
        child2.options.topBar.buttons.back.visible = new Bool(false);
        stack.push(child2, new CommandListenerAdapter());

        verify(child2, times(0)).mergeOptions(any());
    }

    @Test
    public void clear() {
        child1.options.topBar.buttons.back.visible = new Bool(true);
        uut.clear(child1);
        assertThat(child1.options.topBar.buttons.back.visible.get()).isFalse();
    }
}
