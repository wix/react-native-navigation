package com.reactnativenavigation.presentation;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.SideMenuRootOptions;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.views.SideMenu;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;

public class SideMenuPresenterTest extends BaseTest {
    private SideMenuPresenter uut;

    @Override
    public void beforeEach() {
        uut = new SideMenuPresenter();
        SideMenu sideMenu = Mockito.mock(SideMenu.class);
        uut.bindView(sideMenu);
    }

    @Test
    public void mergeVisibility_visibilityOptionsAreConsumed() {
        SideMenuRootOptions options = new SideMenuRootOptions();
        options.left.visible = new Bool(true);
        options.right.visible = new Bool(true);

        uut.mergeOptions(options);

        assertFalse(options.right.visible.hasValue());
        assertFalse(options.left.visible.hasValue());
    }
}
