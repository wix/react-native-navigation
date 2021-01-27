package com.reactnativenavigation.utils;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.ActionMenuView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonsToolbar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;

import org.junit.Test;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@LooperMode(LooperMode.Mode.PAUSED)
public class ButtonPresenterTest extends BaseTest {
    private static final String BTN_TEXT = "button1";

    private ButtonsToolbar titleBar;
    private ButtonPresenter uut;
    private ButtonController buttonController;
    private ButtonOptions button;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        titleBar = new ButtonsToolbar(activity);
        activity.setContentView(titleBar);
        button = createButton();

        uut = new ButtonPresenter(activity, button, new IconResolverFake(activity));
        buttonController = new ButtonController(
                activity,
                uut,
                button,
                mock(TitleBarButtonCreator.class),
                mock(ButtonController.OnClickListener.class)
        );
    }

    @Test
    public void applyOptions_buttonIsAddedToMenu() {
        addButtonAndApplyOptions();
        assertThat(findButtonView().getText().toString()).isEqualTo(BTN_TEXT);
    }

    @Test
    public void applyOptions_appliesColorOnButtonTextView() {
        button.color = new Colour(Color.RED);
        addButtonAndApplyOptions();
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void apply_disabledColor() {
        button.enabled = new Bool(false);
        addButtonAndApplyOptions();
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(ButtonPresenter.DISABLED_COLOR);
    }

    @Test
    public void applyColor_shouldChangeColor() {
        MenuItem menuItem = addMenuButton();

        uut.applyOptions(titleBar, menuItem, buttonController::getView);
        Colour color = new Colour(Color.RED);
        uut.applyColor(titleBar, menuItem, color);
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void applyColor_shouldChangeDisabledColor() {
        button.enabled = new Bool(false);
        MenuItem menuItem = addMenuButton();
        uut.applyOptions(titleBar, menuItem, buttonController::getView);
        Colour disabledColor = new Colour(Color.BLUE);
        uut.applyDisabledColor(titleBar, menuItem, disabledColor);
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.BLUE);
    }

    private void addButtonAndApplyOptions() {
        MenuItem menuItem = addMenuButton();
        uut.applyOptions(titleBar, menuItem, buttonController::getView);
    }

    private MenuItem addMenuButton() {
        return titleBar.addButton(Menu.NONE,
                1,
                0,
                SpannableString.valueOf(button.text.get("text")));
    }

    @Test
    public void apply_allCaps() {
        button.allCaps = new Bool(false);
        addButtonAndApplyOptions();
        assertThat(findButtonView().isAllCaps()).isEqualTo(false);
    }

    private TextView findButtonView() {
        ShadowLooper.idleMainLooper();
        return (TextView) ViewUtils.findChildrenByClass(
                requireNonNull(ViewUtils.findChildByClass(titleBar, ActionMenuView.class)),
                TextView.class,
                child -> true
        ).get(0);
    }

    private ButtonOptions createButton() {
        ButtonOptions b = new ButtonOptions();
        b.id = "btn1";
        b.text = new Text(BTN_TEXT);
        b.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return b;
    }
}
