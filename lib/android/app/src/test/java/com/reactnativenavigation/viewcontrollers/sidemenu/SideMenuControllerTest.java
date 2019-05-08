package com.reactnativenavigation.viewcontrollers.sidemenu;

import android.app.Activity;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.parse.*;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.presentation.SideMenuPresenter;
import com.reactnativenavigation.utils.*;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.views.Component;

import org.junit.Test;
import org.mockito.Mockito;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("MagicNumber")
public class SideMenuControllerTest extends BaseTest {
    private SideMenuController uut;
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private SideMenuPresenter presenter;
    private ViewController left;
    private ViewController right;
    private ViewController center;
    private ViewController child;
    private ParentController parent;
    private Options resolvedOptions;

    @Override
    public void beforeEach() {
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        presenter = spy(new SideMenuPresenter());
        child = new SimpleComponentViewController(activity, childRegistry, "child", new Options());
        left = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        right = new SimpleComponentViewController(activity, childRegistry, "right", new Options());
        center = spy(new SimpleComponentViewController(activity, childRegistry, "center", new Options()));
        uut = new SideMenuController(activity, childRegistry, "sideMenu", new Options(), presenter, new Presenter(activity, new Options())) {
            @Override
            public Options resolveCurrentOptions() {
                resolvedOptions = super.resolveCurrentOptions();
                return resolvedOptions;
            }
        };
        uut.setCenterController(center);
        parent = Mockito.mock(ParentController.class);
        uut.setParentController(parent);
    }

    @Test
    public void createView_bindView() {
        uut.ensureViewIsCreated();
        verify(presenter).bindView(uut.getView());
    }

    @Test
    public void applyChildOptions() {
        uut.applyChildOptions(new Options(), (Component) child.getView());
        verify(presenter).applyChildOptions(eq(resolvedOptions));
        verify(parent).applyChildOptions(uut.options, (Component) child.getView());
    }

    @Test
    public void mergeOptions_openLeftSideMenu() {
        uut.setLeftController(new SimpleComponentViewController(activity, childRegistry, "left", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isTrue();
    }

    @Test
    public void mergeOptions_openRightSideMenu() {
        uut.setRightController(new SimpleComponentViewController(activity, childRegistry, "right", new Options()));

        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(true);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isTrue();
    }

    @Test
    public void mergeOptions_optionsAreClearedAfterMerge() {
        Options initialOptions = uut.options;
        Options options = new Options();
        uut.mergeOptions(options);
        assertThat(uut.options.sideMenuRootOptions).isNotEqualTo(initialOptions.sideMenuRootOptions);
    }

    @Test
    public void mergeChildOptions() {
        Options options = new Options();
        uut.mergeChildOptions(options, child, (Component) child.getView());
        verify(presenter).mergeChildOptions(options.sideMenuRootOptions);
    }

    @Test
    public void resolveCurrentOptions_centerOptionsAreMergedEvenIfDrawerIsOpen() {
        uut.setLeftController(left);
        center.options.topBar.title.text = new Text("Center");
        assertThat(uut.resolveCurrentOptions().topBar.title.text.hasValue()).isTrue();

        uut.getView().openDrawer(Gravity.LEFT);
        assertThat(uut.resolveCurrentOptions().topBar.title.text.hasValue()).isTrue();
    }

    @Test
    public void setLeftController_matchesParentByDefault() {
        SideMenuOptions options = new SideMenuOptions();
        assertThat(options.width.hasValue()).isFalse();
        assertThat(options.height.hasValue()).isFalse();
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(MATCH_PARENT);
        assertThat(params.height).isEqualTo(MATCH_PARENT);
    }

    @Test
    public void setLeftController_setHeightAndWidthWithOptions() {
        SideMenuOptions options = new SideMenuOptions();
        options.height = new Number(100);
        options.width = new Number(200);
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, Resources.getSystem().getDisplayMetrics());
        int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, Resources.getSystem().getDisplayMetrics());

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(widthInDp);
        assertThat(params.height).isEqualTo(heightInDp);
    }

