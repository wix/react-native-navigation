package com.reactnativenavigation.utils;

import com.facebook.react.ReactHost;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.LayoutFactory;
import com.reactnativenavigation.options.LayoutNode;
import com.reactnativenavigation.react.events.EventEmitter;
import com.reactnativenavigation.viewcontrollers.child.ChildControllersRegistry;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Ignore("New architecture - WIP")
public class LayoutFactoryTest extends BaseTest {
    private LayoutFactory uut;
    private ReactHost reactHost;

    @Override
    public void beforeEach() {
        super.beforeEach();
        reactHost = mock(ReactHost.class);
        uut = new LayoutFactory(reactHost);
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
    public void shouldParseOptionsWhenReactContextIsNull() {
        when(reactHost.getCurrentReactContext()).thenReturn(null);
        try {
            uut.create(component());
        } catch (Exception e) {
            fail("Create should not fail! when react instance has null context");
        }
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
        final JSONObject component = new JSONObject();
        final JSONObject layout = new JSONObject();
        final JSONObject backgroundColor = new JSONObject();
        backgroundColor.put("dark",0);
        backgroundColor.put("light",1);
        layout.put("backgroundColor",backgroundColor );
        component.put("name", "com.component");
        component.put("options",new JSONObject().put("layout", layout));
        return new LayoutNode("Component1", LayoutNode.Type.Component, component, null);
    }
}
