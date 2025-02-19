package com.reactnativenavigation.utils;

import com.reactnativenavigation.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

@Ignore("New architecture - WIP")
public class CompatUtilsTest extends BaseTest {

    @Test
    public void generateViewId() throws Exception {
        assertThat(CompatUtils.generateViewId())
                .isPositive()
                .isNotEqualTo(CompatUtils.generateViewId());
    }
}
