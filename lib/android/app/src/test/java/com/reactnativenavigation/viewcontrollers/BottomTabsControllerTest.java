package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.BottomTabs;
import com.reactnativenavigation.views.ReactComponent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
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
    private Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock = ImageLoaderMock.mock();

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        uut = spy(new BottomTabsController(activity, imageLoaderMock, "uut", new Options()));
        child1 = spy(new SimpleViewController(activity, "child1", tabOptions));
        child2 = spy(new SimpleViewController(activity, "child2", tabOptions));
        child3 = spy(new SimpleViewController(activity, "child3", tabOptions));
        child4 = spy(new SimpleViewController(activity, "child4", tabOptions));
        child5 = spy(new SimpleViewController(activity, "child5", tabOptions));
    }

    @Test
    public void containsRelativeLayoutView() {
        assertThat(uut.getView()).isInstanceOf(RelativeLayout.class);
        assertThat(uut.getView().getChildAt(0)).isInstanceOf(BottomTabs.class);
    }

    @Test(expected = RuntimeException.class)
    public void setTabs_ThrowWhenMoreThan5() {
        List<ViewController> tabs = createTabs();
        tabs.add(new SimpleViewController(activity, "6", tabOptions));
        uut.setTabs(tabs);
    }

    @Test
    public void setTab_controllerIsSetAsParent() {
        List<ViewController> tabs = createTabs();
        uut.setTabs(tabs);
        for (ViewController tab : tabs) {
            assertThat(tab.getParentController()).isEqualTo(uut);
        }
    }

    @Test
    public void setTabs_AddAllViews() {
        List<ViewController> tabs = createTabs();
        uut.setTabs(tabs);
        assertThat(uut.getView().getChildCount()).isEqualTo(2);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNotNull();
    }

    @Test
    public void selectTabAtIndex() {
        uut.setTabs(createTabs());
        assertThat(uut.getSelectedIndex()).isZero();

        uut.selectTabAtIndex(3);

        assertThat(uut.getSelectedIndex()).isEqualTo(3);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNull();
    }

    @Test
    public void findControllerById_ReturnsSelfOrChildren() {
        assertThat(uut.findControllerById("123")).isNull();
        assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
        StackController inner = createStack("inner");
        inner.push(child1, new CommandListenerAdapter());
        assertThat(uut.findControllerById(child1.getId())).isNull();
        uut.setTabs(Collections.singletonList(inner));
        assertThat(uut.findControllerById(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void handleBack_DelegatesToSelectedChild() {
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

    @Test
    public void applyOptions_bottomTabsOptionsAreClearedAfterApply() {
        List<ViewController> tabs = createTabs();
        child1.options.bottomTabsOptions.tabColor = new Color(android.graphics.Color.RED);
        uut.setTabs(tabs);
        uut.ensureViewIsCreated();

        StackController stack = spy(createStack("stack"));
        stack.ensureViewIsCreated();
        stack.push(uut, new CommandListenerAdapter());

        child1.onViewAppeared();
        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ReactComponent> viewCaptor = ArgumentCaptor.forClass(ReactComponent.class);
        verify(stack, times(1)).applyChildOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(viewCaptor.getValue()).isEqualTo(child1.getView());
        assertThat(optionsCaptor.getValue().bottomTabsOptions.tabColor.hasValue()).isFalse();
    }

    @Test
    public void mergeOptions_currentTabIndex() {
        List<ViewController> tabs = createTabs();
        uut.setTabs(tabs);
        uut.ensureViewIsCreated();

        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);
        uut.mergeOptions(options);
        verify(uut, times(1)).selectTabAtIndex(1);
    }

    @Test
    public void buttonPressInvokedOnCurrentTab() {
        uut.setTabs(createTabs());
        uut.ensureViewIsCreated();
        uut.selectTabAtIndex(1);

        uut.sendOnNavigationButtonPressed("btn1");
        verify(child2, times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @NonNull
    private List<ViewController> createTabs() {
        return Arrays.asList(child1, child2, child3, child4, child5);
    }

    private StackController createStack(String id) {
        return new StackController(activity,
                new TopBarButtonCreatorMock(),
                new TitleBarReactViewCreatorMock(),
                new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()),
                new TopBarController(),
                id,
                tabOptions
        );
    }
}
