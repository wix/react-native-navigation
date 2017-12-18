package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TopTabLayoutMock;
import com.reactnativenavigation.parse.NavigationOptions;
import com.reactnativenavigation.viewcontrollers.ContainerViewController.IReactView;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabController;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.TopTabsLayout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopTabsViewControllerTest extends BaseTest {

    private static final int SIZE = 2;

    private TopTabsController uut;
    private List<TopTabLayoutMock> tabs = new ArrayList<>(SIZE);
    private List<TopTabController> tabControllers = new ArrayList<>(SIZE);
    private List<NavigationOptions> tabOptions = new ArrayList<>(SIZE);

    @Override
    public void beforeEach() {
        super.beforeEach();
        tabControllers.clear();
        tabs.clear();
        Activity activity = newActivity();
        createTabs(activity);
        uut = new TopTabsController(activity, "containerId", tabControllers);
    }

    private void createTabs(Activity activity) {
        for (int i = 0; i < SIZE; i++) {
            TopTabLayoutMock reactView = spy(new TopTabLayoutMock(activity));
            tabs.add(reactView);
            tabOptions.add(new NavigationOptions());
            tabControllers.add(spy(new TopTabController(activity,
                    "idTab" + i,
                    "tab" + i,
                    (activity1, containerId, containerName) -> reactView,
                    tabOptions.get(i))
            ));
        }
    }

    @Test
    public void createsViewFromContainerViewCreator() throws Exception {
        uut.ensureViewIsCreated();
        for (int i = 0; i < SIZE; i++) {
            verify(tabControllers.get(i), times(1)).createView();
        }
    }

    @Test
    public void containerViewDestroyedOnDestroy() throws Exception {
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
        verify(initialTab, times(1)).sendContainerStop();
        verify(selectedTab, times(1)).sendContainerStart();
        verify(selectedTab, times(0)).sendContainerStop();
    }

    @Test
    public void lifecycleMethodsSentWhenSelectedPreviouslySelectedTab() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        uut.switchToTab(1);
        uut.switchToTab(0);
        verify(tabs.get(0), times(1)).sendContainerStop();
        verify(tabs.get(0), times(2)).sendContainerStart();
        verify(tabs.get(1), times(1)).sendContainerStart();
        verify(tabs.get(1), times(1)).sendContainerStop();
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

    private IReactView tab(TopTabsLayout topTabs, final int index) {
        return (IReactView) ((ViewGroup) topTabs.getViewPager().getChildAt(index)).getChildAt(0);
    }
}
