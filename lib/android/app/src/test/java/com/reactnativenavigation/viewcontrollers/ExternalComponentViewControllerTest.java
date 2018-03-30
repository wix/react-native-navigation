package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.ExternalComponent;
import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.parse.params.Text;
import com.reactnativenavigation.viewcontrollers.externalcomponent.ExternalComponentViewController;
import com.reactnativenavigation.viewcontrollers.externalcomponent.FragmentCreatorMock;
import com.reactnativenavigation.views.ExternalComponentLayout;

import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ExternalComponentViewControllerTest extends BaseTest {
    private ExternalComponentViewController uut;
    private FragmentCreatorMock componentCreator;
    private Activity activity;
    private ExternalComponent ec;
    private ReactInstanceManager reactInstanceManager;

    @Override
    public void beforeEach() {
        componentCreator = spy(new FragmentCreatorMock());
        activity = newActivity();
        ec = createExternalComponent();
        reactInstanceManager = Mockito.mock(ReactInstanceManager.class);
        uut = spy(new ExternalComponentViewController(activity,
                "fragmentId",
                ec,
                componentCreator,
                reactInstanceManager,
                new Options())
        );
    }

    private ExternalComponent createExternalComponent() {
        ExternalComponent component = new ExternalComponent();
        component.name = new Text("fragmentComponent");
        component.passProps = new JSONObject();
        return component;
    }

    @Test
    public void createView_returnsFrameLayout() {
        ExternalComponentLayout view = uut.getView();
        assertThat(FrameLayout.class.isAssignableFrom(view.getClass())).isTrue();
    }

    @Test
    public void createView_createsExternalComponent() {
        ExternalComponentLayout view = uut.getView();
        verify(componentCreator, times(1)).create((FragmentActivity) activity, reactInstanceManager, ec.passProps);
        assertThat(view.getChildCount()).isGreaterThan(0);
    }
}
