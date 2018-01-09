package com.reactnativenavigation.e2e.androide2e;

import android.content.pm.*;
import android.graphics.*;
import android.support.test.runner.*;
import android.support.test.uiautomator.*;

import org.junit.*;
import org.junit.runner.*;

import java.io.*;

import static android.support.test.InstrumentationRegistry.*;
import static org.assertj.core.api.Java6Assertions.*;

@RunWith(AndroidJUnit4.class)
public abstract class BaseTest {
    static final String PACKAGE_NAME = "com.reactnativenavigation.playground";
    private static final long TIMEOUT = 60000;

    @Before
    public void beforeEach() throws Exception {
        device().wakeUp();
        device().setOrientationNatural();
        launchTheApp();
        assertMainShown();
    }

    @After
    public void afterEach() throws Exception {
        device().executeShellCommand("am force-stop " + PACKAGE_NAME);
        device().executeShellCommand("am kill " + PACKAGE_NAME);
    }

    public UiDevice device() {
        return UiDevice.getInstance(getInstrumentation());
    }

    public void launchTheApp() throws Exception {
        device().executeShellCommand("am start -n " + PACKAGE_NAME + "/.MainActivity");
        device().waitForIdle();
        acceptOverlayPermissionIfNeeded();
        device().wait(Until.gone(By.textContains("Please wait")), 1000 * 60 * 3);
    }

    public void assertMainShown() {
        assertExists(By.text("React Native Navigation!"));
    }

    public void acceptOverlayPermissionIfNeeded() throws Exception {
        if (isRequestingOverlayPermission()) {
            if (!elementByText("Playground").exists()) {
                scrollToText("Playground");
            }
            elementByText("Playground").click();
            device().findObject(new UiSelector().checkable(true).checked(false)).click();
            device().pressBack();
            device().pressBack();
        }
    }

    private boolean isRequestingOverlayPermission() {
        return device().wait(Until.hasObject(By.pkg("com.android.settings").depth(0)), 300);
    }

    public UiObject elementByText(String text) {
        return device().findObject(new UiSelector().text(text));
    }

    public UiObject elementByTextContains(String text) {
        return device().findObject(new UiSelector().textContains(text));
    }

    public void scrollToText(String txt) throws Exception {
        new UiScrollable(new UiSelector().scrollable(true)).scrollTextIntoView(txt);
    }

    public void assertExists(BySelector selector) {
        assertThat(device().wait(Until.hasObject(selector), TIMEOUT)).withFailMessage("expected %1$s to be visible", selector).isTrue();
        assertThat(device().findObject(selector).getVisibleCenter().x).isPositive().isLessThan(device().getDisplayWidth());
        assertThat(device().findObject(selector).getVisibleCenter().y).isPositive().isLessThan(device().getDisplayHeight());
    }

    public Bitmap captureScreenshot() throws Exception {
        File file = File.createTempFile("tmpE2E", "png");
        device().takeScreenshot(file);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        file.delete();
        return bitmap;
    }

    public void swipeOpenFromLeft() {
        int w = device().getDisplayWidth();
        int h = device().getDisplayHeight();
        device().swipe(5, h / 2, w / 2, h / 2, 10);
    }

    public void swipeOpenFromRight() {
        int w = device().getDisplayWidth();
        int h = device().getDisplayHeight();
        device().swipe(w - 5, h / 2, w / 2, h / 2, 10);
    }

    public void swipeUp() {
        int w = device().getDisplayWidth();
        int h = device().getDisplayHeight();
        device().drag(w / 2, h / 2 + 100, w / 2, h / 2 - 100, 10);
    }

    public boolean isDebug() throws Exception {
        PackageInfo packageInfo = getInstrumentation().getTargetContext().getPackageManager().getPackageInfo("com.reactnativenavigation.playground", 0);
        return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }
}
