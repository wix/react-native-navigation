package com.reactnativenavigation;

import android.app.*;
import android.support.v7.app.*;
import android.view.*;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.viewcontrollers.ViewController;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.*;
import org.robolectric.annotation.*;

import static org.assertj.core.api.Java6Assertions.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 27, application = TestApplication.class)
public abstract class BaseTest {
    @Before
    public void beforeEach() {
        //
    }

    @After
    public void afterEach() {
        //
    }

    public Activity newActivity() {
        return Robolectric.setupActivity(AppCompatActivity.class);
    }

    public void assertIsChildById(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(child.getId()).isNotZero().isPositive();
        assertThat(parent.findViewById(child.getId())).isNotNull().isEqualTo(child);
    }

    public void assertNotChildOf(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(child.getId()).isNotZero().isPositive();
        assertThat(parent.findViewById(child.getId())).isNull();
    }

    protected void disablePushAnimation(ViewController... controllers) {
        for (ViewController controller : controllers) {
            controller.options.animations.push.enable = new Bool(false);
        }
    }
}
