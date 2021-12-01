package com.reactnativenavigation.viewcontrollers.navigator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestActivity;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.SimpleComponentViewController;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TypefaceLoaderMock;
import com.reactnativenavigation.options.ModalOptions;
import com.reactnativenavigation.options.ModalPresentationStyle;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.CommandListener;
import com.reactnativenavigation.react.CommandListenerAdapter;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.utils.CompatUtils;
import com.reactnativenavigation.utils.ImageLoader;
import com.reactnativenavigation.utils.OptionHelper;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsAnimator;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsController;
import com.reactnativenavigation.viewcontrollers.bottomtabs.BottomTabsPresenter;
import com.reactnativenavigation.viewcontrollers.bottomtabs.attacher.BottomTabsAttacher;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.component.ComponentViewController;
import com.reactnativenavigation.viewcontrollers.modal.ModalStack;
import com.reactnativenavigation.viewcontrollers.overlay.OverlayManager;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.RootPresenter;
import com.reactnativenavigation.viewcontrollers.viewcontroller.ViewController;
import com.reactnativenavigation.views.bottomtabs.BottomTabs;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
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
    private SimpleViewController child1;
    private ViewController<?> child2;
    private ViewController<?> child3;
    private ViewController<?> child4;
    private ViewController<?> child5;
    private final Options tabOptions = OptionHelper.createBottomTabOptions();
    private ImageLoader imageLoaderMock;
    private ActivityController<TestActivity> activityController;
    private OverlayManager overlayManager;
    private EventEmitter eventEmitter;
    private ViewController.ViewVisibilityListener parentVisibilityListener;
    private ModalStack modalStack;
    private ReactInstanceManager reactInstanceManager;

    @Override
    public void beforeEach() {
        super.beforeEach();
        childRegistry = new ChildControllersRegistry();
        eventEmitter = Mockito.mock(EventEmitter.class);
        reactInstanceManager = Mockito.mock(ReactInstanceManager.class);
        overlayManager = spy(new OverlayManager());
        imageLoaderMock = ImageLoaderMock.mock();
        activityController = newActivityController(TestActivity.class);
        activity = activityController.create().get();
        modalStack = spy(new ModalStack(activity));
        rootPresenter = spy(new RootPresenter());
        modalStack.setEventEmitter(Mockito.mock(EventEmitter.class));
        uut = new Navigator(activity, childRegistry, modalStack, overlayManager, rootPresenter);
        activity.setNavigator(uut);

        ViewController<?> initialChild = new SimpleViewController(activity, childRegistry, "initialChild", Options.EMPTY);
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
        idleMainLooper();
    }

    @Test
    public void onConfigurationChange_shouldCallOnConfigurationChangedForModals() {
        Navigator spyUUT = spy(uut);
        SimpleViewController spyChild1 = spy(child1);
        ViewController<?> spyChild2 = spy(child2);
        ViewController<?> spyChild3 = spy(child3);

        spyUUT.setRoot(spyChild1, new CommandListenerAdapter(), reactInstanceManager);
        spyUUT.showModal(spyChild2, new CommandListenerAdapter());
        spyUUT.showModal(spyChild3, new CommandListenerAdapter());
        spyUUT.onConfigurationChanged(mockConfiguration);

        verify(spyChild2).onConfigurationChanged(any());
        verify(spyChild3).onConfigurationChanged(any());
    }

    @Test
    public void onConfigurationChange_shouldCallOnConfigurationChangedForOverlays() {
        Navigator spyUUT = spy(uut);
        SimpleViewController spyChild1 = spy(child1);
        ViewController<?> spyChild2 = spy(child2);
        ViewController<?> spyChild3 = spy(child3);

        spyUUT.setRoot(spyChild1, new CommandListenerAdapter(), reactInstanceManager);
        spyUUT.showOverlay(spyChild2, new CommandListenerAdapter());
        spyUUT.showOverlay(spyChild3, new CommandListenerAdapter());
        spyUUT.onConfigurationChanged(mockConfiguration);

        verify(spyChild2).onConfigurationChanged(any());
        verify(spyChild3).onConfigurationChanged(any());
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
    public void shouldCallOnViewDidAppearWhenHostResumes() {
        SimpleViewController child1 = spy(this.child1);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.onHostResume();
        verify(child1, times(2)).onViewDidAppear();
    }

    @Test
    public void shouldCallOverlaysOnHostResumeWhenHostResumes() {
        SimpleViewController child1 = spy(this.child1);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.onHostResume();
        verify(overlayManager).onHostResume();
    }

    @Test
    public void shouldCallOverlaysChildrenOnViewDidAppearOnHostResume() {
        SimpleViewController child1 = spy(this.child1);
        ViewController<?> child2 = spy(this.child2);
        ViewController<?> child3 = spy(this.child3);

        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showOverlay(child2, new CommandListenerAdapter());
        uut.showOverlay(child3, new CommandListenerAdapter());
        verify(child1, times(1)).onViewDidAppear();

        uut.onHostResume();

        verify(overlayManager).onHostResume();
        verify(child1, times(2)).onViewDidAppear();
        verify(child2).onViewDidAppear();
        verify(child3).onViewDidAppear();
    }

    @Test
    public void shouldCallRootOnViewDidAppearWhenModalDisplayedOverContext(){
        SimpleViewController child1 = spy(this.child1);
        final Options overContextOptions = tabOptions.copy();
        overContextOptions.modal =new ModalOptions();
        overContextOptions.modal.presentationStyle = ModalPresentationStyle.OverCurrentContext;
        ViewController<?> overContextModal = spy(new SimpleViewController(activity, childRegistry, "overContextModal",
                overContextOptions));
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(overContextModal, new CommandListenerAdapter());
        uut.onHostResume();

        verify(child1, times(2)).onViewDidAppear();
        verify(overContextModal, times(1)).onViewDidAppear();
    }

    @Test
    public void shouldCallRootOnViewDisappearWhenModalDisplayedOverContext(){
        SimpleViewController child1 = spy(this.child1);
        final Options overContextOptions = tabOptions.copy();
        overContextOptions.modal =new ModalOptions();
        overContextOptions.modal.presentationStyle = ModalPresentationStyle.OverCurrentContext;
        ViewController<?> overContextModal = spy(new SimpleViewController(activity, childRegistry, "overContextModal",
                overContextOptions));
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(overContextModal, new CommandListenerAdapter());
        uut.onHostPause();

        verify(child1, times(1)).onViewDisappear();
        verify(overContextModal, times(1)).onViewDisappear();
    }

    @Test
    public void shouldCallModalOnViewDisappearWhenModalDisplayedOverContextUnderneath(){
        SimpleViewController child1 = spy(this.child1);
        ViewController<?> child2 = spy(this.child2);
        final Options overContextOptions = tabOptions.copy();
        overContextOptions.modal =new ModalOptions();
        overContextOptions.modal.presentationStyle = ModalPresentationStyle.OverCurrentContext;
        ViewController<?> overContextModal = spy(new SimpleViewController(activity, childRegistry, "overContextModal",
                overContextOptions));
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(overContextModal, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());
        uut.onHostPause();

        verify(child2, times(1)).onViewDisappear();
        verify(overContextModal, never()).onViewDisappear();
    }
    @Test
    public void shouldCallOverlaysAndModalsChildrenOnViewDidAppearOnHostResume() {
        SimpleViewController child1 = spy(this.child1);
        ViewController<?> child2 = spy(this.child2);
        ViewController<?> child3 = spy(this.child3);
        ViewController<?> child4 = spy(this.child4);

        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child2, new CommandListenerAdapter());
        uut.showOverlay(child3, new CommandListenerAdapter());
        uut.showOverlay(child4, new CommandListenerAdapter());
        verify(child1, times(1)).onViewDidAppear();

        uut.onHostResume();

        verify(overlayManager).onHostResume();
        verify(child1, times(1)).onViewDidAppear();
        verify(child2).onViewDidAppear();
        verify(child3).onViewDidAppear();
        verify(child4).onViewDidAppear();
    }

    @Test
    public void shouldNotCallModalOnHostResumeWhenHostResumesAndNoModals() {
        SimpleViewController child1 = spy(this.child1);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.onHostResume();
        verify(modalStack, never()).onHostResume();
    }

    @Test
    public void shouldCallModalPeekDidAppearWhenHostResumes() {
        SimpleViewController child1 = spy(this.child1);
        ViewController<?> child2 = spy(this.child2);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child2, new CommandListenerAdapter());
        uut.onHostResume();

        verify(modalStack).onHostResume();
        verify(child2).onViewDidAppear();
        verify(child1, times(1)).onViewDidAppear();
    }

    @Test
    public void shouldCallOnViewDisappearWhenHostPauses() {
        SimpleViewController child1 = spy(this.child1);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.onHostPause();
        verify(child1).onViewDidAppear();
    }

    @Test
    public void shouldNotCallModalOnHostPauseWhenHostPausesAndNoModals() {
        SimpleViewController child1 = spy(this.child1);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.onHostPause();
        verify(modalStack, never()).onHostPause();
    }

    @Test
    public void shouldCallModalPeekDidDisappearWhenHostPauses() {
        SimpleViewController child1 = spy(this.child1);
        ViewController<?> child2 = spy(this.child2);
        uut.setRoot(child1, new CommandListenerAdapter(), reactInstanceManager);
        uut.showModal(child2, new CommandListenerAdapter());
        uut.onHostPause();

        verify(modalStack).onHostPause();
        verify(child2).onViewDisappear();
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
        verify(rootPresenter).setRoot(eq(child1), eq(null), eq(uut.getDefaultOptions()), captor.capture(), eq(reactInstanceManager));
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
    public void setRoot_withWaitForRender() {
        ViewController<?> initialRoot = spy(child2);
        uut.setRoot(initialRoot, new CommandListenerAdapter(), reactInstanceManager);

        child3.options.animations.setRoot.getEnter().waitForRender = new Bool(true);
        ViewController<?> secondRoot = spy(child3);
        CommandListenerAdapter listener = spy(new CommandListenerAdapter());
        uut.setRoot(secondRoot, listener, reactInstanceManager);

        verify(secondRoot).addOnAppearedListener(any());

        secondRoot.getView().addView(new View(activity)); // make isRendered return true and trigger onViewAppeared
        idleMainLooper();
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
        StackController stack1 = newStack();
        stack1.ensureViewIsCreated();
        StackController stack2 = newStack();
        stack2.ensureViewIsCreated();
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
        StackController stack = newStack(child1, child2);
        stack.ensureViewIsCreated();
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
        idleMainLooper();
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

        ViewController<?> root = spy(child1);
        uut.setRoot(root, new CommandListenerAdapter(), reactInstanceManager);
        when(root.handleBack(any(CommandListener.class))).thenReturn(true);
        assertThat(uut.handleBack(new CommandListenerAdapter())).isTrue();
        verify(root, times(1)).handleBack(any());
    }

    @Test
    public void handleBack_modalTakePrecedenceOverRoot() {
        ViewController<?> root = spy(child1);
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
    private BottomTabsController newTabs(List<ViewController<?>> tabs) {
        BottomTabsPresenter bottomTabsPresenter = new BottomTabsPresenter(tabs, new Options(), new BottomTabsAnimator());
        return new BottomTabsController(activity, tabs, childRegistry, eventEmitter, imageLoaderMock, "tabsController", new Options(), new Presenter(activity, new Options()), new BottomTabsAttacher(tabs, bottomTabsPresenter, Options.EMPTY), bottomTabsPresenter, new BottomTabPresenter(activity, tabs, ImageLoaderMock.mock(), new TypefaceLoaderMock(), new Options())) {
            @NonNull
            @Override
            protected BottomTabs createBottomTabs() {
                return new BottomTabs(activity) {
                    @Override
                    protected void createItems() {

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
        idleMainLooper();
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
        parent.options.animations.setRoot.getEnter().enabled = new Bool(false);

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
                idleMainLooper();
                verify(parentVisibilityListener).onViewAppeared(parentController.getView());

                uut.showModal(child1, new CommandListenerAdapter() {
                    @Override
                    public void onSuccess(String childId) {
                        assertThat(parentController.getView().getParent()).isNull();
                        idleMainLooper();
                        verify(parentVisibilityListener).onViewDisappear(parentController.getView());
                    }
                });
            }
        }, reactInstanceManager);
    }

    @Test
    public void dismissModal_onViewAppearedInvokedOnRoot() {
        disableModalAnimations(child1);
        disablePushAnimation(child2);

        parentController.push(child2, new CommandListenerAdapter());
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        idleMainLooper();
        verify(parentVisibilityListener).onViewAppeared(parentController.getView());

        uut.showModal(child1, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(parentController.getView().getParent()).isNull();
        verify(parentVisibilityListener).onViewDisappear(parentController.getView());

        uut.dismissModal(child1.getId(), new CommandListenerAdapter());
        idleMainLooper();
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
        disableModalAnimations(child1);
        disablePushAnimation(child2);

        parentController.push(child2, new CommandListenerAdapter());
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        idleMainLooper();
        verify(parentVisibilityListener).onViewAppeared(parentController.getView());

        uut.showModal(child1, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(parentController.getView().getParent()).isNull();
        verify(parentVisibilityListener).onViewDisappear(parentController.getView());

        uut.dismissAllModals(Options.EMPTY, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(parentController.getView().getParent()).isNotNull();
        verify(parentVisibilityListener, times(2)).onViewAppeared(parentController.getView());
    }

    @Test
    public void handleBack_onViewAppearedInvokedOnRoot() {
        disableModalAnimations(child1);
        disablePushAnimation(child2);

        parentController.push(child2, new CommandListenerAdapter());
        uut.setRoot(parentController, new CommandListenerAdapter(), reactInstanceManager);
        idleMainLooper();
        verify(parentVisibilityListener).onViewAppeared(parentController.getView());

        uut.showModal(child1, new CommandListenerAdapter());
        idleMainLooper();
        assertThat(parentController.getView().getParent()).isNull();
        verify(parentVisibilityListener).onViewDisappear(parentController.getView());

        uut.handleBack(new CommandListenerAdapter());
        idleMainLooper();
        assertThat(parentController.getView().getParent()).isNotNull();
        verify(parentVisibilityListener, times(2)).onViewAppeared(parentController.getView());
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
    public void destroy_shouldNotChangeViewIds() {
        disablePushAnimation(child1);
        disableShowModalAnimation(child1, child2, child3);

        StackController spy = spy(parentController);
        SimpleViewController.SimpleView view = child1.getView();
        ViewGroup view1 = child2.getView();
        view.setId(10);
        view1.setId(11);
        spy.options.animations.setRoot.getEnter().enabled = new Bool(false);
        uut.setRoot(spy, new CommandListenerAdapter(), reactInstanceManager);
        spy.push(child1, new CommandListenerAdapter());
        uut.showModal(child2, new CommandListenerAdapter());
        activityController.destroy();
        assertThat(view.getId()).isEqualTo(10);
        assertThat(view1.getId()).isEqualTo(11);
        verify(spy, times(1)).destroy();
    }

    @Test
    public void destroy_destroyedRoot() {
        disablePushAnimation(child1);

        StackController spy = spy(parentController);
        spy.options.animations.setRoot.getEnter().enabled = new Bool(false);
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
    private StackController newStack(ViewController<?>... children) {
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
