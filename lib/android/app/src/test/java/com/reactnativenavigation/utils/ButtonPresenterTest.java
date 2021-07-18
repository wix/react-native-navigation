package com.reactnativenavigation.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.ActionMenuView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.IconBackgroundOptions;
import com.reactnativenavigation.options.params.Bool;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.ThemeColour;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.IconBackgroundDrawable;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;

import org.junit.Test;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@LooperMode(LooperMode.Mode.PAUSED)
public class ButtonPresenterTest extends BaseTest {
    private static final String BTN_TEXT = "button1";

    private ButtonBar titleBar;
    private ButtonPresenter uut;
    private ButtonController buttonController;
    private ButtonOptions button;
    private Activity activity;

    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
        titleBar = new ButtonBar(activity);
        activity.setContentView(titleBar);
        button = createButton();
        ImageLoader imageLoaderMock;

        imageLoaderMock = ImageLoaderMock.mock();
        initUUt(imageLoaderMock);
    }

    private void initUUt(ImageLoader imageLoaderMock) {
        IconResolverFake iconResolver = new IconResolverFake(activity, imageLoaderMock);
        uut = new ButtonPresenter(activity, button, iconResolver);
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
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        addButtonAndApplyOptions();
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void applyOptions_appliesColorOnButtonTextViewOnDarkMode() {
        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_NO;
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.BLACK));
        MenuItem menuItem = addButtonAndApplyOptions();
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.RED);

        mockConfiguration.uiMode = Configuration.UI_MODE_NIGHT_YES;
        uut.applyOptions(titleBar, menuItem, buttonController::getView);
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.BLACK);

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
        ThemeColour color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        uut.applyColor(titleBar, menuItem, color);
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.RED);
    }

    @Test
    public void applyOptions_shouldChangeIconColorTint() {
        IconBackgroundDrawable mockD = mock(IconBackgroundDrawable.class);
        initUUt(ImageLoaderMock.mock(mockD));
        button.enabled = new Bool(true);
        button.icon = new Text("icon");
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        MenuItem menuItem = spy(addMenuButton());
        uut.applyOptions(titleBar, menuItem, buttonController::getView);

        Drawable icon = menuItem.getIcon();
        assertThat(icon).isNotNull();
        verify(icon).setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
    }

    @Test
    public void applyOptions_shouldChangeIconDisabledColorTint() {
        IconBackgroundDrawable mockD = mock(IconBackgroundDrawable.class);
        initUUt(ImageLoaderMock.mock(mockD));
        button.enabled = new Bool(false);
        button.icon = new Text("icon");
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        button.disabledColor = new ThemeColour(new Colour(Color.YELLOW), new Colour(Color.YELLOW));
        MenuItem menuItem = spy(addMenuButton());
        uut.applyOptions(titleBar, menuItem, buttonController::getView);

        Drawable icon = menuItem.getIcon();
        assertThat(icon).isNotNull();
        verify(icon).setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN));
    }

    @Test
    public void applyOptions_shouldChangeIconColorBackground() {
        IconBackgroundDrawable mockD = mock(IconBackgroundDrawable.class);
        initUUt(ImageLoaderMock.mock(mockD));
        button.enabled = new Bool(true);
        button.icon = new Text("icon");
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        IconBackgroundOptions iconBackground = new IconBackgroundOptions();
        iconBackground.color = new ThemeColour(new Colour(Color.GREEN),new Colour(Color.GREEN));
        button.iconBackground = iconBackground;
        MenuItem menuItem = spy(addMenuButton());
        uut.applyOptions(titleBar, menuItem, buttonController::getView);

        Drawable icon = menuItem.getIcon();
        assertThat(icon).isNotNull();
        assertThat(icon).isInstanceOf(IconBackgroundDrawable.class);
        IconBackgroundDrawable modifed = (IconBackgroundDrawable) icon;
        verify(modifed.getWrappedDrawable()).setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        assertThat(modifed.getBackgroundColor()).isEqualTo(Color.GREEN);
    }

    @Test
    public void applyOptions_shouldChangeIconDisabledColorBackground() {
        IconBackgroundDrawable mockD = mock(IconBackgroundDrawable.class);
        initUUt(ImageLoaderMock.mock(mockD));
        button.enabled = new Bool(false);
        button.icon = new Text("icon");
        button.color = new ThemeColour(new Colour(Color.RED), new Colour(Color.RED));
        button.disabledColor = new ThemeColour(new Colour(Color.YELLOW), new Colour(Color.YELLOW));
        IconBackgroundOptions iconBackground = new IconBackgroundOptions();
        iconBackground.color = new ThemeColour( new Colour(Color.GREEN), new Colour(Color.GREEN));
        iconBackground.disabledColor = new ThemeColour(new Colour(Color.CYAN),new Colour(Color.CYAN));
        button.iconBackground = iconBackground;
        MenuItem menuItem = spy(addMenuButton());
        uut.applyOptions(titleBar, menuItem, buttonController::getView);

        Drawable icon = menuItem.getIcon();
        assertThat(icon).isNotNull();
        assertThat(icon).isInstanceOf(IconBackgroundDrawable.class);
        IconBackgroundDrawable modifed = (IconBackgroundDrawable) icon;
        verify(modifed.getWrappedDrawable()).setColorFilter(new PorterDuffColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN));
        assertThat(modifed.getBackgroundColor()).isEqualTo(Color.CYAN);
    }

    @Test
    public void applyColor_shouldChangeDisabledColor() {
        button.enabled = new Bool(false);
        MenuItem menuItem = addMenuButton();
        uut.applyOptions(titleBar, menuItem, buttonController::getView);
        ThemeColour disabledColor = new ThemeColour(new Colour(Color.BLUE), new Colour(Color.BLUE));
        uut.applyDisabledColor(titleBar, menuItem, disabledColor);
        assertThat(findButtonView().getCurrentTextColor()).isEqualTo(Color.BLUE);
    }

    private MenuItem addButtonAndApplyOptions() {
        MenuItem menuItem = addMenuButton();
        uut.applyOptions(titleBar, menuItem, buttonController::getView);
        return menuItem;
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
