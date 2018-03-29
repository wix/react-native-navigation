package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Alignment;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.views.titlebar.TitleBar;
import com.reactnativenavigation.views.titlebar.TitleBarReactView;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
        createButtons();
        buttonControllers = new HashMap<>();
        TitleBarReactViewCreatorMock reactViewCreator = new TitleBarReactViewCreatorMock();
        uut = spy(new TitleBar(activity, buttonCreator, reactViewCreator, (buttonId -> {})) {
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
        uut.setRightButtons(rightButtons(textButton));
        uut.setLeftButtons(leftButton(leftButton));
        assertThat(uut.getMenu().getItem(0).getTitle()).isEqualTo(textButton.title.get());
    }

    @Test
    public void setButton_setsCustomButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton));
        ReactView btnView = (ReactView) uut.getMenu().getItem(0).getActionView();
        assertThat(btnView.getComponentName()).isEqualTo(customButton.component.get());
    }

    @Test
    public void destroy_destroysButtonControllers() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton, textButton));
        uut.clear();
        for (TopBarButtonController controller : buttonControllers.values()) {
            verify(controller, times(1)).destroy();
        }
    }

    @Test
    public void setRightButtons_destroysRightButtons() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton));
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(textButton));
        verify(buttonControllers.get(customButton.id), times(1)).destroy();
    }

    @Test
    public void setRightButtons_onlyDestroysRightButtons() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton));
        uut.setLeftButtons(null);
        uut.setRightButtons(rightButtons(textButton));
        verify(buttonControllers.get(leftButton.id), times(0)).destroy();
    }

    @Test
    public void setRightButtons_emptyButtonsListClearsRightButtons() {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(customButton, textButton));
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(new ArrayList<>());
        for (TopBarButtonController controller : buttonControllers.values()) {
            verify(controller, times(1)).destroy();
        }
        assertThat(uut.getMenu().size()).isEqualTo(0);
    }

    @Test
    public void setLeftButtons_emptyButtonsListClearsLeftButton() {
        uut.setLeftButtons(leftButton(leftButton));
        uut.setRightButtons(rightButtons(customButton));
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(textButton));
        verify(buttonControllers.get(leftButton.id), times(1)).destroy();
    }

    @Test
    public void setRightButtons_buttonsAreAddedInReverseOrderToMatchOrderOnIOs() {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons(textButton, customButton));
        assertThat(uut.getMenu().getItem(1).getTitle()).isEqualTo(textButton.title.get());
    }

    @Test
    public void setComponent_addsComponentToTitleBar() {
        uut.setComponent("com.rnn.CustomView", Alignment.Center);
        verify(uut, times(1)).addView(any(TitleBarReactView.class), any(Toolbar.LayoutParams.class));
    }

    @Test
    public void setComponent_alignFill() {
        uut.setComponent("com.rnn.CustomView", Alignment.Fill);
        verify(uut, times(1)).getComponentLayoutParams(Alignment.Fill);
        ArgumentCaptor<Toolbar.LayoutParams> lpCaptor = ArgumentCaptor.forClass(Toolbar.LayoutParams.class);
        verify(uut, times(1)).addView(any(TitleBarReactView.class), lpCaptor.capture());
        assertThat(lpCaptor.getValue().width == ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Test
    public void setComponent_alignCenter() {
        uut.setComponent("com.rnn.CustomView", Alignment.Center);
        verify(uut, times(1)).getComponentLayoutParams(Alignment.Center);
        ArgumentCaptor<Toolbar.LayoutParams> lpCaptor = ArgumentCaptor.forClass(Toolbar.LayoutParams.class);
        verify(uut, times(1)).addView(any(TitleBarReactView.class), lpCaptor.capture());
        assertThat(lpCaptor.getValue().width == ViewGroup.LayoutParams.WRAP_CONTENT);
        assertThat(lpCaptor.getValue().gravity == Gravity.CENTER);
    }

    @Test
    public void clear() {
        uut.setComponent("someComponent", Alignment.Center);
        uut.clear();
        assertThat(uut.getTitle()).isNullOrEmpty();
        assertThat(uut.getMenu().size()).isZero();
        assertThat(uut.getNavigationIcon()).isNull();
        assertThat(ViewUtils.findChildrenByClassRecursive(uut, TitleBarReactView.class).size()).isZero();
    }

    private List<Button> leftButton(Button leftButton) {
        return Collections.singletonList(leftButton);
    }

    private List<Button> rightButtons(Button... buttons) {
        return Arrays.asList(buttons);
    }
}
