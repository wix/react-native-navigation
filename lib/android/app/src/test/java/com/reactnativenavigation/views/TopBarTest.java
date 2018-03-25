package com.reactnativenavigation.views;

import android.util.Log;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.TitleBarHelper;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarTest extends BaseTest {

    private TopBar uut;
    private TopBarAnimator animator;
    private ArrayList<Button> leftButton;
    private ArrayList<Button> rightButtons;
    private TopBarButtonController.OnClickListener onClickListener;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        onClickListener = spy(new TopBarButtonController.OnClickListener() {
            @Override
            public void onPress(String buttonId) {
                Log.i("TopBarTest", "onPress: " + buttonId);
            }
        });
        StackLayout parent = new StackLayout(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), this.onClickListener);
        uut = new TopBar(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), this.onClickListener, parent);
        animator = spy(new TopBarAnimator(uut));
        uut.setAnimator(animator);
        leftButton = createLeftButton();
        rightButtons = createRightButtons();
        parent.addView(uut);
    }

    private ArrayList<Button> createLeftButton() {
        ArrayList<Button> result = new ArrayList<>();
        Button leftButton = new Button();
        leftButton.id = "leftButton";
        leftButton.title = new Text("");
        result.add(spy(leftButton));
        return result;
    }

    private ArrayList<Button> createRightButtons() {
        ArrayList<Button> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Button button = new Button();
            button.id = "rightButtons" + i;
            button.title = new Text("btn" + i);
            button.showAsAction = MenuItem.SHOW_AS_ACTION_ALWAYS;
            result.add(spy(button));
        }
        return result;
    }

    @Test
    public void title() throws Exception {
        assertThat(uut.getTitle()).isEmpty();
        uut.setTitle("new title");
        assertThat(uut.getTitle()).isEqualTo("new title");
    }

    @Test
    public void hide_animate() throws Exception {
        AnimationOptions options = new AnimationOptions();
        uut.hideAnimate(options);
        verify(animator, times(1)).hide(options, null);
    }

    @Test
    public void show_animate() throws Exception {
        AnimationOptions options = new AnimationOptions();
        uut.hide();
        uut.showAnimate(options);
        verify(animator, times(1)).show(options);
    }

    @Test
    public void button_TitleBarButtonOnClickInvoked() throws Exception {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons);
        for (int i = 0; i < rightButtons.size(); i++) {
            Button rightButton = rightButtons.get(i);
            TitleBarHelper.getRightButton(uut.getTitleBar(), i).callOnClick();
            verify(onClickListener, times(1)).onPress(rightButton.id);
        }
    }
}
