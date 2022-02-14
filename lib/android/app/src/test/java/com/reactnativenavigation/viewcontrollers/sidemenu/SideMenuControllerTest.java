package com.reactnativenavigation.viewcontrollers.sidemenu;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.Mocks;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.SideMenuOptions;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.utils.Functions;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.parent.ParentController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.sidemenu.SideMenu;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.mockito.Mockito;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.reactnativenavigation.utils.CollectionUtils.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("MagicNumber")
public class SideMenuControllerTest extends BaseTest {
    private SideMenuController uut;
    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private SideMenuPresenter presenter;
    private ViewController<?> left;
    private ViewController<?> right;
    private ViewController<?> center;
    private ViewController<?> child;
    private ParentController<?> parent;
    private Options resolvedOptions;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = createActivity();

        childRegistry = new ChildControllersRegistry();
        presenter = spy(new SideMenuPresenter());
        child = new SimpleComponentViewController(activity, childRegistry, "child", new Options());
        left = spy(new SimpleComponentViewController(activity, childRegistry, "left", new Options()));
        right = spy(new SimpleComponentViewController(activity, childRegistry, "right", createSideMenuOptions()));
        center = spy(new SimpleComponentViewController(activity, childRegistry, "center", createSideMenuOptions()));
        uut = new SideMenuController(activity, childRegistry, "sideMenu", new Options(), presenter, new Presenter(activity, new Options())) {
            @Override
            public Options resolveCurrentOptions() {
                resolvedOptions = super.resolveCurrentOptions();
                return resolvedOptions;
            }
        };
        uut.setCenterController(center);
        parent = Mocks.INSTANCE.parentController(null);
        Mockito.when(parent.resolveChildOptions(uut)).thenReturn(Options.EMPTY);
        uut.setParentController(parent);
    }

    @NotNull
    private Options createSideMenuOptions() {
        Options options = new Options();
        options.sideMenuRootOptions.left.animate = new Bool(false);
        options.sideMenuRootOptions.right.animate = new Bool(false);
        return options;
    }

    @Test
    public void createView_bindView() {
        uut.ensureViewIsCreated();
        SideMenu sideMenu = uut.getSideMenu();
        verify(presenter).bindView(eq(sideMenu));
    }

    @Test
    public void applyOptions() {
        Options options = new Options();
        uut.applyOptions(options);
        verify(presenter).applyOptions(options);
    }

    @Test
    public void getCurrentChild() {
        setLeftRight(left, right);

        assertThat(uut.getCurrentChild()).isEqualTo(center);

        openLeftMenu();
        assertThat(uut.getCurrentChild()).isEqualTo(left);

        closeLeftMenu();
        openRightMenu();
        assertThat(uut.getCurrentChild()).isEqualTo(right);

        closeRightMenu();
        assertThat(uut.getCurrentChild()).isEqualTo(center);
        uut.destroy();
        assertThat(uut.getCurrentChild()).isEqualTo(center);
    }

    @Test
    public void onViewAppeared() {
        ViewController left = spy(this.left);
        ViewGroup leftView = spy(left.getView());
        when(left.findController(leftView)).thenReturn(left);
        Mockito.doReturn(leftView).when(left).getView();

        ViewController right = spy(this.right);
        ViewGroup rightView = spy(right.getView());
        when(right.findController(rightView)).thenReturn(right);
        Mockito.doReturn(rightView).when(right).getView();

        setLeftRight(left, right);

        uut.onViewWillAppear();
        verify(leftView).requestLayout();
        verify(rightView).requestLayout();
    }

    @Test
    public void applyChildOptions() {
        uut.applyChildOptions(new Options(), child);
        verify(presenter).applyChildOptions(eq(resolvedOptions));
        verify(parent).applyChildOptions(uut.options, child);
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
        uut.mergeChildOptions(options, child);
        verify(presenter).mergeOptions(options.sideMenuRootOptions);
        verify(parent).mergeChildOptions(options, child);
    }

    @Test
    public void resolveCurrentOptions_centerOptionsAreMergedEvenIfDrawerIsOpen() {
        uut.setLeftController(left);
        center.options.topBar.title.text = new Text("Center");
        assertThat(uut.resolveCurrentOptions().topBar.title.text.hasValue()).isTrue();

        openLeftMenu();
        assertThat(uut.resolveCurrentOptions().topBar.title.text.hasValue()).isTrue();
    }

    @Test
    public void mergeChildOptions_lockModeIsUpdatedInInitialOptions() {
        setLeftRight(left, right);

        Options leftDisabled = new Options();
        leftDisabled.sideMenuRootOptions.left.enabled = new Bool(false);
        left.mergeOptions(leftDisabled);
        assertThat(uut.resolveCurrentOptions().sideMenuRootOptions.left.enabled.get()).isFalse();

        Options rightVisible = new Options();
        rightVisible.sideMenuRootOptions.right.visible = new Bool(true);
        right.mergeOptions(rightVisible);
        assertThat(uut.resolveCurrentOptions().sideMenuRootOptions.left.enabled.get()).isFalse();
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
        uut.setLeftController(left);
        activity.setContentView(uut.getView());

        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        verify(left, never()).onViewWillAppear();

        openLeftMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isTrue();
        verify(left).onViewDidAppear();

        closeLeftMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.LEFT)).isFalse();
        verify(left).onViewDisappear();
    }

    @Test
    public void rightMenuOpen_visibilityEventsAreEmitted() {
        uut.setRightController(right);
        activity.setContentView(uut.getView());

        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        verify(right, never()).onViewWillAppear();

        openRightMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isTrue();
        verify(right).onViewDidAppear();

        closeRightMenu();
        assertThat(uut.getView().isDrawerOpen(Gravity.RIGHT)).isFalse();
        verify(right).onViewDisappear();
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

    @Test
    public void onDrawerSlide_componentDidAppearIsEmittedWhenDrawerIsFullyOpened() {
        uut.setLeftController(left);
        uut.setRightController(right);
        uut.ensureViewIsCreated();

        uut.onDrawerSlide(left.getView(),0.1f);
        uut.onDrawerSlide(right.getView(),0.1f);

        verify(left, times(0)).onViewDidAppear();
        verify(right, times(0)).onViewDidAppear();

        uut.onDrawerSlide(left.getView(),1);
        uut.onDrawerSlide(right.getView(),1);

        verify(left).onViewDidAppear();
        verify(right).onViewDidAppear();
    }

    @Test
    public void applyTopInsets_delegatesToChildren() {
        setLeftRight(spy(left), spy(right));
        uut.applyTopInset();
        forEach(uut.getChildControllers(), c -> verify(c).applyTopInset());
    }

    @Test
    public void onMeasureChild_topInsetsAreApplied() {
        setLeftRight(spy(left), spy(right));
        idleMainLooper();
        uut.applyTopInset();
        forEach(uut.getChildControllers(), c -> verify(c).applyTopInset());
    }

    private void openDrawerAndAssertVisibility(ViewController<?> side, Functions.FuncR1<ViewController<?>, SideMenuOptions> opt) {
        Options options = new Options();
        (side == left ? options.sideMenuRootOptions.left : options.sideMenuRootOptions.right).visible = new Bool(true);
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(getGravity(side))).isTrue();
        assertThat(opt.run(side).visible.isFalseOrUndefined()).isTrue();
    }

    private void closeDrawerAndAssertVisibility(ViewController<?> side, Functions.FuncR1<ViewController<?>, SideMenuOptions> opt) {
        Options options = new Options();
        (side == left ? options.sideMenuRootOptions.left : options.sideMenuRootOptions.right).visible = new Bool(false);
        uut.mergeOptions(options);
        assertThat(uut.getView().isDrawerOpen(getGravity(side))).isFalse();
        assertThat(opt.run(side).visible.isTrue()).isFalse();
    }

    private int getGravity(ViewController<?> side) {
        return side == left ? Gravity.LEFT : Gravity.RIGHT;
    }

    private void openLeftMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(true);
        options.sideMenuRootOptions.left.animate = new Bool(false);
        uut.mergeOptions(options);
        uut.onDrawerSlide(left.getView(), 1);
    }

    private void openRightMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(true);
        options.sideMenuRootOptions.right.animate = new Bool(false);
        uut.mergeOptions(options);
        uut.onDrawerSlide(right.getView(), 1);
    }

    private void closeLeftMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.left.visible = new Bool(false);
        options.sideMenuRootOptions.left.animate = new Bool(false);
        uut.mergeOptions(options);
        uut.onDrawerSlide(left.getView(), 0);
    }

    private void closeRightMenu() {
        Options options = new Options();
        options.sideMenuRootOptions.right.visible = new Bool(false);
        options.sideMenuRootOptions.right.animate = new Bool(false);
        uut.mergeOptions(options);
        uut.onDrawerSlide(right.getView(), 0);
    }

    private Activity createActivity() {
        Activity activity = spy(newActivity());
        Window window = mock(Window.class);
        when(window.getDecorView()).thenReturn(mock(View.class));
        when(activity.getWindow()).thenReturn(window);
        return activity;
    }

    private void setLeftRight(ViewController<?> left, ViewController<?> right) {
        uut.setLeftController(left);
        uut.setRightController(right);
        left.setParentController(uut);
        right.setParentController(uut);
    }
}
