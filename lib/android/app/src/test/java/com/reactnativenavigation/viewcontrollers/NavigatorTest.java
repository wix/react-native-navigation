package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TestStackAnimator;
import com.reactnativenavigation.utils.CompatUtils;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigatorTest extends BaseTest {
	private Activity activity;
	private Navigator uut;
	private ViewController child1;
	private ViewController child2;
	private ViewController child3;
	private ViewController child4;
	private ViewController child5;

	@Override
	public void beforeEach() {
		super.beforeEach();
		activity = newActivity();
		uut = new Navigator(activity);
		child1 = new SimpleViewController(activity, "child1");
		child2 = new SimpleViewController(activity, "child2");
		child3 = new SimpleViewController(activity, "child3");
		child4 = new SimpleViewController(activity, "child4");
		child5 = new SimpleViewController(activity, "child5");
	}

	@Test
	public void setRoot_AddsChildControllerView() throws Exception {
		assertThat(uut.getView().getChildCount()).isZero();
		uut.setRoot(child1);
		assertIsChildById(uut.getView(), child1.getView());
	}

	@Test
	public void setRoot_ReplacesExistingChildControllerViews() throws Exception {
		uut.setRoot(child1);
		uut.setRoot(child2);
		assertIsChildById(uut.getView(), child2.getView());
	}

	@Test
	public void hasUniqueId() throws Exception {
		assertThat(uut.getId()).startsWith("navigator");
		assertThat(new Navigator(activity).getId()).isNotEqualTo(uut.getId());
	}

	@Test
	public void push() throws Exception {
		StackController stackController = newStack();
		stackController.push(child1);
		uut.setRoot(stackController);

		assertIsChildById(uut.getView(), stackController.getView());
		assertIsChildById(stackController.getView(), child1.getView());

		uut.push(child1.getId(), child2);

		assertIsChildById(uut.getView(), stackController.getView());
		assertIsChildById(stackController.getView(), child2.getView());
	}

	@Test
	public void push_InvalidPushWithoutAStack_DoesNothing() throws Exception {
		uut.setRoot(child1);
		uut.push(child1.getId(), child2);
		assertIsChildById(uut.getView(), child1.getView());
	}

	@Test
	public void push_OnCorrectStackByFindingChildId() throws Exception {
		BottomTabsController bottomTabsController = newTabs();
		StackController stack1 = newStack();
		StackController stack2 = newStack();
		stack1.push(child1);
		stack2.push(child2);
		bottomTabsController.setTabs(Arrays.<ViewController>asList(stack1, stack2));
		uut.setRoot(bottomTabsController);

		SimpleViewController newChild = new SimpleViewController(activity, "new child");
		uut.push(child2.getId(), newChild);

		assertThat(stack1.getChildControllers()).doesNotContain(newChild);
		assertThat(stack2.getChildControllers()).contains(newChild);
	}

	@Test
	public void pop_InvalidDoesNothing() throws Exception {
		uut.pop("123");
		uut.setRoot(child1);
		uut.pop(child1.getId());
		assertThat(uut.getChildControllers()).hasSize(1);
	}

	@Test
	public void pop_FromCorrectStackByFindingChildId() throws Exception {
		BottomTabsController bottomTabsController = newTabs();
		StackController stack1 = newStack();
		StackController stack2 = newStack();
		stack1.push(child1);
		stack2.push(child2);
		stack2.push(child3);
		stack2.push(child4);
		bottomTabsController.setTabs(Arrays.<ViewController>asList(stack1, stack2));
		uut.setRoot(bottomTabsController);

		uut.pop("child4");

		assertThat(stack2.getChildControllers()).containsOnly(child2, child3);
	}

	@Test
	public void popSpecific() throws Exception {
		BottomTabsController bottomTabsController = newTabs();
		StackController stack1 = newStack();
		StackController stack2 = newStack();
		stack1.push(child1);
		stack2.push(child2);
		stack2.push(child3);
		stack2.push(child4);
		bottomTabsController.setTabs(Arrays.<ViewController>asList(stack1, stack2));
		uut.setRoot(bottomTabsController);

		uut.popSpecific(child2.getId());

		assertThat(stack2.getChildControllers()).containsOnly(child4, child3);
	}

	@Test
	public void popTo_FromCorrectStackUpToChild() throws Exception {
		BottomTabsController bottomTabsController = newTabs();
		StackController stack1 = newStack();
		StackController stack2 = newStack();
		stack1.push(child1);
		stack2.push(child2);
		stack2.push(child3);
		stack2.push(child4);
		stack2.push(child5);
		bottomTabsController.setTabs(Arrays.<ViewController>asList(stack1, stack2));
		uut.setRoot(bottomTabsController);

		uut.popTo(child2.getId());

		assertThat(stack2.getChildControllers()).containsOnly(child2);
	}

	@Test
	public void popToRoot() throws Exception {
		BottomTabsController bottomTabsController = newTabs();
		StackController stack1 = newStack();
		StackController stack2 = newStack();
		stack1.push(child1);
		stack2.push(child2);
		stack2.push(child3);
		stack2.push(child4);
		stack2.push(child5);

		bottomTabsController.setTabs(Arrays.<ViewController>asList(stack1, stack2));
		uut.setRoot(bottomTabsController);

		uut.popToRoot(child3.getId());

		assertThat(stack2.getChildControllers()).containsOnly(child2);
	}

	@Test
	public void handleBack_DelegatesToRoot() throws Exception {
		assertThat(uut.handleBack()).isFalse();
		ViewController spy = spy(child1);
		uut.setRoot(spy);
		when(spy.handleBack()).thenReturn(true);
		assertThat(uut.handleBack()).isTrue();
		verify(spy, times(1)).handleBack();
	}

	@NonNull
	private BottomTabsController newTabs() {
		return new BottomTabsController(activity, "tabsController");
	}

	@NonNull
	private StackController newStack() {
		return new StackController(activity, "stack" + CompatUtils.generateViewId(), new TestStackAnimator());
	}
}
