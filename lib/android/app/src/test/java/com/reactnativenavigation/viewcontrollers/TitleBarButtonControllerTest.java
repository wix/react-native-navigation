package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TitleBarButtonCreatorMock;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ButtonPresenter;
import com.reactnativenavigation.viewcontrollers.button.IconResolver;
import com.reactnativenavigation.views.titlebar.TitleBar;

import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class TitleBarButtonControllerTest extends BaseTest {
    private TitleBarButtonController uut;
    private TitleBar titleBar;

    @Override
    public void beforeEach() {
        Activity activity = newActivity();
        titleBar = new TitleBar(activity);

        Button button = createComponentButton();
        uut = new TitleBarButtonController(
                activity,
                new ButtonPresenter(button, new IconResolver(activity, ImageLoaderMock.mock())),
                button,
                new TitleBarButtonCreatorMock(),
                Mockito.mock(TitleBarButtonController.OnClickListener.class)
        );
    }

    @Test
    public void addToMenu_componentButtonIsNotRecreatedIfAlreadyAddedWithSameOrder() {
        uut.addToMenu(titleBar, 0);
        MenuItem first = titleBar.getRightButton(0);

        uut.addToMenu(titleBar, 0);
        MenuItem second = titleBar.getRightButton(0);
        assertThat(first).isEqualTo(second);

        uut.addToMenu(titleBar, 1);
        MenuItem third = titleBar.getRightButton(0);
        assertThat(third).isNotEqualTo(second);
    }

    private Button createComponentButton() {
        Button componentButton = new Button();
        componentButton.id = "customBtn";
        componentButton.component.name = new Text("com.rnn.customBtn");
        componentButton.component.componentId = new Text("component4");
        return componentButton;
    }
}
