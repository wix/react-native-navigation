package com.reactnativenavigation.viewcontrollers.navigator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabPresenter;
import com.reactnativenavigation.presentation.BottomTabsPresenter;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.presentation.Presenter;
import com.reactnativenavigation.presentation.RootPresenter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsAttacher;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.BottomTabs;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(qualifiers = "xxhdpi")
public class NavigatorTest extends BaseTest {
    private TestActivity activity;
    private ChildControllersRegistry childRegistry;
    private Navigator uut;
    private RootPresenter rootPresenter;
    private StackController parentController;
    private ViewController initialChild;
    private SimpleViewController child1;
    private ViewController child2;
    private ViewController child3;
    private ViewController child4;
    private ViewController child5;
    private Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock;
    private ActivityController<TestActivity> activityController;
    private OverlayManager overlayManager;
    private EventEmitter eventEmitter;
    private ViewController.ViewVisibilityListener parentVisibilityListener;
    private ModalStack modalStack;
    private ReactInstanceManager reactInstanceManager;

    @Override
    public void beforeEach() {
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);
        reactInstanceManager = Mockito.mock(ReactInstanceManager.class);
        overlayManager = spy(new OverlayManager());
        imageLoaderMock = ImageLoaderMock.mock();
        activityController = newActivityController(TestActivity.class);
        activity = activityController.create().get();
        modalStack = spy(new ModalStack(activity));
        rootPresenter = spy(new RootPresenter(activity));
        modalStack.setEventEmitter(Mockito.mock(EventEmitter.class));
        uut = new Navigator(activity, childRegistry, modalStack, overlayManager, rootPresenter);
        activity.setNavigator(uut);

        initialChild = new SimpleViewController(activity, childRegistry, "initialChild", Options.EMPTY);
        parentController = newStack(initialChild);
        parentVisibilityListener = spy(new ViewController.ViewVisibilityListener() {
            @Override
            public boolean onViewAppeared(View view) {
                return false;
            }

            @Override
            public boolean onViewDisappear(View view) {
                return false;
            }
        });
        parentController.setViewVisibilityListener(parentVisibilityListener);
        child1 = new SimpleViewController(activity, childRegistry, "child1", tabOptions);
        child2 = new SimpleViewController(activity, childRegistry, "child2", tabOptions);
        child3 = new SimpleViewController(activity, childRegistry, "child3", tabOptions);
        child4 = new SimpleViewController(activity, childRegistry, "child4", tabOptions);
        child5 = new SimpleViewController(activity, childRegistry, "child5", tabOptions);

        uut.bindViews();

