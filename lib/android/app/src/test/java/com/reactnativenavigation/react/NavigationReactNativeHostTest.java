package com.reactnativenavigation.react;

import com.facebook.react.*;
import com.facebook.react.shell.*;
import com.reactnativenavigation.*;

import org.junit.*;
import org.robolectric.*;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.Mockito.*;

public class NavigationReactNativeHostTest extends BaseTest {

    @Test
    public void getPackagesDefaults() {
        NavigationReactNativeHost uut = new NavigationReactNativeHost(RuntimeEnvironment.application, false, null);
        assertThat(uut.getPackages()).hasSize(2).extracting("class").containsOnly(MainReactPackage.class, NavigationPackage.class);
    }

    @Test
    public void getPackagesAddsAdditionalPackages() {
        ReactPackage myPackage = mock(ReactPackage.class);
        NavigationReactNativeHost uut = new NavigationReactNativeHost(RuntimeEnvironment.application, false, Collections.singletonList(myPackage));
        assertThat(uut.getPackages()).hasSize(3).containsOnlyOnce(myPackage);
    }
}