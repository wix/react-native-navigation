package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.BottomTabOptionsPresenter;
import com.reactnativenavigation.presentation.BottomTabsOptionsPresenter;
import com.reactnativenavigation.presentation.OptionsPresenter;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.stack.StackController;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.android.controller.ActivityController;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigatorTest extends BaseTest {
    private TestActivity activity;
    private ChildControllersRegistry childRegistry;
    private Navigator uut;
    private StackController parentController;
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

    @Override
    public void beforeEach() {
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);
        overlayManager = Mockito.mock(OverlayManager.class);
        imageLoaderMock = ImageLoaderMock.mock();
        activityController = newActivityController(TestActivity.class);
        activity = activityController.create().get();
        modalStack = spy(new ModalStack(activity));
        modalStack.setEventEmitter(Mockito.mock(EventEmitter.class));
        uut = new Navigator(activity, childRegistry, modalStack, overlayManager);
        activity.setNavigator(uut);

        parentController = newStack();
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

        activityController.visible();
    }

    @Test
    public void setDefaultOptions() {
        uut.setDefaultOptions(new Options());

        SimpleViewController spy = spy(child1);
        uut.setRoot(spy, new CommandListenerAdapter());
        Options defaultOptions = new Options();
        uut.setDefaultOptions(defaultOptions);

        verify(spy).setDefaultOptions(defaultOptions);
        verify(modalStack).setDefaultOptions(defaultOptions);
    }

    @Test
    public void setRoot_setContentViewWhenFirstRootIsSet() {
        assertThat(uut.view).isNull();
        uut.setRoot(child1, new CommandListenerAdapter());
        assertThat(uut.view).isNotNull();
    }

    @Test
    public void setRoot_AddsChildControllerView() {
        uut.setRoot(child1, new CommandListenerAdapter());
        assertIsChild(uut.getContentLayout(), child1.getView());
    }

    @Test
    public void setRoot_ReplacesExistingChildControllerViews() {
        uut.setRoot(child1, new CommandListenerAdapter());
        uut.setRoot(child2, new CommandListenerAdapter());
        assertIsChild(uut.getContentLayout(), child2.getView());
    }

    @Test
    public void hasUniqueId() {
        assertThat(uut.getId()).startsWith("navigator");
        assertThat(new Navigator(activity, childRegistry, modalStack, overlayManager).getId()).isNotEqualTo(uut.getId());
    }

    @Test
    public void push() {
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new CommandListenerAdapter());

        assertIsChild(uut.getView(), stackController.getView());
        assertIsChild(stackController.getView(), child1.getView());

        uut.push(child1.getId(), child2, new CommandListenerAdapter());

        assertIsChild(uut.getView(), stackController.getView());
        assertIsChild(stackController.getView(), child2.getView());
    }

    @Test
    public void push_InvalidPushWithoutAStack_DoesNothing() {
        uut.setRoot(child1, new CommandListenerAdapter());
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
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

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
        uut.pop("123", new CommandListenerAdapter());
        uut.setRoot(child1, new CommandListenerAdapter());
        uut.pop(child1.getId(), new CommandListenerAdapter());
        assertThat(uut.getChildControllers()).hasSize(1);
    }

    @Test
    public void pop_FromCorrectStackByFindingChildId() {
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                stack2.push(child4, new CommandListenerAdapter() {
                            @Override
                            public void onSuccess(String childId) {
                                uut.pop("child4", new CommandListenerAdapter());
                                assertThat(stack2.getChildControllers()).containsOnly(child2, child3);
                            }
                        }
                );
            }
        });
    }

    @Test
    public void popSpecific() {
        disablePushAnimation(child1, child2, child3, child4);
        StackController stack1 = newStack(); stack1.ensureViewIsCreated();
        StackController stack2 = newStack(); stack2.ensureViewIsCreated();
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter());
        stack2.push(child4, new CommandListenerAdapter());
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        uut.popSpecific(child2.getId(), new CommandListenerAdapter());

        assertThat(stack2.getChildControllers()).containsOnly(child4, child3);
    }

    @Test
    public void popTo_FromCorrectStackUpToChild() {
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter());
        stack2.push(child4, new CommandListenerAdapter());
        stack2.push(child5, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popTo(child2.getId(), new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2);
            }
        });
    }

    @Test
    public void popToRoot() {
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter());
        stack2.push(child4, new CommandListenerAdapter());
        stack2.push(child5, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.popToRoot(child3.getId(), new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2);
            }
        });
    }

    @Test
    public void setStackRoot() {
        disablePushAnimation(child1, child2, child3);

        StackController stack = newStack();
        uut.setRoot(stack, new CommandListenerAdapter());

        stack.push(child1, new CommandListenerAdapter());
        stack.push(child2, new CommandListenerAdapter());
        stack.setRoot(child3, new CommandListenerAdapter());

        assertThat(stack.getChildControllers()).containsOnly(child3);
    }

    @Test
    public void handleBack_DelegatesToRoot() {
        assertThat(uut.handleBack(new CommandListenerAdapter())).isFalse();

        ViewController root = spy(child1);
        uut.setRoot(root, new CommandListenerAdapter());
        when(root.handleBack(any(CommandListener.class))).thenReturn(true);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(root, times(1)).handleBack(any());
    }

    @Test
    public void handleBack_modalTakePrecedenceOverRoot() {
        ViewController root = spy(child1);
        uut.setRoot(root, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());
        verify(root, times(0)).handleBack(new CommandListenerAdapter());
    }

    @Test
    public void mergeOptions_CallsApplyNavigationOptions() {
        ComponentViewController componentVc = new SimpleComponentViewController(activity, childRegistry, "theId", new Options());
        componentVc.setParentController(parentController);
        assertThat(componentVc.options.topBar.title.text.get("")).isEmpty();
        uut.setRoot(componentVc, new CommandListenerAdapter());

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
        return new BottomTabsController(activity, tabs, childRegistry, eventEmitter, imageLoaderMock, "tabsController", new Options(), new OptionsPresenter(activity, new Options()), new BottomTabsOptionsPresenter(tabs, new Options()), new BottomTabOptionsPresenter(activity, tabs, new Options()));
    }

    @NonNull
    private StackController newStack() {
        StackController stack = TestUtils.newStackController(activity)
                .setChildRegistry(childRegistry)
                .setId("stack" + CompatUtils.generateViewId())
                .setInitialOptions(tabOptions)
                .build();
        stack.ensureViewIsCreated();
        return stack;
    }

    @Test
    public void push_promise() {
        final StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new CommandListenerAdapter());

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
        uut.setRoot(child1, new CommandListenerAdapter());
        uut.push(child1.getId(), child2, new CommandListenerAdapter() {
            @Override
            public void onError(String message) {
                assertIsChild(uut.getView(), child1.getView());
            }
        });

    }

    @Test
    public void pop_InvalidDoesNothing_Promise() {
        uut.pop("123", new CommandListenerAdapter());
        uut.setRoot(child1, new CommandListenerAdapter());
        uut.pop(child1.getId(), new CommandListenerAdapter() {
            @Override
            public void onError(String reason) {
                assertThat(uut.getChildControllers()).hasSize(1);
            }
        });
    }

    @Test
    public void pop_FromCorrectStackByFindingChildId_Promise() {
        StackController stack1 = newStack();
        final StackController stack2 = newStack();
        BottomTabsController bottomTabsController = newTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter());
        stack2.push(child4, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                uut.pop("child4", new CommandListenerAdapter());
                assertThat(stack2.getChildControllers()).containsOnly(child2, child3);
            }
        });
    }

    @Test
    public void pushIntoModal() {
        uut.setRoot(parentController, new CommandListenerAdapter());
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.showModal(stackController, new CommandListenerAdapter());
        uut.push(stackController.getId(), child2, new CommandListenerAdapter());
        assertIsChild(stackController.getView(), child2.getView());
    }

    @Test
    public void pushedStackCanBePopped() {
        child1.options.animations.push.enable = new Bool(false);
        child2.options.animations.push.enable = new Bool(false);
        StackController spy = spy(parentController);
        StackController parent = newStack();
        parent.ensureViewIsCreated();
        uut.setRoot(parent, new CommandListenerAdapter());
        parent.push(spy, new CommandListenerAdapter());

        spy.push(child1, new CommandListenerAdapter());
        spy.push(child2, new CommandListenerAdapter());
        assertThat(spy.getChildControllers().size()).isEqualTo(2);
        child1.ensureViewIsCreated();
        child2.ensureViewIsCreated();

        CommandListenerAdapter listener = new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(spy.getChildControllers().size()).isEqualTo(1);
            }
        };
        uut.popSpecific("child2", listener);
        verify(spy, times(1)).popSpecific(child2, listener);
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
        });
    }

    @Test
    public void dismissModal_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1, child2, child3);
        disableDismissModalAnimation(child1, child2);

        uut.setRoot(parentController, new CommandListenerAdapter());
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
    public void dismissAllModals_onViewAppearedInvokedOnRoot() {
        disablePushAnimation(child2);
        disableShowModalAnimation(child1);

        uut.dismissAllModals(new CommandListenerAdapter());
        verify(parentVisibilityListener, times(0)).onViewAppeared(parentController.getView());

        uut.setRoot(parentController, new CommandListenerAdapter());
        parentController.push(child2, new CommandListenerAdapter());

        verify(parentVisibilityListener, times(1)).onViewAppeared(parentController.getView());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.dismissAllModals(new CommandListenerAdapter());

        verify(parentVisibilityListener, times(2)).onViewAppeared(parentController.getView());
    }

    @Test
    public void handleBack_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1, child2, child3);

        parentController.push(child3, new CommandListenerAdapter());
        StackController spy = spy(parentController);
        uut.setRoot(spy, new CommandListenerAdapter());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        uut.handleBack(new CommandListenerAdapter());
        verify(parentVisibilityListener, times(1)).onViewAppeared(spy.getView());

        uut.handleBack(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(spy.getView().getParent()).isNotNull();
                verify(spy, times(2)).onViewAppeared();
            }
        });
    }

    @Test
    public void destroy_destroyedRoot() {
        disablePushAnimation(child1);

        StackController spy = spy(parentController);
        spy.options.animations.startApp.enable = new Bool(false);
        uut.setRoot(spy, new CommandListenerAdapter());
        spy.push(child1, new CommandListenerAdapter());
        activityController.destroy();
        verify(spy, times(1)).destroy();
    }

    @Test
    public void destroy_destroyOverlayManager() {
        uut.setRoot(parentController, new CommandListenerAdapter());
        activityController.destroy();
        verify(overlayManager, times(1)).destroy();
    }

    @Test
    public void destroyViews() {
        disableShowModalAnimation(child1);
        uut.setRoot(parentController, new CommandListenerAdapter());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showOverlay(child2, new CommandListenerAdapter());
        uut.destroy();
        assertThat(childRegistry.size()).isZero();
    }
}
