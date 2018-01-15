package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.view.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.viewcontrollers.ComponentViewController.*;
import com.reactnativenavigation.viewcontrollers.toptabs.*;
import com.reactnativenavigation.views.*;

import org.junit.*;
import org.mockito.*;

import java.util.*;

import static org.mockito.Mockito.*;

public class TopTabsViewControllerTest extends BaseTest {

    private static final int SIZE = 2;

    private TopTabsController uut;
    private List<TopTabLayoutMock> tabs = new ArrayList<>(SIZE);
    private List<ViewController> tabControllers = new ArrayList<>(SIZE);
    private List<Options> tabOptions = new ArrayList<>(SIZE);
    private Options options;
    private TopTabsLayout topTabsLayout;

    @Override
    public void beforeEach() {
        super.beforeEach();
        tabControllers.clear();
        tabs.clear();
        Activity activity = newActivity();
        tabControllers = createTabs(activity);
        options = new Options();
        topTabsLayout = spy(new TopTabsLayout(activity, tabControllers, new TopTabsAdapter(tabControllers)));

        TopTabsLayoutCreator layoutCreator = Mockito.mock(TopTabsLayoutCreator.class);
        Mockito.when(layoutCreator.create()).thenReturn(topTabsLayout);
        uut = new TopTabsController(activity, "componentId", tabControllers, layoutCreator, options);
    }

    private List<ViewController> createTabs(Activity activity) {
        List<ViewController> result = new ArrayList<>(SIZE);
        tabOptions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            TopTabLayoutMock reactView = spy(new TopTabLayoutMock(activity));
            tabs.add(reactView);
            Options options = new Options();
            options.topTabOptions.title = new Text("Tab " + i);
            tabOptions.add(options);
            result.add(spy(new TopTabController(activity,
                    "idTab" + i,
                    "tab" + i,
                    (activity1, componentId, componentName) -> reactView,
                    options)
            ));
        }
        return result;
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
        TopTabsLayout topTabs = (TopTabsLayout) uut.getView();
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
        uut.ensureViewIsCreated();
        TopTabLayoutMock initialTab = tabs.get(0);
        TopTabLayoutMock selectedTab = tabs.get(1);
        uut.onViewAppeared();
        uut.switchToTab(1);
        verify(initialTab, times(1)).sendComponentStop();
        verify(selectedTab, times(1)).sendComponentStart();
        verify(selectedTab, times(0)).sendComponentStop();
    }

    @Test
    public void lifecycleMethodsSentWhenSelectedPreviouslySelectedTab() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        uut.switchToTab(1);
        uut.switchToTab(0);
        verify(tabs.get(0), times(1)).sendComponentStop();
        verify(tabs.get(0), times(2)).sendComponentStart();
        verify(tabs.get(1), times(1)).sendComponentStart();
        verify(tabs.get(1), times(1)).sendComponentStop();
    }

    @Test
    public void setOptionsOfInitialTab() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(tabControllers.get(0), times(1)).applyOptions(tabOptions.get(0));
    }

    @Test
    public void setOptionsWhenTabChanges() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(tabControllers.get(0), times(1)).applyOptions(tabOptions.get(0));
        uut.switchToTab(1);
        verify(tabControllers.get(1), times(1)).applyOptions(tabOptions.get(1));
        uut.switchToTab(0);
        verify(tabControllers.get(0), times(2)).applyOptions(tabOptions.get(0));
    }

    @Test
    public void appliesOptionsOnLayoutWhenVisible() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(topTabsLayout, times(1)).applyOptions(options);
    }

    private IReactView tab(TopTabsLayout topTabs, final int index) {
        return (IReactView) ((ViewGroup) topTabs.getViewPager().getChildAt(index)).getChildAt(0);
    }
}
