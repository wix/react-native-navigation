package com.reactnativenavigation.react;

import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.reactnativenavigation.BaseTest;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class NavigationReactNativeHostTest extends BaseTest {

	@Test
	public void getPackagesDefaults() throws Exception {
		NavigationReactNativeHost uut = new NavigationReactNativeHost(RuntimeEnvironment.application, false, null);
		assertThat(uut.getPackages()).hasSize(2).extracting("class").containsOnly(MainReactPackage.class, NavigationPackage.class);
	}

	@Test
	public void getPackagesAddsAdditionalPackages() throws Exception {
		ReactPackage myPackage = mock(ReactPackage.class);
		NavigationReactNativeHost uut = new NavigationReactNativeHost(RuntimeEnvironment.application, false, Collections.singletonList(myPackage));
		assertThat(uut.getPackages()).hasSize(3).containsOnlyOnce(myPackage);
	}

	@Test
	public void getPackages_DoesNotAddDefaultTwice() throws Exception {
		NavigationReactNativeHost uut = new NavigationReactNativeHost(
				RuntimeEnvironment.application,
				false,
				Arrays.<ReactPackage>asList(
						new MainReactPackage(),
						new MainReactPackage(),
						new MainReactPackage()));
		assertThat(uut.getPackages()).hasSize(2).extracting("class").containsOnly(MainReactPackage.class, NavigationPackage.class);
	}
}