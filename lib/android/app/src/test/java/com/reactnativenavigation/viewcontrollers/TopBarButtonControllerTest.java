package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarButtonControllerTest extends BaseTest {

    private TopBarButtonController uut;
    private StackController stackController;

    @Override
    public void beforeEach() {
        Button button = createButton();
        final Activity activity = newActivity();

        TopBarButtonCreatorMock buttonCreatorMock = new TopBarButtonCreatorMock();
        uut = spy(new TopBarButtonController(activity, button, buttonCreatorMock, (buttonId) -> {}));
        stackController = spy(new StackController(activity, buttonCreatorMock, new TitleBarReactViewCreatorMock(), "stack", new Options()));

    }

    @Test
    public void buttonDoesNotClearStackOptionsOnAppear() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        verify(stackController, times(0)).clearOptions();
    }

    @Test @Ignore
    public void destroy_buttonIsDestroyedWhenStackIsDestroyed() throws Exception {
        uut.ensureViewIsCreated();
        uut.onViewAppeared();
        stackController.destroy();
        verify(uut, times(1)).destroy();
    }

    @NonNull
    private Button createButton() {
        Button button = new Button();
        button.id = "btnId";
        button.component = new Text("com.example.customBtn");
        return button;
    }
}
