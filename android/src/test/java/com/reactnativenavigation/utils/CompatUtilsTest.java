package com.reactnativenavigation.utils;

import com.reactnativenavigation.*;

import org.junit.*;

import static org.assertj.core.api.Java6Assertions.*;

public class CompatUtilsTest extends BaseRobolectricTest {

    @Test
    public void generateViewId() {
        assertThat(CompatUtils.generateViewId())
                .isPositive()
                .isNotEqualTo(CompatUtils.generateViewId());
    }
}
