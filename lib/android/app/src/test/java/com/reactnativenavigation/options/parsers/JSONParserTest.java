package com.reactnativenavigation.options.parsers;

import com.facebook.react.bridge.JavaOnlyArray;
import com.facebook.react.bridge.JavaOnlyMap;
import com.reactnativenavigation.BaseTest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class JSONParserTest extends BaseTest {
    @Test
    public void parsesMap() throws Exception {
        JavaOnlyMap input = new JavaOnlyMap();
        input.putString("keyString", "stringValue");
        input.putInt("keyInt", 123);
        input.putDouble("keyDouble", 123.456);
        input.putBoolean("keyBoolean", true);
        input.putArray("keyArray", new JavaOnlyArray());
        input.putMap("keyMap", new JavaOnlyMap());
        input.putNull("bla");

        JSONObject result = new JSONParser().parse(input);


        assertThat(result.keys()).containsOnly(
                "keyString",
                "keyInt",
                "keyDouble",
                "keyBoolean",
                "keyMap",
                "keyArray");

        assertThat(result.get("keyString")).isEqualTo("stringValue");
        assertThat(result.get("keyInt")).isEqualTo(123);
        assertThat(result.get("keyDouble")).isEqualTo(123.456);
        assertThat(result.get("keyBoolean")).isEqualTo(true);
        assertThat(result.getJSONObject("keyMap").keys()).isEmpty();
        assertThat(result.getJSONArray("keyArray").length()).isZero();
    }

    @Test
    public void parsesArrays() throws Exception {
        JavaOnlyArray input = new JavaOnlyArray();
        input.pushString("Hello");
        input.pushInt(123);
        input.pushDouble(123.456);
        input.pushBoolean(true);
        input.pushArray(new JavaOnlyArray());
        input.pushMap(new JavaOnlyMap());
        input.pushNull();

        JSONArray result = new JSONParser().parse(input);
        assertThat(result.length()).isEqualTo(6);
        assertThat(result.get(0)).isEqualTo("Hello");
        assertThat(result.get(1)).isEqualTo(123);
        assertThat(result.get(2)).isEqualTo(123.456);
        assertThat(result.get(3)).isEqualTo(true);
        assertThat(result.getJSONArray(4).length()).isZero();
        assertThat(result.getJSONObject(5).keys()).isEmpty();
    }
}
