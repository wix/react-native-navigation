package com.reactnativenavigation.options.parsers;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.options.params.DontApplyColour;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ColorParseTest extends BaseTest {

    @Test
    public void nullIsParsedAsNoColor() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("color", "NoColor");
        assertThat(ColorParser.parse(null, json, "color")).isInstanceOf(DontApplyColour.class);
    }
}
