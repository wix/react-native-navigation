package com.reactnativenavigation.react;

import com.facebook.react.uimanager.ThemedReactContext;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.mocks.SimpleViewController;
import com.reactnativenavigation.views.element.Element;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Shadows;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ElementViewManagerTest extends BaseTest {
    private ElementViewManager uut;
    private Element view;
    private ThemedReactContext reactContext = Mockito.mock(ThemedReactContext.class);
    private SimpleViewController.SimpleView parentView;

    @Override
    public void beforeEach() {
        view = new Element(newActivity());
        uut = new ElementViewManager() {
            @Override
            public Element createView(ThemedReactContext reactContext) {
                return view;
            }
        };
        parentView = Mockito.mock(SimpleViewController.SimpleView.class);
        Shadows.shadowOf(view).setMyParent(parentView);
    }

    @Test
    public void createViewInstance() {
        Element element = uut.createViewInstance(reactContext);
        assertThat(element).isNotNull();
    }

    @Test
    public void createViewInstance_registersInParentReactView() {
        ElementViewManager spy = spy(uut);

        parentView.addView(view);
        spy.createViewInstance(reactContext);

        dispatchPreDraw(view);
        dispatchOnGlobalLayout(view);
        verify(parentView).registerElement(view);
    }

    @Test
    public void onDropViewInstance() {
        uut.onDropViewInstance(view);
        verify(parentView).unregisterElement(view);
    }
}
