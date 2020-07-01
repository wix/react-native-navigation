package com.reactnativenavigation.viewcontrollers.stack;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.anim.StackAnimator;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.RenderChecker;
import com.reactnativenavigation.presentation.StackPresenter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.StatusBarUtils;
import com.reactnativenavigation.utils.TitleBarHelper;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.utils.ViewHelper;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ParentController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;
import com.reactnativenavigation.views.element.ElementTransitionManager;
import com.reactnativenavigation.views.stack.StackBehaviour;
import com.reactnativenavigation.views.topbar.ScrollDIsabledBehavior;
import com.reactnativenavigation.views.topbar.TopBar;

import org.assertj.core.api.iterable.Extractor;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import static com.reactnativenavigation.utils.ObjectUtils.take;
import static com.reactnativenavigation.utils.ViewUtils.topMargin;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@LooperMode(LooperMode.Mode.PAUSED)
public class StackControllerTest extends BaseTest {

    private Activity activity;
    private ChildControllersRegistry childRegistry;
    private StackController uut;
    private ViewController child1;
    private ViewController child1a;
    private ViewController child2;
    private ViewController child3;
    private SimpleViewController.SimpleView child3View;
    private ViewController child4;
    private StackAnimator animator;
    private TopBarController topBarController;
    private StackPresenter presenter;
    private BackButtonHelper backButtonHelper;
    private EventEmitter eventEmitter;

    @Override
    public void beforeEach() {
        super.beforeEach();
        eventEmitter = Mockito.mock(EventEmitter.class);
        backButtonHelper = spy(new BackButtonHelper());
        activity = newActivity();
        StatusBarUtils.saveStatusBarHeight(63);
        animator = spy(new StackAnimator(activity, new ElementTransitionManager()));
        childRegistry = new ChildControllersRegistry();
        presenter = spy(new StackPresenter(
                    activity,
                    new TitleBarReactViewCreatorMock(),
                    new TopBarBackgroundViewCreatorMock(),
                    new TitleBarButtonCreatorMock(),
                    new IconResolver(activity, ImageLoaderMock.mock()),
                    new RenderChecker(),
                    new Options()
                )
        );
        createChildren();
        uut = createStack();
        activity.setContentView(uut.getView());
    }

    private void createChildren() {
        child1 = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        child1a = spy(new SimpleViewController(activity, childRegistry, "child1", new Options()));
        child2 = spy(new SimpleViewController(activity, childRegistry, "child2", new Options()));
        child3 = spy(new SimpleViewController(activity, childRegistry, "child3", new Options()) {
            @Override
            protected SimpleView createView() {
                return take(child3View, super.createView());
            }
        });
        child4 = spy(new SimpleViewController(activity, childRegistry, "child4", new Options()));
    }

    @Test
    public void isAViewController() {
        assertThat(uut).isInstanceOf(ViewController.class);
    }

    @Test
    public void childrenAreAssignedParent() {
        StackController uut = createStack(Arrays.asList(child1, child2));
        for (ViewController child : uut.getChildControllers()) {
            assertThat(child.getParentController().equals(uut)).isTrue();
        }
    }

    @Test
    public void constructor_backButtonIsAddedToChild() {
        createStack(Arrays.asList(child1, child2, child3));
        assertThat(child2.options.topBar.buttons.back.visible.get(false)).isTrue();
        assertThat(child3.options.topBar.buttons.back.visible.get(false)).isTrue();
    }

    @Test
    public void createView_currentChildIsAdded() {
        StackController uut = createStack(Arrays.asList(child1, child2, child3, child4));
        assertThat(uut.getChildControllers().size()).isEqualTo(4);
        assertThat(uut.getView().getChildCount()).isEqualTo(2);
        assertThat(uut.getView().getChildAt(0)).isEqualTo(child4.getView());
    }

