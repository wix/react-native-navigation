package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.parse.params.DontApplyColour;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ColorParseTest extends BaseTest {

    @Test
    public void nullIsParsedAsNoColor() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("color", "NoColor");
        assertThat(ColorParser.parse(json, "color")).isInstanceOf(DontApplyColour.class);
    }
}
