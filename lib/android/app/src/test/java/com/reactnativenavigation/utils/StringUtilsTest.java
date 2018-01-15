package com.reactnativenavigation.utils;

import com.reactnativenavigation.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class StringUtilsTest extends BaseTest {
    @Test
    public void isEqual() throws Exception {
        assertThat(StringUtils.isEqual(null, "a")).isFalse();
        assertThat(StringUtils.isEqual("a", null)).isFalse();
        assertThat(StringUtils.isEqual("a", "b")).isFalse();
        assertThat(StringUtils.isEqual("a", "A")).isFalse();
        assertThat(StringUtils.isEqual("a", "a")).isTrue();
        assertThat(StringUtils.isEqual("", "")).isTrue();
    }
}
