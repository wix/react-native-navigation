package com.reactnativenavigation.react;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.NavigationActivity;
import com.reactnativenavigation.parse.LayoutFactory;
import com.reactnativenavigation.parse.LayoutNode;
import com.reactnativenavigation.parse.parsers.JSONParser;
import com.reactnativenavigation.viewcontrollers.ViewController;
import com.reactnativenavigation.viewcontrollers.navigator.Navigator;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NavigationModuleTest extends BaseTest {
    private NavigationModule uut;
    private Navigator navigator;
    private JSONParser jsonParser;
    private NavigationActivity activity;
    private ReactApplicationContext reactApplicationContext;
    private LayoutFactory layoutFactory;

    @Override
    public void beforeEach() {
        jsonParser = mock(JSONParser.class);
        navigator = mock(Navigator.class);
        activity = mockActivity();
        reactApplicationContext = mock(ReactApplicationContext.class);
        layoutFactory = mock(LayoutFactory.class);

        uut = spy(new NavigationModule(
                reactApplicationContext,
                mock(ReactInstanceManager.class),
                jsonParser,
                layoutFactory
        ));
    }

    @Test
    public void setRoot_delegatesToNavigator() throws JSONException {
        when(reactApplicationContext.getCurrentActivity()).thenReturn(activity);
        ReadableMap root = mock(ReadableMap.class);
        when(jsonParser.parse(root)).thenReturn(rootJson());
        ViewController rootViewController = mock(ViewController.class);
        when(layoutFactory.create(any(LayoutNode.class))).thenReturn(rootViewController);

        uut.setRoot("1", root, mock(Promise.class));
        verify(navigator).setRoot(eq(rootViewController), any(), any());
    }

    private NavigationActivity mockActivity() {
        NavigationActivity activity = mock(NavigationActivity.class);
        when(activity.getNavigator()).thenReturn(navigator);
        return activity;
    }

    private JSONObject rootJson() throws JSONException {
        JSONObject root = new JSONObject();
        root.put("root", componentJson());
        return root;
    }

    private JSONObject componentJson() throws JSONException {
        JSONObject component = new JSONObject();
        component.put("id", "Component1");
        component.put("type", "Component");
        component.put("data", new JSONObject().put("name", "mockComponent"));
        return component;
    }
}
