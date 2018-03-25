package com.reactnativenavigation.views;

import android.util.Log;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.R;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarBackgroundViewCreatorMock;
import com.reactnativenavigation.mocks.TopBarButtonCreatorMock;
import com.reactnativenavigation.parse.TopBarBackgroundOptions;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.react.ReactView;
import com.reactnativenavigation.viewcontrollers.TopBarButtonController;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;

public class TopBarBackgroundComponentTest extends BaseTest {
    private TopBar uut;

    @SuppressWarnings("Convert2Lambda")
    @Override
    public void beforeEach() {
        TopBarButtonController.OnClickListener onClickListener = spy(new TopBarButtonController.OnClickListener() {
            @Override
            public void onPress(String buttonId) {
                Log.i("TopBarTest", "onPress: " + buttonId);
            }
        });
        StackLayout parent = new StackLayout(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), onClickListener);
        uut = new TopBar(newActivity(), new TopBarButtonCreatorMock(), new TitleBarReactViewCreatorMock(), new TopBarBackgroundViewCreatorMock(), onClickListener, parent);
        parent.addView(uut);
    }

    @Test
    public void setBackgroundComponent() throws Exception {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        options.component = new Text("someComponent");
        uut.setBackgroundComponent(options);
        assertThat(ReactView.class.isAssignableFrom(uut.findViewById(R.id.topBarBackgroundComponent).getClass())).isTrue();
    }

    @Test
    public void setBackgroundComponent_doesNotSetIfNoComponentIsDefined() throws Exception {
        TopBarBackgroundOptions options = new TopBarBackgroundOptions();
        uut.setBackgroundComponent(options);
        assertThat(uut.findViewById(R.id.topBarBackgroundComponent)).isNull();
    }
}
