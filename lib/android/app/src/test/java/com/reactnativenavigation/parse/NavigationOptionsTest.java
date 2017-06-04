package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;
import com.reactnativenavigation.layout.NavigationOptions;

import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NavigationOptionsTest extends BaseTest {

	@Test
	public void parsesNullAsDefaultEmptyOptions() throws Exception {
		assertThat(NavigationOptions.parse(null)).isNotNull();
	}

	@Test
	public void parsesJson() throws Exception {
		JSONObject json = new JSONObject();
		json.put("title", "the title");

		NavigationOptions result = NavigationOptions.parse(json);
		assertThat(result.title).isEqualTo("the title");
	}

	@Test
	public void defaultEmptyOptions() throws Exception {
		NavigationOptions uut = new NavigationOptions();
		assertThat(uut.title).isEmpty();
	}
}
