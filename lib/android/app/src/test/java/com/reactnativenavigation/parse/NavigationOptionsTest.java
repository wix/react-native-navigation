package com.reactnativenavigation.parse;

import com.reactnativenavigation.BaseTest;

import org.json.JSONObject;
import org.junit.Test;

import static com.reactnativenavigation.parse.NavigationOptions.BooleanOptions.True;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class NavigationOptionsTest extends BaseTest {

	@Test
	public void parsesNullAsDefaultEmptyOptions() throws Exception {
		assertThat(NavigationOptions.parse(null)).isNotNull();
	}

	@Test
	public void parsesJson() throws Exception {
		JSONObject json = new JSONObject();
		JSONObject topBarJson = new JSONObject();

		topBarJson.put("title", "the title");
		topBarJson.put("backgroundColor", 0xff123456);
		topBarJson.put("textColor", 0xff123456);
		topBarJson.put("textFontSize", 18);
		topBarJson.put("textFontFamily", "HelveticaNeue-CondensedBold");
		topBarJson.put("hidden", true);

		json.put("topBar", topBarJson);

		NavigationOptions result = NavigationOptions.parse(json);
		assertThat(result.topBarOptions.title).isEqualTo("the title");
		assertThat(result.topBarOptions.backgroundColor).isEqualTo(0xff123456);
		assertThat(result.topBarOptions.textColor).isEqualTo(0xff123456);
		assertThat(result.topBarOptions.textFontSize).isEqualTo(18);
		assertThat(result.topBarOptions.textFontFamily).isEqualTo("HelveticaNeue-CondensedBold");
		assertThat(result.topBarOptions.hidden).isEqualTo(True);
	}

	@Test
	public void defaultEmptyOptions() throws Exception {
		NavigationOptions uut = new NavigationOptions();
		assertThat(uut.topBarOptions.title).isEmpty();
	}
}
