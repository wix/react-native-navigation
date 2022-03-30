package com.reactnativenavigation.options.parsers;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.params.DontApplyColour;
import com.reactnativenavigation.options.params.ReactPlatformColor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.assertj.core.api.Java6Assertions.assertThat;

import android.app.Activity;

public class ColorParseTest extends BaseTest {

    Activity activity;
    @Override
    public void beforeEach() {
        super.beforeEach();
        activity = newActivity();
    }

    @Test
    public void nullIsParsedAsNoColor() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("color", "NoColor");
        assertThat(ColorParser.parse(null, json, "color")).isInstanceOf(DontApplyColour.class);
    }

    @Test
    public void shouldParsePlatformColors() throws JSONException {
        JSONObject color = new JSONObject();
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put("@color/colorPrimary");
        color.put("resource_paths",
                jsonArray);
        try (MockedStatic<Arguments> theMock = Mockito.mockStatic(Arguments.class)) {
            theMock.when(Arguments::createMap).thenReturn(new JavaOnlyMap());
            theMock.when(Arguments::createArray).thenReturn(new JavaOnlyArray());
            assertThat(ColorParser.parse(activity, color, "color")).isInstanceOf(ReactPlatformColor.class);
        }
    }
}
