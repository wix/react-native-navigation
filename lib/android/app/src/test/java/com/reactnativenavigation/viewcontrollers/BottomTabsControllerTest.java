package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Color;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.EventEmitter;
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
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
    private StackController child4;
    private ViewController child5;
    private Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock = ImageLoaderMock.mock();
    private EventEmitter eventEmitter;
    private ChildControllersRegistry childRegistry;
    private List<ViewController> tabs;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);

        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", tabOptions));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", tabOptions));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child3", tabOptions));
        child4 = spy(createStack("someStack"));
        child5 = spy(new SimpleViewController(activity, childRegistry, "child5", tabOptions));
        when(child5.handleBack(any())).thenReturn(true);
        tabs = createTabs();

        uut = new BottomTabsController(activity, tabs, childRegistry, eventEmitter, imageLoaderMock, "uut", new Options()) {
            @Override
            public void ensureViewIsCreated() {
                super.ensureViewIsCreated();
                uut.getView().layout(0, 0, 1000, 1000);
                uut.getBottomTabs().layout(0, 0, 1000, 100);
            }
        };
    }

    @Test
    public void containsRelativeLayoutView() {
        assertThat(uut.getView()).isInstanceOf(RelativeLayout.class);
        assertThat(uut.getView().getChildAt(0)).isInstanceOf(BottomTabs.class);
    }

    @Test(expected = RuntimeException.class)
    public void setTabs_ThrowWhenMoreThan5() {
        tabs.add(new SimpleViewController(activity, childRegistry, "6", tabOptions));
        new BottomTabsController(activity, tabs, childRegistry, eventEmitter, imageLoaderMock, "uut", new Options()) {
            @Override
            public void ensureViewIsCreated() {
                super.ensureViewIsCreated();
                uut.getView().layout(0, 0, 1000, 1000);
                uut.getBottomTabs().layout(0, 0, 1000, 100);
            }
        };
    }

    @Test
    public void setTab_controllerIsSetAsParent() {
        for (ViewController tab : tabs) {
            assertThat(tab.getParentController()).isEqualTo(uut);
        }
    }

    @Test
    public void setTabs_AddAllViews() {
        uut.onViewAppeared();
        assertThat(uut.getView().getChildCount()).isEqualTo(2);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNotNull();
    }

    @Test
    public void onTabSelected() {
        assertThat(uut.getSelectedIndex()).isZero();

        uut.onTabSelected(3, false);

        assertThat(uut.getSelectedIndex()).isEqualTo(3);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNull();
        verify(eventEmitter, times(1)).emitBottomTabSelected(0, 3);
    }

    @Test
    public void onTabReSelected() {
        assertThat(uut.getSelectedIndex()).isZero();

        uut.onTabSelected(0, false);

        assertThat(uut.getSelectedIndex()).isEqualTo(0);
        assertThat(((ViewController) ((List) uut.getChildControllers()).get(0)).getView().getParent()).isNotNull();
        verify(eventEmitter, times(1)).emitBottomTabSelected(0, 0);
    }

    @Test
    public void handleBack_DelegatesToSelectedChild() {
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
        uut.selectTab(4);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(child5, times(1)).handleBack(any());
    }

    @Test
    public void applyOptions_bottomTabsOptionsAreClearedAfterApply() {
        Options options = new Options();
        options.bottomTabsOptions.tabColor = new Color(android.graphics.Color.RED);
        child1.mergeOptions(options);
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
        uut.ensureViewIsCreated();
        assertThat(uut.getSelectedIndex()).isZero();

        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);
        uut.mergeOptions(options);
        assertThat(uut.getSelectedIndex()).isOne();
        verify(eventEmitter, times(0)).emitBottomTabSelected(any(Integer.class), any(Integer.class));
    }

    @Test
    public void mergeOptions_drawBehind() {
        uut.ensureViewIsCreated();
        child1.onViewAppeared();
        uut.selectTab(0);

        assertThat(childLayoutParams(0).bottomMargin).isEqualTo(uut.getBottomTabs().getHeight());

        Options o1 = new Options();
        o1.bottomTabsOptions.drawBehind = new Bool(true);
        child1.mergeOptions(o1);
        assertThat(childLayoutParams(0).bottomMargin).isEqualTo(0);

        Options o2 = new Options();
        o2.topBar.title.text = new Text("Some text");
        child1.mergeOptions(o1);
        assertThat(childLayoutParams(0).bottomMargin).isEqualTo(0);
    }

    @Test
    public void child_mergeOptions_currentTabIndex() {
        uut.ensureViewIsCreated();

        assertThat(uut.getSelectedIndex()).isZero();

        Options options = new Options();
        options.bottomTabsOptions.currentTabIndex = new Number(1);
        child1.mergeOptions(options);

        assertThat(uut.getSelectedIndex()).isOne();
    }

    @Test
    public void buttonPressInvokedOnCurrentTab() {
        uut.ensureViewIsCreated();
        uut.selectTab(4);

        uut.sendOnNavigationButtonPressed("btn1");
        verify(child5, times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @Test
    public void push() {
        uut.selectTab(3);

        SimpleViewController stackChild = new SimpleViewController(activity, childRegistry, "stackChild", new Options());
        SimpleViewController stackChild2 = new SimpleViewController(activity, childRegistry, "stackChild", new Options());
        disablePushAnimation(stackChild, stackChild2);

        child4.push(stackChild, new CommandListenerAdapter());
        assertThat(child4.size()).isOne();
        child4.push(stackChild2, new CommandListenerAdapter());
        assertThat(child4.size()).isEqualTo(2);
    }

    @NonNull
    private List<ViewController> createTabs() {
        return Arrays.asList(child1, child2, child3, child4, child5);
    }

    private StackController createStack(String id) {
        return new StackControllerBuilder(activity)
                .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                .setTitleBarReactViewCreator(new TitleBarReactViewCreatorMock())
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                .setTopBarController(new TopBarController())
                .setId(id)
                .setInitialOptions(tabOptions)
                .createStackController();
    }

    private ViewGroup.MarginLayoutParams childLayoutParams(int index) {
        return (ViewGroup.MarginLayoutParams) tabs.get(index).getView().getLayoutParams();
    }
}
