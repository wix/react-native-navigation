package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;

import org.junit.Test;

import java.util.Arrays;

import javax.annotation.Nullable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigatorTest extends BaseTest {
    private Activity activity;
    private Navigator uut;
    private StackController parentController;
    private SimpleViewController child1;
    private ViewController child2;
    private ViewController child3;
    private ViewController child4;
    private ViewController child5;
    private Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock;

    @Override
    public void beforeEach() {
        super.beforeEach();
        imageLoaderMock = ImageLoaderMock.mock();
        activity = newActivity();
        uut = new Navigator(activity);
        parentController = spy(newStack());
        parentController.ensureViewIsCreated();
        child1 = new SimpleViewController(activity, "child1", tabOptions);
        child2 = new SimpleViewController(activity, "child2", tabOptions);
        child3 = new SimpleViewController(activity, "child3", tabOptions);
        child4 = new SimpleViewController(activity, "child4", tabOptions);
        child5 = new SimpleViewController(activity, "child5", tabOptions);
    }

    @Test
    public void setRoot_AddsChildControllerView() {
        assertThat(uut.getView().getChildCount()).isZero();
        uut.setRoot(child1, new MockPromise());
        assertIsChildById(uut.getView(), child1.getView());
    }

    @Test
    public void setRoot_ReplacesExistingChildControllerViews() {
        uut.setRoot(child1, new MockPromise());
        uut.setRoot(child2, new MockPromise());
        assertIsChildById(uut.getView(), child2.getView());
    }

    @Test
    public void hasUniqueId() {
        assertThat(uut.getId()).startsWith("navigator");
        assertThat(new Navigator(activity).getId()).isNotEqualTo(uut.getId());
    }

    @Test
    public void push() {
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new MockPromise());

        assertIsChildById(uut.getView(), stackController.getView());
        assertIsChildById(stackController.getView(), child1.getView());

        uut.push(child1.getId(), child2, new CommandListenerAdapter());

        assertIsChildById(uut.getView(), stackController.getView());
        assertIsChildById(stackController.getView(), child2.getView());
    }

    @Test
    public void push_InvalidPushWithoutAStack_DoesNothing() {
        uut.setRoot(child1, new MockPromise());
        uut.push(child1.getId(), child2, new CommandListenerAdapter());
        assertIsChildById(uut.getView(), child1.getView());
    }

    @Test
    public void push_OnCorrectStackByFindingChildId() {
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        stack1.push(child1, new CommandListenerAdapter());
        stack2.push(child2, new CommandListenerAdapter());
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new MockPromise());

        SimpleViewController newChild = new SimpleViewController(activity, "new child", tabOptions);
        uut.push(child2.getId(), newChild, new CommandListenerAdapter());

        assertThat(stack1.getChildControllers()).doesNotContain(newChild);
        assertThat(stack2.getChildControllers()).contains(newChild);
    }

    @Test
    public void pop_InvalidDoesNothing() {
        uut.pop("123", new CommandListenerAdapter());
        uut.setRoot(child1, new MockPromise());
        uut.pop(child1.getId(), new CommandListenerAdapter());
        assertThat(uut.getChildControllers()).hasSize(1);
    }

    @Test
    public void pop_FromCorrectStackByFindingChildId() {
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new MockPromise());
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
        uut.setRoot(bottomTabsController, new MockPromise());

        uut.popSpecific(child2.getId(), new CommandListenerAdapter());

        assertThat(stack2.getChildControllers()).containsOnly(child4, child3);
    }

    @Test
    public void popTo_FromCorrectStackUpToChild() {
        BottomTabsController bottomTabsController = newTabs();
        StackController stack1 = newStack();
        StackController stack2 = newStack();
        bottomTabsController.setTabs(Arrays.asList(stack1, stack2));
        uut.setRoot(bottomTabsController, new MockPromise());

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
        uut.setRoot(bottomTabsController, new MockPromise());

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
        uut.setRoot(stack, new MockPromise());

        stack.push(child1, new CommandListenerAdapter());
        stack.push(child2, new CommandListenerAdapter());
        stack.setRoot(child3, new CommandListenerAdapter());

        assertThat(stack.getChildControllers()).containsOnly(child3);
    }

    @Test
    public void handleBack_DelegatesToRoot() {
        ViewController root = spy(child1);
        uut.setRoot(root, new MockPromise());
        when(root.handleBack()).thenReturn(true);
        assertThat(uut.handleBack()).isTrue();
        verify(root, times(1)).handleBack();
    }

    @Test
    public void handleBack_modalTakePrecedenceOverRoot() {
        ViewController root = spy(child1);
        uut.setRoot(root, new MockPromise());
        uut.showModal(child2, new MockPromise());
        verify(root, times(0)).handleBack();
    }

    @Test
    public void setOptions_CallsApplyNavigationOptions() {
        ComponentViewController componentVc = new SimpleComponentViewController(activity, "theId", new Options());
        componentVc.setParentController(parentController);
        assertThat(componentVc.options.topBar.title.text.get("")).isEmpty();
        uut.setRoot(componentVc, new MockPromise());

        Options options = new Options();
        options.topBar.title.text = new Text("new title");

        uut.setOptions("theId", options);
        assertThat(componentVc.options.topBar.title.text.get()).isEqualTo("new title");
    }

    @Test
    public void setOptions_AffectsOnlyComponentViewControllers() {
        uut.setOptions("some unknown child id", new Options());
    }

    @NonNull
    private BottomTabsController newTabs() {
        return new BottomTabsController(activity, imageLoaderMock, "tabsController", new Options());
    }

    @NonNull
    private StackController newStack() {
        return new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()), new TopBarController(),
                "stack" + CompatUtils.generateViewId(), tabOptions);
    }

    @Test
    public void push_Promise() {
        final StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.setRoot(stackController, new MockPromise());

        assertIsChildById(uut.getView(), stackController.getView());
        assertIsChildById(stackController.getView(), child1.getView());

        uut.push(child1.getId(), child2, new CommandListenerAdapter() {
            @Override
            public void onSuccess(String childId) {
                assertIsChildById(uut.getView(), stackController.getView());
                assertIsChildById(stackController.getView(), child2.getView());
            }
        });
    }

    @Test
    public void push_InvalidPushWithoutAStack_DoesNothing_Promise() {
        uut.setRoot(child1, new MockPromise());
        uut.push(child1.getId(), child2, new CommandListenerAdapter() {
            @Override
            public void onError(String message) {
                assertIsChildById(uut.getView(), child1.getView());
            }
        });

    }

    @Test
    public void pop_InvalidDoesNothing_Promise() {
        uut.pop("123", new CommandListenerAdapter());
        uut.setRoot(child1, new MockPromise());
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
        uut.setRoot(bottomTabsController, new MockPromise());

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
        uut.setRoot(parentController, new MockPromise());
        StackController stackController = newStack();
        stackController.push(child1, new CommandListenerAdapter());
        uut.showModal(stackController, new MockPromise());
        uut.push(stackController.getId(), child2, new CommandListenerAdapter());
        assertIsChildById(stackController.getView(), child2.getView());
    }

    @Test
    public void pushedStackCanBePopped() {
        child1.options.animations.push.enable = new Bool(false);
        child2.options.animations.push.enable = new Bool(false);
        StackController parent = newStack();
        parent.ensureViewIsCreated();
        uut.setRoot(parent, new MockPromise());
        parent.push(parentController, new CommandListenerAdapter());

        parentController.push(child1, new CommandListenerAdapter());
        parentController.push(child2, new CommandListenerAdapter());
        assertThat(parentController.getChildControllers().size()).isEqualTo(2);
        child1.ensureViewIsCreated();
        child2.ensureViewIsCreated();

        Navigator.CommandListener listener = new CommandListenerAdapter() {
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
        uut.setRoot(parentController, new MockPromise());
        uut.showModal(child1, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(parentController, times(1)).onViewLostFocus();
            }
        });
    }

    @Test
    public void dismissModal_onViewAppearedInvokedOnRoot() {
        uut.setRoot(parentController, new MockPromise());
        uut.showModal(child1, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                uut.dismissModal("child1", new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        verify(parentController, times(1)).onViewRegainedFocus();
                    }
                });
            }
        });
    }
}
