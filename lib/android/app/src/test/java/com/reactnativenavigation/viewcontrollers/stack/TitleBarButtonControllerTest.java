package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.options.ButtonOptions;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonController;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.stack.topbar.button.IconResolver;
import com.reactnativenavigation.views.stack.topbar.titlebar.ButtonBar;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TitleBarButtonControllerTest extends BaseTest {
    private ButtonController uut;
    private ButtonBar titleBar;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        titleBar = new ButtonBar(activity);

        ButtonOptions button = createComponentButton();
        uut = new ButtonController(
                activity,
                new ButtonPresenter(activity, button, new IconResolver(activity, ImageLoaderMock.mock())),
                button,
                new TitleBarButtonCreatorMock(),
                Mockito.mock(ButtonController.OnClickListener.class)
        );
    }

    @Test
    public void addToMenu_componentButtonIsNotRecreatedIfAlreadyAddedWithSameOrder() {
        uut.addToMenu(titleBar, 0);
        MenuItem first = titleBar.getButton(0);

        uut.addToMenu(titleBar, 0);
        MenuItem second = titleBar.getButton(0);
        assertThat(first).isEqualTo(second);

        uut.addToMenu(titleBar, 1);
        MenuItem third = titleBar.getButton(0);
        assertThat(third).isNotEqualTo(second);
    }

    private ButtonOptions createComponentButton() {
        ButtonOptions componentButton = new ButtonOptions();
        componentButton.id = "customBtn";
        componentButton.component.name = new Text("com.rnn.customBtn");
        componentButton.component.componentId = new Text("component4");
        return componentButton;
    }
}
