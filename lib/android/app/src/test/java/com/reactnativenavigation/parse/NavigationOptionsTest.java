package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;

import org.json.JSONObject;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NavigationOptionsTest extends BaseTest {

	@Test
	public void parsesNullAsDefaultEmptyOptions() throws Exception {
		assertThat(new NavigationOptions(null)).isNotNull();
	}

	@Test
	public void parsesJson() throws Exception {
		JSONObject json = new JSONObject();
		json.put("title", "the title");
		json.put("topBarBackgroundColor", 0xff123456);
		json.put("topBarTextColor", 0xff123456);
		json.put("topBarTextFontSize", 18);
		json.put("topBarTextFontFamily", "HelveticaNeue-CondensedBold");
		json.put("topBarHidden", true);

		NavigationOptions result = new NavigationOptions(json);
		assertThat(result.title).isEqualTo("the title");
		assertThat(result.topBarBackgroundColor).isEqualTo(0xff123456);
		assertThat(result.topBarTextColor).isEqualTo(0xff123456);
		assertThat(result.topBarTextFontSize).isEqualTo(18);
		assertThat(result.topBarTextFontFamily).isEqualTo("HelveticaNeue-CondensedBold");
		assertThat(result.topBarHidden).isEqualTo(true);
	}

	@Test
	public void defaultEmptyOptions() throws Exception {
		NavigationOptions uut = new NavigationOptions();
		assertThat(uut.title).isEmpty();
	}
}
