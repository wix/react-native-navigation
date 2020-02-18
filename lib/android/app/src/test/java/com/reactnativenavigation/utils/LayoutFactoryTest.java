package com.reactnativenavigation.utils;

import com.facebook.react.ReactInstanceManager;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.LayoutFactory;
import com.reactnativenavigation.parse.LayoutNode;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.ChildControllersRegistry;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class LayoutFactoryTest extends BaseTest {
    private LayoutFactory uut;

    @Override
    public void beforeEach() {
        uut = new LayoutFactory(mock(ReactInstanceManager.class));
        uut.init(
                newActivity(),
                Mockito.mock(EventEmitter.class),
                new ChildControllersRegistry(),
                new HashMap<>()
        );
    }

    @Test
    public void sanity() throws JSONException {
        assertThat(uut.create(component())).isNotNull();
    }

    @Test
    public void defaultOptionsAreNotNull() {
        assertThat(uut.getDefaultOptions()).isNotNull();
        boolean exceptionThrown = false;
        try {
            //noinspection ConstantConditions
            uut.setDefaultOptions(null);
        } catch (AssertionError exception) {
            exceptionThrown = true;
        }
        assertThat(exceptionThrown).isTrue();
    }

    private LayoutNode component() throws JSONException {
        return new LayoutNode("Component1", LayoutNode.Type.Component, new JSONObject().put("name", "com.component"), null);
    }
}
