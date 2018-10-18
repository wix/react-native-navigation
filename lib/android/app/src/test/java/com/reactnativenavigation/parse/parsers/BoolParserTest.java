package com.reactnativenavigation.parse.parsers;

import com.reactnativenavigation.BaseTest;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class BoolParserTest extends BaseTest {

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
