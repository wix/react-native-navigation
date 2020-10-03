package com.reactnativenavigation.viewcontrollers.stack;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.options.ComponentOptions;
import com.reactnativenavigation.options.params.Text;
import com.reactnativenavigation.viewcontrollers.stack.topbar.title.TitleBarReactViewController;

import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TitleBarReactViewControllerTest extends BaseTest {

    private TitleBarReactViewController uut;
    private TitleBarReactViewCreatorMock viewCreator;
    private Activity activity;
    private ComponentOptions component;

    @Override
    public void beforeEach() {
        viewCreator = spy(new TitleBarReactViewCreatorMock());
        activity = newActivity();
        component = createComponent();
        uut = new TitleBarReactViewController(activity, viewCreator, component);
    }

    @Test
    public void createView() {
        uut.createView();
        verify(viewCreator).create(activity, component.componentId.get(), component.name.get());
    }

    private ComponentOptions createComponent() {
        ComponentOptions component = new ComponentOptions();
        component.componentId = new Text("compId");
        component.name = new Text("compName");
        return component;
    }
}
