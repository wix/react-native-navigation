package com.reactnativenavigation.viewcontrollers;

import android.app.Activity;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.TitleBarReactViewCreatorMock;
import com.reactnativenavigation.parse.Component;
import com.reactnativenavigation.parse.params.Text;

import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TitleBarReactViewControllerTest extends BaseTest {

    private TitleBarReactViewController uut;
    private TitleBarReactViewCreatorMock viewCreator;
    private Activity activity;
    private Component component;

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

    private Component createComponent() {
        Component component = new Component();
        component.componentId = new Text("compId");
        component.name = new Text("compName");
        return component;
    }
}
