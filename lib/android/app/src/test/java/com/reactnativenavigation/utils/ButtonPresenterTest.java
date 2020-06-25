package com.reactnativenavigation.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.MenuItem;
import android.widget.TextView;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.TestUtils;
import com.reactnativenavigation.fakes.IconResolverFake;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Colour;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.viewcontrollers.TitleBarButtonController;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarButtonCreator;

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
    private TitleBarButtonController buttonController;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        titleBar = new TitleBar(activity);
        activity.setContentView(titleBar);
        Button button = createButton();

        uut = new ButtonPresenter(button, new IconResolverFake(activity));
        buttonController = new TitleBarButtonController(
                activity,
                uut,
                button,
                mock(TitleBarButtonCreator.class),
                mock(TitleBarButtonController.OnClickListener.class)
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

    private Button createButton() {
        Button b = new Button();
        b.id = "btn1";
        b.text = new Text("button");
        b.color = new Colour(Color.RED);
        b.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return b;
    }
}
