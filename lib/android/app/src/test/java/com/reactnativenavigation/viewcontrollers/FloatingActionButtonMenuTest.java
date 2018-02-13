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
import com.reactnativenavigation.views.FabMenu;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FloatingActionButtonMenuTest extends BaseTest {

    private final static int CHILD_FAB_COUNT = 3;

    private StackController stackController;
    private SimpleViewController controllerFab;
    private SimpleViewController controllerNoFab;

    @Override
    public void beforeEach() {
        super.beforeEach();
        Activity activity = newActivity();
        stackController = new StackController(activity, "stackController", new Options());
        Options options = getOptionsWithFab();
        controllerFab = new SimpleViewController(activity, "child1", options);
        controllerNoFab = new SimpleViewController(activity, "child2", new Options());
    }

    @NonNull
    private Options getOptionsWithFab() {
        Options options = new Options();
        FabOptions fabOptions = new FabOptions();
        fabOptions.id = new Text("FAB");
        for (int i = 0; i < CHILD_FAB_COUNT; i++) {
            FabOptions childOptions = new FabOptions();
            childOptions.id = new Text("fab" + i);
            fabOptions.actionsArray.add(childOptions);
        }
        options.fabMenuOptions = fabOptions;
        return options;
    }

    private boolean hasFab() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            if (stackLayout.getChildAt(i) instanceof FabMenu) {
                return true;
            }
        }
        return false;
    }

    private boolean containsFabChildren() {
        StackLayout stackLayout = stackController.getStackLayout();
        for (int i = 0; i < stackLayout.getChildCount(); i++) {
            View child = stackLayout.getChildAt(i);
            if (child instanceof FabMenu) {
                return ((FabMenu) child).getChildCount() == CHILD_FAB_COUNT + 2;
            }
        }
        return false;
    }

    @Test
    public void showOnPush() throws Exception {
        stackController.push(controllerFab, new MockPromise());
        controllerFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hideOnPush() throws Exception {
        stackController.push(controllerFab, new MockPromise());
        controllerFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.push(controllerNoFab, new MockPromise());
        controllerNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void hideOnPop() throws Exception {
        stackController.push(controllerNoFab, new MockPromise());
        stackController.push(controllerFab, new MockPromise());
        controllerFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        stackController.pop(new MockPromise());
        controllerNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
    }

    @Test
    public void showOnPop() throws Exception {
        stackController.push(controllerFab, new MockPromise());
        stackController.push(controllerNoFab, new MockPromise());
        controllerNoFab.onViewAppeared();
        assertThat(hasFab()).isFalse();
        stackController.pop(new MockPromise());
        controllerFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
    }

    @Test
    public void hasChildren() throws Exception {
        stackController.push(controllerFab, new MockPromise());
        controllerFab.onViewAppeared();
        assertThat(hasFab()).isTrue();
        assertThat(containsFabChildren()).isTrue();
    }
}
