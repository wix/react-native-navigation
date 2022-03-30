package com.reactnativenavigation.viewcontrollers.toptabs;

import android.app.Activity;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TestComponentViewCreator;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.component.ComponentPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.utils.ViewHelper;
import com.reactnativenavigation.viewcontrollers.viewcontroller.IReactView;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.toptabs.TopTabsLayoutCreator;
import com.reactnativenavigation.views.toptabs.TopTabsViewPager;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopTabsViewControllerTest extends BaseTest {
    private static final int SIZE = 2;

    private StackController stack;
    private TopTabsController uut;
    private List<ViewController<?>> tabControllers = new ArrayList<>(SIZE);
    private final Options options = new Options();
    private TopTabsViewPager topTabsLayout;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        super.beforeEach();

        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        List<Options> tabOptions = createOptions();
        tabControllers = createTabsControllers(activity, tabOptions);

        topTabsLayout = spy(new TopTabsViewPager(activity, tabControllers, new TopTabsAdapter(tabControllers)));
        TopTabsLayoutCreator layoutCreator = Mockito.mock(TopTabsLayoutCreator.class);
        Mockito.when(layoutCreator.create()).thenReturn(topTabsLayout);
        Presenter presenter = new Presenter(activity, new Options());
        options.topBar.buttons.back.visible = new Bool(false);
        uut = spy(new TopTabsController(activity, childRegistry, "componentId", tabControllers, layoutCreator, options, presenter));
        tabControllers.forEach(viewController -> viewController.setParentController(uut));

        stack = spy(TestUtils.newStackController(activity).build());
        stack.ensureViewIsCreated();
    }

    @NonNull
    private ArrayList<Options> createOptions() {
        ArrayList<Options> result = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            final Options options = new Options();
            options.topTabOptions.title = new Text("Tab " + i);
            options.topBar.title.text = new Text(createTabTopBarTitle(i));
            result.add(options);
        }
        return result;
    }

    private List<ViewController<?>> createTabsControllers(Activity activity, List<Options> tabOptions) {
        List<ViewController<?>> tabControllers = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            ComponentViewController viewController = new ComponentViewController(
                    activity,
                    childRegistry,
                    "idTab" + i,
                    "theComponentName",
                    new TestComponentViewCreator(),
                    tabOptions.get(i),
                    new Presenter(activity, new Options()),
                    new ComponentPresenter(Options.EMPTY)
            );
            tabControllers.add(spy(viewController));
        }
        return tabControllers;
    }

    @Test
    public void createsViewFromComponentViewCreator() {
        uut.ensureViewIsCreated();
        for (int i = 0; i < SIZE; i++) {
            verify(tabControllers.get(i), times(1)).createView();
        }
    }

    @Test
    public void componentViewDestroyedOnDestroy() {
        uut.ensureViewIsCreated();
        TopTabsViewPager topTabs = uut.getView();
        for (int i = 0; i < SIZE; i++) {
            verify(tab(topTabs, i), times(0)).destroy();
        }
        uut.destroy();
        for (ViewController<?> tabController : tabControllers) {
            verify(tabController, times(1)).destroy();
        }
    }

    @Test
    @Ignore("TopTabs not yet well supported")
    public void lifecycleMethodsSentWhenSelectedTabChanges() {
        stack.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        tabControllers.get(0).ensureViewIsCreated();
        tabControllers.get(1).ensureViewIsCreated();

        TestReactView initialTab = getActualTabView(0);
        TestReactView selectedTab = getActualTabView(1);

        uut.switchToTab(1);
        verify(initialTab, times(1)).sendComponentStop(ComponentType.Component);
        verify(selectedTab, times(1)).sendComponentWillStart(ComponentType.Component);
        verify(selectedTab, times(1)).sendComponentStart(ComponentType.Component);
        verify(selectedTab, times(0)).sendComponentStop(ComponentType.Component);
    }

    @Test
    public void lifecycleMethodsSentWhenSelectedPreviouslySelectedTab() {
        stack.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewDidAppear();
        uut.switchToTab(1);
        uut.switchToTab(0);

        verify(getActualTabView(0), times(1)).sendComponentStop(ComponentType.Component);
        verify(getActualTabView(0), times(2)).sendComponentWillStart(ComponentType.Component);
        verify(getActualTabView(0), times(2)).sendComponentStart(ComponentType.Component);
        verify(getActualTabView(1), times(1)).sendComponentStart(ComponentType.Component);
        verify(getActualTabView(1), times(1)).sendComponentStop(ComponentType.Component);
    }

    @Test
    public void setOptionsOfInitialTab() {
        stack.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewWillAppear();
        verify(tabControllers.get(0), times(1)).onViewWillAppear();
        verify(tabControllers.get(1), times(0)).onViewWillAppear();

        ViewController<?> comp = tabControllers.get(0);
        verify(uut, times(1)).applyChildOptions(any(Options.class), eq(comp));
    }

    @Test
    public void setOptionsWhenTabChanges() {
        stack.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        tabControllers.get(0).ensureViewIsCreated();
        tabControllers.get(1).ensureViewIsCreated();

        uut.onViewWillAppear();
        ViewController<?> currentTab = tab(0);
        verify(uut, times(1)).applyChildOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBar.title.text.get()).isEqualTo(createTabTopBarTitle(0));

        uut.switchToTab(1);
        currentTab = tab(1);
        verify(uut, times(1)).applyChildOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBar.title.text.get()).isEqualTo(createTabTopBarTitle(1));

        uut.switchToTab(0);
        currentTab = tab(0);
        verify(uut, times(2)).applyChildOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBar.title.text.get()).isEqualTo(createTabTopBarTitle(0));
    }

    private TestReactView getActualTabView(int index) {
        return (TestReactView) tabControllers.get(index).getView().getChildAt(0);
    }

    @Test
    public void appliesOptionsOnLayoutWhenVisible() {
        tabControllers.get(0).ensureViewIsCreated();
        stack.ensureViewIsCreated();
        uut.ensureViewIsCreated();

        uut.onViewWillAppear();

        verify(topTabsLayout, times(1)).applyOptions(any(Options.class));
    }

    @Test
    public void applyOptions_tabsAreRemovedAfterViewDisappears() {
        StackController stackController = TestUtils.newStackController(activity).build();
        stackController.ensureViewIsCreated();
        ViewController<?> first = new SimpleViewController(activity, childRegistry, "first", Options.EMPTY);
        disablePushAnimation(first, uut);
        stackController.push(first, new CommandListenerAdapter());
        stackController.push(uut, new CommandListenerAdapter());

        uut.onViewWillAppear();

        assertThat(ViewHelper.isVisible(stackController.getTopBar().getTopTabs())).isTrue();
        disablePopAnimation(uut);
        stackController.pop(Options.EMPTY, new CommandListenerAdapter());

        first.onViewWillAppear();

        assertThat(ViewHelper.isVisible(stackController.getTopBar().getTopTabs())).isFalse();
    }

    @Test
    public void onNavigationButtonPressInvokedOnCurrentTab() {
        uut.ensureViewIsCreated();
        uut.onViewWillAppear();
        uut.switchToTab(1);
        uut.sendOnNavigationButtonPressed("btn1");
        verify(tabControllers.get(1), times(1)).sendOnNavigationButtonPressed("btn1");
    }

    private IReactView tab(TopTabsViewPager topTabs, final int index) {
        return (IReactView) ((ViewGroup) topTabs.getChildAt(index)).getChildAt(0);
    }

    private String createTabTopBarTitle(int i) {
        return "Title " + i;
    }

    private ViewController<?> tab(int index) {
        return tabControllers.get(index);
    }
}
