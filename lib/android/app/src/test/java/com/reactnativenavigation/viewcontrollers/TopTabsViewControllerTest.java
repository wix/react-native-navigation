package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TestComponentViewCreator;
import com.reactnativenavigation.mocks.TestReactView;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsAdapter;
import com.reactnativenavigation.viewcontrollers.toptabs.TopTabsController;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.TopTabsLayoutCreator;
import com.reactnativenavigation.views.TopTabsViewPager;

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

        topTabsLayout = spy(new TopTabsViewPager(activity, tabControllers, new TopTabsAdapter(tabControllers)));
        TopTabsLayoutCreator layoutCreator = Mockito.mock(TopTabsLayoutCreator.class);
        Mockito.when(layoutCreator.create()).thenReturn(topTabsLayout);
        uut = spy(new TopTabsController(activity, "componentId", tabControllers, layoutCreator, options));
        tabControllers.forEach(viewController -> viewController.setParentController(uut));

        parentController = spy(new StackController(activity, "stackId", new Options()));
        uut.setParentController(parentController);
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

    private List<ViewController> createTabsControllers(Activity activity) {
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
        verify(uut, times(1)).applyOptions(tabOptions.get(0), tabView(0));
        uut.switchToTab(1);
        verify(uut, times(1)).applyOptions(tabOptions.get(1), tabView(1));
        uut.switchToTab(0);
        verify(uut, times(2)).applyOptions(tabOptions.get(0), tabView(0));
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

    private IReactView tab(TopTabsViewPager topTabs, final int index) {
        return (IReactView) ((ViewGroup) topTabs.getChildAt(index)).getChildAt(0);
    }
}
