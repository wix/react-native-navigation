package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.TitleOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TitleBarTest extends BaseTest {

    private TitleBar uut;
    private Button leftButton;
    private Button textButton;
    private Button customButton;
    private Map<String, TopBarButtonController> buttonControllers;

    @Override
    public void beforeEach() {
        final TopBarButtonCreatorMock buttonCreator = new TopBarButtonCreatorMock();
        final Activity activity = newActivity();
        TitleBarReactViewController reactViewController = new TitleBarReactViewController(activity, new TitleBarReactViewCreatorMock());
        createButtons();
        buttonControllers = new HashMap<>();
        uut = spy(new TitleBar(activity, buttonCreator, reactViewController, (buttonId -> {})) {
            @Override
            public TopBarButtonController createButtonController(Button button) {
                TopBarButtonController controller = spy(super.createButtonController(button));
                buttonControllers.put(button.id, controller);
                return controller;
            }
        });
    }

    private void createButtons() {
        leftButton = new Button();
        leftButton.id = "back";
        leftButton.title = new Text("jfjf");

        textButton = new Button();
        textButton.id = "textButton";
        textButton.title = new Text("Btn");

        customButton = new Button();
        customButton.id = "customBtn";
        customButton.component = new Text("com.rnn.customBtn");
    }

    @Test
    public void setButton_setsTextButton() {
        uut.setButtons(leftButton(leftButton), rightButtons(textButton));
        assertThat(uut.getMenu().getItem(0).getTitle()).isEqualTo(textButton.title.get());
    }

    @Test
    public void setButton_setsCustomButton() {
        uut.setButtons(leftButton(leftButton), rightButtons(customButton));
        ReactView btnView = (ReactView) uut.getMenu().getItem(0).getActionView();
        assertThat(btnView.getComponentName()).isEqualTo(customButton.component.get());
    }

    @Test
    public void destroy_destroysButtonControllers() throws Exception {
        uut.setButtons(leftButton(leftButton), rightButtons(customButton, textButton));
        uut.clear();
        for (TopBarButtonController controller : buttonControllers.values()) {
            verify(controller, times(1)).destroy();
        }
    }

    @Test
    public void setRightButtons_destroysRightButtons() throws Exception {
        uut.setButtons(leftButton(leftButton), rightButtons(customButton));
        uut.setButtons(leftButton(leftButton), rightButtons(textButton));
        verify(buttonControllers.get(customButton.id), times(1)).destroy();
    }

    @Test
    public void setRightButtons_onlyDestroysRightButtons() throws Exception {
        uut.setButtons(leftButton(leftButton), rightButtons(customButton));
        uut.setButtons(null, rightButtons(textButton));
        verify(buttonControllers.get(leftButton.id), times(0)).destroy();
    }

    @Test
    public void setRightButtons_emptyButtonsListClearsRightButtons() throws Exception {
        uut.setButtons(new ArrayList<>(), rightButtons(customButton, textButton));
        uut.setButtons(new ArrayList<>(), new ArrayList<>());
        for (TopBarButtonController controller : buttonControllers.values()) {
            verify(controller, times(1)).destroy();
        }
        assertThat(uut.getMenu().size()).isEqualTo(0);
    }

    @Test
    public void setLeftButtons_emptyButtonsListClearsLeftButton() throws Exception {
        uut.setButtons(leftButton(leftButton), rightButtons(customButton));
        uut.setButtons(new ArrayList<>(), rightButtons(textButton));
        verify(buttonControllers.get(leftButton.id), times(1)).destroy();
    }

    @Test
    public void setRightButtons_buttonsAreAddedInReverseOrderToMatchOrderOnIOs() throws Exception {
        uut.setButtons(new ArrayList<>(), rightButtons(textButton, customButton));
        assertThat(uut.getMenu().getItem(1).getTitle()).isEqualTo(textButton.title.get());
    }

    @Test
    public void setComponent_addsComponentToTitleBar() {
        uut.setComponent("com.rnn.CustomView", TitleOptions.Alignment.Center);
        final int height = uut.getHeight();
        verify(uut, times(1)).addView(any(TitleBarReactView.class), eq(ViewGroup.LayoutParams.WRAP_CONTENT), eq(height));
    }

    private List<Button> leftButton(Button leftButton) {
        return Collections.singletonList(leftButton);
    }

    private List<Button> rightButtons(Button... buttons) {
        return Arrays.asList(buttons);
    }
}
