package com.reactnativenavigation.views;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.R;
import com.reactnativenavigation.mocks.ImageLoaderMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarBackgroundViewController;
import com.reactnativenavigation.viewcontrollers.topbar.TopBarController;
import com.reactnativenavigation.views.topbar.TopBar;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarBackgroundComponentTest extends BaseTest {
    private TopBar uut;
    private TopBarBackgroundViewController topBarBackgroundViewController;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        TopBarButtonController.OnClickListener onClickListener = spy(new TopBarButtonController.OnClickListener() {
            @Override
            public void onPress(String buttonId) {
                Log.i("TopBarTest", "onPress: " + buttonId);
            }
        });
        Activity activity = newActivity();
        topBarBackgroundViewController = spy(new TopBarBackgroundViewController(activity, new TopBarBackgroundViewCreatorMock()));
        StackLayout parent = new StackLayout(activity, new TopBarButtonCreatorMock(), topBarBackgroundViewController, new TopBarController(), onClickListener, null);
        uut = new TopBar(activity, new TopBarButtonCreatorMock(), topBarBackgroundViewController, onClickListener, parent, ImageLoaderMock.mock());
        parent.addView(uut);
    }

    @Test
    public void setBackgroundComponent() {
        uut.getLayoutParams().height = 100;
        Component component = new Component();
        component.name = new Text("someComponent");
        component.componentId = new Text("id");
        uut.setBackgroundComponent(component);
        TopBarBackgroundView background = (TopBarBackgroundView) ViewUtils.findChildrenByClassRecursive(uut, TopBarBackgroundView.class).get(0);
        assertThat(background).isNotNull();
        assertThat(background.getLayoutParams().width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
        assertThat(background.getLayoutParams().height).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Test
    public void setBackgroundComponent_doesNotSetIfNoComponentIsDefined() {
        Component component = new Component();
        component.name = new Text("someComponent");
        component.componentId = new Text("id");
        uut.setBackgroundComponent(component);
        assertThat((View) uut.findViewById(R.id.topBarBackgroundComponent)).isNull();
    }

    @Test
    public void clear_componentIsDestroyed() {
        Component component = new Component();
        component.name = new Text("someComponent");
        component.componentId = new Text("id");
        uut.setBackgroundComponent(component);
        uut.clear();
        verify(topBarBackgroundViewController, times(1)).destroy();
    }
}