        activityController.visible();
        activityController.postCreate(Bundle.EMPTY);
    }

    @Test
    public void setContentLayout() {
        ViewGroup contentLayout = Mockito.mock(ViewGroup.class);
        uut.setContentLayout(contentLayout);
    }

    @Test
    public void setContentLayout_modalAndOverlayLayoutsAreGONE() {
        ViewGroup contentLayout = Mockito.mock(ViewGroup.class);
        uut.setContentLayout(contentLayout);
        assertThat(uut.getModalsLayout().getVisibility()).isEqualTo(View.GONE);
        assertThat(uut.getOverlaysLayout().getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void bindViews() {
        verify(rootPresenter).setRootContainer(uut.getRootLayout());
        verify(modalStack).setModalsLayout(uut.getModalsLayout());
    }

    @Test
    public void setDefaultOptions() {
        uut.setDefaultOptions(new Options());

        SimpleViewController spy = spy(child1);
        uut.setRoot(spy, new CommandListenerAdapter(), reactInstanceManager);
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);

        verify(spy).setDefaultOptions(defaultOptions);
        verify(modalStack).setDefaultOptions(defaultOptions);
    }

    @Test
    public void setRoot_delegatesToRootPresenter() {
        CommandListenerAdapter listener = new CommandListenerAdapter();
        uut.setRoot(child1, listener, reactInstanceManager);
        ArgumentCaptor<CommandListenerAdapter> captor = ArgumentCaptor.forClass(CommandListenerAdapter.class);
        verify(rootPresenter).setRoot(eq(child1), eq(uut.getDefaultOptions()), captor.capture(), eq(reactInstanceManager));
        assertThat(captor.getValue().getListener()).isEqualTo(listener);
    }

    @Test
    public void setRoot_clearsSplashLayout() {
        FrameLayout content = activity.findViewById(android.R.id.content);
        assertThat(content.getChildCount()).isEqualTo(4); // 3 frame layouts (root, modal and overlay containers) and the default splash layout

        uut.setRoot(child2, new CommandListenerAdapter(), reactInstanceManager);

        assertThat(content.getChildCount()).isEqualTo(3);
    }

    @Test
    public void setRoot_AddsChildControllerView() {
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        assertIsChild(uut.getRootLayout(), child1.getView());
    }

    @Test
    public void setRoot_ReplacesExistingChildControllerViews() {
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.setRoot(child2, new CommandListenerAdapter(), reactInstanceManager);
        assertIsChild(uut.getRootLayout(), child2.getView());
    }

    @Test
    public void setRoot_WithWaitForRender() {
        ViewController initialRoot = spy(child2);
        uut.setRoot(initialRoot, new CommandListenerAdapter(), reactInstanceManager);

        child3.options.animations.setRoot.waitForRender = new Bool(true);
        ViewController secondRoot = spy(child3);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(secondRoot, listener, reactInstanceManager);

        verify(secondRoot).addOnAppearedListener(any());

        secondRoot.getView().addView(new View(activity)); // make isRendered return true and trigger onViewAppeared
        assertThat(initialRoot.isDestroyed()).isTrue();
        assertThat(secondRoot.isViewShown()).isEqualTo(true);
    }

    @Test
    public void setRoot_destroysModals() {
        uut.showModal(child1, new CommandListenerAdapter());
        uut.setRoot(child2, new CommandListenerAdapter(), reactInstanceManager);
        assertTrue(child1.isDestroyed());
    }

    @Test
    public void hasUniqueId() {
        assertThat(uut.getId()).startsWith("navigator");
        assertThat(new Navigator(activity, childRegistry, modalStack, overlayManager, rootPresenter).getId()).isNotEqualTo(uut.getId());
    }

    @Test
    public void push() {
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new CommandListenerAdapter(), reactInstanceManager);

        assertIsChild(uut.getView(), stackController.getView());
        assertIsChild(stackController.getView(), child1.getView());

        uut.push(child1.getId(), child2, new CommandListenerAdapter());

        assertIsChild(uut.getView(), stackController.getView());
        assertIsChild(stackController.getView(), child2.getView());
    }

    @Test
    public void push_InvalidPushWithoutAStack_DoesNothing() {
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.push(child1.getId(), child2, new CommandListenerAdapter());
        assertIsChild(uut.getView(), child1.getView());
    }

    @Test
    public void push_OnCorrectStackByFindingChildId() {
        StackController stack1 = newStack(); stack1.ensureViewIsCreated();
        StackController stack2 = newStack(); stack2.ensureViewIsCreated();
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter(), reactInstanceManager);

        SimpleViewController newChild = new SimpleViewController(activity, childRegistry, "new child", tabOptions);
        uut.push(child2.getId(), newChild, new CommandListenerAdapter());

        assertThat(stack1.getChildControllers()).doesNotContain(newChild);
        assertThat(stack2.getChildControllers()).contains(newChild);
    }

    @Test
    public void push_rejectIfNotContainedInStack() {
        CommandListener listener = Mockito.mock(CommandListener.class);
        uut.push("someId", child1, listener);
        verify(listener).onError(any());
    }

    @Test
    public void pop_InvalidDoesNothing() {
        uut.pop("123", Options.EMPTY, new CommandListenerAdapter());
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.pop(child1.getId(), Options.EMPTY, new CommandListenerAdapter());
        assertThat(uut.getChildControllers()).hasSize(1);
    }

    @Test
    public void pop_FromCorrectStackByFindingChildId() {
        StackController stack1 = newStack(child1);
        StackController stack2 = newStack(child2);
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter(), reactInstanceManager);
        stack2.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                stack2.push(child4, new CommandListenerAdapter() {
                            @Override
                            public void onSuccess(String childId) {
                                uut.pop("child4", Options.EMPTY, new CommandListenerAdapter());
                                assertThat(stack2.getChildControllers()).containsOnly(child2, child3);
                            }
                        }
                );
            }
        });
    }

    @Test
    public void pop_byStackId() {
        disablePushAnimation(child1, child2);
        disablePopAnimation(child2, child1);
        StackController stack = newStack(child1, child2); stack.ensureViewIsCreated();
        uut.setRoot(stack, new CommandListenerAdapter(), reactInstanceManager);

        uut.pop(stack.getId(), Options.EMPTY, new CommandListenerAdapter());
        assertThat(stack.getChildControllers()).containsOnly(child1);
    }

    @Test
    public void popTo_FromCorrectStackUpToChild() {
        disablePushAnimation(child5);

        StackController stack1 = newStack(child1);
        StackController stack2 = newStack(child2, child3, child4);
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter(), reactInstanceManager);

        CommandListenerAdapter listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popTo(child2.getId(), Options.EMPTY, new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2);
            }
        });
        stack2.push(child5, listener);
        verify(listener).onSuccess(child5.getId());
    }

    @Test
    public void popToRoot() {
        StackController stack1 = newStack(child1);
        StackController stack2 = newStack(child2, child3, child4);
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter(), reactInstanceManager);

        stack2.push(child5, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popToRoot(child3.getId(), Options.EMPTY, new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2);
            }
        });
    }

    @Test
    public void setStackRoot() {
        disablePushAnimation(child1, child2, child3);

        StackController stack = newStack(child1, child2);
        uut.setRoot(stack, new CommandListenerAdapter(), reactInstanceManager);

        stack.setRoot(Collections.singletonList(child3), new CommandListenerAdapter());

        assertThat(stack.getChildControllers()).containsOnly(child3);
    }

    @Test
    public void handleBack_DelegatesToRoot() {
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();

        ViewController root = spy(child1);
        uut.setRoot(root, new CommandListenerAdapter(), reactInstanceManager);
        when(root.handleBack(any(CommandListener.class))).thenReturn(true);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(root, times(1)).handleBack(any());
    }

    @Test
    public void handleBack_modalTakePrecedenceOverRoot() {
        ViewController root = spy(child1);
        uut.setRoot(root, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child2, new CommandListenerAdapter());
        verify(root, times(0)).handleBack(new CommandListenerAdapter());
    }

    @Test
    public void mergeOptions_CallsApplyNavigationOptions() {
        ComponentViewController componentVc = new SimpleComponentViewController(activity, childRegistry, "theId", new Options());
        componentVc.setParentController(parentController);
        assertThat(componentVc.options.topBar.title.text.get("")).isEmpty();
        uut.setRoot(componentVc, new CommandListenerAdapter(), reactInstanceManager);

        Options options = new Options();
        options.topBar.title.text = new Text("new title");

        uut.mergeOptions("theId", options);
        assertThat(componentVc.options.topBar.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void mergeOptions_AffectsOnlyComponentViewControllers() {
        uut.mergeOptions("some unknown child id", new Options());
    }

    @NonNull
    private BottomTabsController newTabs(List<ViewController> tabs) {
        BottomTabsPresenter bottomTabsPresenter = new BottomTabsPresenter(tabs, new Options());
        return new BottomTabsController(activity, tabs, childRegistry, eventEmitter, imageLoaderMock, "tabsController", new Options(), new Presenter(activity, new Options()), new BottomTabsAttacher(tabs, bottomTabsPresenter, Options.EMPTY), bottomTabsPresenter, new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new Options())) {
            @NonNull
            @Override
            protected BottomTabs createBottomTabs() {
                return new BottomTabs(activity) {
                    @Override
                    public void superCreateItems() {

                    }
                };
            }
        };
    }

    @Test
    public void findController_root() {
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        assertThat(uut.findController(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void findController_overlay() {
        uut.showOverlay(child1, new CommandListenerAdapter());
        assertThat(uut.findController(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void findController_modal() {
        uut.showModal(child1, new CommandListenerAdapter());
        assertThat(uut.findController(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void push_promise() {
        final StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new CommandListenerAdapter(), reactInstanceManager);

        assertIsChild(uut.getView(), stackController.getView());
        assertIsChild(stackController.getView(), child1.getView());

        uut.push(child1.getId(), child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertIsChild(uut.getView(), stackController.getView());
                assertIsChild(stackController.getView(), child2.getView());
            }
        });
    }

    @Test
    public void push_InvalidPushWithoutAStack_DoesNothing_Promise() {
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.push(child1.getId(), child2, new CommandListenerAdapter() {
            @Override
            public void onError(String message) {
                assertIsChild(uut.getView(), child1.getView());
            }
        });

    }

    @Test
    public void pop_InvalidDoesNothing_Promise() {
        uut.pop("123", Options.EMPTY, new CommandListenerAdapter());
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.pop(child1.getId(), Options.EMPTY, new CommandListenerAdapter() {
            @Override
            public void onError(String reason) {
                assertThat(uut.getChildControllers()).hasSize(1);
            }
        });
    }

    @Test
    public void pop_FromCorrectStackByFindingChildId_Promise() {
        disablePushAnimation(child4);

        StackController stack1 = newStack(child1);
        final StackController stack2 = newStack(child2, child3);
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter(), reactInstanceManager);

        CommandListenerAdapter listener = spy(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.pop("child4", Options.EMPTY, new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2, child3);
            }
        });
        stack2.push(child4, listener);
        verify(listener).onSuccess(child4.getId());
    }

    @Test
    public void pushIntoModal() {
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.showModal(stackController, new CommandListenerAdapter());
        uut.push(stackController.getId(), child2, new CommandListenerAdapter());
        assertIsChild(stackController.getView(), child2.getView());
    }

    @Test
    public void pushedStackCanBePopped() {
        StackController spy = spy(parentController);
        disablePushAnimation(spy, child2);
        spy.push(child2, new CommandListenerAdapter());

        StackController parent = newStack(spy);
        parent.options.animations.setRoot.enabled = new Bool(false);

        uut.setRoot(parent, new CommandListenerAdapter(), reactInstanceManager);

        CommandListenerAdapter listener = new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(spy.getChildControllers().size()).isEqualTo(1);
            }
        };
        disablePopAnimation(child2);
        uut.pop("child2", Options.EMPTY, listener);
        verify(spy, times(1)).pop(Options.EMPTY, listener);
    }

    @Test
    public void showModal_onViewDisappearIsInvokedOnRoot() {
        uut.setRoot(parentController, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.showModal(child1, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        assertThat(parentController.getView().getParent()).isNull();
                        verify(parentController, times(1)).onViewDisappear();
                    }
                });
            }
        }, reactInstanceManager);
    }

    @Test
    public void dismissModal_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1, child2, child3);
        disableDismissModalAnimation(child1, child2);

        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        parentController.push(child3, new CommandListenerAdapter());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        uut.dismissModal(child2.getId(), new CommandListenerAdapter());
        assertThat(parentController.getView().getParent()).isNull();
        verify(parentVisibilityListener, times(1)).onViewAppeared(parentController.getView());

        uut.dismissModal(child1.getId(), new CommandListenerAdapter());
        assertThat(parentController.getView().getParent()).isNotNull();

        verify(parentVisibilityListener, times(2)).onViewAppeared(parentController.getView());
    }

    @Test
    public void dismissModal_reattachedToRoot() {
        disableModalAnimations(child1);

        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        assertThat(ViewUtils.isChildOf(uut.getRootLayout(), parentController.getView())).isTrue();
        uut.showModal(child1, new CommandListenerAdapter());

        uut.dismissModal(child1.getId(), new CommandListenerAdapter());
        assertThat(ViewUtils.isChildOf(uut.getRootLayout(), parentController.getView())).isTrue();
    }

    @Test
    public void dismissModal_rejectIfRootIsNotSetAndSingleModalIsDisplayed() {
        disableModalAnimations(child1, child2);
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        CommandListenerAdapter listener1 = spy(new CommandListenerAdapter());
        uut.dismissModal(child2.getId(), listener1);
        verify(listener1).onSuccess(any());
        assertThat(child2.isDestroyed()).isTrue();

        CommandListenerAdapter listener2 = spy(new CommandListenerAdapter());
        uut.dismissModal(child1.getId(), listener2);
        verify(listener2).onError(any());
        assertThat(child1.isDestroyed()).isFalse();
    }

    @Test
    public void dismissAllModals_onViewAppearedInvokedOnRoot() {
        disablePushAnimation(child2);
        disableShowModalAnimation(child1);
        uut.setRoot(child3, new CommandListenerAdapter(), reactInstanceManager);

        uut.dismissAllModals(Options.EMPTY, new CommandListenerAdapter());
        verify(parentVisibilityListener, times(0)).onViewAppeared(parentController.getView());

        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        parentController.push(child2, new CommandListenerAdapter());

        verify(parentVisibilityListener, times(1)).onViewAppeared(parentController.getView());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.dismissAllModals(Options.EMPTY, new CommandListenerAdapter());

        verify(parentVisibilityListener, times(2)).onViewAppeared(parentController.getView());
    }

    @Test
    public void handleBack_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1, child2, child3);

        parentController.push(child3, new CommandListenerAdapter());
        StackController spy = spy(parentController);
        uut.setRoot(spy, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        uut.handleBack(new CommandListenerAdapter());
        verify(parentVisibilityListener, times(1)).onViewAppeared(spy.getView());

        uut.handleBack(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(spy.getView().getParent()).isNotNull();
                verify(spy, times(2)).onViewWillAppear();
            }
        });
    }

    @Test
    public void handleBack_falseIfRootIsNotSetAndSingleModalIsDisplayed() {
        disableShowModalAnimation(child1, child2, child3);
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();
    }

    @Test
    public void destroy_destroyedRoot() {
        disablePushAnimation(child1);

        StackController spy = spy(parentController);
        spy.options.animations.setRoot.enabled = new Bool(false);
        uut.setRoot(spy, new CommandListenerAdapter(), reactInstanceManager);
        spy.push(child1, new CommandListenerAdapter());
        activityController.destroy();
        verify(spy, times(1)).destroy();
    }

    @Test
    public void destroy_destroyOverlayManager() {
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        activityController.destroy();
        verify(overlayManager).destroy(uut.getOverlaysLayout());
    }

    @Test
    public void destroyViews() {
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showOverlay(child2, new CommandListenerAdapter());
        uut.destroy();
        assertThat(childRegistry.size()).isZero();
    }

    @NonNull
    private StackController newStack(ViewController... children) {
        StackController stack = TestUtils.newStackController(activity)
                .setChildren(children)
                .setChildRegistry(childRegistry)
                .setId("stack" + CompatUtils.generateViewId())
                .setInitialOptions(tabOptions)
                .build();
        stack.ensureViewIsCreated();
        return stack;
    }
}
