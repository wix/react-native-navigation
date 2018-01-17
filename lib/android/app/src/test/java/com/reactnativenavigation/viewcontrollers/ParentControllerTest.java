package com.reactnativenavigation.viewcontrollers;

import android.app.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;

import com.reactnativenavigation.*;
import com.reactnativenavigation.mocks.*;

import org.junit.*;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class ParentControllerTest extends BaseTest {

    private Activity activity;
    private List<ViewController> children;
    private ParentController uut;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        children = new ArrayList<>();
        uut = new ParentController(activity, "uut") {

            @NonNull
            @Override
            protected ViewGroup createView() {
                return new FrameLayout(activity);
            }

            @NonNull
            @Override
            public Collection<ViewController> getChildControllers() {
                return children;
            }
        };
    }

    @Test
    public void holdsViewGroup() throws Exception {
        assertThat(uut.getView()).isInstanceOf(ViewGroup.class);
    }

    @Test
    public void mustHaveChildControllers() throws Exception {
        assertThat(uut.getChildControllers()).isNotNull();
    }

    @Test
    public void findControllerById_ChildById() throws Exception {
        SimpleViewController child1 = new SimpleViewController(activity, "child1");
        SimpleViewController child2 = new SimpleViewController(activity, "child2");
        children.add(child1);
        children.add(child2);

        assertThat(uut.findControllerById("uut")).isEqualTo(uut);
        assertThat(uut.findControllerById("child1")).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Recursive() throws Exception {
        StackController stackController = new StackController(activity, "stack");
        SimpleViewController child1 = new SimpleViewController(activity, "child1");
        SimpleViewController child2 = new SimpleViewController(activity, "child2");
        stackController.animatePush(child1, new MockPromise());
        stackController.animatePush(child2, new MockPromise());
        children.add(stackController);

        assertThat(uut.findControllerById("child2")).isEqualTo(child2);
    }

    @Test
    public void destroy_DestroysChildren() throws Exception {
        ViewController child1 = spy(new SimpleViewController(activity, "child1"));
        children.add(child1);

        verify(child1, times(0)).destroy();
        uut.destroy();
        verify(child1, times(1)).destroy();
    }
}
