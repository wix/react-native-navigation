package com.reactnativenavigation.viewcontrollers.externalcomponent;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.ExternalComponent;
import com.reactnativenavigation.options.Options;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.react.events.ComponentType;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;
import com.reactnativenavigation.viewcontrollers.viewcontroller.Presenter;
import com.reactnativenavigation.views.ExternalComponentLayout;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

@Ignore("New architecture - WIP")
public class ExternalComponentViewControllerTest extends BaseTest {
    private ExternalComponentViewController uut;
    private FragmentCreatorMock componentCreator;
    private Activity activity;
    private ExternalComponent ec;
    private EventEmitter emitter;
    private ChildControllersRegistry childRegistry;

    @Override
    public void beforeEach() {
        componentCreator = spy(new FragmentCreatorMock());
        activity = newActivity();
        ec = createExternalComponent();
        emitter = Mockito.mock(EventEmitter.class);
        childRegistry = new ChildControllersRegistry();
        uut = spy(new ExternalComponentViewController(activity,
                childRegistry,
                "fragmentId",
                new Presenter(activity, Options.EMPTY),
                ec,
                componentCreator,
                emitter,
                new ExternalComponentPresenter(),
                new Options())
        );
    }

    @Test
    public void createView_returnsFrameLayout() {
        ExternalComponentLayout view = uut.getView();
        assertThat(CoordinatorLayout.class.isAssignableFrom(view.getClass())).isTrue();
    }

    @Test
    public void createView_createsExternalComponent() {
        ExternalComponentLayout view = uut.getView();
        verify(componentCreator, times(1)).create((FragmentActivity) activity, ec.passProps);
        assertThat(view.getChildCount()).isGreaterThan(0);
    }

    @Test
    public void onViewAppeared_appearEventIsEmitted() {
        uut.onViewWillAppear();
        verify(emitter).emitComponentDidAppear(uut.getId(), ec.name.get(), ComponentType.Component);
    }

    @Test
    public void onViewDisappear_disappearEventIsEmitted() {
        uut.onViewDisappear();
        verify(emitter).emitComponentDidDisappear(uut.getId(), ec.name.get(), ComponentType.Component);
    }

    @Test
    public void registersInChildRegister() {
        uut.onViewWillAppear();
        assertThat(childRegistry.size()).isOne();
        uut.onViewDisappear();
        assertThat(childRegistry.size()).isZero();
    }

    private ExternalComponent createExternalComponent() {
        ExternalComponent component = new ExternalComponent();
        component.name = new Text("fragmentComponent");
        component.passProps = new JSONObject();
        return component;
    }
}
