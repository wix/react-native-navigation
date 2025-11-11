package com.reactnativenavigation.options.parsers;

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.reactnativenavigation.BaseRobolectricTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class BoolParserTest extends BaseRobolectricTest {

    @Test
    public void parse() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("value", true);
        assertThat(BoolParser.parse(json, "value").get()).isTrue();
    }

    @Test
    public void parseFirst() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("value1", true);
        json.put("value2", false);
        assertThat(BoolParser.parseFirst(json, "value1", "value2").get()).isTrue();
        assertThat(BoolParser.parseFirst(json, "value2", "value1").get()).isFalse();
    }
}
