package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.TestComponentViewCreator;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ViewHelper;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopTabsLayoutCreator;
import com.reactnativenavigation.views.TopTabsViewPager;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopTabsViewControllerTest extends BaseTest {

    private static final int SIZE = 2;

    private StackController parentController;
    private TopTabsController uut;
    private List<ViewController> tabControllers = new ArrayList<>(SIZE);
    private List<Options> tabOptions = new ArrayList<>(SIZE);
    private final Options options = new Options();
    private TopTabsViewPager topTabsLayout;
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();

        activity = newActivity();
        tabOptions = createOptions();
        tabControllers = createTabsControllers(activity, tabOptions);

        topTabsLayout = spy(new TopTabsViewPager(activity, tabControllers, new TopTabsAdapter(tabControllers)));
        TopTabsLayoutCreator layoutCreator = Mockito.mock(TopTabsLayoutCreator.class);
        Mockito.when(layoutCreator.create()).thenReturn(topTabsLayout);
        uut = spy(new TopTabsController(activity, "componentId", tabControllers, layoutCreator, options));
        tabControllers.forEach(viewController -> viewController.setParentController(uut));

        parentController = spy(createStackController("stackId"));
        parentController.push(uut, new MockPromise());
        uut.setParentController(parentController);
    }

    @NonNull
    private StackController createStackController(String id) {
        return new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), id, new Options());
    }

    @NonNull
    private ArrayList<Options> createOptions() {
        ArrayList result = new ArrayList();
        for (int i = 0; i < SIZE; i++) {
            final Options options = new Options();
            options.topTabOptions.title = new Text("Tab " + i);
            options.topBarOptions.title.text = new Text(createTabTopBarTitle(i));
            result.add(options);
        }
        return result;
    }

    private List<ViewController> createTabsControllers(Activity activity, List<Options> tabOptions) {
        List<ViewController> tabControllers = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            ComponentViewController viewController = new ComponentViewController(
                    activity,
                    "idTab" + i,
                    "theComponentName",
                    new TestComponentViewCreator(),
                    tabOptions.get(i)
            );
            tabControllers.add(spy(viewController));
        }
        return tabControllers;
    }

    private ReactComponent tabView(int index) {
        return (ReactComponent) tabControllers.get(index).getView();
    }

    @Test
    public void createsViewFromComponentViewCreator() throws Exception {
        uut.ensureViewIsCreated();
        for (int i = 0; i < SIZE; i++) {
            verify(tabControllers.get(i), times(1)).createView();
        }
    }

    @Test
    public void componentViewDestroyedOnDestroy() throws Exception {
        uut.ensureViewIsCreated();
        TopTabsViewPager topTabs = uut.getView();
        for (int i = 0; i < SIZE; i++) {
            verify(tab(topTabs, i), times(0)).destroy();
        }
        uut.destroy();
        for (ViewController tabController : tabControllers) {
            verify(tabController, times(1)).destroy();
        }
    }

    @Test
    public void lifecycleMethodsSentWhenSelectedTabChanges() throws Exception {
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        tabControllers.get(0).ensureViewIsCreated();
        tabControllers.get(1).ensureViewIsCreated();

        tabControllers.get(0).onViewAppeared();

        uut.onViewAppeared();

        TestReactView initialTab = getActualTabView(0);
        TestReactView selectedTab = getActualTabView(1);

        uut.switchToTab(1);
        verify(initialTab, times(1)).sendComponentStop();
        verify(selectedTab, times(1)).sendComponentStart();
        verify(selectedTab, times(0)).sendComponentStop();
    }

    @Test
    public void lifecycleMethodsSentWhenSelectedPreviouslySelectedTab() throws Exception {
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        uut.switchToTab(1);
        uut.switchToTab(0);

        verify(getActualTabView(0), times(1)).sendComponentStop();
        verify(getActualTabView(0), times(2)).sendComponentStart();
        verify(getActualTabView(1), times(1)).sendComponentStart();
        verify(getActualTabView(1), times(1)).sendComponentStop();
    }

    @Test
    public void setOptionsOfInitialTab() throws Exception {
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(tabControllers.get(0), times(1)).onViewAppeared();
        verify(tabControllers.get(1), times(0)).onViewAppeared();

        ReactComponent comp = ((ComponentViewController) tabControllers.get(0)).getComponent();
        verify(uut, times(1)).applyOptions(any(Options.class), eq(comp));
    }

    @Test
    public void setOptionsWhenTabChanges() throws Exception {
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        tabControllers.get(0).ensureViewIsCreated();
        tabControllers.get(1).ensureViewIsCreated();

        uut.onViewAppeared();
        ReactComponent currentTab = tabView(0);
        verify(uut, times(1)).applyOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBarOptions.title.text.get()).isEqualTo(createTabTopBarTitle(0));

        uut.switchToTab(1);
        currentTab = tabView(1);
        verify(uut, times(1)).applyOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBarOptions.title.text.get()).isEqualTo(createTabTopBarTitle(1));

        uut.switchToTab(0);
        currentTab = tabView(0);
        verify(uut, times(2)).applyOptions(any(Options.class), eq(currentTab));
        assertThat(uut.options.topBarOptions.title.text.get()).isEqualTo(createTabTopBarTitle(0));
    }

    private TestReactView getActualTabView(int index) {
        return (TestReactView) tabControllers.get(index).getView().getChildAt(0);
    }

    @Test
    public void appliesOptionsOnLayoutWhenVisible() throws Exception {
        tabControllers.get(0).ensureViewIsCreated();
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();

        uut.onViewAppeared();

        verify(topTabsLayout, times(1)).applyOptions(any(Options.class));
    }

    @Test
    public void applyOptions_applyOnlyOnFirstTopTabs() throws Exception {
        tabOptions.get(0).topTabOptions.title = new Text("tab title");
        tabControllers.get(0).onViewAppeared();
        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ReactComponent> viewCaptor = ArgumentCaptor.forClass(ReactComponent.class);
        verify(parentController, times(1)).applyOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(optionsCaptor.getValue().topTabOptions.title.hasValue()).isFalse();
    }

    @Test
    public void applyOptions_tabsAreRemovedAfterViewDisappears() throws Exception {
        parentController.getView().removeAllViews();

        StackController stackController = spy(createStackController("stack"));
        ComponentViewController first = new ComponentViewController(
                activity,
                "firstScreen",
                "comp1",
                new TestComponentViewCreator(),
                new Options()
        );
        stackController.push(first, new MockPromise());
        stackController.push(uut, new MockPromise());

        first.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();

        assertThat(ViewHelper.isVisible(stackController.getTopBar().getTopTabs())).isTrue();
        stackController.animatePop(new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertThat(ViewHelper.isVisible(stackController.getTopBar().getTopTabs())).isFalse();
            }
        });
    }

    @Test
    public void onNavigationButtonPressInvokedOnCurrentTab() throws Exception {
        uut.ensureViewIsCreated();
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
}
