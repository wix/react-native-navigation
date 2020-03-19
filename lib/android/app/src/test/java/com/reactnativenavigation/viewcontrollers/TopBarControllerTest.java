package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.utils.CollectionUtils;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.reactnativenavigation.utils.TitleBarHelper.createButtonController;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class TopBarControllerTest extends BaseTest {
    private TopBarController uut;
    private Activity activity;
    private Button leftButton;
    private Button textButton;
    private Button customButton;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = new TopBarController();
        StackLayout stack = Mockito.mock(StackLayout.class);
        uut.createView(activity, stack);

        createButtons();
    }

    @Test
    public void setButton_setsTextButton() {
        uut.setRightButtons(rightButtons(textButton), Collections.EMPTY_LIST);
        uut.setLeftButtons(leftButton(leftButton));
        assertThat(uut.getRightButton(0).getTitle()).isEqualTo(textButton.text.get());
    }

    @Test
    public void setButton_setsCustomButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton), Collections.EMPTY_LIST);
        ReactView btnView = (ReactView) uut.getRightButton(0).getActionView();
        assertThat(btnView.getComponentName()).isEqualTo(customButton.component.name.get());
    }

    @Test
    public void setRightButtons_emptyButtonsListClearsRightButtons() {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(customButton, textButton), Collections.EMPTY_LIST);
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(new ArrayList<>(), Collections.EMPTY_LIST);
        assertThat(uut.getRightButtonsCount()).isEqualTo(0);
    }

    @Test
    public void setLeftButtons_emptyButtonsListClearsLeftButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton), Collections.EMPTY_LIST);
        assertThat(uut.getLeftButton()).isNotNull();

        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(textButton), Collections.EMPTY_LIST);
        assertThat(uut.getLeftButton()).isNull();
    }

    @Test
    public void setRightButtons_buttonsAreAddedInReverseOrderToMatchOrderOnIOs() {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(textButton, customButton), Collections.EMPTY_LIST);
        assertThat(uut.getRightButton(1).getTitle()).isEqualTo(textButton.text.get());
    }

    private void createButtons() {
        leftButton = new Button();
        leftButton.id = Constants.BACK_BUTTON_ID;

        textButton = new Button();
        textButton.id = "textButton";
        textButton.text = new Text("Btn");

        customButton = new Button();
        customButton.id = "customBtn";
        customButton.component.name = new Text("com.rnn.customBtn");
        customButton.component.componentId = new Text("component4");
    }

    private List<TitleBarButtonController> leftButton(Button leftButton) {
        return Collections.singletonList(createButtonController(activity, leftButton));
    }

    private List<TitleBarButtonController> rightButtons(Button... buttons) {
        return CollectionUtils.map(Arrays.asList(buttons), button -> createButtonController(activity, button));
    }
}