    @Test
    public void setRightController_matchesParentByDefault() {
        SideMenuOptions options = new SideMenuOptions();
        assertThat(options.width.hasValue()).isFalse();
        assertThat(options.height.hasValue()).isFalse();
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "right", new Options());
        uut.setRightController(componentViewController);

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(MATCH_PARENT);
        assertThat(params.height).isEqualTo(MATCH_PARENT);
    }

    @Test
    public void setRightController_setHeightAndWidthWithOptions() {
        SideMenuOptions options = new SideMenuOptions();
        options.height = new Number(100);
        options.width = new Number(200);
        uut.options.sideMenuRootOptions.left = options;

        SimpleComponentViewController componentViewController = new SimpleComponentViewController(activity, childRegistry, "left", new Options());
        uut.setLeftController(componentViewController);

        int heightInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, Resources.getSystem().getDisplayMetrics());
        int widthInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, Resources.getSystem().getDisplayMetrics());

        LayoutParams params = componentViewController.getView().getLayoutParams();
        assertThat(params.width).isEqualTo(widthInDp);
        assertThat(params.height).isEqualTo(heightInDp);
    }

    @Test
    public void handleBack_closesLeftMenu() {
        uut.setLeftController(left);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
        verify(center, times(1)).handleBack(any());

        openLeftMenu();
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(center, times(1)).handleBack(any());
    }

    @Test
    public void handleBack_closesRightMenu() {
        uut.setRightController(right);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
        verify(center, times(1)).handleBack(any());

        openRightMenu();
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(center, times(1)).handleBack(any());
    }

    @Test
    public void leftMenuOpen_visibilityEventsAreEmitted() {
        ViewController spy = spy(left);
        uut.setLeftController(spy);
        activity.setContentView(uut.getView());

        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        verify(spy, times(0)).onViewAppeared();

        openLeftMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isTrue();
        verify(spy).onViewAppeared();

        closeLeft();
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        verify(spy).onViewDisappear();
    }

    @Test
    public void rightMenuOpen_visibilityEventsAreEmitted() {
        ViewController spy = spy(right);
        uut.setRightController(spy);
        activity.setContentView(uut.getView());

        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        verify(spy, times(0)).onViewAppeared();

        openRightMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isTrue();
        verify(spy).onViewAppeared();

        closeRightMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        verify(spy).onViewDisappear();
    }

    @Test
    public void onDrawerOpened_drawerOpenedWIthSwipe_visibilityIsUpdated() {
        uut.setLeftController(left);
        uut.setRightController(right);
        uut.ensureViewIsCreated();

        openDrawerAndAssertVisibility(right, (side) -> side.resolveCurrentOptions().sideMenuRootOptions.right);
        closeDrawerAndAssertVisibility(right, (side) -> side.resolveCurrentOptions().sideMenuRootOptions.right);

        openDrawerAndAssertVisibility(left, (side) -> side.resolveCurrentOptions().sideMenuRootOptions.left);
        closeDrawerAndAssertVisibility(left, (side) -> side.resolveCurrentOptions().sideMenuRootOptions.left);
    }

    private void openDrawerAndAssertVisibility(ViewController side, Functions.FuncR1<ViewController, SideMenuOptions> opt) {
        Options options = new Options();
        (side == left ? options.sideMenuRootOptions.left : options.sideMenuRootOptions.right).visible = new Bool(true);
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerVisible(side.getView())).isTrue();
        assertThat(opt.run(side).visible.isFalseOrUndefined()).isTrue();
    }

    private void closeDrawerAndAssertVisibility(ViewController side, Functions.FuncR1<ViewController, SideMenuOptions> opt) {
        Options options = new Options();
        (side == left ? options.sideMenuRootOptions.left : options.sideMenuRootOptions.right).visible = new Bool(false);
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerVisible(side.getView())).isFalse();
        assertThat(opt.run(side).visible.isTrue()).isFalse();
    }

    private void openLeftMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(true);
        options.sideMenuRootOptions.left.animate = new Bool(false);
        uut.mergeOptions(options);
    }

    private void openRightMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(true);
        options.sideMenuRootOptions.right.animate = new Bool(false);
        uut.mergeOptions(options);
    }

    private void closeLeft() {
        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(false);
        options.sideMenuRootOptions.left.animate = new Bool(false);
        uut.mergeOptions(options);
    }

    private void closeRightMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(false);
        options.sideMenuRootOptions.right.animate = new Bool(false);
        uut.mergeOptions(options);
    }
}
