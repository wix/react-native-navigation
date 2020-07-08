package com.reactnativenavigation.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.TextView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Colour;
import com.reactnativenavigation.options.params.Number;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBar;
import com.reactnativenavigation.views.stack.topbar.titlebar.TitleBarButtonCreator;

import org.junit.Test;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowLooper;

import java.util.List;

import androidx.appcompat.widget.ActionMenuView;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

@LooperMode(LooperMode.Mode.PAUSED)
public class ButtonPresenterTest extends BaseTest {
    private TitleBar titleBar;
    private ButtonPresenter uut;
    private ButtonController buttonController;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        titleBar = new TitleBar(activity);
        activity.setContentView(titleBar);
        ButtonOptions button = createButton();

        uut = new ButtonPresenter(button, new IconResolverFake(activity));
        buttonController = new ButtonController(
                activity,
                uut,
                button,
                mock(TitleBarButtonCreator.class),
                mock(ButtonController.OnClickListener.class)
        );
    }

    @Test
    public void applyOptions_appliesColorOnButtonTextView() {
        MenuItem menuItem = buttonController.createAndAddButtonToTitleBar(titleBar, 0);
        uut.applyOptions(titleBar, menuItem, buttonController::getView);

        ShadowLooper.idleMainLooper();
        List<TextView> textualButtons = ViewUtils.findChildrenByClass(
                requireNonNull(ViewUtils.findChildByClass(titleBar, ActionMenuView.class)),
                TextView.class,
                child -> true
        );
        assertThat(textualButtons.get(0).getCurrentTextColor()).isEqualTo(Color.RED);
    }

    private ButtonOptions createButton() {
        ButtonOptions b = new ButtonOptions();
        b.id = "btn1";
        b.text = new Text("button");
        b.color = new Colour(Color.RED);
        b.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return b;
    }
}
