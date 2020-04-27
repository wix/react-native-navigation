package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.CommandListenerAdapter;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;

import androidx.annotation.NonNull;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FloatingActionButtonTest extends BaseTest {

    private final static int CHILD_FAB_COUNT = 3;

    private StackController stackController;
    private SimpleViewController childFab;
    private SimpleViewController childNoFab;
    private Activity activity;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        childRegistry = new ChildControllersRegistry();
        stackController = TestUtils.newStackController(activity).build();
        stackController.ensureViewIsCreated();
        Options options = getOptionsWithFab();
        childFab = new SimpleViewController(activity, childRegistry, "child1", options);
        childNoFab = new SimpleViewController(activity, childRegistry, "child2", new Options());
    }

    @NonNull
    private Options getOptionsWithFab() {
        Options options = new Options();
        FabOptions fabOptions = new FabOptions();
        fabOptions.id = new Text("FAB");
        options.fabOptions = fabOptions;
        return options;
    }

    @NonNull
    private Options getOptionsWithFabActions() {
        Options options = new Options();
        FabOptions fabOptions = new FabOptions();
        fabOptions.id = new Text("FAB");
        options.fabOptions = fabOptions;
        return options;
    }

    private boolean hasFab() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            if (stackLayout.getChildAt(i) instanceof Fab) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void showOnPush() {
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hideOnPush() {
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.push(childNoFab, new CommandListenerAdapter());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void hideOnPop() {
        disablePushAnimation(childNoFab, childFab);
        stackController.push(childNoFab, new CommandListenerAdapter());
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.pop(Options.EMPTY, new CommandListenerAdapter());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void showOnPop() {
        disablePushAnimation(childFab, childNoFab);
        stackController.push(childFab, new CommandListenerAdapter());
        stackController.push(childNoFab, new CommandListenerAdapter());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
        stackController.pop(Options.EMPTY, new CommandListenerAdapter());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hasChildren() {
        childFab = new SimpleViewController(activity, childRegistry, "child1", getOptionsWithFabActions());
        stackController.push(childFab, new CommandListenerAdapter());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }
}
