package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.viewcontrollers.ComponentViewController.IReactView;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.TopTabsLayoutCreator;
import com.reactnativenavigation.views.TopTabsViewPager;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopTabsViewControllerTest extends BaseTest {

    private static final int SIZE = 2;

    private StackController parentController;
    private TopTabsController uut;
    private List<TestReactView> tabViews = new ArrayList<>(SIZE);
    private List<ViewController> tabControllers = new ArrayList<>(SIZE);
    private List<Options> tabOptions = new ArrayList<>(SIZE);
    private final Options options = new Options();
    private TopTabsViewPager topTabsLayout;

    @Override
    public void beforeEach() {
        super.beforeEach();

        final Activity activity = newActivity();
        tabOptions = createOptions();
        tabControllers = createTabsControllers(activity);
        tabViews = getTabViews();

        topTabsLayout = spy(new TopTabsViewPager(activity, tabControllers, new TopTabsAdapter(tabControllers)));
        TopTabsLayoutCreator layoutCreator = Mockito.mock(TopTabsLayoutCreator.class);
        Mockito.when(layoutCreator.create()).thenReturn(topTabsLayout);
        uut = spy(new TopTabsController(activity, "componentId", tabControllers, layoutCreator, options));

        parentController = spy(new StackController(activity, "stackId"));
        uut.setParentController(parentController);
//        parentController.push(uut, new MockPromise());
    }

    @NonNull
    private ArrayList<Options> createOptions() {
        ArrayList result = new ArrayList();
        for (int i = 0; i < SIZE; i++) {
            final Options options = new Options();
            options.topTabOptions.title = new Text("Tab " + i);
            options.topBarOptions.title = new Text("Title " + i);
            result.add(options);
        }
        return result;
    }

    private List<TestReactView> getTabViews() {
        List<TestReactView> tabs = new ArrayList();
        for (ViewController tabController : tabControllers) {
            tabs.add((TestReactView) tabController.getView().getChildAt(0));
        }
        return tabs;
    }

    private List<ViewController> createTabsControllers(Activity activity) {
        List<ViewController> result = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            final SimpleComponentViewController viewController = new SimpleComponentViewController(
                    activity,
                    "idTab" + i,
                    tabOptions.get(i)
            );
            result.add(spy(viewController));
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

        TestReactView initialTab = tabViews.get(0);
        TestReactView selectedTab = tabViews.get(1);

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
        verify(tabViews.get(0), times(1)).sendComponentStop();
        verify(tabViews.get(0), times(2)).sendComponentStart();
        verify(tabViews.get(1), times(1)).sendComponentStart();
        verify(tabViews.get(1), times(1)).sendComponentStop();
    }

    @Test
    @Ignore
    public void setOptionsOfInitialTab() throws Exception {
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        tabControllers.get(0).setParentController(uut);
        verify(tabControllers.get(0), times(1)).onViewAppeared();
        verify(tabControllers.get(1), times(0)).onViewAppeared();
        verify(uut, times(1)).applyOptions(eq(tabOptions.get(0)), any());
//        verify(parentController, times(1)).applyOptions(tabOptions.get(0), (ReactComponent) tabControllers.get(0).getView());
    }

    @Test
    @Ignore
    public void setOptionsWhenTabChanges() throws Exception {
        parentController.ensureViewIsCreated();
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
        tabControllers.get(0).ensureViewIsCreated();
        parentController.ensureViewIsCreated();
        uut.ensureViewIsCreated();

        parentController.onViewAppeared();
        uut.onViewAppeared();

        tabControllers.get(0).onViewAppeared();

        verify(topTabsLayout, times(1)).applyOptions(options);
        verify(uut, times(1)).applyOnParentController(any());
    }

    private IReactView tab(TopTabsViewPager topTabs, final int index) {
        return (IReactView) ((ViewGroup) topTabs.getChildAt(index)).getChildAt(0);
    }
}
