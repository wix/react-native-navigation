package com.reactnativenavigation;

import android.app.*;
import android.support.v7.app.*;
import android.view.*;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.utils.ViewUtils;
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

    public void assertIsChild(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(ViewUtils.isChildOf(parent, child)).isTrue();
    }

    public void assertNotChildOf(ViewGroup parent, View child) {
        assertThat(parent).isNotNull();
        assertThat(child).isNotNull();
        assertThat(ViewUtils.isChildOf(parent, child)).isFalse();
    }

    protected void disablePushAnimation(ViewController... controllers) {
        for (ViewController controller : controllers) {
            controller.options.animations.push.enable = new Bool(false);
        }
    }
}
