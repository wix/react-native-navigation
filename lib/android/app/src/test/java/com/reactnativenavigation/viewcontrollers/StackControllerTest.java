package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.NavigationAnimator;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.NestedAnimationsOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ViewHelper;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.Component;
import com.reactnativenavigation.views.ReactComponent;
import com.reactnativenavigation.views.StackLayout;

import org.assertj.core.api.iterable.Extractor;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.annotation.Nullable;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StackControllerTest extends BaseTest {

    private Activity activity;
    private StackController uut;
    private ViewController child1;
    private ViewController child2;
    private ViewController child3;
    private NavigationAnimator animator;
    private TopBarController topBarController;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        uut = createStackController();
        child1 = spy(new SimpleViewController(activity, "child1", new Options()));
        child2 = spy(new SimpleViewController(activity, "child2", new Options()));
        child3 = spy(new SimpleViewController(activity, "child3", new Options()));
    }

    @Test
    public void isAViewController() {
        assertThat(uut).isInstanceOf(ViewController.class);
    }

    @Test
    public void holdsAStackOfViewControllers() {
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise());
        assertThat(uut.peek()).isEqualTo(child3);
        assertContainsOnlyId(child1.getId(), child2.getId(), child3.getId());
    }

    @Test
    public void push() {
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void pop() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertContainsOnlyId(child2.getId(), child1.getId());
                uut.pop(new MockPromise());
                assertContainsOnlyId(child1.getId());
            }
        });
    }

    @Test
    public void pop_appliesOptionsAfterPop() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                uut.pop(new MockPromise());
                verify(uut, times(1)).applyChildOptions(uut.options, eq((ReactComponent) child1.getView()));
            }
        });
    }

    @Test
    public void pop_layoutHandlesChildWillDisappear() {
        final StackLayout[] stackLayout = new StackLayout[1];
        uut = new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()), new TopBarController(), "uut", new Options()) {
            @NonNull
            @Override
            protected StackLayout createView() {
                stackLayout[0] = spy(super.createView());
                return stackLayout[0];
            }
        };
        uut.push(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                uut.animatePop(new MockPromise() {
                    @Override
                    public void resolve(@Nullable Object value) {
                        verify(stackLayout[0], times(1)).onChildWillDisappear(child2.options, child1.options, () -> {
                        });
                    }
                });
            }
        });
    }

    @Test
    public void stackOperations() {
        assertThat(uut.peek()).isNull();
        assertThat(uut.size()).isZero();
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.peek()).isEqualTo(child1);
        assertThat(uut.size()).isEqualTo(1);
        assertThat(uut.isEmpty()).isFalse();
    }

    @Test
    public void pushAssignsRefToSelfOnPushedController() {
        assertThat(child1.getParentController()).isNull();
        uut.animatePush(child1, new MockPromise());
        assertThat(child1.getParentController()).isEqualTo(uut);

        StackController anotherNavController = createStackController("another");
        anotherNavController.animatePush(child2, new MockPromise());
        assertThat(child2.getParentController()).isEqualTo(anotherNavController);
    }

    @Test
    public void handleBack_PopsUnlessSingleChild() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.handleBack()).isFalse();

        uut.animatePush(child1, new MockPromise());
        assertThat(uut.size()).isEqualTo(1);
        assertThat(uut.handleBack()).isFalse();

        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertThat(uut.size()).isEqualTo(2);
                assertThat(uut.handleBack()).isTrue();

                assertThat(uut.size()).isEqualTo(1);
                assertThat(uut.handleBack()).isFalse();
            }
        });
    }

    @Test
    public void popDoesNothingWhenZeroOrOneChild() {
        assertThat(uut.isEmpty()).isTrue();
        uut.pop(new MockPromise());
        assertThat(uut.isEmpty()).isTrue();

        uut.animatePush(child1, new MockPromise());
        uut.pop(new MockPromise());
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void canPopWhenSizeIsMoreThanOne() {
        assertThat(uut.isEmpty()).isTrue();
        assertThat(uut.canPop()).isFalse();
        uut.animatePush(child1, new MockPromise());
        assertContainsOnlyId(child1.getId());
        assertThat(uut.canPop()).isFalse();
        uut.animatePush(child2, new MockPromise());
        assertContainsOnlyId(child1.getId(), child2.getId());
        assertThat(uut.canPop()).isTrue();
    }

    @Test
    public void pushAddsToViewTree() {
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNotNull();
    }

    @Test
    public void pushRemovesPreviousFromTree() {
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNotNull();
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
                assertThat(uut.getView().findViewById(child2.getView().getId())).isNotNull();
            }
        });
    }

    @Test
    public void popReplacesViewWithPrevious() {
        final View child2View = child2.getView();
        final View child1View = child1.getView();

        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertIsChildById(uut.getView(), child2View);
                assertNotChildOf(uut.getView(), child1View);
                uut.pop(new MockPromise());
                assertNotChildOf(uut.getView(), child2View);
                assertIsChildById(uut.getView(), child1View);
            }
        });
    }

    @Test
    public void popSpecificWhenTopIsRegularPop() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                uut.popSpecific(child2, new MockPromise() {
                    @Override
                    public void resolve(@Nullable Object value) {
                        assertContainsOnlyId(child1.getId());
                        assertIsChildById(uut.getView(), child1.getView());
                    }
                });
            }
        });
    }

    @Test
    public void popSpecificDeepInStack() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        assertIsChildById(uut.getView(), child2.getView());
        uut.popSpecific(child1, new MockPromise());
        assertContainsOnlyId(child2.getId());
        assertIsChildById(uut.getView(), child2.getView());
    }

    @Test
    public void popTo_PopsTopUntilControllerIsNewTop() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertThat(uut.size()).isEqualTo(3);
                assertThat(uut.peek()).isEqualTo(child3);

                uut.popTo(child1, new MockPromise());

                assertThat(uut.size()).isEqualTo(1);
                assertThat(uut.peek()).isEqualTo(child1);
            }
        });
    }

    @Test
    public void popTo_NotAChildOfThisStack_DoesNothing() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child3, new MockPromise());
        assertThat(uut.size()).isEqualTo(2);
        uut.popTo(child2, new MockPromise());
        assertThat(uut.size()).isEqualTo(2);
    }

    @Test
    public void popToRoot_PopsEverythingAboveFirstController() {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                assertThat(uut.size()).isEqualTo(3);
                assertThat(uut.peek()).isEqualTo(child3);

                uut.popToRoot(new MockPromise() {
                    @Override
                    public void resolve(@Nullable Object value) {
                        assertThat(uut.size()).isEqualTo(1);
                        assertThat(uut.peek()).isEqualTo(child1);
                    }
                });
            }
        });
    }

    @Test
    public void popToRoot_EmptyStackDoesNothing() {
        assertThat(uut.isEmpty()).isTrue();
        uut.popToRoot(new MockPromise());
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void findControllerById_ReturnsSelfOrChildrenById() {
        assertThat(uut.findControllerById("123")).isNull();
        assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.findControllerById(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Deeply() {
        StackController stack = createStackController("another");
        stack.animatePush(child2, new MockPromise());
        uut.animatePush(stack, new MockPromise());
        assertThat(uut.findControllerById(child2.getId())).isEqualTo(child2);
    }

    @Test
    public void pop_CallsDestroyOnPoppedChild() {
        child1 = spy(child1);
        child2 = spy(child2);
        child3 = spy(child3);
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(child3, times(0)).destroy();
                uut.pop(new MockPromise());
                verify(child3, times(1)).destroy();
            }
        });
    }

    @Test
    public void pop_callWillAppearWillDisappear() {
        child1 = spy(child1);
        child2 = spy(child2);
        uut.push(child1, new MockPromise());
        uut.push(child2, new MockPromise());
        uut.pop(new MockPromise());
        verify(child1, times(1)).onViewWillAppear();
        verify(child2, times(1)).onViewWillDisappear();
    }

    @Test
    public void pop_animatesTopBarIfNeeded() {
        uut.ensureViewIsCreated();

        child1.options.topBarOptions.visible = new Bool(false);
        child1.options.topBarOptions.animate = new Bool(false);
        child2.options.topBarOptions.visible = new Bool(true);
        uut.push(child1, new MockPromise());
        child1.onViewAppeared();

        assertThat(uut.getTopBar().getVisibility()).isEqualTo(View.GONE);
        uut.push(child2, new MockPromise());
        uut.animatePop(new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(uut.getTopBar(), times(1)).hide();
            }
        });
    }

    @Test
    public void popSpecific_CallsDestroyOnPoppedChild() {
        child1 = spy(child1);
        child2 = spy(child2);
        child3 = spy(child3);
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise());

        verify(child2, times(0)).destroy();
        uut.popSpecific(child2, new MockPromise());
        verify(child2, times(1)).destroy();
    }

    @Test
    public void popTo_CallsDestroyOnPoppedChild() {
        child1 = spy(child1);
        child2 = spy(child2);
        child3 = spy(child3);
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise() {
            @Override
            public void resolve(@Nullable Object value) {
                verify(child2, times(0)).destroy();
                verify(child3, times(0)).destroy();

                uut.popTo(child1, new MockPromise() {
                    @Override
                    public void resolve(@Nullable Object value) {
                        verify(child2, times(1)).destroy();
                        verify(child3, times(1)).destroy();
                    }
                });
            }
        });
    }

    @Test
    public void stackCanBePushed() {
        StackController parent = createStackController("someStack");
        parent.ensureViewIsCreated();
        parent.push(uut, new MockPromise());
        uut.onViewAppeared();
        assertThat(parent.getView().getChildAt(1)).isEqualTo(uut.getView());
    }

    @Test
    public void applyOptions_applyOnlyOnFirstStack() {
        StackController parent = spy(createStackController("someStack"));
        parent.ensureViewIsCreated();
        parent.push(uut, new MockPromise());

        Options childOptions = new Options();
        childOptions.topBarOptions.title.text = new Text("Something");
        child1.options = childOptions;
        uut.push(child1, new MockPromise());
        child1.ensureViewIsCreated();
        child1.onViewAppeared();

        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ReactComponent> viewCaptor = ArgumentCaptor.forClass(ReactComponent.class);
        verify(parent, times(1)).applyChildOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(optionsCaptor.getValue().topBarOptions.title.text.hasValue()).isFalse();
    }

    @Test
    public void applyOptions_topTabsAreNotVisibleIfNoTabsAreDefined() {
        uut.ensureViewIsCreated();
        uut.push(child1, new MockPromise());
        child1.ensureViewIsCreated();
        child1.onViewAppeared();
        assertThat(ViewHelper.isVisible(uut.getTopBar().getTopTabs())).isFalse();
    }

    @Test
    public void buttonPressInvokedOnCurrentStack() {
        uut.ensureViewIsCreated();
        uut.push(child1, new MockPromise());
        uut.sendOnNavigationButtonPressed("btn1");
        verify(child1, times(1)).sendOnNavigationButtonPressed("btn1");
    }

    @Test
    public void mergeChildOptions_updatesViewWithNewOptions() {
        final StackLayout[] stackLayout = new StackLayout[1];
        StackController uut = new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()), new TopBarController(), "stack", new Options()) {
            @NonNull
            @Override
            protected StackLayout createView() {
                stackLayout[0] = spy(super.createView());
                return stackLayout[0];
            }
        };
        Options optionsToMerge = new Options();
        Component component = mock(Component.class);
        uut.mergeChildOptions(optionsToMerge, component);
        verify(stackLayout[0], times(1)).mergeChildOptions(optionsToMerge, component);
    }

    @Test
    public void mergeChildOptions_updatesParentControllerWithNewOptions() {
        final StackLayout[] stackLayout = new StackLayout[1];
        StackController uut = new StackController(activity, new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()), new TopBarController(), "stack", new Options()) {
            @NonNull
            @Override
            protected StackLayout createView() {
                stackLayout[0] = spy(super.createView());
                return stackLayout[0];
            }
        };
        ParentController parentController = Mockito.mock(ParentController.class);
        uut.setParentController(parentController);
        Options optionsToMerge = new Options();
        optionsToMerge.topBarOptions.testId = new Text("topBarID");
        optionsToMerge.bottomTabsOptions.testId = new Text("bottomTabsID");
        Component component = mock(Component.class);
        uut.mergeChildOptions(optionsToMerge, component);

        ArgumentCaptor<Options> captor = ArgumentCaptor.forClass(Options.class);
        verify(parentController, times(1)).mergeChildOptions(captor.capture(), eq(component));
        assertThat(captor.getValue().topBarOptions.testId.hasValue()).isFalse();
        assertThat(captor.getValue().bottomTabsOptions.testId.get()).isEqualTo(optionsToMerge.bottomTabsOptions.testId.get());
    }

    @Test
    public void mergeChildOptions_mergeAnimationOptions() {
        Options options = new Options();
        Component component = mock(Component.class);

        uut.mergeChildOptions(options, component);
        verify(animator, times(0)).setOptions(options.animationsOptions);
        verify(animator, times(1)).mergeOptions(options.animationsOptions);
    }

    @Test
    public void mergeChildOptions_StackRelatedOptionsAreCleared() {
        ParentController parentController = Mockito.mock(ParentController.class);
        uut.setParentController(parentController);
        Options options = new Options();
        options.animationsOptions.push = NestedAnimationsOptions.parse(new JSONObject());
        options.topBarOptions.testId = new Text("id");
        options.fabOptions.id = new Text("fabId");
        Component component = mock(Component.class);

        assertThat(options.fabOptions.hasValue()).isTrue();
        uut.mergeChildOptions(options, component);
        ArgumentCaptor<Options> captor = ArgumentCaptor.forClass(Options.class);
        verify(parentController, times(1)).mergeChildOptions(captor.capture(), eq(component));
        assertThat(captor.getValue().animationsOptions.push.hasValue()).isFalse();
        assertThat(captor.getValue().topBarOptions.testId.hasValue()).isFalse();
        assertThat(captor.getValue().fabOptions.hasValue()).isFalse();
    }

    @Test
    public void destroy() {
        uut.ensureViewIsCreated();
        uut.destroy();
        verify(topBarController, times(1)).clear();
    }

    private void assertContainsOnlyId(String... ids) {
        assertThat(uut.size()).isEqualTo(ids.length);
        assertThat(uut.getChildControllers()).extracting((Extractor<ViewController, String>) ViewController::getId).containsOnly(ids);
    }

    private StackController createStackController() {
        return createStackController("stackId");
    }

    private StackController createStackController(String id) {
        topBarController = spy(new TopBarController());
        return new StackController(activity,
                new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()),
                topBarController, id, new Options()) {
            @Override
            NavigationAnimator createAnimator() {
                animator = Mockito.mock(NavigationAnimator.class);
                return animator;
            }
        };
    }
}
