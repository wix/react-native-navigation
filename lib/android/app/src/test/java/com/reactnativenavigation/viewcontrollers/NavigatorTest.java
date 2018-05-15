package com.reactnativenavigation.viewcontrollers;

import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.presentation.OverlayManager;
import com.reactnativenavigation.react.EventEmitter;
import com.reactnativenavigation.utils.CommandListener;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.android.controller.ActivityController;

import java.util.Arrays;

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

    @Override
    public void beforeEach() {
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);
        overlayManager = Mockito.mock(OverlayManager.class);
        imageLoaderMock = ImageLoaderMock.mock();
        activityController = newActivityController(TestActivity.class);
        activity = activityController.create().get();
        uut = new Navigator(activity, childRegistry, overlayManager);
        activity.setNavigator(uut);

        parentController = spy(newStack());
        parentController.ensureViewIsCreated();
        child1 = new SimpleViewController(activity, childRegistry, "child1", tabOptions);
        child2 = new SimpleViewController(activity, childRegistry, "child2", tabOptions);
        child3 = new SimpleViewController(activity, childRegistry, "child3", tabOptions);
        child4 = new SimpleViewController(activity, childRegistry, "child4", tabOptions);
        child5 = new SimpleViewController(activity, childRegistry, "child5", tabOptions);
        activity.setContentView(uut.getView());

        activityController.visible();
    }

    @Test
    public void setRoot_AddsChildControllerView() {
        assertThat(uut.getContentLayout().getChildCount()).isZero();
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
        assertThat(new Navigator(activity, childRegistry, overlayManager).getId()).isNotEqualTo(uut.getId());
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
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        SimpleViewController newChild = new SimpleViewController(activity, childRegistry, "new child", tabOptions);
        uut.push(child2.getId(), newChild, new CommandListenerAdapter());

        assertThat(stack1.getChildControllers()).doesNotContain(newChild);
        assertThat(stack2.getChildControllers()).contains(newChild);
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
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
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
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        stack2.push(child3, new CommandListenerAdapter());
        stack2.push(child4, new CommandListenerAdapter());
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new CommandListenerAdapter());

        uut.popSpecific(child2.getId(), new CommandListenerAdapter());

        assertThat(stack2.getChildControllers()).containsOnly(child4, child3);
    }

    @Test
    public void popTo_FromCorrectStackUpToChild() {
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
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
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
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
    private BottomTabsController newTabs() {
        return new BottomTabsController(activity, childRegistry, eventEmitter, imageLoaderMock, "tabsController", new Options());
    }

    @NonNull
    private StackController newStack() {
        return new StackControllerBuilder(activity)
                .setChildRegistry(childRegistry)
                .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                .setTitleBarReactViewCreator(new TitleBarReactViewCreatorMock())
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                .setTopBarController(new TopBarController())
                .setId("stack" + CompatUtils.generateViewId())
                .setInitialOptions(tabOptions)
                .createStackController();
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
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        final StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
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
        StackController parent = newStack();
        parent.ensureViewIsCreated();
        uut.setRoot(parent, new CommandListenerAdapter());
        parent.push(parentController, new CommandListenerAdapter());

        parentController.push(child1, new CommandListenerAdapter());
        parentController.push(child2, new CommandListenerAdapter());
        assertThat(parentController.getChildControllers().size()).isEqualTo(2);
        child1.ensureViewIsCreated();
        child2.ensureViewIsCreated();

        CommandListenerAdapter listener = new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(parentController.getChildControllers().size()).isEqualTo(1);
            }
        };
        uut.popSpecific("child2", listener);
        verify(parentController, times(1)).popSpecific(child2, listener);
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
        disableShowModalAnimation(child1, child2);
        disableDismissModalAnimation(child1, child2);

        uut.setRoot(parentController, new CommandListenerAdapter());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        uut.dismissModal(child2.getId(), new CommandListenerAdapter());
        assertThat(parentController.getView().getParent()).isNull();
        verify(parentController, times(1)).onViewAppeared();

        uut.dismissModal(child1.getId(), new CommandListenerAdapter());
        assertThat(parentController.getView().getParent()).isNotNull();

        verify(parentController, times(2)).onViewAppeared();
    }

    @Test
    public void dismissAllModals_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1);

        uut.dismissAllModals(new CommandListenerAdapter());
        verify(parentController, times(0)).onViewAppeared();

        uut.setRoot(parentController, new CommandListenerAdapter());
        verify(parentController, times(1)).onViewAppeared();
        uut.showModal(child1, new CommandListenerAdapter());
        uut.dismissAllModals(new CommandListenerAdapter());

        verify(parentController, times(2)).onViewAppeared();
    }

    @Test
    public void handleBack_onViewAppearedInvokedOnRoot() {
        disableShowModalAnimation(child1, child2);

        uut.setRoot(parentController, new CommandListenerAdapter());
        uut.showModal(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());

        uut.handleBack(new CommandListenerAdapter());
        verify(parentController, times(1)).onViewAppeared();

        uut.handleBack(new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertThat(parentController.getView().getParent()).isNotNull();
                verify(parentController, times(2)).onViewAppeared();
            }
        });
    }

    @Test
    public void destroy_destroyedRoot() {
        parentController.options.animations.startApp.enable = new Bool(false);
        uut.setRoot(parentController, new CommandListenerAdapter());
        activityController.destroy();
        verify(parentController, times(1)).destroy();
    }

    @Test
    public void destroy_destroyOverlayManager() {
        uut.setRoot(parentController, new CommandListenerAdapter());
        activityController.destroy();
        verify(overlayManager, times(1)).destroy();
    }

    @Test
    public void reload_navigatorIsDestroyedOnReload() {
        uut.setRoot(parentController, new CommandListenerAdapter());
        uut.onReload();
        verify(parentController, times(1)).destroy();
        verify(overlayManager, times(1)).destroy();
    }
}
