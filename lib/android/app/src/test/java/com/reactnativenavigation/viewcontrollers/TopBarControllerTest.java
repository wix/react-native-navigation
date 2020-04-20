package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.Constants;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.StackLayout;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.reactnativenavigation.utils.CollectionUtils.*;
import static com.reactnativenavigation.utils.TitleBarHelper.createButtonController;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class TopBarControllerTest extends BaseTest {
    private TopBarController uut;
    private Activity activity;
    private Button leftButton;
    private Button textButton1;
    private Button textButton2;
    private Button componentButton;

    @Override
    public void beforeEach() {
        activity = newActivity();
        uut = spy(new TopBarController());
        StackLayout stack = Mockito.mock(StackLayout.class);
        uut.createView(activity, stack);

        createButtons();
    }

    @Test
    public void setButton_setsTextButton() {
        uut.applyRightButtons(rightButtons(textButton1));
        uut.setLeftButtons(leftButton(leftButton));
        assertThat(uut.getRightButton(0).getTitle().toString()).isEqualTo(textButton1.text.get());
    }

    @Test
    public void setButton_setsCustomButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.applyRightButtons(rightButtons(componentButton));
        ReactView btnView = (ReactView) uut.getRightButton(0).getActionView();
        assertThat(btnView.getComponentName()).isEqualTo(componentButton.component.name.get());
    }

    @Test
    public void applyRightButtons_emptyButtonsListClearsRightButtons() {
        uut.setLeftButtons(new ArrayList<>());
        uut.applyRightButtons(rightButtons(componentButton, textButton1));
        uut.setLeftButtons(new ArrayList<>());
        uut.applyRightButtons(new ArrayList<>());
        assertThat(uut.getRightButtonsCount()).isEqualTo(0);
    }

    @Test
    public void applyRightButtons_previousButtonsAreCleared() {
        uut.applyRightButtons(rightButtons(textButton1, componentButton));
        assertThat(uut.getRightButtonsCount()).isEqualTo(2);

        uut.applyRightButtons(rightButtons(textButton2));
        assertThat(uut.getRightButtonsCount()).isEqualTo(1);
    }

    @Test
    public void applyRightButtons_buttonsAreAddedInReverseOrderToMatchOrderOnIOs() {
        uut.setLeftButtons(new ArrayList<>());
        uut.applyRightButtons(rightButtons(textButton1, componentButton));
        assertThat(uut.getRightButton(1).getTitle().toString()).isEqualTo(textButton1.text.get());
    }

    @Test
    public void applyRightButtons_componentButtonIsReapplied() {
        List<TitleBarButtonController> initialButtons = rightButtons(componentButton);
        uut.applyRightButtons(initialButtons);
        assertThat(uut.getRightButton(0).getItemId()).isEqualTo(componentButton.getIntId());

        uut.applyRightButtons(rightButtons(textButton1));
        assertThat(uut.getRightButton(0).getItemId()).isEqualTo(textButton1.getIntId());

        uut.applyRightButtons(initialButtons);
        assertThat(uut.getRightButton(0).getItemId()).isEqualTo(componentButton.getIntId());
    }

    @Test
    public void mergeRightButtons_componentButtonIsNotAddedIfAlreadyAddedToMenu() {
        List<TitleBarButtonController> initialButtons = rightButtons(componentButton);
        uut.applyRightButtons(initialButtons);

        uut.mergeRightButtons(initialButtons, Collections.EMPTY_LIST);

    }

    @Test
    public void setLeftButtons_emptyButtonsListClearsLeftButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.applyRightButtons(rightButtons(componentButton));
        assertThat(uut.getLeftButton()).isNotNull();

        uut.setLeftButtons(new ArrayList<>());
        uut.applyRightButtons(rightButtons(textButton1));
        assertThat(uut.getLeftButton()).isNull();
    }

    private void createButtons() {
        leftButton = new Button();
        leftButton.id = Constants.BACK_BUTTON_ID;

        textButton1 = createTextButton("1");
        textButton2 = createTextButton("2");

        componentButton = new Button();
        componentButton.id = "customBtn";
        componentButton.component.name = new Text("com.rnn.customBtn");
        componentButton.component.componentId = new Text("component4");
    }

    private Button createTextButton(String id) {
        Button button = new Button();
        button.id = id;
        button.text = new Text("txt" + id);
        return button;
    }

    private List<TitleBarButtonController> leftButton(Button leftButton) {
        return Collections.singletonList(createButtonController(activity, leftButton));
    }

    private List<TitleBarButtonController> rightButtons(Button... buttons) {
        return map(Arrays.asList(buttons), button -> createButtonController(activity, button));
    }
}
