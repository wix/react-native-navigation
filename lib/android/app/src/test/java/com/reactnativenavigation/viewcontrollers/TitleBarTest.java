package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.views.TitleBar;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class TitleBarTest extends BaseTest {

    private TitleBar uut;
    private Button textButton;
    private Button customButton;

    @Override
    public void beforeEach() {
        final TopBarButtonCreatorMock buttonCreator = new TopBarButtonCreatorMock();
        final Activity activity = newActivity();
        createButtons();
        uut = spy(new TitleBar(activity, buttonCreator, (buttonId -> {})));
    }

    private void createButtons() {
        textButton = new Button();
        textButton.id = "textButton";
        textButton.title = new Text("Btn");

        customButton = new Button();
        customButton.id = "customBtn";
        customButton.component = new Text("com.rnn.customBtn");
    }

    @Test
    public void setButton_setsTextButton() {
        uut.setButtons(leftButton(), rightButtons(textButton));
        assertThat(uut.getMenu().getItem(0).getTitle()).isEqualTo(textButton.title.get());
    }

    @Test
    public void setButton_setsCustomButton() {
        uut.setButtons(leftButton(), rightButtons(customButton));
        ReactView btnView = (ReactView) uut.getMenu().getItem(0).getActionView();
        assertThat(btnView.getComponentName()).isEqualTo(customButton.component.get());
    }

    private List<Button> leftButton() {
        return Collections.singletonList(new Button());
    }

    private List<Button> rightButtons(Button... buttons) {
        return Arrays.asList(buttons);
    }
}
