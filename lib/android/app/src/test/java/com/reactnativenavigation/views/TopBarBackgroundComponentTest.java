package com.reactnativenavigation.views;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.R;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.utils.ViewUtils;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;
import com.reactnativenavigation.views.topbar.TopBar;
import com.reactnativenavigation.views.topbar.TopBarBackgroundView;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TopBarBackgroundComponentTest extends BaseTest {
    private TopBar uut;
    private TopBarBackgroundView backgroundView;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        TopBarButtonController.OnClickListener onClickListener = spy(new TopBarButtonController.OnClickListener() {
            @Override
            public void onPress(String buttonId) {
                Log.i("TopBarTest", "onPress: " + buttonId);
            }
        });
        TopBarBackgroundViewCreatorMock backgroundViewCreator = new TopBarBackgroundViewCreatorMock() {
            @Override
            public TopBarBackgroundView create(Activity activity, String componentId, String componentName) {
                backgroundView = spy(super.create(activity, componentId, componentName));
                return backgroundView;
            }
        };
        StackLayout parent = new StackLayout(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), backgroundViewCreator, onClickListener, null);
        uut = new TopBar(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), backgroundViewCreator, onClickListener, parent);
        parent.addView(uut);
    }

    @Test
    public void setBackgroundComponent() throws Exception {
        uut.getLayoutParams().height = 100;
        uut.setBackgroundComponent(new Text("someComponent"));
        TopBarBackgroundView background = (TopBarBackgroundView) ViewUtils.findChildrenByClassRecursive(uut, TopBarBackgroundView.class).get(0);
        assertThat(background).isNotNull();
        assertThat(background.getLayoutParams().width).isEqualTo(ViewGroup.LayoutParams.MATCH_PARENT);
        assertThat(background.getLayoutParams().height).isEqualTo(uut.getHeight());
    }

    @Test
    public void setBackgroundComponent_doesNotSetIfNoComponentIsDefined() throws Exception {
        uut.setBackgroundComponent(new Text("someComponent"));
        assertThat(uut.findViewById(R.id.topBarBackgroundComponent)).isNull();
    }

    @Test
    public void clear_componentIsDestroyed() throws Exception {
        uut.setBackgroundComponent(new Text("someComponent"));
        uut.clear();
        verify(backgroundView, times(1)).destroy();
        assertThat(backgroundView.getParent()).isNull();
    }
}