    @Test
    public void createView_topBarScrollIsDisabled() {
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) uut.getTopBar().getLayoutParams()).getBehavior();
        assertThat(behavior instanceof ScrollDIsabledBehavior).isTrue();
    }

    @Test
    public void holdsAStackOfViewControllers() {
        assertThat(uut.isEmpty()).isTrue();
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter());
        assertThat(uut.peek()).isEqualTo(child3);
        assertContainsOnlyId(child1.getId(), child2.getId(), child3.getId());
    }

    @Test
    public void isRendered_falseIfStackIsEmpty() {
        assertThat(uut.size()).isZero();
        assertThat(uut.isRendered()).isFalse();
    }

    @Test
    public void isRendered() {
        disablePushAnimation(child1);

        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.isRendered()).isTrue();

        child1.setWaitForRender(new Bool(true));
        assertThat(uut.isRendered()).isFalse();

        child1.getView().addView(new View(activity));
        assertThat(uut.isRendered()).isTrue();

        Mockito.when(presenter.isRendered(child1.getView())).then(ignored -> false);
        assertThat(uut.isRendered()).isFalse();
    }

    @Test
    public void push() {
        assertThat(uut.isEmpty()).isTrue();
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.push(child1, listener);
        assertContainsOnlyId(child1.getId());
        assertThat(((CoordinatorLayout.LayoutParams) child1.getView().getLayoutParams()).getBehavior()).isInstanceOf(StackBehaviour.class);
        verify(listener, times(1)).onSuccess(child1.getId());
    }

    @Test
    public void push_backButtonIsNotAddedIfScreenContainsLeftButton() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());

        child2.options.topBar.buttons.left = new ArrayList<>(Collections.singleton(TitleBarHelper.iconButton("someButton", "icon.png")));
        uut.push(child2, new CommandListenerAdapter());
        ShadowLooper.idleMainLooper();

        assertThat(topBarController.getView().getTitleBar().getNavigationIcon()).isNotNull();
        verify(topBarController.getView(), times(0)).setBackButton(any());
    }

    @Test
    public void push_backButtonIsNotAddedIfScreenClearsLeftButton() {
        child1.options.topBar.buttons.left = new ArrayList<>();
        uut.push(child1, new CommandListenerAdapter());
        verify(child1, times(0)).mergeOptions(any());
    }

    @Test
    public void push_backButtonAddedBeforeChildViewIsCreated() {
        disablePopAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        InOrder inOrder = inOrder(backButtonHelper, child2);
        inOrder.verify(backButtonHelper).addToPushedChild(child2);
        inOrder.verify(child2).setParentController(uut);
        inOrder.verify(child2, atLeastOnce()).getView(); // creates view
    }

    @Test
    public void push_waitForRender() {
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());
        assertThat(child1.getView().getParent()).isEqualTo(uut.getView());

        child2.options.animations.push.waitForRender = new Bool(true);
        uut.push(child2, new CommandListenerAdapter());

        // Both children are attached
        assertThat(child1.getView().getParent()).isEqualTo(uut.getView());
        assertThat(child2.getView().getParent()).isEqualTo(uut.getView());
        assertThat(child2.isViewShown()).isFalse();
        verify(child2, times(0)).onViewWillAppear();

        child2.getView().addView(new View(activity));
        ShadowLooper.idleMainLooper();
        verify(child2).onViewWillAppear();
        assertThat(child2.isViewShown()).isTrue();
        animator.endPushAnimation(child2.getView());
        assertThat(child1.getView().getParent()).isNull();
    }

    @Test
    public void push_backPressedDuringPushAnimationDestroysPushedScreenImmediately() {
        backPressedDuringPushAnimation(false);
    }

    @Test @Ignore
    public void push_backPressedDuringPushAnimationDestroysPushedScreenImmediatelyWaitForRender() {
        backPressedDuringPushAnimation(true);
    }

    private void backPressedDuringPushAnimation(boolean waitForRender) {
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());

        CommandListenerAdapter pushListener = spy(new CommandListenerAdapter());
        child2.options.animations.push.waitForRender = new Bool(waitForRender);
        uut.push(child2, pushListener);
        // both children are attached
        assertThat(child1.getView().getParent()).isEqualTo(uut.getView());
        assertThat(child2.getView().getParent()).isEqualTo(uut.getView());
        CommandListenerAdapter backListener = spy(new CommandListenerAdapter());
        uut.handleBack(backListener);
        assertThat(uut.size()).isOne();
        assertThat(child1.getView().getParent()).isEqualTo(uut.getView());
        assertThat(child2.isDestroyed()).isTrue();

        InOrder inOrder = inOrder(pushListener, backListener);
        inOrder.verify(pushListener).onSuccess(any());
        inOrder.verify(backListener).onSuccess(any());
    }

    @Test
    public void push_rejectIfStackContainsChildWithId() {
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.size()).isEqualTo(1);

        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.push(child1a, listener);
        verify(listener).onError(any());
        assertThat(uut.size()).isEqualTo(1);
    }

    @Test
    public void push_onViewDidAppearInvokedOnPushedScreen() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter()); // Initialize stack with a child

        uut.push(child2, new CommandListenerAdapter());
        verify(child2).onViewDidAppear();
    }

    @Test
    public void animateSetRoot() {
        disablePushAnimation(child1, child2, child3);
        assertThat(uut.isEmpty()).isTrue();
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.setRoot(Collections.singletonList(child3), new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertContainsOnlyId(child3.getId());
            }
        });
    }

    @Test
    public void setRoot_singleChild() {
        activity.setContentView(uut.getView());
        disablePushAnimation(child1, child2, child3);

        assertThat(uut.isEmpty()).isTrue();
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        ShadowLooper.idleMainLooper();
        assertThat(uut.getTopBar().getTitleBar().getNavigationIcon()).isNotNull();
        uut.setRoot(Collections.singletonList(child3), new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertContainsOnlyId(child3.getId());
                ShadowLooper.idleMainLooper();
                assertThat(uut.getTopBar().getTitleBar().getNavigationIcon()).isNull();
            }
        });
    }

    @Test
    public void setRoot_multipleChildren() {
        Robolectric.getForegroundThreadScheduler().pause();

        activity.setContentView(uut.getView());
        disablePushAnimation(child1, child2, child3, child4);
        disablePopAnimation(child4);

        assertThat(uut.isEmpty()).isTrue();
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        ShadowLooper.idleMainLooper();
        assertThat(uut.getTopBar().getTitleBar().getNavigationIcon()).isNotNull();

        uut.setRoot(Arrays.asList(child3, child4), new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertContainsOnlyId(child3.getId(), child4.getId());
                assertThat(child4.isViewShown()).isTrue();
                assertThat(child3.isViewShown()).isFalse();

                assertThat(uut.getCurrentChild()).isEqualTo(child4);
                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                ShadowLooper.idleMainLooper();
                assertThat(uut.getTopBar().getTitleBar().getNavigationIcon()).isNull();
                assertThat(uut.getCurrentChild()).isEqualTo(child3);
            }
        });
    }

    @Test
    public void setRoot_backButtonIsAddedToAllChildren() {
        Robolectric.getForegroundThreadScheduler().pause();

        activity.setContentView(uut.getView());
        disablePushAnimation(child1, child2);

        uut.setRoot(Arrays.asList(child1, child2), new CommandListenerAdapter());
        assertThat(child1.options.topBar.buttons.back.visible.get(false)).isFalse();
        assertThat(child2.options.topBar.buttons.back.visible.get(false)).isTrue();
    }

    @Test
    public void setRoot_doesNotCrashWhenCalledInQuickSuccession() {
        disablePushAnimation(child1);
        uut.setRoot(Collections.singletonList(child1), new CommandListenerAdapter());

        ViewGroup c2View = child2.getView();
        ViewGroup c3View = child3.getView();
        uut.setRoot(Collections.singletonList(child2), new CommandListenerAdapter());
        uut.setRoot(Collections.singletonList(child3), new CommandListenerAdapter());
        animator.endPushAnimation(c2View);
        animator.endPushAnimation(c3View);

        assertContainsOnlyId(child3.getId());
    }

    @Test
    public void setRoot_doesNotCrashWhenCalledWithSameId() {
        disablePushAnimation(child1, child1a);
        uut.setRoot(Collections.singletonList(child1), new CommandListenerAdapter());
        uut.setRoot(Collections.singletonList(child1a), new CommandListenerAdapter());

        assertContainsOnlyId(child1a.getId());
    }

    @Test
    public void setRoot_topScreenIsStartedThenTheRest() {
        disablePushAnimation(child1, child2, child3);
        child3View = spy(new SimpleViewController.SimpleView(activity));

        uut.setRoot(Arrays.asList(child1, child2, child3), new CommandListenerAdapter());
        ShadowLooper.idleMainLooper();
        InOrder inOrder = inOrder(child3View, child2, child1);
        inOrder.verify(child3View).start();
        inOrder.verify(child2).start();
        inOrder.verify(child1).start();
    }

    @Test
    public void setRoot_onViewDidAppearIsInvokedOnAppearingChild() {
        disablePushAnimation(child1);
        uut.setRoot(Collections.singletonList(child1), new CommandListenerAdapter());

        verify(child1).onViewDidAppear();
    }

    @Test
    public void setRoot_inViewDidAppearIsInvokedBeforePreviousRootIsDestroyed() {
        disablePushAnimation(child1, child2, child3);
        uut.push(child1, new CommandListenerAdapter());

        uut.setRoot(Arrays.asList(child2, child3), new CommandListenerAdapter());
        ShadowLooper.idleMainLooper();

        InOrder inOrder = inOrder(child2, child3, child1);
        inOrder.verify(child3).onViewDidAppear();
        inOrder.verify(child1).onViewDisappear();
        verify(child2, times(0)).onViewDidAppear();
    }

    @Test
    public void pop() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertContainsOnlyId(child2.getId(), child1.getId());
                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                assertContainsOnlyId(child1.getId());
            }
        });
    }

    @Test
    public void pop_screenCurrentlyBeingPushedIsPopped() {
        disablePushAnimation(child1, child2);
        uut.push(child1, Mockito.mock(CommandListenerAdapter.class));
        uut.push(child2, Mockito.mock(CommandListenerAdapter.class));

        uut.push(child3, Mockito.mock(CommandListenerAdapter.class));
        uut.pop(Options.EMPTY, Mockito.mock(CommandListenerAdapter.class));
        assertThat(uut.size()).isEqualTo(2);
        assertContainsOnlyId(child1.getId(), child2.getId());
    }

    @Test
    public void pop_appliesOptionsAfterPop() {
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                verify(uut, times(1)).applyChildOptions(uut.options, eq(child1));
            }
        });
    }

    @Test
    public void pop_layoutHandlesChildWillDisappear() {
        TopBarController topBarController = new TopBarController();
        uut = TestUtils.newStackController(activity)
                .setTopBarController(topBarController)
                .setId("uut")
                .build();
        uut.ensureViewIsCreated();
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.pop(Options.EMPTY, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        verify(presenter, times(1)).onChildWillAppear(uut, child1, child2);
                    }
                });
            }
        });
    }

    @Test
    public void pop_popEventIsEmitted() {
        disablePushAnimation(child1, child2);
        disablePopAnimation(child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        verify(eventEmitter).emitScreenPoppedEvent(child2.getId());
    }

    @Test
    public void popToRoot_popEventIsEmitted() {
        disablePushAnimation(child1, child2, child3);
        disablePopAnimation(child2, child3);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter());

        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        verify(eventEmitter).emitScreenPoppedEvent(child3.getId());
        verifyNoMoreInteractions(eventEmitter);
    }

    @Test
    public void stackOperations() {
        assertThat(uut.peek()).isNull();
        assertThat(uut.size()).isZero();
        assertThat(uut.isEmpty()).isTrue();
        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.peek()).isEqualTo(child1);
        assertThat(uut.size()).isEqualTo(1);
        assertThat(uut.isEmpty()).isFalse();
    }

    @Test
    public void onChildDestroyed() {
        uut.onChildDestroyed(child2);
        verify(presenter).onChildDestroyed(child2);
    }

    @Test
    public void handleBack_PopsUnlessSingleChild() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();

        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.size()).isEqualTo(1);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();

        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.size()).isEqualTo(2);
                assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();

                assertThat(uut.size()).isEqualTo(1);
                assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
            }
        });
    }

    @Test
    public void pop_doesNothingWhenZeroOrOneChild() {
        assertThat(uut.isEmpty()).isTrue();
        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        assertThat(uut.isEmpty()).isTrue();

        uut.push(child1, new CommandListenerAdapter());
        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        assertContainsOnlyId(child1.getId());
    }

    @SuppressWarnings("MagicNumber")
    @Test
    public void pop_animationOptionsAreMergedCorrectlyToDisappearingChild() throws JSONException {
        disablePushAnimation(child1, child2);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        Options mergeOptions = new Options();
        JSONObject content = new JSONObject();
        JSONObject x = new JSONObject();
        x.put("duration", 300);
        x.put("from", 0);
        x.put("to", 1000);
        content.put("x", x);
        mergeOptions.animations.pop.content = new AnimationOptions(content);

        uut.pop(mergeOptions, new CommandListenerAdapter());
        ArgumentCaptor<NestedAnimationsOptions> captor = ArgumentCaptor.forClass(NestedAnimationsOptions.class);
        verify(animator, times(1)).pop(any(), captor.capture(), any());
        Animator animator = captor.getValue().content
                .getAnimation(mockView(activity))
                .getChildAnimations()
                .get(0);
        assertThat(animator.getDuration()).isEqualTo(300);
    }

    @SuppressWarnings("MagicNumber")
    @Test
    public void pop_animationOptionsAreMergedCorrectlyToDisappearingChildWithDefaultOptions() throws JSONException {
        disablePushAnimation(child1, child2);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        Options defaultOptions = new Options();
        JSONObject content = new JSONObject();
        JSONObject x = new JSONObject();
        x.put("duration", 300);
        x.put("from", 0);
        x.put("to", 1000);
        content.put("x", x);
        defaultOptions.animations.pop.content = new AnimationOptions(content);
        uut.setDefaultOptions(defaultOptions);

        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        ArgumentCaptor<NestedAnimationsOptions> captor = ArgumentCaptor.forClass(NestedAnimationsOptions.class);
        verify(animator, times(1)).pop(any(), captor.capture(), any());
        Animator animator = captor.getValue().content
                .getAnimation(mockView(activity))
                .getChildAnimations()
                .get(0);
        assertThat(animator.getDuration()).isEqualTo(300);
    }

    @Test
    public void canPopWhenSizeIsMoreThanOne() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.canPop()).isFalse();
        uut.push(child1, new CommandListenerAdapter());
        assertContainsOnlyId(child1.getId());
        assertThat(uut.canPop()).isFalse();
        uut.push(child2, new CommandListenerAdapter());
        assertContainsOnlyId(child1.getId(), child2.getId());
        assertThat(uut.canPop()).isTrue();
    }

    @Test
    public void push_addsToViewTree() {
        assertNotChildOf(uut.getView(), child1.getView());
        uut.push(child1, new CommandListenerAdapter());
        assertIsChild(uut.getView(), child1.getView());
    }

    @Test
    public void push_removesPreviousFromTree() {
        assertNotChildOf(uut.getView(), child1.getView());
        uut.push(child1, new CommandListenerAdapter());
        assertIsChild(uut.getView(), child1.getView());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat((View) uut.getView().findViewById(child1.getView().getId())).isNull();
                assertThat((View) uut.getView().findViewById(child2.getView().getId())).isNotNull();
            }
        });
    }

    @Test
    public void push_assignsRefToSelfOnPushedController() {
        assertThat(child1.getParentController()).isNull();
        uut.push(child1, new CommandListenerAdapter());
        assertThat(child1.getParentController()).isEqualTo(uut);

        StackController anotherNavController = createStack("another");
        anotherNavController.ensureViewIsCreated();
        anotherNavController.push(child2, new CommandListenerAdapter());
        assertThat(child2.getParentController()).isEqualTo(anotherNavController);
    }

    @Test
    public void push_doesNotAnimateTopBarIfScreenIsPushedWithoutAnimation() {
        uut.ensureViewIsCreated();
        child1.ensureViewIsCreated();

        child1.options.topBar.visible = new Bool(false);
        child1.options.topBar.animate = new Bool(false);
        disablePushAnimation(child1, child2);

        uut.push(child1, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                child1.onViewWillAppear();
                assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);

                uut.push(child2, new CommandListenerAdapter());
                child2.onViewWillAppear();
                verify(topBarController, times(0)).showAnimate(child2.options.animations.push.topBar, 0);
                assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.VISIBLE);
                verify(topBarController, times(2)).resetViewProperties();
            }
        });
    }

    @Test
    public void push_animatesAndClearsPreviousAnimationValues() {
        uut.ensureViewIsCreated();

        child1.options.topBar.visible = new Bool(false);
        child1.options.topBar.animate = new Bool(false);
        child1.options.animations.push.enabled = new Bool(false);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(topBarController, times(1)).resetViewProperties();
            }
        });
    }

    @Test
    public void pop_replacesViewWithPrevious() {
        final View child2View = child2.getView();
        final View child1View = child1.getView();

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertIsChild(uut.getView(), child2View);
                assertNotChildOf(uut.getView(), child1View);
                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                assertNotChildOf(uut.getView(), child2View);
                assertIsChild(uut.getView(), child1View);
            }
        });
    }

    @Test
    public void popTo_PopsTopUntilControllerIsNewTop() {
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.size()).isEqualTo(3);
                assertThat(uut.peek()).isEqualTo(child3);

                uut.popTo(child1, Options.EMPTY, new CommandListenerAdapter());

                assertThat(uut.size()).isEqualTo(1);
                assertThat(uut.peek()).isEqualTo(child1);
            }
        });
    }

    @Test
    public void popTo_optionsAreMergedOnTopChild() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());

        Options mergeOptions = new Options();
        uut.popTo(child2, mergeOptions, new CommandListenerAdapter());
        uut.popTo(child1, mergeOptions, new CommandListenerAdapter());
        verify(child1, times(0)).mergeOptions(mergeOptions);

        uut.push(child2, new CommandListenerAdapter());
        uut.popTo(child1, mergeOptions, new CommandListenerAdapter());
        verify(child2).mergeOptions(mergeOptions);
    }

    @Test
    public void popTo_NotAChildOfThisStack_DoesNothing() {
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter());
        assertThat(uut.size()).isEqualTo(2);
        uut.popTo(child2, Options.EMPTY, new CommandListenerAdapter());
        assertThat(uut.size()).isEqualTo(2);
    }

    @Test
    public void popTo_animatesTopController() {
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter());
        uut.push(child4, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popTo(child2, Options.EMPTY, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        verify(animator, times(0)).pop(eq(child1.getView()), any(), any());
                        verify(animator, times(0)).pop(eq(child2.getView()), any(), any());
                        verify(animator, times(1)).pop(eq(child4.getView()), eq(child4.options.animations.push), any());
                    }
                });
            }
        });
    }

    @Test
    public void popTo_pushAnimationIsCancelled() {
        disablePushAnimation(child1, child2);
        uut.push(child1, Mockito.mock(CommandListenerAdapter.class));
        uut.push(child2, Mockito.mock(CommandListenerAdapter.class));

        ViewGroup pushed = child3.getView();
        uut.push(child3, Mockito.mock(CommandListenerAdapter.class));
        uut.popTo(child1, Options.EMPTY, Mockito.mock(CommandListenerAdapter.class));
        animator.endPushAnimation(pushed);
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void popToRoot_PopsEverythingAboveFirstController() {
        child1.options.animations.push.enabled = new Bool(false);
        child2.options.animations.push.enabled = new Bool(false);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(uut.size()).isEqualTo(3);
                assertThat(uut.peek()).isEqualTo(child3);

                uut.popToRoot(Options.EMPTY, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        assertThat(uut.size()).isEqualTo(1);
                        assertThat(uut.peek()).isEqualTo(child1);
                    }
                });
            }
        });
    }

    @Test
    public void popToRoot_onlyTopChildIsAnimated() {
        child1.options.animations.push.enabled = new Bool(false);
        child2.options.animations.push.enabled = new Bool(false);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popToRoot(Options.EMPTY, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        verify(animator, times(1)).pop(eq(child3.getView()), eq(child3.options.animations.pop), any());
                    }
                });
            }
        });
    }

    @Test
    public void popToRoot_topChildrenAreDestroyed() {
        child1.options.animations.push.enabled = new Bool(false);
        child2.options.animations.push.enabled = new Bool(false);
        child3.options.animations.push.enabled = new Bool(false);

        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter());

        uut.popToRoot(Options.EMPTY, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(child1, times(0)).destroy();
                verify(child2, times(1)).destroy();
                verify(child3, times(1)).destroy();
            }
        });
    }

    @Test
    public void popToRoot_EmptyStackDoesNothing() {
        assertThat(uut.isEmpty()).isTrue();
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.popToRoot(Options.EMPTY, listener);
        assertThat(uut.isEmpty()).isTrue();
        verify(listener, times(1)).onError(any());
    }

    @Test
    public void popToRoot_optionsAreMergedOnTopChild() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());

        Options mergeOptions = new Options();
        uut.popToRoot(mergeOptions, new CommandListenerAdapter());
        verify(child2).mergeOptions(mergeOptions);
        verify(child1, times(0)).mergeOptions(mergeOptions);
    }

    @Test
    public void popToRoot_screenPushedBeforePopAnimationCompletesIsPopped() {
        disablePushAnimation(child1, child2);
        uut.push(child1, Mockito.mock(CommandListenerAdapter.class));
        uut.push(child2, Mockito.mock(CommandListenerAdapter.class));

        ViewGroup pushed = child3.getView();
        uut.push(child3, Mockito.mock(CommandListenerAdapter.class));
        uut.popToRoot(Options.EMPTY, Mockito.mock(CommandListenerAdapter.class));
        animator.endPushAnimation(pushed);
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void findControllerById_ReturnsSelfOrChildrenById() {
        assertThat(uut.findController("123")).isNull();
        assertThat(uut.findController(uut.getId())).isEqualTo(uut);
        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.findController(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Deeply() {
        StackController stack = createStack("another");
        stack.ensureViewIsCreated();
        stack.push(child2, new CommandListenerAdapter());
        uut.push(stack, new CommandListenerAdapter());
        assertThat(uut.findController(child2.getId())).isEqualTo(child2);
    }

    @Test
    public void pop_CallsDestroyOnPoppedChild() {
        child1 = spy(child1);
        child2 = spy(child2);
        child3 = spy(child3);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(child3, times(0)).destroy();
                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                verify(child3, times(1)).destroy();
            }
        });
    }

    @Test
    public void pop_callWillDisappear() {
        disablePushAnimation(child1, child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        verify(child2).onViewWillDisappear();
    }

    @Test
    public void pop_callDidAppear() {
        disablePushAnimation(child1, child2);
        disablePopAnimation(child2);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.pop(Options.EMPTY, new CommandListenerAdapter());
        verify(child1).onViewDidAppear();
    }

    @Test
    public void pop_animatesTopBar() {
        uut.ensureViewIsCreated();

        child1.options.topBar.visible = new Bool(false);
        child1.options.animations.push.enabled = new Bool(false);
        child2.options.animations.push.enabled = new Bool(true);
        uut.push(child1, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                child1.onViewWillAppear();
                assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);
                uut.push(child2, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        uut.pop(Options.EMPTY, new CommandListenerAdapter() {
                            @Override
                            public void onSuccess(String childId) {
                                verify(topBarController, times(1)).hideAnimate(child2.options.animations.pop.topBar, 0, 0);
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void pop_doesNotAnimateTopBarIfScreenIsPushedWithoutAnimation() {
        uut.ensureViewIsCreated();
        disablePushAnimation(child1, child2);

        child1.options.topBar.visible = new Bool(false);
        child1.options.topBar.animate = new Bool(false);
        child2.options.animations.push.enabled = new Bool(false);
        child2.options.topBar.animate = new Bool(false);

        child1.ensureViewIsCreated();
        uut.push(child1, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.push(child2, new CommandListenerAdapter());
                ShadowLooper.idleMainLooper();
                assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.VISIBLE);

                uut.pop(Options.EMPTY, new CommandListenerAdapter());
                verify(topBarController, times(0)).hideAnimate(child2.options.animations.pop.topBar, 0, 0);
                assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);
            }
        });
    }

    @Test
    public void popTo_CallsDestroyOnPoppedChild() {
        child1 = spy(child1);
        child2 = spy(child2);
        child3 = spy(child3);
        uut.push(child1, new CommandListenerAdapter());
        uut.push(child2, new CommandListenerAdapter());
        uut.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                verify(child2, times(0)).destroy();
                verify(child3, times(0)).destroy();

                uut.popTo(child1, Options.EMPTY, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        verify(child2, times(1)).destroy();
                        verify(child3, times(1)).destroy();
                    }
                });
            }
        });
    }

    @Test
    public void stackCanBePushed() {
        ViewUtils.removeFromParent(uut.getView());
        StackController parent = createStack("someStack");
        parent.ensureViewIsCreated();
        parent.push(uut, new CommandListenerAdapter());
        uut.onViewWillAppear();
        assertThat(parent.getView().getChildAt(0)).isEqualTo(uut.getView());
    }

    @Test
    public void applyOptions_applyOnlyOnFirstStack() {
        ViewUtils.removeFromParent(uut.getView());
        StackController parent = spy(createStack("someStack"));
        parent.ensureViewIsCreated();
        parent.push(uut, new CommandListenerAdapter());

        Options childOptions = new Options();
        childOptions.topBar.title.text = new Text("Something");
        child1.options = childOptions;
        uut.push(child1, new CommandListenerAdapter());
        child1.ensureViewIsCreated();
        child1.onViewWillAppear();

        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ViewController> viewCaptor = ArgumentCaptor.forClass(ViewController.class);
        verify(parent, times(1)).applyChildOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(optionsCaptor.getValue().topBar.title.text.hasValue()).isFalse();
    }

    @Test
    public void applyOptions_topTabsAreNotVisibleIfNoTabsAreDefined() {
        uut.ensureViewIsCreated();
        uut.push(child1, new CommandListenerAdapter());
        child1.ensureViewIsCreated();
        child1.onViewWillAppear();
        assertThat(ViewHelper.isVisible(uut.getTopBar().getTopTabs())).isFalse();
    }

    @Test
    public void buttonPressInvokedOnCurrentStack() {
        uut.ensureViewIsCreated();
        uut.push(child1, new CommandListenerAdapter());
        uut.sendOnNavigationButtonPressed("btn1");
        verify(child1, times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @Test
    public void mergeChildOptions_updatesViewWithNewOptions() {
        StackController uut = spy(TestUtils.newStackController(activity)
                .setId("stack")
                .build());
        Options optionsToMerge = new Options();
        ViewController vc = mock(ViewController.class);
        uut.mergeChildOptions(optionsToMerge, vc);
        verify(uut, times(1)).mergeChildOptions(optionsToMerge, vc);
    }

    @Test
    public void mergeOptions_doesNotMergeOptionsIfViewIsNotVisible() {
        uut.mergeOptions(Options.EMPTY);
        verify(presenter, times(0)).mergeOptions(any(), any(), any());
    }

    @Test
    public void mergeChildOptions_updatesParentControllerWithNewOptions() {
        StackController uut = TestUtils.newStackController(activity)
                        .setId("stack")
                        .build();
        ParentController parentController = Mockito.mock(ParentController.class);
        uut.setParentController(parentController);
        uut.ensureViewIsCreated();
        Options optionsToMerge = new Options();
        optionsToMerge.topBar.testId = new Text("topBarID");
        optionsToMerge.bottomTabsOptions.testId = new Text("bottomTabsID");
        ViewController vc = mock(ViewController.class);
        uut.mergeChildOptions(optionsToMerge, vc);

        ArgumentCaptor<Options> captor = ArgumentCaptor.forClass(Options.class);
        verify(parentController, times(1)).mergeChildOptions(captor.capture(), eq(vc));
        assertThat(captor.getValue().topBar.testId.hasValue()).isFalse();
        assertThat(captor.getValue().bottomTabsOptions.testId.get()).isEqualTo(optionsToMerge.bottomTabsOptions.testId.get());
    }

    @Test
    public void mergeChildOptions_StackRelatedOptionsAreCleared() {
        uut.ensureViewIsCreated();
        ParentController parentController = Mockito.mock(ParentController.class);
        uut.setParentController(parentController);
        Options options = new Options();
        options.animations.push = NestedAnimationsOptions.parse(new JSONObject());
        options.topBar.testId = new Text("id");
        options.fabOptions.id = new Text("fabId");
        ViewController vc = mock(ViewController.class);

        assertThat(options.fabOptions.hasValue()).isTrue();
        uut.mergeChildOptions(options, vc);
        ArgumentCaptor<Options> captor = ArgumentCaptor.forClass(Options.class);
        verify(parentController, times(1)).mergeChildOptions(captor.capture(), eq(vc));
        assertThat(captor.getValue().animations.push.hasValue()).isFalse();
        assertThat(captor.getValue().topBar.testId.hasValue()).isFalse();
        assertThat(captor.getValue().fabOptions.hasValue()).isFalse();
    }

    @Test
    public void applyChildOptions_appliesResolvedOptions() {
        disablePushAnimation(child1, child2);

        uut.push(child1, new CommandListenerAdapter());

        assertThat(uut.getTopBar().getTitle()).isNullOrEmpty();

        Options uutOptions = new Options();
        uutOptions.topBar.title.text = new Text("UUT");
        uut.mergeOptions(uutOptions);

        assertThat(uut.getTopBar().getTitle()).isEqualTo("UUT");

        uut.push(child2, new CommandListenerAdapter());
        assertThat(uut.getTopBar().getTitle()).isEqualTo("UUT");
    }

    @Test
    public void mergeChildOptions_presenterDoesNotApplyOptionsIfViewIsNotShown() {
        ViewController vc = mock(ViewController.class);
        when(vc.isViewShown()).thenReturn(false);
        uut.mergeChildOptions(new Options(), vc);
        verify(presenter, times(0)).mergeChildOptions(any(), any(), any(), any());
    }

    @Test
    public void mergeChildOptions_presenterMergesOptionsOnlyForCurrentChild() {
        ViewController vc = mock(ViewController.class);
        when(vc.isViewShown()).thenReturn(true);
        uut.mergeChildOptions(new Options(), vc);
        verify(presenter, times(0)).mergeChildOptions(any(), any(), any(), any());
    }

    @Test
    public void resolvedOptionsAreAppliedWhenStackIsAttachedToParentAndNotVisible() {
        FrameLayout parent = new FrameLayout(activity);
        activity.setContentView(parent);

        ViewController child = new SimpleViewController(activity, childRegistry, "child1", new Options());
        StackController stack = createStack(Collections.singletonList(child));
        stack.getView().setVisibility(View.INVISIBLE);

        parent.addView(stack.getView());

        ShadowLooper.idleMainLooper();
        verify(presenter).applyChildOptions(any(), eq(stack), eq(child));
    }

    @Test
    public void onAttachToParent_doesNotCrashWhenCalledAfterDestroy() {
        Robolectric.getForegroundThreadScheduler().pause();
        StackController spy = spy(createStack());

        StackLayout view = spy.getView();
        spy.push(child1, new CommandListenerAdapter());
        activity.setContentView(view);

        child1.destroy();
        ShadowLooper.idleMainLooper();

        verify(spy).onAttachToParent();
    }

    @Test
    public void onDependentViewChanged_delegatesToPresenter() {
        CoordinatorLayout parent = Mockito.mock(CoordinatorLayout.class);
        uut.push(child1, new CommandListenerAdapter());
        assertThat(uut.onDependentViewChanged(parent, child1.getView(), Mockito.mock(TopBar.class))).isFalse();
        verify(presenter).applyTopInsets(eq(uut), eq(child1));
    }

    @Test
    public void onDependentViewChanged_TopBarIsRenderedBellowStatusBar() {
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());

        ShadowLooper.idleMainLooper();
        assertThat(topMargin(uut.getTopBar())).isEqualTo(StatusBarUtils.getStatusBarHeight(activity));
    }

    @Test
    public void onDependentViewChanged_TopBarIsRenderedBehindStatusBar() {
        uut.initialOptions.statusBar.visible = new Bool(false);
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());

        ShadowLooper.idleMainLooper();
        assertThat(uut.getTopBar().getY()).isEqualTo(0);
    }

    @Test
    public void getTopInset() {
        disablePushAnimation(child1);
        uut.push(child1, new CommandListenerAdapter());

        assertThat(uut.getTopInset(child1)).isEqualTo(topBarController.getHeight());

        Options options = new Options();
        options.topBar.drawBehind = new Bool(true);
        child1.mergeOptions(options);
        assertThat(uut.getTopInset(child1)).isEqualTo(0);
    }

    @Test
    public void getTopInset_defaultOptionsAreTakenIntoAccount() {
        assertThat(uut.getTopInset(child1)).isEqualTo(topBarController.getHeight());
        Options defaultOptions = new Options();
        defaultOptions.topBar.drawBehind = new Bool(true);
        uut.setDefaultOptions(defaultOptions);

        assertThat(uut.getTopInset(child1)).isZero();
    }

    private void assertContainsOnlyId(String... ids) {
        assertThat(uut.size()).isEqualTo(ids.length);
        assertThat(uut.getChildControllers()).extracting((Extractor<ViewController, String>) ViewController::getId).containsOnly(ids);
    }

    private StackController createStack() {
        return createStackBuilder("stack", new ArrayList<>()).build();
    }

    private StackController createStack(String id) {
        return createStackBuilder(id, new ArrayList<>()).build();
    }

    private StackController createStack(List<ViewController> children) {
        return createStackBuilder("stack", children).build();
    }

    private StackControllerBuilder createStackBuilder(String id, List<ViewController> children) {
        createTopBarController();
        return TestUtils.newStackController(activity)
                .setEventEmitter(eventEmitter)
                .setChildren(children)
                .setId(id)
                .setTopBarController(topBarController)
                .setChildRegistry(childRegistry)
                .setAnimator(animator)
                .setStackPresenter(presenter)
                .setBackButtonHelper(backButtonHelper);
    }

    private void createTopBarController() {
        topBarController = spy(new TopBarController() {
            @Override
            protected TopBar createTopBar(Context context, StackLayout stackLayout) {
                TopBar spy = spy(super.createTopBar(context, stackLayout));
                spy.layout(0, 0, 1000, UiUtils.getTopBarHeight(activity));
                return spy;
            }
        });
    }
}
