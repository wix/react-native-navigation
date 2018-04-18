package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.ReactComponent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ParentControllerTest extends BaseTest {

    private static final String INITIAL_TITLE = "initial title";
    private Activity activity;
    private List<ViewController> children;
    private ParentController uut;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        children = new ArrayList<>();
        Options initialOptions = new Options();
        initialOptions.topBar.title.text = new Text(INITIAL_TITLE);
        uut = spy(new ParentController(activity, "uut", initialOptions) {

            @NonNull
            @Override
            protected ViewGroup createView() {
                FrameLayout layout = new FrameLayout(activity);
                for (ViewController child : children) {
                    child.setParentController(this);
                    layout.addView(child.getView());
                }
                return layout;
            }

            @Override
            public void sendOnNavigationButtonPressed(String buttonId) {

            }

            @NonNull
            @Override
            public Collection<ViewController> getChildControllers() {
                return children;
            }
        });
    }

    @Test
    public void holdsViewGroup() {
        assertThat(uut.getView()).isInstanceOf(ViewGroup.class);
    }

    @Test
    public void mustHaveChildControllers() {
        assertThat(uut.getChildControllers()).isNotNull();
    }

    @Test
    public void findControllerById_ChildById() {
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, "child2", new Options());
        children.add(child1);
        children.add(child2);

        assertThat(uut.findControllerById("uut")).isEqualTo(uut);
        assertThat(uut.findControllerById("child1")).isEqualTo(child1);
    }

    @Test
    public void findControllerById_Recursive() {
        StackController stackController = createStack();
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        SimpleViewController child2 = new SimpleViewController(activity, "child2", new Options());
        stackController.push(child1, new CommandListenerAdapter());
        stackController.push(child2, new CommandListenerAdapter());
        children.add(stackController);

        assertThat(uut.findControllerById("child2")).isEqualTo(child2);
    }

    @Test
    public void destroy_DestroysChildren() {
        ViewController child1 = spy(new SimpleViewController(activity, "child1", new Options()));
        children.add(child1);

        verify(child1, times(0)).destroy();
        uut.destroy();
        verify(child1, times(1)).destroy();
    }

    @Test
    public void optionsAreClearedWhenChildIsAppeared() {
        StackController stackController = spy(createStack());
        SimpleViewController child1 = new SimpleViewController(activity, "child1", new Options());
        stackController.push(child1, new CommandListenerAdapter());

        child1.onViewAppeared();
        verify(stackController, times(1)).clearOptions();
    }

    @Test
    public void mergeOptions_optionsAreMergedWhenChildAppears() {
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, "child1", options));
        children.add(child1);
        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewAppeared();
        ArgumentCaptor<Options> optionsCaptor = ArgumentCaptor.forClass(Options.class);
        ArgumentCaptor<ReactComponent> viewCaptor = ArgumentCaptor.forClass(ReactComponent.class);
        verify(uut, times(1)).clearOptions();
        verify(uut, times(1)).applyChildOptions(optionsCaptor.capture(), viewCaptor.capture());
        assertThat(optionsCaptor.getValue().topBar.title.text.get()).isEqualTo("new title");
        assertThat(viewCaptor.getValue()).isEqualTo(child1.getView());
    }

    @Test
    public void mergeOptions_initialParentOptionsAreNotMutatedWhenChildAppears() {
        Options options = new Options();
        options.topBar.title.text = new Text("new title");
        ViewController child1 = spy(new SimpleViewController(activity, "child1", options));
        children.add(child1);

        uut.ensureViewIsCreated();

        child1.ensureViewIsCreated();
        child1.onViewAppeared();
        assertThat(uut.initialOptions.topBar.title.text.get()).isEqualTo(INITIAL_TITLE);
    }

    private StackController createStack() {
        return new StackControllerBuilder(activity)
                .setTopBarButtonCreator(new TopBarButtonCreatorMock())
                .setTitleBarReactViewCreator(new TitleBarReactViewCreatorMock())
                .setTopBarBackgroundViewController(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()))
                .setTopBarController(new TopBarController())
                .setId("stack")
                .setInitialOptions(new Options())
                .createStackController();
    }
}
