package com.reactnativenavigation.e2e.androide2e;

import android.support.test.runner.*;

import org.junit.*;
import org.junit.runner.*;

import static android.support.test.InstrumentationRegistry.*;
import static org.assertj.core.api.Java6Assertions.*;

@RunWith(AndroidJUnit4.class)
public class EnvironmentTest {
    @Test
    public void instrumentationAndAssertJ() throws Exception {
        assertThat(getInstrumentation().getTargetContext().getPackageName()).isEqualTo("com.reactnativenavigation.e2e.androide2e");
    }
}
