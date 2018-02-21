package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.MockPromise;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.parse.FabOptions;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.Text;
import com.reactnativenavigation.views.Fab;
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FloatingActionButtonTest extends BaseTest {

    private final static int CHILD_FAB_COUNT = 3;

    private StackController stackController;
    private SimpleViewController childFab;
    private SimpleViewController childNoFab;
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        stackController = new StackController(activity, "stackController", new Options());
        Options options = getOptionsWithFab();
        childFab = new SimpleViewController(activity, "child1", options);
        childNoFab = new SimpleViewController(activity, "child2", new Options());
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
        for (int i = 0; i < CHILD_FAB_COUNT; i++) {
            FabOptions childOptions = new FabOptions();
            childOptions.id = new Text("fab" + i);
            fabOptions.actionsArray.add(childOptions);
        }
        options.fabOptions = fabOptions;
        return options;
    }

    private boolean hasFab() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            if (stackLayout.getChildAt(i) instanceof Fab) {
                return true;
            }
            if (stackLayout.getChildAt(i) instanceof FabMenu) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void showOnPush() throws Exception {
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hideOnPush() throws Exception {
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.push(childNoFab, new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void hideOnPop() throws Exception {
        stackController.push(childNoFab, new MockPromise());
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.pop(new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void showOnPop() throws Exception {
        stackController.push(childFab, new MockPromise());
        stackController.push(childNoFab, new MockPromise());
        childNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
        stackController.pop(new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hasChildren() throws Exception {
        childFab = new SimpleViewController(activity, "child1", getOptionsWithFabActions());
        stackController.push(childFab, new MockPromise());
        childFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        assertThat(containsActions()).isTrue();
    }

    private boolean containsActions() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            View child = stackLayout.getChildAt(i);
            if (child instanceof FabMenu) {
                return ((FabMenu) child).getChildCount() == CHILD_FAB_COUNT + 2;
            }
        }
        return false;
    }
}
