package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TestStackAnimator;

import org.assertj.core.api.iterable.Extractor;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StackControllerTest extends BaseTest {

	private Activity activity;
	private StackController uut;
	private ViewController child1;
	private ViewController child2;
	private ViewController child3;

	@Override
	public void beforeEach() {
		super.beforeEach();
		activity = newActivity();
		uut = new StackController(activity, "uut", new TestStackAnimator());
		child1 = new SimpleViewController(activity, "child1");
		child2 = new SimpleViewController(activity, "child2");
		child3 = new SimpleViewController(activity, "child3");
	}

	@Test
	public void isAViewController() throws Exception {
		assertThat(uut).isInstanceOf(ViewController.class);
	}

	@Test
	public void holdsAStackOfViewControllers() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);
		assertThat(uut.peek()).isEqualTo(child3);
		assertContainsOnlyId(child1.getId(), child2.getId(), child3.getId());
	}

	@Test
	public void push() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		uut.push(child1);
		assertContainsOnlyId(child1.getId());
	}

	@Test
	public void pop() throws Exception {
		uut.push(child1);
		uut.push(child2);
		assertContainsOnlyId(child2.getId(), child1.getId());
		uut.pop();
		assertContainsOnlyId(child1.getId());
	}

	@Test
	public void stackOperations() throws Exception {
		assertThat(uut.peek()).isNull();
		assertThat(uut.size()).isZero();
		assertThat(uut.isEmpty()).isTrue();
		uut.push(child1);
		assertThat(uut.peek()).isEqualTo(child1);
		assertThat(uut.size()).isEqualTo(1);
		assertThat(uut.isEmpty()).isFalse();
	}

	@Test
	public void pushAssignsRefToSelfOnPushedController() throws Exception {
		assertThat(child1.getParentStackController()).isNull();
		uut.push(child1);
		assertThat(child1.getParentStackController()).isEqualTo(uut);

		StackController anotherNavController = new StackController(activity, "another", new TestStackAnimator());
		anotherNavController.push(child2);
		assertThat(child2.getParentStackController()).isEqualTo(anotherNavController);
	}

	@Test
	public void handleBack_PopsUnlessSingleChild() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		assertThat(uut.handleBack()).isFalse();

		uut.push(child1);
		assertThat(uut.size()).isEqualTo(1);
		assertThat(uut.handleBack()).isFalse();

		uut.push(child2);
		assertThat(uut.size()).isEqualTo(2);
		assertThat(uut.handleBack()).isTrue();
		assertThat(uut.size()).isEqualTo(1);
		assertThat(uut.handleBack()).isFalse();
	}

	@Test
	public void popDoesNothingWhenZeroOrOneChild() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		uut.pop();
		assertThat(uut.isEmpty()).isTrue();

		uut.push(child1);
		uut.pop();
		assertContainsOnlyId(child1.getId());
	}

	@Test
	public void canPopWhenSizeIsMoreThanOne() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		assertThat(uut.canPop()).isFalse();
		uut.push(child1);
		assertContainsOnlyId(child1.getId());
		assertThat(uut.canPop()).isFalse();
		uut.push(child2);
		assertContainsOnlyId(child1.getId(), child2.getId());
		assertThat(uut.canPop()).isTrue();
	}

	@Test
	public void pushAddsToViewTree() throws Exception {
		assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
		uut.push(child1);
		assertThat(uut.getView().findViewById(child1.getView().getId())).isNotNull();
	}

	@Test
	public void pushRemovesPreviousFromTree() throws Exception {
		assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
		uut.push(child1);
		assertThat(uut.getView().findViewById(child1.getView().getId())).isNotNull();
		uut.push(child2);
		assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
		assertThat(uut.getView().findViewById(child2.getView().getId())).isNotNull();
	}

	@Test
	public void popReplacesViewWithPrevious() throws Exception {
		uut.push(child1);
		uut.push(child2);
		assertIsChildById(uut.getView(), child2.getView());
		assertNotChildOf(uut.getView(), child1.getView());
		uut.pop();
		assertNotChildOf(uut.getView(), child2.getView());
		assertIsChildById(uut.getView(), child1.getView());
	}

	@Test
	public void popSpecificWhenTopIsRegularPop() throws Exception {
		uut.push(child1);
		uut.push(child2);
		uut.popSpecific(child2);
		assertContainsOnlyId(child1.getId());
		assertIsChildById(uut.getView(), child1.getView());
	}

	@Test
	public void popSpecificDeepInStack() throws Exception {
		uut.push(child1);
		uut.push(child2);
		assertIsChildById(uut.getView(), child2.getView());
		uut.popSpecific(child1);
		assertContainsOnlyId(child2.getId());
		assertIsChildById(uut.getView(), child2.getView());
	}

	@Test
	public void popTo_PopsTopUntilControllerIsNewTop() throws Exception {
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);

		assertThat(uut.size()).isEqualTo(3);
		assertThat(uut.peek()).isEqualTo(child3);

		uut.popTo(child1);

		assertThat(uut.size()).isEqualTo(1);
		assertThat(uut.peek()).isEqualTo(child1);
	}

	@Test
	public void popTo_NotAChildOfThisStack_DoesNothing() throws Exception {
		uut.push(child1);
		uut.push(child3);
		assertThat(uut.size()).isEqualTo(2);
		uut.popTo(child2);
		assertThat(uut.size()).isEqualTo(2);
	}

	@Test
	public void popToRoot_PopsEverythingAboveFirstController() throws Exception {
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);

		assertThat(uut.size()).isEqualTo(3);
		assertThat(uut.peek()).isEqualTo(child3);

		uut.popToRoot();

		assertThat(uut.size()).isEqualTo(1);
		assertThat(uut.peek()).isEqualTo(child1);
	}

	@Test
	public void popToRoot_EmptyStackDoesNothing() throws Exception {
		assertThat(uut.isEmpty()).isTrue();
		uut.popToRoot();
		assertThat(uut.isEmpty()).isTrue();
	}

	@Test
	public void findControllerById_ReturnsSelfOrChildrenById() throws Exception {
		assertThat(uut.findControllerById("123")).isNull();
		assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
		uut.push(child1);
		assertThat(uut.findControllerById(child1.getId())).isEqualTo(child1);
	}

	@Test
	public void findControllerById_Deeply() throws Exception {
		StackController stack = new StackController(activity, "stack2", new TestStackAnimator());
		stack.push(child2);
		uut.push(stack);
		assertThat(uut.findControllerById(child2.getId())).isEqualTo(child2);
	}

	@Test
	public void pop_CallsDestroyOnPoppedChild() throws Exception {
		child1 = spy(child1);
		child2 = spy(child2);
		child3 = spy(child3);
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);

		verify(child3, times(0)).destroy();
		uut.pop();
		verify(child3, times(1)).destroy();
	}

	@Test
	public void popSpecific_CallsDestroyOnPoppedChild() throws Exception {
		child1 = spy(child1);
		child2 = spy(child2);
		child3 = spy(child3);
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);

		verify(child2, times(0)).destroy();
		uut.popSpecific(child2);
		verify(child2, times(1)).destroy();
	}

	@Test
	public void popTo_CallsDestroyOnPoppedChild() throws Exception {
		child1 = spy(child1);
		child2 = spy(child2);
		child3 = spy(child3);
		uut.push(child1);
		uut.push(child2);
		uut.push(child3);

		verify(child2, times(0)).destroy();
		verify(child3, times(0)).destroy();
		uut.popTo(child1);
		verify(child2, times(1)).destroy();
		verify(child3, times(1)).destroy();
	}

	private void assertContainsOnlyId(String... ids) {
		assertThat(uut.size()).isEqualTo(ids.length);
		assertThat(uut.getChildControllers()).extracting(new Extractor<ViewController, String>() {
			@Override
			public String extract(final ViewController input) {
				return input.getId();
			}
		}).containsOnly(ids);
	}
}
