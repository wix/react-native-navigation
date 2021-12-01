package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.graphics.Color;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.NullText;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("MagicNumber")
public class TopBarButtonControllerTest extends BaseTest {

    private ButtonController uut;
    private StackController stackController;
    private ButtonOptions button;
    private ButtonPresenter optionsPresenter;

    @Override
    public void beforeEach() {
        button = new ButtonOptions();
        final Activity activity = newActivity();

        TitleBarButtonCreatorMock buttonCreatorMock = new TitleBarButtonCreatorMock();
        stackController = spy(TestUtils.newStackController(activity).build());
        stackController.getView().layout(0, 0, 1080, 1920);
        stackController.getTopBar().layout(0, 0, 1080, 200);
        getTitleBar().layout(0, 0, 1080, 200);

        optionsPresenter = spy(new ButtonPresenter(activity, button, new IconResolverFake(activity, ImageLoaderMock.mock())));
        uut = new ButtonController(activity, optionsPresenter, button, buttonCreatorMock, (buttonId) -> {
        });

        stackController.ensureViewIsCreated();
    }

    @Test
    public void buttonDoesNotClearStackOptionsOnAppear() {
        setReactComponentButton();
        uut.ensureViewIsCreated();
        uut.onViewWillAppear();
        verify(stackController, times(0)).clearOptions();
    }

    @Test
    public void setIconColor_enabled() {
        setIconButton(true);
        uut.addToMenu(getTitleBar(), 0);

        assertThat(getTitleBar().getMenu().size()).isOne();
        verify(optionsPresenter).tint(any(), eq(Color.RED));
    }

    @Test
    public void setIconColor_disabled() {
        setIconButton(false);
        uut.addToMenu(getTitleBar(), 0);

        verify(optionsPresenter).tint(any(), eq(Color.LTGRAY));
    }

    @Test
    public void setIconColor_disabledColor() {
        setIconButton(false);
        button.disabledColor = new ThemeColour(new Colour(Color.BLACK), new Colour(Color.BLACK));
        uut.addToMenu(getTitleBar(), 0);

        verify(optionsPresenter).tint(any(), eq(Color.BLACK));
    }

    @Test
    public void disableIconTint() {
        setIconButton(false);
        button.disableIconTint = new Bool(true);
        uut.addToMenu(getTitleBar(), 0);
        verify(optionsPresenter, times(0)).tint(any(), anyInt());
    }

    private ButtonBar getTitleBar() {
        return stackController.getTopBar().getRightButtonBar();
    }

    private void setIconButton(boolean enabled) {
        button.id = "btn1";
        button.icon = new Text("someIcon");
        button.color = new ThemeColour(new Colour(Color.RED),new Colour(Color.RED));
        button.component.name = new NullText();
        button.component.componentId = new NullText();
        button.enabled = new Bool(enabled);
        button.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void setReactComponentButton() {
        button.id = "btnId";
        button.component.name = new Text("com.example.customBtn");
        button.component.componentId = new Text("component666");
    }
}
