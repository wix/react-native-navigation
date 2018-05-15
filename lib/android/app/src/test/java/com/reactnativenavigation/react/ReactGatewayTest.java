package com.reactnativenavigation.react;

import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

public class ReactGatewayTest extends BaseTest {

    @Override
    public void beforeEach() {
        super.beforeEach();
    }

    /**
     * This is to make sure that the constructor that takes a host remains public as per
     * reasons described on this ticket:
     * https://github.com/wix/react-native-navigation/issues/3145
     * Exception comes from SoLoader.init so we simply ignore it as it's not related to this test
     */
    @Test(expected = RuntimeException.class)
    public void testPublicConstructor() {
        NavigationReactNativeHost host = new NavigationReactNativeHost(RuntimeEnvironment.application, false, null);
        new ReactGateway(RuntimeEnvironment.application, false, host);
    }

}