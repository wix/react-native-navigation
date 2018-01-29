package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.view.View;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;

import org.assertj.core.api.iterable.*;
import org.junit.*;

import javax.annotation.Nullable;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class StackControllerTest extends BaseTest {

    private Activity activity;
    private StackController uut;
    private ViewController child1;
    private ViewController child2;
    private ViewController child3;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        uut = new StackController(activity, "uut");
        child1 = new SimpleViewController(activity, "child1");
        child2 = new SimpleViewController(activity, "child2");
        child3 = new SimpleViewController(activity, "child3");
    }

    @Test
    public void isAViewController() throws Exception {
        assertThat(uut).isInstanceOf(ViewController.class);
    }

    @Test
    public void holdsAStackOfViewControllers() throws Exception {
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        uut.animatePush(child3, new MockPromise());
        assertThat(uut.peek()).isEqualTo(child3);
        assertContainsOnlyId(child1.getId(), child2.getId(), child3.getId());
    }

    @Test
    public void push() throws Exception {
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void pop() throws Exception {
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
    public void stackOperations() throws Exception {
        assertThat(uut.peek()).isNull();
        assertThat(uut.size()).isZero();
        assertThat(uut.isEmpty()).isTrue();
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.peek()).isEqualTo(child1);
        assertThat(uut.size()).isEqualTo(1);
        assertThat(uut.isEmpty()).isFalse();
    }

    @Test
    public void pushAssignsRefToSelfOnPushedController() throws Exception {
        assertThat(child1.getParentController()).isNull();
        uut.animatePush(child1, new MockPromise());
        assertThat(child1.getParentController()).isEqualTo(uut);

        StackController anotherNavController = new StackController(activity, "another");
        anotherNavController.animatePush(child2, new MockPromise());
        assertThat(child2.getParentController()).isEqualTo(anotherNavController);
    }

    @Test
    public void handleBack_PopsUnlessSingleChild() throws Exception {
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
    public void popDoesNothingWhenZeroOrOneChild() throws Exception {
        assertThat(uut.isEmpty()).isTrue();
        uut.pop(new MockPromise());
        assertThat(uut.isEmpty()).isTrue();

        uut.animatePush(child1, new MockPromise());
        uut.pop(new MockPromise());
        assertContainsOnlyId(child1.getId());
    }

    @Test
    public void canPopWhenSizeIsMoreThanOne() throws Exception {
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
    public void pushAddsToViewTree() throws Exception {
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNull();
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.getView().findViewById(child1.getView().getId())).isNotNull();
    }

    @Test
    public void pushRemovesPreviousFromTree() throws Exception {
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
    public void popReplacesViewWithPrevious() throws Exception {
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
    public void popSpecificWhenTopIsRegularPop() throws Exception {
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
    public void popSpecificDeepInStack() throws Exception {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child2, new MockPromise());
        assertIsChildById(uut.getView(), child2.getView());
        uut.popSpecific(child1, new MockPromise());
        assertContainsOnlyId(child2.getId());
        assertIsChildById(uut.getView(), child2.getView());
    }

    @Test
    public void popTo_PopsTopUntilControllerIsNewTop() throws Exception {
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
    public void popTo_NotAChildOfThisStack_DoesNothing() throws Exception {
        uut.animatePush(child1, new MockPromise());
        uut.animatePush(child3, new MockPromise());
        assertThat(uut.size()).isEqualTo(2);
        uut.popTo(child2, new MockPromise());
        assertThat(uut.size()).isEqualTo(2);
    }

    @Test
    public void popToRoot_PopsEverythingAboveFirstController() throws Exception {
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
    public void popToRoot_EmptyStackDoesNothing() throws Exception {
        assertThat(uut.isEmpty()).isTrue();
        uut.popToRoot(new MockPromise());
        assertThat(uut.isEmpty()).isTrue();
    }

    @Test
    public void findControllerById_ReturnsSelfOrChildrenById() throws Exception {
        assertThat(uut.findControllerById("123")).isNull();
        assertThat(uut.findControllerById(uut.getId())).isEqualTo(uut);
        uut.animatePush(child1, new MockPromise());
        assertThat(uut.findControllerById(child1.getId())).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Deeply() throws Exception {
        StackController stack = new StackController(activity, "stack2");
        stack.animatePush(child2, new MockPromise());
        uut.animatePush(stack, new MockPromise());
        assertThat(uut.findControllerById(child2.getId())).isEqualTo(child2);
    }

    @Test
    public void pop_CallsDestroyOnPoppedChild() throws Exception {
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
    public void popSpecific_CallsDestroyOnPoppedChild() throws Exception {
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
    public void popTo_CallsDestroyOnPoppedChild() throws Exception {
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

    private void assertContainsOnlyId(String... ids) {
        assertThat(uut.size()).isEqualTo(ids.length);
        assertThat(uut.getChildControllers()).extracting((Extractor<ViewController, String>) ViewController::getId).containsOnly(ids);
    }
}
