package com.reactnativenavigation.views;

import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.anim.TopBarAnimator;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.AnimationOptions;
import com.reactnativenavigation.parse.params.Button;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.TitleBarHelper;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;

import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarTest extends BaseTest {

    private TopBar uut;
    private TopBarAnimator animator;
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
        Activity activity = newActivity();
        TopBarBackgroundViewController topBarBackgroundViewController = new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock());
        StackLayout parent = new StackLayout(activity, new TopBarButtonCreatorMock(), topBarBackgroundViewController, new TopBarController(), this.onClickListener, null);
        uut = new TopBar(activity, new TopBarButtonCreatorMock(), topBarBackgroundViewController, this.onClickListener, parent, ImageLoaderMock.mock());
        animator = spy(new TopBarAnimator(uut));
        uut.setAnimator(animator);
        rightButtons = createRightButtons();
        parent.addView(uut);
    }

    private ArrayList<Button> createRightButtons() {
        ArrayList<Button> result = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Button button = new Button();
            button.id = "rightButtons" + i;
            button.text = new Text("btn" + i);
            button.showAsAction = new Number(MenuItem.SHOW_AS_ACTION_ALWAYS);
            result.add(spy(button));
        }
        return result;
    }

    @Test
    public void title() {
        assertThat(uut.getTitle()).isEmpty();
        uut.setTitle("new title");
        assertThat(uut.getTitle()).isEqualTo("new title");
    }

    @Test
    public void hide_animate() {
        AnimationOptions options = new AnimationOptions();
        uut.hideAnimate(options);
        verify(animator, times(1)).hide(eq(options), any());
    }

    @Test
    public void show_animate() {
        AnimationOptions options = new AnimationOptions();
        uut.hide();
        uut.showAnimate(options);
        verify(animator, times(1)).show(options);
    }

    @Test
    public void button_TitleBarButtonOnClickInvoked() {
        uut.setLeftButtons(new ArrayList<>());
        uut.setRightButtons(rightButtons);
        for (int i = 0; i < rightButtons.size(); i++) {
            Button rightButton = rightButtons.get(i);
            TitleBarHelper.getRightButton(uut.getTitleBar(), i).callOnClick();
            verify(onClickListener, times(1)).onPress(rightButton.id);
        }
    }
}
