package com.reactnativenavigation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
public class EnvironmentTest {
    @Test
    public void assertJ() {
        assertThat(1 + 2).isEqualTo(3).isGreaterThan(2).isLessThan(4).isNotNegative().isPositive().isNotZero();
    }
}
