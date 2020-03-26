package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Fraction;
import com.reactnativenavigation.parse.params.NullText;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;
import com.reactnativenavigation.viewcontrollers.stack.StackController;
import com.reactnativenavigation.views.titlebar.TitleBar;

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

    private TitleBarButtonController uut;
    private StackController stackController;
    private Button button;
    private ButtonPresenter optionsPresenter;

    @Override
    public void beforeEach() {
        button = new Button();
        final Activity activity = newActivity();

        TopBarButtonCreatorMock buttonCreatorMock = new TopBarButtonCreatorMock();
        stackController = spy(TestUtils.newStackController(activity).build());
        stackController.getView().layout(0, 0, 1080, 1920);
        stackController.getTopBar().layout(0, 0, 1080, 200);
        getTitleBar().layout(0, 0, 1080, 200);

        optionsPresenter = spy(new ButtonPresenter(button));
        uut = new TitleBarButtonController(activity, new IconResolver(activity, ImageLoaderMock.mock()), optionsPresenter, button, buttonCreatorMock, (buttonId) -> {});

        stackController.ensureViewIsCreated();
    }

    @Test
    public void buttonDoesNotClearStackOptionsOnAppear() {
        setReactComponentButton();
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(stackController, times(0)).clearOptions();
    }

    @Test
    public void setIconColor_enabled() {
        setIconButton(true);
        uut.addToMenu(getTitleBar(), 0);

        assertThat(getTitleBar().getMenu().size()).isOne();
        verify(optionsPresenter, times(1)).tint(any(), eq(Color.RED));
    }

    @Test
    public void setIconColor_disabled() {
        setIconButton(false);
        uut.addToMenu(getTitleBar(), 0);

        verify(optionsPresenter, times(1)).tint(any(), eq(Color.LTGRAY));
    }

    @Test
    public void setIconColor_disabledColor() {
        setIconButton(false);
        button.disabledColor = new Colour(Color.BLACK);
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

    @Test
    public void fontFamily() {
        setTextButton();
        uut.addToMenu(getTitleBar(), 0);
        verify(optionsPresenter, times(1)).setTypeFace(getTitleBar(), Typeface.MONOSPACE);
    }

    @Test
    public void fontSize() {
        setTextButton();
        uut.addToMenu(getTitleBar(), 0);
        verify(optionsPresenter, times(0)).setFontSize(getTitleBar().getMenu().getItem(0));

        clearMenu();
        button.fontSize = new Fraction(10);
        uut.addToMenu(getTitleBar(), 0);
        verify(optionsPresenter, times(1)).setFontSize(getTitleBar().getMenu().getItem(0));
    }

    @Test
    public void textColor_enabled() {
        setTextButton();
        button.enabled = new Bool(false);
        uut.addToMenu(getTitleBar(), 0);
        dispatchPreDraw(getTitleBar());
        verify(optionsPresenter, times(0)).setEnabledColor(any());

        clearMenu();
        button.enabled = new Bool(true);
        button.color = new Colour(android.graphics.Color.RED);
        uut.addToMenu(getTitleBar(), 0);
        dispatchPreDraw(getTitleBar());
        verify(optionsPresenter, times(1)).setEnabledColor(any());
    }

    private void clearMenu() {
        getTitleBar().getMenu().clear();
    }

    @Test
    public void textColor_disabled() {
        setTextButton();
        button.enabled = new Bool(false);
        uut.addToMenu(getTitleBar(), 0);
        dispatchPreDraw(getTitleBar());
        verify(optionsPresenter, times(1)).setDisabledColor(any(), eq(Color.LTGRAY));

        clearMenu();
        button.disabledColor = new Colour(android.graphics.Color.BLACK);
        uut.addToMenu(getTitleBar(), 0);
        dispatchPreDraw(getTitleBar());
        verify(optionsPresenter, times(1)).setDisabledColor(any(), eq(Color.BLACK));
    }

    private TitleBar getTitleBar() {
        return stackController.getTopBar().getTitleBar();
    }

    private void setTextButton() {
        button.id = "btn1";
        button.text = new Text("Button");
        button.fontFamily = Typeface.MONOSPACE;
        button.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    private void setIconButton(boolean enabled) {
        button.id = "btn1";
        button.icon = new Text("someIcon");
        button.color = new Colour(Color.RED);
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
