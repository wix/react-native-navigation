package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;

import org.assertj.core.api.iterable.Extractor;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BottomTabsControllerTest extends BaseTest {

	private Activity activity;
	private BottomTabsController uut;
	private ViewController child1;
	private ViewController child2;
	private ViewController child3;
	private ViewController child4;
	private ViewController child5;

	@Override
	public void beforeEach() {
		super.beforeEach();
		activity = newActivity();
		uut = new BottomTabsController(activity, "uut");
		child1 = new SimpleViewController(activity, "child1");
		child2 = new SimpleViewController(activity, "child2");
		child3 = new SimpleViewController(activity, "child3");
		child4 = new SimpleViewController(activity, "child4");
		child5 = new SimpleViewController(activity, "child5");
	}

	@Test
	public void containsRelativeLayoutView() throws Exception {
		assertThat(uut.getView()).isInstanceOf(RelativeLayout.class);
		assertThat(uut.getView().getChildAt(0)).isInstanceOf(BottomNavigationView.class);
	}

	@Test(expected = RuntimeException.class)
	public void setTabs_ThrowWhenMoreThan5() throws Exception {
		List<ViewController> tabs = createTabs();
		tabs.add(new SimpleViewController(activity, "6"));
		uut.setTabs(tabs);
	}

	@Test
	public void setTabs_AddAllViewsAsGoneExceptFirst() throws Exception {
		List<ViewController> tabs = createTabs();
		uut.setTabs(tabs);
		assertThat(uut.getView().getChildCount()).isEqualTo(6);
		assertThat(uut.getChildControllers()).extracting(new Extractor<ViewController, Integer>() {
			@Override
			public Integer extract(final ViewController input) {
				return input.getView().getVisibility();
			}
		}).containsExactly(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
	}

	@Test
	public void selectTabAtIndex() throws Exception {
		uut.setTabs(createTabs());
		assertThat(uut.getSelectedIndex()).isZero();

		uut.selectTabAtIndex(3);

		assertThat(uut.getSelectedIndex()).isEqualTo(3);
		assertThat(uut.getChildControllers()).extracting(new Extractor<ViewController, Integer>() {
			@Override
			public Integer extract(final ViewController input) {
				return input.getView().getVisibility();
			}
		}).containsExactly(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
	}

	@Test
	public void findControllerById_ReturnsSelfOrChildren() throws Exception {
		assertThat(uut.findControllerById("123")).isNull();
		assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
		StackController inner = new StackController(activity, "inner");
		inner.push(child1);
		assertThat(uut.findControllerById(child1.getId())).isNull();
		uut.setTabs(Arrays.<ViewController>asList(inner));
		assertThat(uut.findControllerById(child1.getId())).isEqualTo(child1);
	}

	@Test
	public void handleBack_DelegatesToSelectedChild() throws Exception {
		assertThat(uut.handleBack()).isFalse();

		List<ViewController> tabs = createTabs();
		ViewController spy = spy(tabs.get(2));
		tabs.set(2, spy);
		when(spy.handleBack()).thenReturn(true);
		uut.setTabs(tabs);

		assertThat(uut.handleBack()).isFalse();
		uut.selectTabAtIndex(2);
		assertThat(uut.handleBack()).isTrue();

		verify(spy, times(1)).handleBack();
	}

	@NonNull
	private List<ViewController> createTabs() {
		return Arrays.asList(child1, child2, child3, child4, child5);
	}
}
