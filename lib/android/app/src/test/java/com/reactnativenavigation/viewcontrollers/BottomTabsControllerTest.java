package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.support.annotation.*;
import android.support.design.widget.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;

import org.assertj.core.api.iterable.*;
import org.junit.*;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

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
        assertThat(uut.getChildControllers()).extracting((Extractor<ViewController, Integer>) input -> input.getView().getVisibility()).containsExactly(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE);
    }

    @Test
    public void selectTabAtIndex() throws Exception {
        uut.setTabs(createTabs());
        assertThat(uut.getSelectedIndex()).isZero();

        uut.selectTabAtIndex(3);

        assertThat(uut.getSelectedIndex()).isEqualTo(3);
        assertThat(uut.getChildControllers()).extracting((Extractor<ViewController, Integer>) input -> input.getView().getVisibility()).containsExactly(View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE);
    }

    @Test
    public void findControllerById_ReturnsSelfOrChildren() throws Exception {
        assertThat(uut.findControllerById("123")).isNull();
        assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
        StackController inner = new StackController(activity, "inner");
        inner.animatePush(child1, new MockPromise());
        assertThat(uut.findControllerById(child1.getId())).isNull();
        uut.setTabs(Collections.singletonList(inner));
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
